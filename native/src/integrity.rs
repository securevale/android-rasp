#![allow(non_snake_case)]

use jni::objects::{JClass, JObject, JObjectArray, JValue};
use jni::sys::jboolean;
use jni::JNIEnv;
use sha2::{Digest, Sha256};

mod trusted_signers;

fn certificate_matches(certificate: &[u8]) -> bool {
    let calculated = Sha256::digest(certificate);
    trusted_signers::matches(&calculated)
}

fn any_certificate_matches(env: &mut JNIEnv, certificates: JObject) -> jni::errors::Result<bool> {
    let certificates = JObjectArray::from(certificates);
    for index in 0..env.get_array_length(&certificates)? {
        let signature = env.get_object_array_element(&certificates, index)?;
        let bytes = env
            .call_method(signature, "toByteArray", "()[B", &[])?
            .l()?;
        let bytes = jni::objects::JByteArray::from(bytes);
        if certificate_matches(&env.convert_byte_array(&bytes)?) {
            return Ok(true);
        }
    }
    Ok(false)
}

fn signing_certificates<'a>(
    env: &mut JNIEnv<'a>,
    context: &JObject<'a>,
) -> jni::errors::Result<JObject<'a>> {
    let package_manager = env
        .call_method(
            context,
            "getPackageManager",
            "()Landroid/content/pm/PackageManager;",
            &[],
        )?
        .l()?;
    let package_name = env
        .call_method(context, "getPackageName", "()Ljava/lang/String;", &[])?
        .l()?;
    let sdk = env
        .get_static_field("android/os/Build$VERSION", "SDK_INT", "I")?
        .i()?;

    if sdk >= 28 {
        // GET_SIGNING_CERTIFICATES. Use the complete signing lineage for rotated keys.
        let package_info = env
            .call_method(
                &package_manager,
                "getPackageInfo",
                "(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;",
                &[JValue::Object(&package_name), JValue::Int(0x0800_0000)],
            )?
            .l()?;
        let signing_info = env
            .get_field(
                package_info,
                "signingInfo",
                "Landroid/content/pm/SigningInfo;",
            )?
            .l()?;
        let has_multiple = env
            .call_method(&signing_info, "hasMultipleSigners", "()Z", &[])?
            .z()?;
        let method = if has_multiple {
            "getApkContentsSigners"
        } else {
            "getSigningCertificateHistory"
        };
        env.call_method(
            signing_info,
            method,
            "()[Landroid/content/pm/Signature;",
            &[],
        )?
        .l()
    } else {
        // GET_SIGNATURES for the library's API 24-27 range.
        let package_info = env
            .call_method(
                package_manager,
                "getPackageInfo",
                "(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;",
                &[JValue::Object(&package_name), JValue::Int(0x40)],
            )?
            .l()?;
        env.get_field(
            package_info,
            "signatures",
            "[Landroid/content/pm/Signature;",
        )?
        .l()
    }
}

/// Returns true when the installed package is not signed by a configured trusted signer.
#[no_mangle]
pub unsafe extern "C" fn Java_com_securevale_rasp_android_integrity_checks_SignatureChecks_a<'a>(
    mut env: JNIEnv<'a>,
    _class: JClass,
    context: JObject<'a>,
) -> jboolean {
    let trusted = signing_certificates(&mut env, &context)
        .and_then(|certificates| any_certificate_matches(&mut env, certificates))
        .unwrap_or(false);

    // Fail closed: inability to obtain or inspect the signing certificate is a violation.
    u8::from(!trusted)
}

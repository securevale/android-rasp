use jni::objects::{JClass, JObject, JValue};
use jni::sys::jboolean;
use jni::JNIEnv;

use crate::util;

// hasSuspiciousPackages
#[no_mangle]
pub unsafe extern "C" fn Java_com_securevale_rasp_android_emulator_checks_PackageChecks_y<'a>(
    mut env: JNIEnv<'a>,
    _class: JClass,
    context: JObject<'a>,
) -> jboolean {
    let package_manager = JObject::try_from(
        env.call_method(
            context,
            "getPackageManager",
            "()Landroid/content/pm/PackageManager;",
            &[],
        )
        .unwrap(),
    )
    .unwrap();

    for package in SUSPICIOUS_PACKAGES {
        let package_info = env.call_method(
            &package_manager,
            "getPackageInfo",
            "(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;",
            &[
                JValue::Object(&JObject::from(env.new_string(package).unwrap())),
                JValue::Int(0),
            ],
        );

        util::ignore_error(&mut env);

        if package_info.is_ok() {
            return u8::from(true);
        }
    }

    u8::from(false)
}

/**
 * Suspicious emulator packages.
 */
const SUSPICIOUS_PACKAGES: [&str; 10] = [
    "com.google.android.launcher.layouts.genymotion",
    "com.bluestacks",
    "com.vphone.launcher",
    "com.bluestacks.appmart",
    "com.bignox",
    "com.bignox.app",
    "com.google.android.launcher.layouts.genymotion",
    "com.microvirt.tools",
    "com.microvirt.download",
    "com.mumu.store",
];

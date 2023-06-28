use jni::JNIEnv;
use jni::objects::{JClass, JObject, JValue};
use jni::sys::jboolean;

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

        crate::util::ignore_error(&mut env);

        if let Ok(_) = package_info {
            return u8::from(true);
        }
    }

    u8::from(false)
}

/**
 * Suspicious emulator packages.
 */
const SUSPICIOUS_PACKAGES: [&str; 13] = [
    "com.google.android.launcher.layouts.genymotion",
    "com.bluestacks",
    "com.bignox.app",
    "com.vphone.launcher",
    "com.bluestacks",
    "com.bluestacks.appmart",
    "com.bignox",
    "com.bignox.app",
    "com.google.android.launcher.layouts.genymotion",
    "com.vphone.launcher",
    "com.microvirt.tools",
    "com.microvirt.download",
    "com.mumu.store",
];
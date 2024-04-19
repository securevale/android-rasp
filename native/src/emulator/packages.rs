use jni::objects::{JClass, JObject};
use jni::sys::jboolean;
use jni::JNIEnv;

use crate::common::package::has_emulator_packages;

// hasSuspiciousPackages
#[no_mangle]
pub unsafe extern "C" fn Java_com_securevale_rasp_android_emulator_checks_PackageChecks_y<'a>(
    mut env: JNIEnv<'a>,
    _class: JClass,
    context: JObject<'a>,
) -> jboolean {
    u8::from(has_emulator_packages(&mut env, &context))
}

use jni::objects::JClass;
use jni::sys::jboolean;
use jni::JNIEnv;

use crate::common::property::emulator_properties_found;

// hasQemuProperties
#[no_mangle]
pub unsafe extern "C" fn Java_com_securevale_rasp_android_emulator_checks_PropertyChecks_l(
    mut env: JNIEnv,
    _class: JClass,
) -> jboolean {
    u8::from(emulator_properties_found(&mut env))
}

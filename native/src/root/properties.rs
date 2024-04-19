use crate::common::property;
use jni::objects::JClass;
use jni::sys::jboolean;
use jni::JNIEnv;

// hasRootProperties
#[no_mangle]
pub unsafe extern "C" fn Java_com_securevale_rasp_android_root_checks_PropertyChecks_y(
    mut env: JNIEnv,
    _class: JClass,
) -> jboolean {
    u8::from(property::root_properties_found(&mut env))
}

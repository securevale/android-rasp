use crate::common::build::get_build_config_value;
use crate::common::{build, property};
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

// isEngBuild
#[no_mangle]
pub unsafe extern "C" fn Java_com_securevale_rasp_android_root_checks_PropertyChecks_g(
    mut env: JNIEnv,
    _class: JClass,
) -> jboolean {
    u8::from(get_build_config_value(&mut env, build::TYPE) == "eng")
}

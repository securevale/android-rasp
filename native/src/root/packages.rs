use jni::objects::{JClass, JObject};
use jni::sys::jboolean;
use jni::JNIEnv;

use crate::common::package::{has_root_cloaking_packages, has_root_packages};

// hasRootAppPackages
#[no_mangle]
pub unsafe extern "C" fn Java_com_securevale_rasp_android_root_checks_AppsChecks_r<'a>(
    mut env: JNIEnv<'a>,
    _class: JClass,
    context: JObject<'a>,
) -> jboolean {
    let result = has_root_packages(&mut env, &context);
    u8::from(result)
}

// hasRootCloakingAppPackages
#[no_mangle]
pub unsafe extern "C" fn Java_com_securevale_rasp_android_root_checks_AppsChecks_k<'a>(
    mut env: JNIEnv<'a>,
    _class: JClass,
    context: JObject<'a>,
) -> jboolean {
    u8::from(has_root_cloaking_packages(&mut env, &context))
}

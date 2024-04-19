#[cfg(debug_assertions)]
#[macro_use]
extern crate log;
#[cfg(debug_assertions)]
extern crate android_logger;

use jni::objects::JClass;
use jni::JNIEnv;

mod debuggable;
mod emulator;

mod common;
mod root;

const EXTENDED_LOGGING: bool = false;

#[allow(clippy::missing_safety_doc)]
#[no_mangle]
pub unsafe extern "C" fn Java_com_securevale_rasp_android_native_SecureApp_initJni(
    _env: JNIEnv,
    _class: JClass,
) {
    common::logging::init_logger();
}

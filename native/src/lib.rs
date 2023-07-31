#[cfg(debug_assertions)]
#[macro_use]
extern crate log;
#[cfg(debug_assertions)]
extern crate android_logger;

use jni::objects::JClass;
use jni::JNIEnv;

mod build;
mod debuggable;
mod emulator;
mod files;
mod info;
mod logging;
mod system;
mod util;

const EXTENDED_LOGGING: bool = false;

#[allow(clippy::missing_safety_doc)]
#[no_mangle]
pub unsafe extern "C" fn Java_com_securevale_rasp_android_native_SecureApp_initJni(
    _env: JNIEnv,
    _class: JClass,
) {
    logging::init_logger();
}

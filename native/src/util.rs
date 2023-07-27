#![allow(dead_code)]

use jni::JNIEnv;

pub fn throw_java(env: &mut JNIEnv, message: &str) {
    let _ = env.throw(format!("Securevale exception: {}", message));
}

pub fn ignore_error(env: &mut JNIEnv) {
    if env.exception_check().unwrap() {
        let _ = env.exception_clear();
    }
}

#![allow(non_snake_case)]

use jni::objects::{JClass, JObject, JString, JValue};
use jni::sys::jboolean;
use jni::JNIEnv;

// isDebuggable
#[no_mangle]
pub unsafe extern "C" fn Java_com_securevale_rasp_android_debugger_checks_DebuggableChecks_a<'a>(
    mut env: JNIEnv<'a>,
    _class: JClass,
    context: JObject<'a>,
) -> jboolean {
    let app_info_class = env
        .find_class("android/content/pm/ApplicationInfo")
        .unwrap();

    let application_info = JObject::try_from(
        env.call_method(
            context,
            "getApplicationInfo",
            "()Landroid/content/pm/ApplicationInfo;",
            &[],
        )
        .unwrap(),
    )
    .unwrap();

    let flags = env.get_field(application_info, "flags", "I").unwrap();

    let app_info_debuggable = env
        .get_static_field(app_info_class, "FLAG_DEBUGGABLE", "I")
        .unwrap();

    u8::from((flags.i().unwrap() & app_info_debuggable.i().unwrap()) != 0)
}

// isDebuggerConnected
#[no_mangle]
pub unsafe extern "C" fn Java_com_securevale_rasp_android_debugger_checks_DebuggableChecks_k(
    mut env: JNIEnv,
    _class: JClass,
) -> jboolean {
    let debug_class = env.find_class("android/os/Debug").unwrap();

    let is_debugger_connected = env
        .call_static_method(debug_class, "isDebuggerConnected", "()Z", &[])
        .unwrap();

    u8::from(is_debugger_connected.z().unwrap())
}

// someoneIsWaitingForDebugger
#[no_mangle]
pub unsafe extern "C" fn Java_com_securevale_rasp_android_debugger_checks_DebuggableChecks_d(
    mut env: JNIEnv,
    _class: JClass,
) -> jboolean {
    let debug_class = env.find_class("android/os/Debug").unwrap();

    let is_debugger_connected = env
        .call_static_method(debug_class, "waitingForDebugger", "()Z", &[])
        .unwrap();

    u8::from(is_debugger_connected.z().unwrap())
}

// hasDebugBuildConfig
#[no_mangle]
pub unsafe extern "C" fn Java_com_securevale_rasp_android_debugger_checks_DebuggableChecks_b<'a>(
    mut env: JNIEnv<'a>,
    _class: JClass,
    context: JObject<'a>,
) -> jboolean {
    let clazz = env.find_class("java/lang/Class").unwrap();

    let package = JObject::try_from(
        env.call_method(context, "getPackageName", "()Ljava/lang/String;", &[])
            .unwrap(),
    )
    .unwrap();

    let package = &JString::from(package);

    let package_name = env.get_string(package).unwrap();

    let build_config_class = env.call_static_method(
        clazz,
        "forName",
        "(Ljava/lang/String;)Ljava/lang/Class;",
        &[JValue::Object(&JObject::from(
            env.new_string(package_name.to_str().unwrap().to_owned() + ".BuildConfig")
                .unwrap(),
        ))],
    );

    if build_config_class.is_err() {
        crate::util::ignore_error(&mut env);
        return u8::from(false);
    }

    let clazz_obj = JObject::try_from(build_config_class.unwrap());

    if clazz_obj.is_err() {
        crate::util::ignore_error(&mut env);
        return u8::from(false);
    }

    let field = JObject::try_from(
        env.call_method(
            clazz_obj.unwrap(),
            "getField",
            "(Ljava/lang/String;)Ljava/lang/reflect/Field;",
            &[JValue::Object(&JObject::from(
                env.new_string("DEBUG").unwrap(),
            ))],
        )
        .unwrap(),
    )
    .unwrap();

    let is_debuggable = env
        .call_method(
            field,
            "getBoolean",
            "(Ljava/lang/Object;)Z",
            &[JValue::Object(&JObject::null())],
        )
        .unwrap();

    u8::from(is_debuggable.z().unwrap())
}

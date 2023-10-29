use jni::objects::{JClass, JObject, JString};
use jni::sys::jboolean;
use jni::JNIEnv;

use crate::system;

// isRadioVersionSuspicious
#[no_mangle]
pub unsafe extern "C" fn Java_com_securevale_rasp_android_emulator_checks_DeviceChecks_u(
    mut env: JNIEnv,
    _class: JClass,
) -> jboolean {
    let build_clazz = env.find_class("android/os/Build").unwrap();

    let radio_version = JObject::try_from(
        env.call_static_method(build_clazz, "getRadioVersion", "()Ljava/lang/String;", &[])
            .unwrap(),
    )
    .unwrap();

    let radio_version_string = &JString::from(radio_version);

    let binding = env.get_string(radio_version_string).unwrap();

    let radio_version_as_rust_str = binding.to_str().unwrap();

    let result =
        radio_version_as_rust_str.is_empty() || radio_version_as_rust_str.trim() == "1.0.0.0";

    u8::from(result)
}

// isOperatorNameAndroid
#[no_mangle]
pub unsafe extern "C" fn Java_com_securevale_rasp_android_emulator_checks_DeviceChecks_c<'a>(
    mut env: JNIEnv<'a>,
    _class: JClass,
    context: JObject<'a>,
) -> jboolean {
    let telephony_manager_obj = system::get_system_service(&mut env, context, "phone").unwrap();

    let network_operator_name = JObject::try_from(
        env.call_method(
            telephony_manager_obj,
            "getNetworkOperatorName",
            "()Ljava/lang/String;",
            &[],
        )
        .unwrap(),
    )
    .unwrap();

    let result = env
        .get_string(&JString::from(network_operator_name))
        .unwrap()
        .to_str()
        .unwrap()
        .to_lowercase()
        == "android";

    u8::from(result)
}

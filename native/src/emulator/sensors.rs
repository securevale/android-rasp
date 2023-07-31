use jni::objects::{JClass, JObject, JString, JValue};
use jni::sys::jboolean;
use jni::JNIEnv;

// areSensorsFromEmulator
#[no_mangle]
pub unsafe extern "C" fn Java_com_securevale_rasp_android_emulator_checks_SensorChecks_g<'a>(
    mut env: JNIEnv<'a>,
    _class: JClass,
    context: JObject<'a>,
) -> jboolean {
    let sensor_manager_obj =
        crate::system::get_system_service(&mut env, context, "sensor").unwrap();

    let sensor_list = JObject::try_from(
        env.call_method(
            sensor_manager_obj,
            "getSensorList",
            "(I)Ljava/util/List;",
            &[JValue::Int(-1)],
        )
        .unwrap(),
    )
    .unwrap();

    let sensor_list = env.get_list(&sensor_list).unwrap();

    let mut iterator = sensor_list.iter(&mut env).unwrap();

    let result = loop {
        match iterator.next(&mut env) {
            Ok(sensor) => match sensor {
                Some(sensor) => {
                    let name_field = JObject::try_from(
                        env.call_method(sensor, "getName", "()Ljava/lang/String;", &[])
                            .unwrap(),
                    )
                    .unwrap();

                    let name_string = &JString::from(name_field);
                    let name = env.get_string(name_string).unwrap();

                    if name.to_str().unwrap().to_lowercase().contains("goldfish") {
                        break true;
                    }
                }
                None => break false,
            },
            Err(_) => break false,
        }
    };

    u8::from(result)
}

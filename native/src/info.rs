#![allow(non_snake_case)]

use crate::build::get_build_config_value;
use jni::objects::{JClass, JObject, JString, JValue};
use jni::sys::jstring;
use jni::JNIEnv;

#[no_mangle]
pub unsafe extern "C" fn Java_com_securevale_rasp_android_util_DeviceInfoKt_deviceInfo(
    mut env: JNIEnv,
    _class: JClass,
) -> jstring {
    let result = format!(
        "
         Bootloader: {}
         Device: {}
         Model: {}
         Product: {}
         Manufacturer: {}
         Brand: {}
         Board: {}
         Hardware: {}
         Host: {}
         Fingerprint: {}
         Tags: {}",
        get_build_config_value(&mut env, crate::build::BOOTLOADER),
        get_build_config_value(&mut env, crate::build::DEVICE),
        get_build_config_value(&mut env, crate::build::MODEL),
        get_build_config_value(&mut env, crate::build::PRODUCT),
        get_build_config_value(&mut env, crate::build::MANUFACTURER),
        get_build_config_value(&mut env, crate::build::BRAND),
        get_build_config_value(&mut env, crate::build::BOARD),
        get_build_config_value(&mut env, crate::build::HARDWARE),
        get_build_config_value(&mut env, crate::build::HOST),
        get_build_config_value(&mut env, crate::build::FINGERPRINT),
        get_build_config_value(&mut env, crate::build::TAGS)
    );

    env.new_string(result)
        .expect("Unable to collect device info.")
        .into_raw()
}

#[no_mangle]
pub unsafe extern "C" fn Java_com_securevale_rasp_android_util_DeviceInfoKt_extendedDeviceInfo(
    mut env: JNIEnv,
    _class: JClass,
) -> jstring {
    let result = format!(
        "
         Nox files: {}
         Andy files: {}
         Blue files: {}
         X86 files: {}
         Emulator files: {}
         Genymotion files: {}
         Emulator Pipes: {}
         Qemu Property ro.kernel.qemu: {}
         Qemu Properties count: {}
        ",
        crate::files::has_nox_files(&mut env),
        crate::files::has_andy_files(&mut env),
        crate::files::has_bluestack_files(&mut env),
        crate::files::has_x86_files(&mut env),
        crate::files::has_emu_files(&mut env),
        crate::files::has_genymotion_files(&mut env),
        crate::files::has_pipes(&mut env),
        crate::system::get_prop(&mut env, &"ro.kernel.qemu".to_string()) == "1",
        crate::emulator::properties::properties_count(&mut env),
    );

    env.new_string(result)
        .expect("Unable to collect device info.")
        .into_raw()
}

#[no_mangle]
pub unsafe extern "C" fn Java_com_securevale_rasp_android_util_DeviceInfoKt_sensorInfo<'a>(
    mut env: JNIEnv<'a>,
    _class: JClass,
    context: JObject<'a>,
) -> jstring {
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

    let mut result = String::new();

    while let Ok(sensor) = iterator.next(&mut env) {
        match sensor {
            Some(sensor) => {
                let name_field = JObject::try_from(
                    env.call_method(sensor, "getName", "()Ljava/lang/String;", &[])
                        .unwrap(),
                )
                .unwrap();

                let name_string = &JString::from(name_field);
                let name = env.get_string(name_string).unwrap();

                result.push('\n');
                result.push_str(name.to_str().unwrap());
            }
            None => break,
        }
    }

    env.new_string(result)
        .expect("Unable to collect device sensors.")
        .into_raw()
}

#![allow(non_snake_case)]

use jni::objects::{JClass, JObject, JObjectArray, JString, JValue};
use jni::sys::jboolean;
use jni::JNIEnv;
#[cfg(debug_assertions)]
use log::LevelFilter::Debug;

use crate::common::build::get_build_config_value;
use crate::common::files::{
    has_busybox_files, has_dynamic_busybox_files, has_dynamic_su_files, has_su_files,
    NON_WRITABLE_PATHS,
};
#[cfg(debug_assertions)]
use crate::common::logging::log_android;
use crate::{common::build, common::util};

// isSuperUser
#[no_mangle]
pub unsafe extern "C" fn Java_com_securevale_rasp_android_root_checks_DeviceChecks_p(
    mut env: JNIEnv,
    _class: JClass,
) -> jboolean {
    let has_su_files = has_su_files();

    let has_busybox_files = has_busybox_files();

    let system_clz = env.find_class("java/lang/System").unwrap();

    let paths_exec = JObject::try_from(
        env.call_static_method(
            system_clz,
            "getenv",
            "(Ljava/lang/String;)Ljava/lang/String;",
            &[JValue::Object(&JObject::from(
                env.new_string("PATH").unwrap(),
            ))],
        )
        .unwrap(),
    )
    .unwrap();

    let jstring = JString::from(paths_exec);
    let binding = env.get_string_unchecked(&jstring).unwrap();

    let paths = binding.to_str().unwrap();

    let hasSuPaths2 = has_dynamic_su_files(paths.split(":").collect::<Vec<&str>>().as_slice());

    let hasBusyBoxFiles2 =
        has_dynamic_busybox_files(paths.split(":").collect::<Vec<&str>>().as_slice());

    let runtime_clz = env.find_class("java/lang/Runtime").unwrap();

    let runtime_obj = JObject::try_from(
        env.call_static_method(runtime_clz, "getRuntime", "()Ljava/lang/Runtime;", &[])
            .unwrap(),
    )
    .unwrap();

    let exec = env.call_method(
        runtime_obj,
        "exec",
        "(Ljava/lang/String;)Ljava/lang/Process;",
        &[JValue::Object(&JObject::from(
            env.new_string("su").unwrap(),
        ))],
    );

    let isSu = exec.is_ok();

    if !isSu {
        util::ignore_error(&mut env);
    }

    let result = hasSuPaths2 || has_su_files || isSu || has_busybox_files || hasBusyBoxFiles2;

    u8::from(result)
}

// hasTestTags
#[no_mangle]
pub unsafe extern "C" fn Java_com_securevale_rasp_android_root_checks_DeviceChecks_c(
    mut env: JNIEnv,
    _class: JClass,
) -> jboolean {
    let tags = get_build_config_value(&mut env, build::TAGS).contains("test-keys");
    let fingerprint =
        get_build_config_value(&mut env, build::FINGERPRINT).contains("genric.*test-keys");
    let display = get_build_config_value(&mut env, build::DISPLAY).contains(".*test-keys");

    u8::from(tags || fingerprint || display)
}

// hasWritableNonWritablePaths
#[no_mangle]
pub unsafe extern "C" fn Java_com_securevale_rasp_android_root_checks_FileChecks_w(
    mut env: JNIEnv,
    _class: JClass,
) -> jboolean {
    let runtime_clz = env.find_class("java/lang/Runtime").unwrap();

    let runtime_obj = JObject::try_from(
        env.call_static_method(runtime_clz, "getRuntime", "()Ljava/lang/Runtime;", &[])
            .unwrap(),
    )
    .unwrap();

    let exec = env.call_method(
        runtime_obj,
        "exec",
        "(Ljava/lang/String;)Ljava/lang/Process;",
        &[JValue::Object(&JObject::from(
            env.new_string("mount").unwrap(),
        ))],
    );

    if exec.is_err() {
        #[cfg(debug_assertions)]
        log_android(
            Debug,
            format!("RW paths: {}", "process exec failed").as_str(),
        );
        return u8::from(false);
    }

    let process_obj = JObject::try_from(exec.unwrap()).unwrap();

    let input_stream_obj = JObject::try_from(
        env.call_method(
            process_obj,
            "getInputStream",
            "()Ljava/io/InputStream;",
            &[],
        )
        .unwrap(),
    )
    .unwrap();

    if input_stream_obj.is_null() {
        #[cfg(debug_assertions)]
        log_android(Debug, format!("RW paths: {}", "null").as_str());
        return u8::from(false);
    }

    let scanner_clz = env.find_class("java/util/Scanner").unwrap();

    let scanner_obj = &env
        .new_object(
            &scanner_clz,
            "(Ljava/io/InputStream;)V",
            &[JValue::Object(&input_stream_obj)],
        )
        .unwrap();

    let prop = env.call_method(
        scanner_obj,
        "useDelimiter",
        "(Ljava/lang/String;)Ljava/util/Scanner;",
        &[JValue::Object(&JObject::from(
            env.new_string("\\A").unwrap(),
        ))],
    );

    let prop_val = env.call_method(
        JObject::try_from(prop.unwrap()).unwrap(),
        "next",
        "()Ljava/lang/String;",
        &[],
    );

    let mountRead = env.call_method(
        JObject::try_from(prop_val.unwrap()).unwrap(),
        "split",
        "(Ljava/lang/String;)[Ljava/lang/String;",
        &[JValue::Object(&JObject::from(
            env.new_string("\n").unwrap(),
        ))],
    );

    let lines = JObjectArray::from(JObject::try_from(mountRead.unwrap()).unwrap());

    if lines.is_null() {
        return u8::from(false);
    }

    let lines_length = env.get_array_length(&lines).unwrap();

    for n in 0..lines_length {
        let line = env.get_object_array_element(&lines, n);

        #[cfg(debug_assertions)]
        {
            let line_str = &JString::from(env.get_object_array_element(&lines, n).unwrap());

            let line_to_log = env.get_string(line_str).unwrap();

            log_android(
                Debug,
                format!("LINE: {}", line_to_log.to_str().unwrap()).as_str(),
            );
        }

        let args_array = env.call_method(
            line.unwrap(),
            "split",
            "(Ljava/lang/String;)[Ljava/lang/String;",
            &[JValue::Object(&JObject::from(env.new_string(" ").unwrap()))],
        );

        let args = JObjectArray::from(JObject::try_from(args_array.unwrap()).unwrap());

        let args_length = env.get_array_length(&args).unwrap();

        if args_length < 6 {
            // Not enough options per line, just skip.
            continue;
        }

        let mountPointString = &JString::from(env.get_object_array_element(&args, 2).unwrap());

        let mountPoint = env.get_string(mountPointString).unwrap();

        #[cfg(debug_assertions)]
        log_android(
            Debug,
            format!("MOUNT POINT {}", mountPoint.to_str().unwrap()).as_str(),
        );

        let mountOptionsString = &JString::from(env.get_object_array_element(&args, 5).unwrap());

        let mountOptions = env.get_string(mountOptionsString).unwrap();

        #[cfg(debug_assertions)]
        log_android(
            Debug,
            format!("MOUNT OPTIONS {}", mountOptions.to_str().unwrap()).as_str(),
        );

        for suspicious_path in NON_WRITABLE_PATHS.iter() {
            if mountPoint
                .to_str()
                .unwrap()
                .eq_ignore_ascii_case(suspicious_path)
            {
                let mut correctedMountOptions = mountOptions.to_str().unwrap().replace('(', "");
                correctedMountOptions = correctedMountOptions.replace(')', "");

                for option in correctedMountOptions.split(',') {
                    if option.eq_ignore_ascii_case("rw") {
                        return u8::from(true);
                    }
                }
            }
        }
    }

    u8::from(false)
}

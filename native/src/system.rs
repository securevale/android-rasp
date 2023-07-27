use std::collections::HashMap;
use std::sync::Mutex;

use jni::errors::Error;
use jni::JNIEnv;
use jni::objects::{JObject, JString, JValue};
use once_cell::sync::Lazy;

/**
 * Helper class for mapping already accessed properties alongside with its access status.
 * @property failedViaReflection whether accessing the property via reflections failed.
 * @property failedViaProcessOut whether accessing the property via reading getprop process
 * output failed.
 */
#[allow(dead_code)]
struct PropertiesAccessStatus {
    tried_with_reflection: bool,
    tried_with_process_out: bool,
    value: Option<String>,
}

/**
 * Map of the properties names and its accessing status(whether they were attempted to access already or not)
 */
static FAILED_ATTEMPTS_MAP: Lazy<Mutex<HashMap<String, PropertiesAccessStatus>>> =
    Lazy::new(|| {
        let map = HashMap::new();
        Mutex::new(map)
    });

// Due to hidden API restrictions exposed from Android P the reflection approach is considered no-go
// TODO: Restrictions needs to be bypassed first, this action item will be addressed in 0.5.0 release.
pub fn get_prop(env: &mut JNIEnv, property_name: &String) -> String {
    if let Some(property) = FAILED_ATTEMPTS_MAP
        .lock()
        .unwrap()
        .get(&property_name.clone())
    {
        if (property.tried_with_process_out) && property.value.is_some() {
            return flatten_result(property.value.to_owned());
        } else if property.tried_with_process_out && property.value.is_none() {
            return "".to_string();
        }
    }

    let result = try_with_system_out(env, property_name);

    flatten_result(result)
}

fn flatten_result(result: Option<String>) -> String {
    match result {
        Some(value) => value,
        None => "".to_string(),
    }
}

fn try_with_system_out(env: &mut JNIEnv, property_name: &String) -> Option<String> {
    let runtime_clz = env.find_class("java/lang/Runtime").unwrap();

    let runtime_obj = JObject::try_from(
        env.call_static_method(runtime_clz, "getRuntime", "()Ljava/lang/Runtime;", &[])
            .unwrap(),
    )
        .unwrap();

    let process_obj = JObject::try_from(
        env.call_method(
            runtime_obj,
            "exec",
            "(Ljava/lang/String;)Ljava/lang/Process;",
            &[JValue::Object(&JObject::from(
                env.new_string(format!("{} {} {}", "getprop", property_name, ""))
                    .unwrap(),
            ))],
        )
            .unwrap(),
    )
        .unwrap();

    // 1
    let buff_reader_clz = env.find_class("java/io/BufferedReader").unwrap();

    // 2
    let input_stream_reader_clz = env.find_class("java/io/InputStreamReader").unwrap();

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

    let args = &[JValue::Object(&input_stream_obj)];

    let input_stream_reader_obj = &env
        .new_object(&input_stream_reader_clz, "(Ljava/io/InputStream;)V", args)
        .unwrap();

    let reader = env
        .new_object(
            &buff_reader_clz,
            "(Ljava/io/Reader;)V",
            &[JValue::Object(input_stream_reader_obj)],
        )
        .unwrap();

    let property = JObject::try_from(
        env.call_method(reader, "readLine", "()Ljava/lang/String;", &[])
            .unwrap(),
    )
        .unwrap();

    let result: String = env
        .get_string(&JString::from(property))
        .unwrap()
        .to_str()
        .unwrap()
        .to_string();

    let result = match !result.is_empty() {
        true => Some(result),
        false => None,
    };

    update_attempts_map(property_name.clone(), true, false, &result);

    result
}

fn update_attempts_map(key: String, process: bool, reflection: bool, result: &Option<String>) {
    FAILED_ATTEMPTS_MAP.lock().unwrap().insert(
        key,
        PropertiesAccessStatus {
            tried_with_reflection: reflection,
            tried_with_process_out: process,
            value: result.clone(),
        },
    );
}

pub fn get_system_service<'a>(
    env: &mut JNIEnv<'a>,
    context: JObject<'a>,
    service_name: &str,
) -> Result<JObject<'a>, Error> {
    let system_service = env
        .call_method(
            context,
            "getSystemService",
            "(Ljava/lang/String;)Ljava/lang/Object;",
            &[JValue::Object(&JObject::from(
                env.new_string(service_name).unwrap(),
            ))],
        )
        .unwrap();
    JObject::try_from(system_service)
}

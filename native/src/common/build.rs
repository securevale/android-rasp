use jni::objects::{JObject, JString};
use jni::JNIEnv;

pub fn get_build_config_value(env: &mut JNIEnv, key: &str) -> String {
    let build_class = env.find_class("android/os/Build").unwrap();

    let field = JObject::try_from(
        env.get_static_field(build_class, key, "Ljava/lang/String;")
            .unwrap(),
    )
    .unwrap();

    unsafe {
        env.get_string_unchecked(&JString::from(field))
            .unwrap()
            .to_str()
            .unwrap()
            .to_string()
    }
}

pub const BOOTLOADER: &str = "BOOTLOADER";
pub const DEVICE: &str = "DEVICE";
pub const MODEL: &str = "MODEL";
pub const BRAND: &str = "BRAND";
pub const BOARD: &str = "BOARD";
pub const MANUFACTURER: &str = "MANUFACTURER";
pub const HARDWARE: &str = "HARDWARE";
pub const DISPLAY: &str = "DISPLAY";
pub const FINGERPRINT: &str = "FINGERPRINT";
pub const PRODUCT: &str = "PRODUCT";
pub const TAGS: &str = "TAGS";
pub const HOST: &str = "HOST";

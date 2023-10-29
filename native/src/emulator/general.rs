#![allow(non_snake_case)]

use jni::objects::JClass;
use jni::sys::jboolean;
use jni::JNIEnv;

use crate::build::get_build_config_value;
use crate::{build, files, system};

const AVD_DEVICES: [&str; 4] = ["generic_x86_arm", "generic_x86", "generic", "x86"];

// isAvdDevice
#[no_mangle]
pub unsafe extern "C" fn Java_com_securevale_rasp_android_emulator_checks_GeneralChecks_z(
    mut env: JNIEnv,
    _class: JClass,
) -> jboolean {
    let is_avd_device = AVD_DEVICES.contains(&"generic_x86_arm");

    let board = get_build_config_value(&mut env, build::BOARD);

    let result = is_avd_device || board == "goldfish_x86" || board == "unknown";

    u8::from(result)
}

// isAvdHardware
#[no_mangle]
pub unsafe extern "C" fn Java_com_securevale_rasp_android_emulator_checks_GeneralChecks_w(
    mut env: JNIEnv,
    _class: JClass,
) -> jboolean {
    let hardware = get_build_config_value(&mut env, build::HARDWARE);

    let result = hardware == "goldfish"
        || hardware == "unknown"
        || hardware == "ranchu"
        || hardware.contains("x86");

    u8::from(result)
}

// isMemu
#[no_mangle]
pub unsafe extern "C" fn Java_com_securevale_rasp_android_emulator_checks_GeneralChecks_t(
    mut env: JNIEnv,
    _class: JClass,
) -> jboolean {
    let hardware = get_build_config_value(&mut env, build::HARDWARE);

    u8::from(hardware == "intel")
}

// isGenymotion
#[no_mangle]
pub unsafe extern "C" fn Java_com_securevale_rasp_android_emulator_checks_GeneralChecks_h(
    mut env: JNIEnv,
    _class: JClass,
) -> jboolean {
    let hardware = get_build_config_value(&mut env, build::HARDWARE);
    let manufacturer = get_build_config_value(&mut env, build::MANUFACTURER);
    let product = get_build_config_value(&mut env, build::PRODUCT);

    let hasGenymotionFiles = files::has_genymotion_files(&mut env);

    let result = manufacturer.contains("Genymotion")
        || product == "vbox86p"
        || hardware == "vbox86"
        || hasGenymotionFiles;

    u8::from(result)
}

// isNox
#[no_mangle]
pub unsafe extern "C" fn Java_com_securevale_rasp_android_emulator_checks_GeneralChecks_s(
    mut env: JNIEnv,
    _class: JClass,
) -> jboolean {
    let hardware = get_build_config_value(&mut env, build::HARDWARE);
    let product = get_build_config_value(&mut env, build::PRODUCT);
    let board = get_build_config_value(&mut env, build::BOARD);

    let hasNoxFiles = files::has_nox_files(&mut env);

    let result = hardware.to_lowercase().contains("nox")
        || product.to_lowercase().contains("nox")
        || board.to_lowercase().contains("nox")
        || hasNoxFiles;

    u8::from(result)
}

// hasSuspiciousFiles
#[no_mangle]
pub unsafe extern "C" fn Java_com_securevale_rasp_android_emulator_checks_GeneralChecks_e(
    mut env: JNIEnv,
    _class: JClass,
) -> jboolean {
    let has_andy_files = files::has_andy_files(&mut env);
    let has_blue_files = files::has_bluestack_files(&mut env);
    let has_x_86_files = files::has_x86_files(&mut env);
    let has_emulator_files = files::has_emu_files(&mut env);

    let result = has_andy_files || has_blue_files || has_x_86_files || has_emulator_files;

    u8::from(result)
}

// isFingerprintFromEmulator
#[no_mangle]
pub unsafe extern "C" fn Java_com_securevale_rasp_android_emulator_checks_GeneralChecks_j(
    mut env: JNIEnv,
    _class: JClass,
) -> jboolean {
    let fingerprint = get_build_config_value(&mut env, build::FINGERPRINT);

    let result = fingerprint.starts_with("generic")
        || fingerprint.starts_with("unknown")
        || fingerprint.starts_with("google/sdk_gphone_")
        || fingerprint.contains("x86")
        || fingerprint.contains("debug");

    u8::from(result)
}

// isGoogleEmulator
#[no_mangle]
pub unsafe extern "C" fn Java_com_securevale_rasp_android_emulator_checks_GeneralChecks_v(
    mut env: JNIEnv,
    _class: JClass,
) -> jboolean {
    let model = get_build_config_value(&mut env, build::MODEL);
    let product = get_build_config_value(&mut env, build::PRODUCT);
    let brand = get_build_config_value(&mut env, build::BRAND);
    let device = get_build_config_value(&mut env, build::DEVICE);
    let tags = get_build_config_value(&mut env, build::TAGS);

    let hasEmulatorPipes = files::has_pipes(&mut env);

    let result = model.contains("Android SDK built for x86")
        || model.contains("google_sdk")
        || model.to_lowercase().contains("droid4x")
        || model.starts_with("sdk_gphone_")
        || product == "skd"
        || product == "sdk_google"
        || product == "google_sdk"
        || product == "sdk_x86"
        || product == "sdk_gphone64_arm64"
        || product == "sdk_gphone_x86"
        || product.contains("emulator")
        || product.contains("simulator")
        || (brand.starts_with("generic") && device.starts_with("generic"))
        || tags == "dev-keys"
        || system::get_prop(&mut env, &"ro.kernel.qemu".to_string()) == "1"
        || hasEmulatorPipes;

    u8::from(result)
}

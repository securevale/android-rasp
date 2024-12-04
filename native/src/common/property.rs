use crate::common::system;
use jni::JNIEnv;
#[cfg(debug_assertions)]
use log::LevelFilter::Debug;

#[cfg(debug_assertions)]
use crate::common::logging::log_android;

pub struct Property<'a> {
    pub name: &'a str,
    pub suspicious_value: Option<&'a str>,
}

impl Property<'_> {
    pub fn looks_suspicious(&self, found_value: Option<&str>) -> bool {
        found_value == self.suspicious_value
    }
}

pub fn emulator_properties_found(env: &mut JNIEnv) -> bool {
    properties_count(env, &KNOWN_SUSPICIOUS_EMU_PROPERTIES) >= EMULATOR_PROPERTIES_THRESHOLD
}

pub fn root_properties_found(env: &mut JNIEnv) -> bool {
    properties_count(env, &KNOWN_ROOT_PROPERTIES) > 0
}

fn properties_count(env: &mut JNIEnv, properties: &[Property]) -> u8 {
    let mut counter = 0;

    properties.iter().for_each(|property| {
        let found_property = system::get_prop(env, &property.name.to_string());
        let looks_like_emulator = property.looks_suspicious(if found_property.is_empty() {
            None
        } else {
            Some(found_property.as_str())
        });

        if looks_like_emulator {
            counter += 1;
        }
    });

    #[cfg(debug_assertions)]
    log_android(Debug, format!("Properties count: {}", counter).as_str());
    counter
}

/**
 * The minimum qemu properties threshold which indicates whether device is suspicious or not.
 */
const EMULATOR_PROPERTIES_THRESHOLD: u8 = 10;

/**
 * Known qemu properties.
 */
pub const KNOWN_SUSPICIOUS_EMU_PROPERTIES: [Property; 20] = [
    Property {
        name: "init.svc.qemud",
        suspicious_value: None,
    },
    Property {
        name: "init.svc.qemu-props",
        suspicious_value: None,
    },
    Property {
        name: "qemu.hw.mainkeys",
        suspicious_value: None,
    },
    Property {
        name: "qemu.sf.fake_camera",
        suspicious_value: None,
    },
    Property {
        name: "qemu.sf.lcd_density",
        suspicious_value: None,
    },
    Property {
        name: "ro.bootloader",
        suspicious_value: Some("unknown"),
    },
    Property {
        name: "ro.bootmode",
        suspicious_value: Some("unknown"),
    },
    Property {
        name: "ro.hardware",
        suspicious_value: Some("goldfish"),
    },
    Property {
        name: "ro.hardware",
        suspicious_value: Some("ranchu"),
    },
    Property {
        name: "ro.kernel.android.qemud",
        suspicious_value: None,
    },
    Property {
        name: "ro.kernel.qemu.gles",
        suspicious_value: None,
    },
    Property {
        name: "ro.kernel.qemu",
        suspicious_value: Some("1"),
    },
    Property {
        name: "ro.product.device",
        suspicious_value: Some("generic"),
    },
    Property {
        name: "ro.product.model",
        suspicious_value: Some("sdk"),
    },
    Property {
        name: "ro.product.name",
        suspicious_value: Some("sdk"),
    },
    Property {
        name: "ro.serialno",
        suspicious_value: None,
    },
    Property {
        name: "ro.secure",
        suspicious_value: Some("0"),
    },
    Property {
        name: "ro.product.cpu.abilist",
        suspicious_value: Some("x86"),
    },
    Property {
        name: "ro.product.model",
        suspicious_value: Some("vmos"),
    },
    Property {
        name: "ro.product.vendor.name",
        suspicious_value: Some("vmos"),
    },
];

/**
 * Known root properties.
 */
pub const KNOWN_ROOT_PROPERTIES: [Property; 2] = [
    Property {
        name: "ro.secure",
        suspicious_value: Some("0"),
    },
    Property {
        name: "ro.debuggable",
        suspicious_value: Some("1"),
    },
];

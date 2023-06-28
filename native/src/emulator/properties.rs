use jni::objects::JClass;
use jni::sys::jboolean;
use jni::JNIEnv;
use std::string::ToString;

struct Property<'a> {
    name: &'a str,
    wanted_value: Option<&'a str>,
}

// TODO cover this method in tests, question is how option is doing equality checks
impl<'a> Property<'a> {
    fn indicates_emulator(&self, found_value: Option<&str>) -> bool {
        found_value == self.wanted_value
    }
}

// hasQemuProperties
#[no_mangle]
pub unsafe extern "C" fn Java_com_securevale_rasp_android_emulator_checks_PropertyChecks_l(
    mut env: JNIEnv,
    _class: JClass,
) -> jboolean {
    let mut counter = 0;

    KNOWN_PROPERTIES.iter().for_each(|property| {
        let found_property = crate::system::get_prop(&mut env, &property.name.to_string());
        let looks_like_emulator = property.indicates_emulator(if found_property.is_empty() {
            None
        } else {
            Some(found_property.as_str())
        });

        if looks_like_emulator {
            counter += 1;
        }
    });

    u8::from(counter >= EMULATOR_PROPERTIES_THRESHOLD)
}

/**
 * The qemu properties found threshold which indicates whether device is suspicious or not.
 */
const EMULATOR_PROPERTIES_THRESHOLD: u8 = 10;

/**
 * Known qemu properties.
 */
const KNOWN_PROPERTIES: [Property; 15] = [
    Property {
        name: "init.svc.qemud",
        wanted_value: None,
    },
    Property {
        name: "init.svc.qemu-props",
        wanted_value: None,
    },
    Property {
        name: "qemu.hw.mainkeys",
        wanted_value: None,
    },
    Property {
        name: "qemu.sf.fake_camera",
        wanted_value: None,
    },
    Property {
        name: "qemu.sf.lcd_density",
        wanted_value: None,
    },
    Property {
        name: "ro.bootloader",
        wanted_value: Some("unknown"),
    },
    Property {
        name: "ro.bootmode",
        wanted_value: Some("unknown"),
    },
    Property {
        name: "ro.hardware",
        wanted_value: Some("goldfish"),
    },
    Property {
        name: "ro.kernel.android.qemud",
        wanted_value: None,
    },
    Property {
        name: "ro.kernel.qemu.gles",
        wanted_value: None,
    },
    Property {
        name: "ro.kernel.qemu",
        wanted_value: Some("1"),
    },
    Property {
        name: "ro.product.device",
        wanted_value: Some("generic"),
    },
    Property {
        name: "ro.product.model",
        wanted_value: Some("sdk"),
    },
    Property {
        name: "ro.product.name",
        wanted_value: Some("sdk"),
    },
    Property {
        name: "ro.serialno",
        wanted_value: None,
    },
];
use log::LevelFilter::Debug;
use std::fs::File;
use std::io;
use std::io::{BufRead, BufReader};
use std::path::Path;

#[cfg(debug_assertions)]
use crate::common::logging::log_android;

pub fn has_genymotion_files() -> bool {
    check_files_present(&GENYMOTION_FILES)
}

pub fn has_pipes() -> bool {
    check_files_present(&PIPES)
}

pub fn has_emu_files() -> bool {
    check_files_present(&EMU_FILES)
}

pub fn has_x86_files() -> bool {
    check_files_present(&X86_FILES)
}

pub fn has_andy_files() -> bool {
    check_files_present(&ANDY_FILES)
}

pub fn has_nox_files() -> bool {
    check_files_present(&NOX_FILES)
}

pub fn has_bluestack_files() -> bool {
    check_files_present(&BLUE_STACKS_FILES)
}

pub fn has_phoenix_files() -> bool {
    check_files_present(&PHOENIX_FILES)
}

pub fn has_su_files() -> bool {
    check_files_present(&SU_FILES)
}

pub fn has_dynamic_su_files(dynamic_paths: &[&str]) -> bool {
    let mut paths = Vec::new();

    for item in dynamic_paths.iter() {
        if !item.ends_with("/") {
            paths.push(format!("{}/su", item));
        } else {
            paths.push(format!("{}su", item));
        }
    }

    #[cfg(debug_assertions)]
    log_android(Debug, format!("SU PATHS: {:?}", paths.as_slice()).as_str());

    paths.iter().any(|file_path| Path::new(&file_path).exists())
}

pub fn has_dynamic_busybox_files(dynamic_paths: &[&str]) -> bool {
    let mut paths = Vec::new();

    for item in dynamic_paths.iter() {
        if !item.ends_with("/") {
            paths.push(format!("{}/busybox", item));
        } else {
            paths.push(format!("{}busybox", item));
        }
    }

    #[cfg(debug_assertions)]
    log_android(
        Debug,
        format!("BUSYBOX PATHS: {:?}", paths.as_slice()).as_str(),
    );

    paths.iter().any(|file_path| Path::new(&file_path).exists())
}

pub fn has_busybox_files() -> bool {
    check_files_present(&BUSYBOX_FILES)
}

fn check_files_present(suspicious_file_paths: &[&str]) -> bool {
    suspicious_file_paths
        .iter()
        .any(|&file_path| Path::new(file_path).exists())
}

pub fn find_in_file(file_path: &str, search_phrases: &[&str]) -> Result<bool, io::Error> {
    let file = File::open(file_path)?;
    let reader = BufReader::new(file);

    for line in reader.lines() {
        let line = line?;
        for phrase in search_phrases.iter() {
            if line.contains(phrase) {
                return Ok(true);
            }
        }
    }
    Ok(false)
}

///  Files  indicate that it is a Genymotion emulator.
const GENYMOTION_FILES: [&str; 3] = [
    "/dev/socket/genyd",
    "/dev/socket/baseband_genyd",
    "/system/bin/genybaseband",
];

/// Pipes indicate that it is most likely an emulator.
const PIPES: [&str; 3] = ["/dev/socket/qemud", "/dev/qemu_pipe", "/dev/goldfish_pipe"];

/// Files indicate that it is most likely an emulator.
const EMU_FILES: [&str; 19] = [
    "/system/lib/libc_malloc_debug_qemu.so",
    "/system/lib64/libc_malloc_debug_qemu.so",
    "/sys/qemu_trace",
    "/system/bin/qemu-props",
    "/system/bin/qemud",
    "/dev/memufp",
    "/dev/memuguest",
    "/dev/memuuser",
    "/system/lib/memuguest.ko",
    "/dev/bst_gps",
    "/dev/bst_ime",
    "/dev/bstgyro",
    "/dev/bstmegn",
    "/system/lib/hw/gps.ld.so",
    "/system/lib/hw/sensors.ld.so",
    "/system/lib/libldutils.so",
    "/system/bin/ldinit",
    "/system/app/LDAppStore/LDAppStore.apk",
    "/data/data/com.ldmnq.launcher3/files/launcher.preferences",
];

/// Su files (indicates superuser - root privileges)
const SU_FILES: [&str; 15] = [
    "/sbin/su",
    "su/bin/su",
    "/system/bin/su",
    "/system/bin/.ext/su",
    "/system/xbin/su",
    "/data/local/xbin/su",
    "/data/local/bin/su",
    "/system/sd/xbin/su",
    "/system/bin/failsafe/su",
    "/system/usr/we-need-root/su",
    "/data/local/su",
    "/cache/su",
    "/dev/su",
    "/data/su",
    "/system/app/Superuser.apk",
];

/// Busybox files TODO check what is busybox
const BUSYBOX_FILES: [&str; 11] = [
    "/sbin/busybox",
    "su/bin/busybox",
    "/system/bin/busybox",
    "/system/bin/.ext/busybox",
    "/system/xbin/busybox",
    "/data/local/xbin/busybox",
    "/data/local/bin/busybox",
    "/system/sd/xbin/busybox",
    "/system/bin/failsafe/busybox",
    "/system/usr/we-need-root/busybox",
    "/data/local/busybox",
];

/// Pipes indicate that it is most likely an emulator.
const X86_FILES: [&str; 10] = [
    "ueventd.android_x86.rc",
    "x86.prop",
    "ueventd.ttVM_x86.rc",
    "init.ttVM_x86.rc",
    "init.goldfish.rc",
    "fstab.ttVM_x86",
    "fstab.vbox86",
    "init.vbox86.rc",
    "ueventd.vbox86.rc",
    "ueventd.ranchu.rc",
];

/// Pipes indicate that it is an Andy emulator.
const ANDY_FILES: [&str; 2] = ["fstab.andy", "ueventd.andy.rc"];

/// Pipes indicate that it is a Nox emulator.
const NOX_FILES: [&str; 12] = [
    "fstab.nox",
    "init.nox.rc",
    "ueventd.nox.rc",
    "/BigNoxGameHD",
    "/YSLauncher",
    "/system/bin/nox-prop",
    "/system/bin/noxd",
    "/system/lib/libnoxd.so",
    "/system/lib/libnoxspeedup.so",
    "/system/etc/init.nox.sh",
    "/system/bin/nox",
    "/system/bin/nox-vbox-sf",
];

/// Pipes indicate that it is a BlueStacks emulator.
const BLUE_STACKS_FILES: [&str; 15] = [
    "/Android/data/com.bluestacks.home",
    "/Android/data/com.bluestacks.settings",
    "/system/priv-app/com.bluestacks.bstfolder.apk",
    "/data/.bluestacks.prop",
    "/data/data/com.bluestacks.bstfolder",
    "/data/data/com.bluestacks.appmart",
    "/data/data/com.bluestacks.home",
    "/data/data/com.bluestacks.launcher",
    "/system/bin/bstfolder",
    "/system/bin/bstfolderd",
    "/system/bin/bstsyncfs",
    "/sys/module/bstsensor",
    "/sys/module/bstpgaipc",
    "/system/xbin/bstk/su",
    "/system/xbin/bstk",
];

/// Pipes indicate that it is a Phoenix emulator.
const PHOENIX_FILES: [&str; 3] = [
    "/system/xbin/phoenix_compat",
    "/data/system/phoenixlog.addr",
    "/system/phoenixos",
];

pub const NON_WRITABLE_PATHS: [&str; 7] = [
    "/system",
    "/system/bin",
    "/system/sbin",
    "/system/xbin",
    "/vendor/bin",
    "/sbin",
    "/etc",
];

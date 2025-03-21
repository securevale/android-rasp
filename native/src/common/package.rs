use jni::objects::{JObject, JValue};
use jni::JNIEnv;

use crate::common::util;

pub fn has_emulator_packages(env: &mut JNIEnv, context: &JObject) -> bool {
    is_package_suspicious(env, context, &SUSPICIOUS_EMULATOR_PACKAGES)
}

pub fn has_root_packages(env: &mut JNIEnv, context: &JObject) -> bool {
    is_package_suspicious(env, context, &ROOT_APP_PACKAGES)
}

pub fn has_root_cloaking_packages(env: &mut JNIEnv, context: &JObject) -> bool {
    is_package_suspicious(env, context, &ROOT_CLOAKING_APP_PACKAGES)
}

fn is_package_suspicious(env: &mut JNIEnv, context: &JObject, suspicious_package: &[&str]) -> bool {
    let package_manager = JObject::try_from(
        env.call_method(
            context,
            "getPackageManager",
            "()Landroid/content/pm/PackageManager;",
            &[],
        )
        .unwrap(),
    )
    .unwrap();

    let mut result = false;

    for package in suspicious_package.iter() {
        let package_info = env.call_method(
            &package_manager,
            "getPackageInfo",
            "(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;",
            &[
                JValue::Object(&JObject::from(env.new_string(package).unwrap())),
                JValue::Int(0),
            ],
        );

        util::ignore_error(env);

        if package_info.is_ok() {
            result = true;
            break;
        }
    }

    result
}

/**
 * Suspicious emulator packages.
 */
const SUSPICIOUS_EMULATOR_PACKAGES: [&str; 10] = [
    "com.google.android.launcher.layouts.genymotion",
    "com.bluestacks",
    "com.vphone.launcher",
    "com.bluestacks.appmart",
    "com.bignox",
    "com.bignox.app",
    "com.google.android.launcher.layouts.genymotion",
    "com.microvirt.tools",
    "com.microvirt.download",
    "com.mumu.store",
];

/**
 * Known root app packages.
 */
const ROOT_APP_PACKAGES: [&str; 43] = [
    "com.noshufou.android.su",
    "com.noshufou.android.su.elite",
    "com.koushikdutta.superuser",
    "com.thirdparty.superuser",
    "com.topjohnwu.magisk",
    "com.kingo.roo",
    "com.zhiqupk.root.global",
    "com.smedialink.oneclickroot",
    "com.alephzain.framaroo",
    "com.yellowes.su",
    "com.kingroot.kinguser",
    "com.zachspong.temprootremovejb",
    "com.ramdroid.appquarantine",
    "eu.chainfire.supersu",
    "stericson.busybox",
    "com.alephzain.framaroot",
    "com.kingo.root",
    "com.koushikdutta.rommanager",
    "com.koushikdutta.rommanager.license",
    "com.dimonvideo.luckypatcher",
    "com.chelpus.lackypatch",
    "com.ramdroid.appquarantinepro",
    "com.xmodgame",
    "com.cih.game_cih",
    "com.charles.lpoqasert",
    "catch_.me_.if_.you_.can_",
    "org.blackmart.market",
    "com.allinone.free",
    "com.repodroid.app",
    "org.creeplays.hack",
    "com.baseappfull.fwd",
    "com.zmapp",
    "com.dv.marketmod.installer",
    "org.mobilism.android",
    "com.android.wp.net.log",
    "com.android.camera.update",
    "cc.madkite.freedom",
    "com.solohsu.android.edxp.manager",
    "org.meowcat.edxposed.manager",
    "com.android.vending.billing.InAppBillingService.COIN",
    "com.android.vending.billing.InAppBillingService.LUCK",
    "com.chelpus.luckypatcher",
    "com.blackmartalpha",
];

/**
 * Known root cloaking app packages.
 */
const ROOT_CLOAKING_APP_PACKAGES: [&str; 8] = [
    "com.devadvance.rootcloak",
    "com.devadvance.rootcloakplus",
    "de.robv.android.xposed.installer",
    "com.saurik.substrate",
    "com.amphoras.hidemyroot",
    "com.amphoras.hidemyrootadfree",
    "com.formyhm.hiderootPremium",
    "com.formyhm.hideroot",
];

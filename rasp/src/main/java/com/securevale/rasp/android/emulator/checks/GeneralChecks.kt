package com.securevale.rasp.android.emulator.checks

/**
 * An object that contains all general check functions.
 */
internal object GeneralChecks {

    /**
     * Checks if there are any indicators that device might be avd one.
     * @return result of the check.
     */
    @JvmName("z")
    external fun isAvdDevice(): Boolean

    /**
     * Checks if hardware looks like from avd.
     * @return result of the check.
     */
    @JvmName("w")
    external fun isAvdHardware(): Boolean

    /**
     * Checks if it is memu device.
     * @return result of the check.
     */
    @JvmName("t")
    external fun isMemu(): Boolean

    /**
     * Checks if it is Genymotion device.
     * @return result of the check.
     */
    @JvmName("h")
    external fun isGenymotion(): Boolean

    /**
     * Checks if it is Nox device.
     * @return result of the check.
     */
    @JvmName("s")
    external fun isNox(): Boolean

    /**
     * Checks for suspicious files.
     * @return result of the check.
     */
    @JvmName("e")
    external fun hasSuspiciousFiles(): Boolean

    /**
     * Checks for suspicious fingerprint build field.
     * @return result of the check.
     */
    @JvmName("j")
    external fun isFingerprintFromEmulator(): Boolean

    /**
     * Checks for device being Google's emulator.
     * @return result of the check.
     */
    @JvmName("v")
    external fun isGoogleEmulator(): Boolean

    /**
     * Checks whether mounts seems suspicious.
     * @return result of the check.
     */
    @JvmName("d")
    external fun mountsSuspicious(): Boolean

    /**
     * Checks whether CPU seems suspicious.
     * @return result of the check.
     */
    @JvmName("c")
    external fun cpuSuspicious(): Boolean

    /**
     * Checks whether Modules seems suspicious.
     * @return result of the check.
     */
    @JvmName("u")
    external fun modulesSuspicious(): Boolean
}

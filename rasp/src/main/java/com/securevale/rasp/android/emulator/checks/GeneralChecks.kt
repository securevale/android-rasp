package com.securevale.rasp.android.emulator.checks

/**
 * An object that contains all general check functions.
 */
internal object GeneralChecks {

    /**
     * Checks if there are any indicators that device might be avd one.
     * @return whether device isAvd or there is any build field that indicates so.
     */
    @JvmName("z")
    external fun isAvdDevice(): Boolean

    /**
     * Checks if hardware looks like from avd.
     * @return whether hardware build field looks suspicious.
     */
    @JvmName("w")
    external fun isAvdHardware(): Boolean

    /**
     * Checks if it is memu device.
     * @return whether hardware build field is from memu emulator.
     */
    @JvmName("t")
    external fun isMemu(): Boolean

    /**
     * Checks if it is Genymotion device.
     * @return whether there are any indicators that it is Genymotion device.
     */
    @JvmName("h")
    external fun isGenymotion(): Boolean

    /**
     * Checks if it is Nox device.
     * @return whether there are any indicators that it is Nox device.
     */
    @JvmName("s")
    external fun isNox(): Boolean

    /**
     * Checks for suspicious files.
     * @return whether there are any suspicious files found.
     */
    @JvmName("e")
    external fun hasSuspiciousFiles(): Boolean

    /**
     * Checks for suspicious fingerprint build field.
     * @return whether fingerprint build field is suspicious.
     */
    @JvmName("j")
    external fun isFingerprintFromEmulator(): Boolean

    /**
     * Checks for device being Google's emulator.
     * @return whether there are any indicators that this device might be a Google emulator.
     */
    @JvmName("v")
    external fun isGoogleEmulator(): Boolean
}

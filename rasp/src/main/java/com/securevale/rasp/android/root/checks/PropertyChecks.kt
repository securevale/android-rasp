package com.securevale.rasp.android.root.checks

/**
 * An object that contains all properties-related check functions.
 */
internal object PropertyChecks {

    /**
     * Checks whether there are any qemu properties found.
     * @return result of the check.
     */
    @JvmName("y")
    external fun hasRootProperties(): Boolean
}

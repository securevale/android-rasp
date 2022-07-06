package com.securevale.rasp.android.os

import androidx.annotation.VisibleForTesting
import com.securevale.rasp.android.util.EMPTY
import java.io.File

/**
 * Helper object used for checking whether files are present on a device.
 */
internal object FilesCheck {

    /**
     * Checks whether files are present on device.
     * @param paths paths to searched files.
     * @return whether any of the given file paths points to existing file.
     */
    fun checkFilesPresent(paths: Array<String>): Boolean =
        paths.findIfAnyFileExists().isNotBlank()

    /**
     * Helper function for finding whether array of [String] containing file paths points to any
     * existing file.
     * @return the file path which points to existing file or [EMPTY] if no existing files found.
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun Array<String>.findIfAnyFileExists(): String {
        this.map { File(it) }
            .find { it.exists() }?.let {
                return it.path
            }
        return EMPTY
    }
}

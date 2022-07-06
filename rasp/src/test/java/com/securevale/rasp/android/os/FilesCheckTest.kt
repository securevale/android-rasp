package com.securevale.rasp.android.os

import com.google.common.truth.Truth.assertThat
import com.securevale.rasp.TestBaseClass
import com.securevale.rasp.android.util.EMPTY
import io.mockk.every
import io.mockk.spyk
import org.junit.Test

class FilesCheckTest : TestBaseClass() {

    @Test
    fun `when any file exists check should return true`() {
        val paths = arrayOf("sample/path/to_existing_file")
        with(spyk<FilesCheck>()) {
            every { any<Array<String>>().findIfAnyFileExists() } returns "existing_path"

            assertThat(checkFilesPresent(paths)).isTrue()
        }
    }

    @Test
    fun `when no file exists check should return false`() {
        val paths = arrayOf("sample/path/to_existing_file")
        with(spyk<FilesCheck>()) {
            every { any<Array<String>>().findIfAnyFileExists() } returns EMPTY

            assertThat(checkFilesPresent(paths)).isFalse()
        }
    }
}

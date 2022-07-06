package com.securevale.rasp.android.emulator.checks

import com.securevale.rasp.android.os.FilesCheck.checkFilesPresent

/**
 * Files which indicates that it is a Genymotion emulator.
 */
private val GENYMOTION_FILES = arrayOf(
    "/dev/socket/genyd",
    "/dev/socket/baseband_genyd"
)

/**
 * Pipes which indicates that it is most likely an emulator.
 */
private val PIPES = arrayOf(
    "/dev/socket/qemud",
    "/dev/qemu_pipe",
    "/dev/goldfish_pipe"
)

/**
 * Files which indicates that it is most likely an emulator.
 */
private val EMU_FILES = arrayOf(
    "/system/lib/libc_malloc_debug_qemu.so",
    "/sys/qemu_trace",
    "/system/bin/qemu-props"
)

/**
 * Pipes which indicates that it is most likely an emulator.
 */
private val X86_FILES = arrayOf(
    "ueventd.android_x86.rc",
    "x86.prop",
    "ueventd.ttVM_x86.rc",
    "init.ttVM_x86.rc",
    "fstab.ttVM_x86",
    "fstab.vbox86",
    "init.vbox86.rc",
    "ueventd.vbox86.rc",
    "ueventd.ranchu.rc"
)

/**
 * Pipes which indicates that it is an Andy emulator.
 */
private val ANDY_FILES = arrayOf(
    "fstab.andy",
    "ueventd.andy.rc"
)

/**
 * Pipes which indicates that it is a Nox emulator.
 */
private val NOX_FILES = arrayOf(
    "fstab.nox",
    "init.nox.rc",
    "ueventd.nox.rc",
    "/BigNoxGameHD",
    "/YSLauncher"
)

/**
 * Pipes which indicates that it is a BlueStacks emulator.
 */
private val BLUE_FILES = arrayOf(
    "/Android/data/com.bluestacks.home",
    "/Android/data/com.bluestacks.settings"
)

/**
 * Functions for checking whether any suspicious files were found.
 */
internal fun hasGenymotionFiles() = checkFilesPresent(GENYMOTION_FILES)
internal fun hasEmulatorPipes() = checkFilesPresent(PIPES)
internal fun hasEmulatorFiles() = checkFilesPresent(EMU_FILES)
internal fun hasX86Files() = checkFilesPresent(X86_FILES)
internal fun hasAndyFiles() = checkFilesPresent(ANDY_FILES)
internal fun hasNoxFiles() = checkFilesPresent(NOX_FILES)
internal fun hasBlueFiles() = checkFilesPresent(BLUE_FILES)

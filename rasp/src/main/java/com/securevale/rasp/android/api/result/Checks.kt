package com.securevale.rasp.android.api.result

/**
 * Base interface for check types.
 */
interface CheckType

/**
 * The enum containing all debugger checks.
 */
enum class DebuggerChecks : CheckType {
    DebuggerCheck, Debuggable, DebugField, DebuggerConnected;

    override fun toString(): String = "Debugger: $name"
}

/**
 * The enum containing all emulator checks.
 */
enum class EmulatorChecks : CheckType {
    EmulatorCheck,
    AvdDevice,
    AvdHardware,
    Genymotion,
    Nox,
    Memu,
    GoogleEmulator,
    Fingerprint,
    SuspiciousFiles,
    Bluestacks,
    Sensors,
    OperatorName,
    RadioVersion,
    SuspiciousPackages,
    SuspiciousProperties,
    Mounts,
    CPU,
    Modules;

    override fun toString(): String = "Emulator: $name"
}

/**
 * The enum containing all root checks.
 */
enum class RootChecks : CheckType {
    RootCheck,
    SuUser,
    TestTags,
    RootApps,
    RootCloakingApps,
    WritablePaths,
    SuspiciousProperties;

    override fun toString(): String = "Root: $name"
}

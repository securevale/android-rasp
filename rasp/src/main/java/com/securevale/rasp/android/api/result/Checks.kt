package com.securevale.rasp.android.api.result

/**
 * Base interface for check types.
 */
interface CheckType

/**
 * The enum that contains all debugger checks.
 */
enum class DebuggerChecks : CheckType {
    DebuggerCheck, Debuggable, DebugField, DebuggerConnected
}

/**
 * The enum that contains all emulator checks.
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
    Sensors,
    OperatorName,
    RadioVersion,
    SuspiciousPackages,
    Properties
}

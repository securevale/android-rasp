package com.securevale.rasp.android.api.result

interface CheckType

enum class DebuggerChecks : CheckType {
    DebuggerCheck, Debuggable, DebugField, DebuggerConnected
}

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

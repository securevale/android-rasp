# Android RASP

[![CI](https://github.com/securevale/android-rasp/actions/workflows/ci.yml/badge.svg)](https://github.com/securevale/android-rasp/actions/workflows/ci.yml)
[![Kotlin](https://img.shields.io/badge/kotlin-1.7.20-blue)](https://kotlinlang.org/docs/whatsnew1720.html)
[![Android](https://img.shields.io/badge/androidSDK-33-brightgreen)](https://developer.android.com/about/versions/13)
[![Gradle](https://img.shields.io/badge/gradle-8.0.2-green)](https://docs.gradle.org/8.0.2/release-notes.html)
[![Maven](https://maven-badges.herokuapp.com/maven-central/com.securevale/rasp-android/badge.svg)](https://search.maven.org/artifact/com.securevale/rasp-android)

An open-source RASP (Runtime Application Self-Protection) solution for protecting Android apps
against being run on vulnerable devices.

> **NOTE:** Android RASP is still in development, meaning that some breaking changes are likely to
> be introduced in future releases.
> See [Versioning](#versioning) section for more information.

## Motivation

In the current threat-rich environment, it is crucial to protect the apps against exploitation of a
wide range of vulnerabilities and reverse engineering techniques. Hooking frameworks,
man-in-the-middle attacks, app repackaging, rooted devices, just to name a few common threats
applicable to mobile applications.

While there are existing solutions for guarding against aforementioned threats, almost all of them
are paid. This library is one attempt to provide a robust solution for "regular" teams and devs that
cannot afford spending hundreds of dollars per month to defend against this kind of threats,
allowing to take control over application execution, security threat detection, and real-time attack
prevention.

> ** DISCLAIMER **
> While adopting this library will shield your app against a number of threats that could only be
> detected at runtime, you need to remember that no security measure can ever guarantee absolute
> security. Any motivated and skilled enough attacker will eventually bypass all security
> protections.
> For this reason, **always keep your threat models up to date**.

## Getting started

First ensure that you have defined `mavenCentral` in your Gradle configuration.

```groovy
repositories {
    mavenCentral()
}
```

Next you just need to add library as a dependency.

```groovy
dependencies {
    implementation 'com.securevale:rasp-android:{version}'
}
```

Before first use the library needs to be initialised, initialisation should be done only once per
app's lifecycle so the best place for doing it is the app's Application class.

```kotlin
import com.securevale.rasp.android.native.SecureApp

class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        SecureApp.init()
    }
}
```

Then next step will be creating the builder with the desired configuration options.

```kotlin
import com.securevale.rasp.android.emulator.CheckLevel
import com.securevale.rasp.android.api.SecureAppChecker

val shouldCheckForEmulator = true
val shouldCheckForDebugger = true
val builder = SecureAppChecker.Builder(
    context,
    checkEmulator = shouldCheckForEmulator,
    checkDebugger = shouldCheckForDebugger
)
```

Next, use the builder to create the checks and trigger them to obtain the result.

```kotlin
import com.securevale.rasp.android.api.result.Result

val check = builder.build()
val checkResult = check.check()

when (checkResult) {
    is Result.EmulatorFound -> {} // app is most likely running on emulator
    is Result.DebuggerEnabled -> {} // app is in debug mode
    is Result.Secure -> {} // app looks safe
}
```

You can do more granular checks.

```kotlin
import com.securevale.rasp.android.api.CheckSubscriber
import com.securevale.rasp.android.api.result.ExtendedResult

val check = builder.build()
check.subscribe(
    granular = true,
    checkOnlyFor = CHECK_ALL,
    subscriber = object : CheckSubscriber {
        override fun onCheck(result: ExtendedResult) {
            // examine result(s) here
        }
    })
```

Or even subscribe to be notified only if potential vulnerability will be found:

```kotlin
import com.securevale.rasp.android.api.CheckSubscriber
import com.securevale.rasp.android.api.result.ExtendedResult

val check = builder.build()
check.subscribeVulnerabilitiesOnly(
    granular = true,
    checkOnlyFor = CHECK_ALL,
    subscriber = object : CheckSubscriber {
        override fun onCheck(result: ExtendedResult) {
            // examine result(s) here
        }
    })
```

You can also choose which checks should be run per any check by passing appropriate list to
the `checkOnlyFor` parameter.

```kotlin
import com.securevale.rasp.android.api.CheckSubscriber
import com.securevale.rasp.android.api.result.ExtendedResult
import com.securevale.rasp.android.api.result.DebuggerChecks
import com.securevale.rasp.android.api.result.EmulatorChecks

val check = builder.build()
check.subscribeVulnerabilitiesOnly(
    granular = true,
    checkOnlyFor = arrayOf(
        EmulatorChecks.EmulatorCheck,
        EmulatorChecks.AvdDevice,
        EmulatorChecks.AvdHardware,
        EmulatorChecks.Genymotion,
        EmulatorChecks.Nox,
        DebuggerChecks.DebuggerCheck,
        DebuggerChecks.Debuggable,
        DebuggerChecks.DebugField
    ),
    subscriber = object : CheckSubscriber {
        override fun onCheck(result: ExtendedResult) {
            // examine result(s) here
        }
    })
```

For more information about possible options check
[SecureAppChecker](https://github.com/securevale/android-rasp/blob/master/rasp/src/main/java/com/securevale/rasp/android/api/SecureAppChecker.kt)
class documentation.

> **NOTE:** A skilled attacker might be able to repackage protected app and remove the checks from
> the source code.
> With that said, it is highly recommended to add these checks in multiple places in code, so as to
> maximize the cost and effort required to successfully bypass all the checks.
> Additionally, in order to further impede the malicious actors' life, the library checks are
> written
> in native code(using Rust language) and attached to library source code as `.so` library files.

This tool is still in a very early stage of the development and it currently supports only two
checks:

- Debugger Detection
- Emulator Detection

### Debugger Detection

Based on several checks for debug flags, connected debugger or whether there are any threads waiting
for debugger to be attached.

### Emulator Detection

Emulator detection checks:

- "basic" emulator indicators (mostly device build configuration
  fields indicating whether particular device is "real" or not). These fields can be easily faked by
  the emulator makers or even by the device user (if the device is also rooted).
- more advanced checks (such as device's operator name, telephone
  number, properties etc.). Recommended when you need to be more certain whether the device is an
  emulator. Please note that in order to take full advantage of this checks you need to add
  *android.permission.READ_PHONE_STATE* permission to your application's manifest file.

Implemented checks were tested on various emulators and devices to decrease both false-positives (
when device that is not an emulator is reported as one) and false-negatives, but as the detection
techniques become more advanced the emulator hiding techniques are improving as well. This is a
never ending cat and mouse game, so there is no guarantee that all emulators will be correctly and
accurately reported as such. The library will be continuously updated with new emulator detection
techniques in order to catch the ones that slip through the existing checks.

## Proguard

Library contains its own proguard rules defined, except one caveat regarding the `DebugField`
check. It relies on `BuildConfig` class which needs to be excluded from obfuscation, in order
for this check to return correct results, add below line to your proguard configuration file:

```
-keep class {your_package}.BuildConfig{ *; }
```

you can also disable this check by excluding `DebugField` in `checkOnlyFor` array field from check method.

## Versioning

This project follows [semantic versioning](https://semver.org/). While still in major version `0`,
source-stability is only guaranteed within minor versions (e.g. between `0.1.0` and `0.1.1`). If you
want to guard against potentially source-breaking package updates, you can specify your package
dependency using exact version as the requirement.

## License

This tool and code is released under Apache License v2.0 with Runtime Library Exception. Please
see [LICENSE](LICENSE) for more information.
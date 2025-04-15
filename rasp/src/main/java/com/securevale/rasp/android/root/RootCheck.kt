package com.securevale.rasp.android.root

import android.content.Context
import com.securevale.rasp.android.api.result.CheckType
import com.securevale.rasp.android.api.result.RootChecks.EngBuild
import com.securevale.rasp.android.api.result.RootChecks.RootApps
import com.securevale.rasp.android.api.result.RootChecks.RootCheck
import com.securevale.rasp.android.api.result.RootChecks.RootCloakingApps
import com.securevale.rasp.android.api.result.RootChecks.SuUser
import com.securevale.rasp.android.api.result.RootChecks.SuspiciousProperties
import com.securevale.rasp.android.api.result.RootChecks.TestTags
import com.securevale.rasp.android.api.result.RootChecks.WritablePaths
import com.securevale.rasp.android.check.ProbabilityCheck
import com.securevale.rasp.android.check.WrappedCheckResult
import com.securevale.rasp.android.check.wrappedCheck
import com.securevale.rasp.android.root.checks.AppsChecks.hasRootAppPackages
import com.securevale.rasp.android.root.checks.AppsChecks.hasRootCloakingAppPackages
import com.securevale.rasp.android.root.checks.DeviceChecks.hasTestTags
import com.securevale.rasp.android.root.checks.DeviceChecks.isSuperUser
import com.securevale.rasp.android.root.checks.FileChecks.hasWritableNonWritablePaths
import com.securevale.rasp.android.root.checks.PropertyChecks.hasRootProperties
import com.securevale.rasp.android.root.checks.PropertyChecks.isEngBuild

/**
 * Root detection check.
 *
 * @property context the app's Context.
 */
@PublishedApi
internal class RootCheck(private val context: Context) : ProbabilityCheck() {

    override val checksMap: Map<CheckType, () -> WrappedCheckResult> = mapOf(
        SuUser to ::checkIsSuperUser,
        TestTags to ::checkTestTags,
        RootApps to ::rootApps,
        EngBuild to ::engBuild,
        RootCloakingApps to ::rootCloakingApps,
        WritablePaths to ::nonWritablePathsAreWritable,
        SuspiciousProperties to ::hasSuspiciousProperties
    )

    override val threshold: Int = 10

    override val checkType: String = RootCheck::class.java.simpleName

    private fun checkIsSuperUser() = wrappedCheck(10, SuUser) {
        isSuperUser(context)
    }

    private fun checkTestTags() = wrappedCheck(3, TestTags) {
        hasTestTags()
    }

    private fun rootApps() = wrappedCheck(10, RootApps) {
        hasRootAppPackages(context)
    }

    private fun engBuild() = wrappedCheck(10, EngBuild) {
        isEngBuild()
    }

    private fun rootCloakingApps() = wrappedCheck(7, RootCloakingApps) {
        hasRootCloakingAppPackages(context)
    }

    private fun nonWritablePathsAreWritable() = wrappedCheck(10, WritablePaths) {
        hasWritableNonWritablePaths()
    }

    private fun hasSuspiciousProperties() = wrappedCheck(10, SuspiciousProperties) {
        hasRootProperties()
    }
}

package com.telestapp.benchmark

import androidx.benchmark.macro.ExperimentalBaselineProfilesApi
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalBaselineProfilesApi::class)
class BaselineProfileGenerator {
    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @Test
    fun generateBaselineProfile() = baselineProfileRule.collectBaselineProfile(
        packageName = "com.telestapp.rateexchange",
        profileBlock = {
            startActivityAndWait()

            val navHost = device.findObject(
                By.res(packageName, "nav_host_fragment")
            )
            repeat(10) { navHost.fling(Direction.DOWN) }
        }
    )
}
package com.wldmedical.capnoeasy.kits

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import io.github.weisiwu.slstracker.SlsLogTracker.Level
import org.junit.Assume.assumeTrue
import org.junit.Assert.assertFalse
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class SlsReadbackTest {
    @Test
    fun allSeverityLevelsReachSls() {
        val runId = InstrumentationRegistry.getArguments().getString("slsRunId").orEmpty()
        assumeTrue("Pass a unique slsRunId to run the live SLS smoke test", runId.isNotEmpty())
        require(Regex("[a-z0-9_]{1,40}").matches(runId))
        for (level in Level.values()) {
            val requestId = CapnoCloudLog.publish(level, "sls_smoke_$runId")
                ?.get(30, TimeUnit.SECONDS)
            assertFalse("Missing SLS receipt for $level", requestId.isNullOrBlank())
        }
    }
}

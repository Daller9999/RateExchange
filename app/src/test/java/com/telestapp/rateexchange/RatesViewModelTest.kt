package com.telestapp.rateexchange

import io.mockk.mockkClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class RatesViewModelTest : KoinTest {
    @ExperimentalCoroutinesApi
    class MainDispatcherRule(val dispatcher: TestDispatcher = UnconfinedTestDispatcher()) :
        TestWatcher() {

        override fun starting(description: Description) = Dispatchers.setMain(dispatcher)

        override fun finished(description: Description) = Dispatchers.resetMain()

    }

    @get:Rule
    val koinRule = KoinTestRule.create {
        modules(atpBaseAtpCoreConfigTest)
    }

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz -> mockkClass(clazz) }

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
}
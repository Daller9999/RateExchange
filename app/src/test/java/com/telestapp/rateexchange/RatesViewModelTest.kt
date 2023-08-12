package com.telestapp.rateexchange

import com.telestapp.rateexchange.core.data.CurrencyInfo
import com.telestapp.rateexchange.database.repository.CurrencyRepository
import com.telestapp.rateexchange.database.repository.RatesRepository
import com.telestapp.rateexchange.database.room.currencies.CurrencyDao
import com.telestapp.rateexchange.database.room.currencies.CurrencyEntity
import com.telestapp.rateexchange.database.room.exchanges.ExchangeDao
import com.telestapp.rateexchange.database.room.exchanges.ExchangeEntity
import com.telestapp.rateexchange.fragments.rates.RatesViewModel
import com.telestapp.rateexchange.fragments.rates.data.ExchangeInfo
import com.telestapp.rateexchange.fragments.rates.models.RatesEvent
import com.telestapp.rateexchange.network.RatesApi
import com.telestapp.rateexchange.preference.Preference
import com.telestapp.rateexchange.usecase.RatesUseCase
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.koin.test.KoinTest
import org.koin.test.mock.MockProviderRule
import kotlin.test.assertEquals

class RatesViewModelTest : KoinTest {

    private val currencyInfoList = arrayListOf<CurrencyInfo>()
    private val currencyEntityList = arrayListOf<CurrencyEntity>()

    private val exchangeEntityList = arrayListOf<ExchangeEntity>()
    private val exchangeInfoList = arrayListOf<ExchangeInfo>()

    @Before
    fun start() {
        currencyInfoList.add(CurrencyInfo("USD", "USD USD"))
        currencyInfoList.add(CurrencyInfo("AED", "AED AED"))
        currencyInfoList.add(CurrencyInfo("JPY", "JPY JPY"))
        currencyInfoList.forEach {
            currencyEntityList.add(CurrencyEntity(it.shortName, it.longName))
            exchangeEntityList.add(ExchangeEntity(it.shortName, exchanges = "123"))
            exchangeInfoList.add(ExchangeInfo(it.shortName, exchangedRate = "123", "123"))
        }
    }

    @ExperimentalCoroutinesApi
    class MainDispatcherRule(val dispatcher: TestDispatcher = UnconfinedTestDispatcher()) :
        TestWatcher() {

        override fun starting(description: Description) = Dispatchers.setMain(dispatcher)

        override fun finished(description: Description) = Dispatchers.resetMain()

    }

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz -> mockkClass(clazz) }

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val ratesDao: ExchangeDao = mockk(relaxed = true) {
        exchangeEntityList.forEach {
            every { getCurrencyByName(it.currency) } returns it
            every { addExchange(it) } just Runs
        }
        every { clearTable() } just Runs
    }

    private val currencyDao: CurrencyDao = mockk(relaxed = true) {
        every { getAllCurrencies() } returns currencyEntityList
        currencyEntityList.forEach {
            every { insetCurrency(it) } just Runs
        }
        every { clearTable() } just Runs
    }

    private val ratesApi: RatesApi = mockk(relaxed = true) {
        coEvery { getCurrencyList() } returns currencyInfoList
    }

    private val preference: Preference = mockk(relaxed = true)

    private val ratesUseCase by lazy {
        RatesUseCase(
            ratesRepository = RatesRepository(ratesDao),
            currencyRepository = CurrencyRepository(currencyDao),
            ratesApi = ratesApi,
            preference = preference
        )
    }

    private val viewModel by lazy {
        RatesViewModel(ratesUseCase = ratesUseCase)
    }

    private val viewState
        get() = viewModel.viewStates().value

    @Test
    fun testOnStart() {
        viewModel.obtainEvent(RatesEvent.OnStart)
        verify { currencyDao.getAllCurrencies() }
        verify { ratesDao.getCurrencyByName("USD") }
    }

    @Test
    fun testSelect() {
        viewModel.obtainEvent(RatesEvent.OnSelectClick)
        assert(viewState.isVisibleSelectDialog)
    }

    @Test
    fun testSelectDismiss() {
        viewModel.obtainEvent(RatesEvent.OnSelectDismiss)
        assert(!viewState.isVisibleSelectDialog)
    }

    @Test
    fun testEnterText() = runBlocking {
        viewModel.obtainEvent(RatesEvent.OnEnterText("12"))
        delay(100)
        assertEquals(viewState.enterText, "12")
        viewModel.obtainEvent(RatesEvent.OnEnterText("123"))
        delay(100)
        assertEquals(viewState.enterText, "123")
    }

    @Test
    fun onSelectCurrency() {
        viewModel.obtainEvent(RatesEvent.OnSelectCurrency(currencyInfoList[0]))
        assertEquals(viewState.selectedCurrency, currencyInfoList[0])
        viewModel.obtainEvent(RatesEvent.OnSelectCurrency(currencyInfoList[1]))
        assertEquals(viewState.selectedCurrency, currencyInfoList[1])
    }
}
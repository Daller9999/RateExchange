package com.telestapp.composetest.rates

import androidx.compose.runtime.Composable
import com.telestapp.composetest.core.BaseComposeFragment
import com.telestapp.composetest.rates.view.RatesScreen

class RatesFragment : BaseComposeFragment() {

    private val viewModel = RatesViewModel()

    @Composable
    override fun OnCreateCompose() {
        RatesScreen(viewModel = viewModel)
    }

}
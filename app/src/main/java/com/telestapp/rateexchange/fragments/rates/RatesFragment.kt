package com.telestapp.rateexchange.fragments.rates

import androidx.compose.runtime.Composable
import com.telestapp.rateexchange.core.BaseComposeFragment
import com.telestapp.rateexchange.fragments.rates.view.RatesScreen
import org.koin.androidx.viewmodel.ext.android.viewModel

class RatesFragment : BaseComposeFragment() {

    private val viewModel by viewModel<RatesViewModel>()

    @Composable
    override fun OnCreateCompose() {
        RatesScreen(viewModel = viewModel)
    }

}
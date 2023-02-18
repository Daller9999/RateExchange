package com.telestapp.rateexchange.fragments.rates.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.telestapp.rateexchange.R
import com.telestapp.rateexchange.fragments.rates.RatesViewModel
import com.telestapp.rateexchange.fragments.rates.models.RateViewState
import com.telestapp.rateexchange.fragments.rates.models.RatesEvent

@Composable
fun RatesScreen(
    viewModel: RatesViewModel
) {
    val state = viewModel.viewStates().collectAsState()
    RateView(
        state = state.value,
        onTextChanged = {
            viewModel.obtainEvent(RatesEvent.OnEnterText(it))
        }
    )

    LaunchedEffect(key1 = Unit, block = {
        viewModel.obtainEvent(RatesEvent.OnStart)
    })

}

@Composable
private fun RateView(
    state: RateViewState,
    onTextChanged: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)
    ) {
        Row(modifier = Modifier.clickable {

        }) {
            Text(text = stringResource(id = R.string.selected_currency))
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = state.currency)
        }
        EnterField(
            state = state,
            onTextChanged = onTextChanged
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun EnterField(
    state: RateViewState,
    onTextChanged: (String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 25.dp),
        shape = RoundedCornerShape(size = 53.dp),
        elevation = 0.dp,
        border = BorderStroke(
            width = 1.dp,
            color = colorResource(id = R.color.blue)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            BasicTextField(
                value = state.enterText,
                onValueChange = {
                    onTextChanged(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                maxLines = 1,
                decorationBox = @Composable { innerTextField ->
                    TextFieldDefaults.TextFieldDecorationBox(
                        placeholder = {
                            Text(
                                text = stringResource(id = R.string.enter_amount),
                                fontSize = 16.sp,
                                color = colorResource(id = R.color.grey),
                                fontWeight = FontWeight.Normal,
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        innerTextField = innerTextField,
                        enabled = true,
                        singleLine = true,
                        visualTransformation = VisualTransformation.None,
                        interactionSource = remember { MutableInteractionSource() },
                        value = state.enterText
                    )
                },
                textStyle = TextStyle(
                    color = colorResource(id = R.color.black),
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Start,
                    fontSize = 16.sp
                )
            )
        }
    }
}

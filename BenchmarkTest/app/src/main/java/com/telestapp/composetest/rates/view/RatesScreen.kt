package com.telestapp.composetest.rates.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.telestapp.composetest.R
import com.telestapp.composetest.core.data.CurrencyInfo
import com.telestapp.composetest.rates.RatesViewModel
import com.telestapp.composetest.rates.data.ExchangeInfo
import com.telestapp.composetest.rates.models.RateViewState
import com.telestapp.composetest.rates.models.RatesEvent

@Composable
fun RatesScreen(
    viewModel: RatesViewModel
) {
    val state = viewModel.viewStates().collectAsState()
    RateView(
        state = state.value,
        onTextChanged = {
            viewModel.obtainEvent(RatesEvent.OnEnterText(it))
        },
        onSelectClick = {
            viewModel.obtainEvent(RatesEvent.OnSelectClick)
        }
    )
    SelectDialog(
        state = state.value,
        onDismissDialog = {
            viewModel.obtainEvent(RatesEvent.OnSelectDismiss)
        },
        onSelectCurrency = {
            viewModel.obtainEvent(RatesEvent.OnSelectCurrency(it))
        }
    )

    LaunchedEffect(key1 = Unit, block = {
        viewModel.obtainEvent(RatesEvent.OnStart)
    })

}


@Composable
private fun SelectDialog(
    state: RateViewState,
    onDismissDialog: () -> Unit,
    onSelectCurrency: (CurrencyInfo) -> Unit
) {
    if (!state.isVisibleSelectDialog) return

    Dialog(onDismissRequest = onDismissDialog) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 30.dp),
            shape = RoundedCornerShape(size = 10.dp),
            elevation = 10.dp,
            border = BorderStroke(
                width = 1.dp,
                color = colorResource(id = R.color.blue)
            )
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Spacer(modifier = Modifier.weight(1f))
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clickable { onDismissDialog() },
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_close),
                                    contentDescription = null
                                )
                            }
                        }
                        Divider(
                            modifier = Modifier.fillMaxWidth(),
                            color = colorResource(id = R.color.blue)
                        )
                    }
                }
                items(state.currencies) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSelectCurrency.invoke(it)
                            },
                        verticalArrangement = Arrangement.Center
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                                .padding(horizontal = 30.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = it.shortName,
                                fontSize = 20.sp,
                                color = colorResource(id = R.color.blue)
                            )
                            Spacer(
                                modifier = Modifier
                                    .weight(1f)
                                    .defaultMinSize(minWidth = 10.dp)
                            )
                            Text(
                                text = it.longName,
                                fontSize = 16.sp,
                                color = colorResource(id = R.color.black),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Divider(
                            modifier = Modifier.fillMaxWidth(),
                            color = colorResource(id = R.color.blue)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RateView(
    state: RateViewState,
    onTextChanged: (String) -> Unit,
    onSelectClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Row(modifier = Modifier
            .clickable { onSelectClick.invoke() }
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .height(50.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(start = 10.dp),
                text = stringResource(id = R.string.selected_currency),
                fontSize = 18.sp,
                color = colorResource(id = R.color.black)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = state.selectedCurrency.shortName,
                fontSize = 20.sp,
                color = colorResource(id = R.color.blue)
            )
        }
        EnterField(
            state = state,
            onTextChanged = onTextChanged
        )
        Spacer(modifier = Modifier.height(10.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(state.rates) {
                CurrencyRate(currency = state.selectedCurrency.shortName, rate = it)
            }
        }
    }
}

@Composable
private fun CurrencyRate(currency: String, rate: ExchangeInfo) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "$currency ${stringResource(id = R.string.to)} ${rate.currency}",
            color = colorResource(id = R.color.blue),
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = "1 $currency = ${rate.exchangedRate} ${rate.currency}",
            color = colorResource(id = R.color.black),
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = "${stringResource(id = R.string.rate_exchange)} = ${rate.rate}",
            fontSize = 18.sp
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp), color = colorResource(id = R.color.black)
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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

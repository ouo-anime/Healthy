package ane.elu.healthy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun DiabetesScreen() {
    var carbsPer100g by remember { mutableStateOf(TextFieldValue("")) }
    var carbExchange by remember { mutableStateOf(TextFieldValue("")) }
    var weightInGrams by remember { mutableStateOf(TextFieldValue("")) }
    val selectedOption = remember { mutableStateOf("Option1") }
    val scrollState = rememberScrollState()
    val gramsForRequiredHE = runCatching {
        val carbs = carbsPer100g.text.toDoubleOrNull() ?: 0.0
        val exchangeUnits = carbExchange.text.toDoubleOrNull() ?: 0.0
        val multiplier = when (selectedOption.value) {
            "Option1" -> 10
            "Option2" -> 12
            "Option3" -> 15
            else -> 10
        }
        val grams = if (exchangeUnits > 0 && carbs > 0) {
            (exchangeUnits * multiplier * 100 / carbs * 100.0).roundToInt() / 100.0
        } else {
            0.0
        }
        grams.toString()
    }.getOrElse { "0.0" }
    val carbExchangeFromWeight = runCatching {
        val carbs = carbsPer100g.text.toDoubleOrNull() ?: 0.0
        val weight = weightInGrams.text.toDoubleOrNull() ?: 0.0
        val multiplier = when (selectedOption.value) {
            "Option1" -> 10
            "Option2" -> 12
            "Option3" -> 15
            else -> 10
        }
        val exchange = if (carbs > 0 && weight > 0) {
            ((carbs * weight) / (100.0 * multiplier)).let { (it * 100).roundToInt() / 100.0 }
        } else {
            0.0
        }
        exchange.toString()
    }.getOrElse { "0.0" }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .imePadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButtonWithLabel(
                selected = selectedOption.value == "Option1",
                onClick = { selectedOption.value = "Option1" },
                label = "10"
            )
            RadioButtonWithLabel(
                selected = selectedOption.value == "Option2",
                onClick = { selectedOption.value = "Option2" },
                label = "12"
            )
            RadioButtonWithLabel(
                selected = selectedOption.value == "Option3",
                onClick = { selectedOption.value = "Option3" },
                label = "15"
            )
        }
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 7.dp, end = 7.dp),
            value = carbsPer100g,
            label = { Text(text = "Carbohydrates per 100g") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = {
                if (it.text.all { char -> char.isDigit() || char == '.' }) {
                    carbsPer100g = it
                }
            }
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 7.dp, end = 7.dp),
            value = carbExchange,
            label = { Text(text = "Carbohydrates exchange") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = {
                if (it.text.all { char -> char.isDigit() || char == '.' }) {
                    carbExchange = it
                }
            }
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 7.dp, end = 7.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .wrapContentSize()
                    .widthIn(min = 50.dp, max = 200.dp)
                    .height(20.dp)
                    .clip(shape = RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "= $gramsForRequiredHE g",
                    color = Color.White,
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 7.dp, end = 7.dp),
            value = weightInGrams,
            label = { Text(text = "Product weight in grams") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = {
                if (it.text.all { char -> char.isDigit() || char == '.' }) {
                    weightInGrams = it
                }
            }
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 7.dp, end = 7.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .wrapContentSize()
                    .widthIn(min = 50.dp, max = 250.dp)
                    .height(20.dp)
                    .clip(shape = RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "= $carbExchangeFromWeight CarbExchange",
                    color = Color.White,
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}
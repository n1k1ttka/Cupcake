package com.example.cupcake.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

private const val PRICE_PER_CUPCAKE = 2.00
private const val PRICE_FOR_SAME_DAY_PICKUP = 3.00

class OrderViewModel : ViewModel() {


    private val _quantity = MutableLiveData<Int>()
    val quantity: LiveData<Int> = _quantity
    private val _flavor = MutableLiveData<String>()
    val flavor: LiveData<String> = _flavor
    private val _date = MutableLiveData<String>()
    val date: LiveData<String> = _date
    private val _price = MutableLiveData<Double>()
    val price: LiveData<String> = Transformations.map(_price) {
        NumberFormat.getCurrencyInstance().format(it)
    }
    val dateOptions = getPickupOptions()

    init {
        resetOrder()
    }


    //  Метод вызываемый извне для установки колличества
    fun setQuantity(numberCupcakes: Int) {
        _quantity.value = numberCupcakes
        updatePrice()
    }

    //  Метод вызываемый извне для установки вкуса
    fun setFlavor(desiredFlavor: String) {
        _flavor.value = desiredFlavor
    }

    //  Метод вызываемый извне для установки даты
    fun setDate(pickupDate: String) {
        _date.value = pickupDate
        updatePrice()
    }

    //  Метод проверки установки вкуса
    fun hasNoFlavorSet(): Boolean {
        return _flavor.value.isNullOrEmpty()
    }

    //  Метож установки дат доставки, согласно календарю
    fun getPickupOptions(): List<String> {
        val options = mutableListOf<String>()
        val formatter = SimpleDateFormat(
            "E MMM d",
            Locale.getDefault()
        ) // присваиваем переменной формат календаря, как на текущем устройстве
        val calendar =
            Calendar.getInstance() // присваиваем переменной экземпляр календаря, равный текущему на устройстве (дату устанавливаем крч)
        repeat(4) {
            options.add(formatter.format(calendar.time)) // добавляем в лист, в определенном формате, значение календаря на данный момент
            calendar.add(
                Calendar.DATE,
                1
            ) // увеличивает значение календаря на 1, то есть присваивает следующий день от предыдущего значения календаря
        }
        return options
    }

    //  Сброс заказа
    fun resetOrder() {
        _quantity.value = 0
        _flavor.value = ""
        _date.value = dateOptions[0]
        _price.value = 0.0
    }

    private fun updatePrice() {
        var calculatedPrice = (quantity.value ?: 0) * PRICE_PER_CUPCAKE
        if (dateOptions[0] == _date.value) {
            calculatedPrice += PRICE_FOR_SAME_DAY_PICKUP
        }
        _price.value = calculatedPrice
    }

}
package com.example.birthdaycalculator.ui.main

import android.app.Application
import androidx.annotation.CallSuper
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.birthdaycalculator.R
import com.example.birthdaycalculator.getString
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*

abstract class MainCalculatorViewModel(application: Application) : AndroidViewModel(application) {

    protected val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    protected val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")!!

    abstract val datePickerDefault: Long

    protected val _showCalendar = MutableLiveData<ShowCalendarSource?>()
    val showCalendar: LiveData<ShowCalendarSource?>
        get() = _showCalendar

    fun onCalendarHandled() {
        _showCalendar.value = null
    }

    @CallSuper
    open fun onDatePicked(dateMillis: Long?) {
        onCalendarHandled()
    }
}

enum class ShowCalendarSource(@StringRes val title: Int) {
    AGE_CALC_DATE_OF_BIRTH(R.string.select_date_of_birth),
    AGE_CALC_DEST_DATE(R.string.select_destination_date),
    BIRTHDAY_CALC_DEST_DATE(R.string.select_destination_date)
}
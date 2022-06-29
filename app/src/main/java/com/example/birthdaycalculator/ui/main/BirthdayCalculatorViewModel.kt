package com.example.birthdaycalculator.ui.main

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.birthdaycalculator.R
import com.example.birthdaycalculator.getString
import com.google.android.material.datepicker.MaterialDatePicker
import java.time.LocalDate
import java.time.Period

class BirthdayCalculatorViewModel(application: Application) : MainCalculatorViewModel(application) {

    override val datePickerDefault: Long
        get() {
            return when (_showCalendar.value) {
                ShowCalendarSource.BIRTHDAY_CALC_DEST_DATE -> destDateMillis.value
                    ?: MaterialDatePicker.todayInUtcMilliseconds()
                else -> LocalDate.now().toEpochDay()
            }
        }


    val destDateYears = MutableLiveData<String>()
    val destDateMonths = MutableLiveData<String>()

    private val _calculatedBirthday: MutableLiveData<String?> by lazy {
        MutableLiveData<String?>()
    }
    val calculatedBirthday: LiveData<String?>
        get() = _calculatedBirthday

    private val _destDateMillis: MutableLiveData<Long> by lazy {
        MutableLiveData<Long>()
    }
    private val destDateMillis: LiveData<Long>
        get() = _destDateMillis

    val destDate = Transformations.map(destDateMillis) {
        return@map sdf.format(it)
    }

    fun onClearBirthdayClick() {
        _calculatedBirthday.value = null
    }

    fun onCalculateBirthdayClick() {
        val destDateLocal = destDate.value
        val ageYearsLocal = destDateYears.value?.toLong()
        val ageMonthsLocal = destDateMonths.value?.toLong()

        if (destDateLocal == null || ageYearsLocal == null ) {
            _calculatedBirthday.value = null
            return
        }

        val destDate = LocalDate.parse(destDateLocal, formatter)

        val birthdayDate = destDate
            .minusYears(ageYearsLocal)
            .apply {
                ageMonthsLocal?.let { minusMonths(it) }
            }

        _calculatedBirthday.value = birthdayDate.format(formatter)
    }

    fun onSelectDestDate() {
        _showCalendar.value = ShowCalendarSource.BIRTHDAY_CALC_DEST_DATE
    }


    override fun onDatePicked(dateMillis: Long?) {
        _showCalendar.value?.let {
            if (dateMillis == null) return

            when (it) {
                ShowCalendarSource.BIRTHDAY_CALC_DEST_DATE -> _destDateMillis.value = dateMillis
                else -> {}
            }
        }

        super.onDatePicked(dateMillis)
    }
}
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

class AgeCalculatorViewModel(application: Application) : MainCalculatorViewModel(application) {

    override val datePickerDefault: Long
        get() {
            return when (_showCalendar.value) {
                ShowCalendarSource.AGE_CALC_DATE_OF_BIRTH -> dateOfBirthMillis.value
                    ?: MaterialDatePicker.todayInUtcMilliseconds()
                ShowCalendarSource.AGE_CALC_DEST_DATE -> destDateMillis.value
                    ?: MaterialDatePicker.todayInUtcMilliseconds()
                else -> LocalDate.now().toEpochDay()
            }
        }


    private val _calculatedAge: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val calculatedAge: LiveData<String>
        get() = _calculatedAge

    private val _dateOfBirthMillis: MutableLiveData<Long> by lazy {
        MutableLiveData<Long>()
    }
    private val dateOfBirthMillis: LiveData<Long>
        get() = _dateOfBirthMillis

    val dateOfBirth = Transformations.map(dateOfBirthMillis) {
        return@map sdf.format(it)
    }

    private val _destDateMillis: MutableLiveData<Long> by lazy {
        MutableLiveData<Long>()
    }
    val destDateMillis: LiveData<Long>
        get() = _destDateMillis

    val destDate = Transformations.map(destDateMillis) {
        return@map sdf.format(it)
    }

    fun onClearAgeClick() {
        _calculatedAge.value = null
    }

    fun onCalculateAgeClick() {
        val destDateLocal = destDate.value
        val birthDateLocal = dateOfBirth.value
        if (destDateLocal == null || birthDateLocal == null) return

        val destDate = LocalDate.parse(destDateLocal, formatter)
        val birthDate = LocalDate.parse(birthDateLocal, formatter)

        if (birthDate.isBefore(destDate)) {
            val period = Period.between(birthDate, destDate)
            val format = getString(R.string.age_format)
            _calculatedAge.value = String.format(format, period.years, period.months, period.days)
            return
        }

        _calculatedAge.value = getString(R.string.invalid_dates)
    }


    fun onSelectBirthDate() {
        _showCalendar.value = ShowCalendarSource.AGE_CALC_DATE_OF_BIRTH
    }

    fun onSelectDestDate() {
        _showCalendar.value = ShowCalendarSource.AGE_CALC_DEST_DATE
    }


    override fun onDatePicked(dateMillis: Long?) {
        _showCalendar.value?.let {
            if (dateMillis == null) return

            when (it) {
                ShowCalendarSource.AGE_CALC_DATE_OF_BIRTH -> _dateOfBirthMillis.value = dateMillis
                ShowCalendarSource.AGE_CALC_DEST_DATE -> _destDateMillis.value = dateMillis
                else -> {}
            }
        }

        super.onDatePicked(dateMillis)
    }
}
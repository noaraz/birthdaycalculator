package com.example.birthdaycalculator.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import com.example.birthdaycalculator.R
import com.example.birthdaycalculator.databinding.MainFragmentBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialDatePicker.INPUT_MODE_TEXT

class MainFragment : Fragment() {

    lateinit var binding: MainFragmentBinding

    private val ageCalculatorViewModel: AgeCalculatorViewModel by lazy {
        ViewModelProvider(this).get(AgeCalculatorViewModel::class.java)
    }

    private val birthdayCalculatorViewModel: BirthdayCalculatorViewModel by lazy {
        ViewModelProvider(this).get(BirthdayCalculatorViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.ageCalculatorViewModel = ageCalculatorViewModel
        binding.birthdayCalculatorViewModel = birthdayCalculatorViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ageCalculatorViewModel.showCalendar.observe(viewLifecycleOwner) {
            it?.let {
                onShowDatePicker(
                    it.title,
                    ageCalculatorViewModel.datePickerDefault,
                    onPicked = { ageCalculatorViewModel.onDatePicked(it) },
                    onHandled = { ageCalculatorViewModel.onCalendarHandled() }
                )
            }
        }

        birthdayCalculatorViewModel.showCalendar.observe(viewLifecycleOwner) {
            it?.let {
                onShowDatePicker(
                    it.title,
                    birthdayCalculatorViewModel.datePickerDefault,
                    onPicked = { birthdayCalculatorViewModel.onDatePicked(it) },
                    onHandled = { birthdayCalculatorViewModel.onCalendarHandled() }
                )
            }
        }

        birthdayCalculatorViewModel.calculatedBirthday.observe(viewLifecycleOwner) {
            binding.edtDestAgeMonths.clearFocus()
            binding.edtDestAgeYears.clearFocus()
        }
    }


    private fun onShowDatePicker(
        @StringRes title: Int,
        datePickerDefault: Long,
        onPicked: (Long) -> Unit,
        onHandled: () -> Unit
    ) {
        val datePickerBuilder: MaterialDatePicker.Builder<Long> = MaterialDatePicker
            .Builder
            .datePicker()
            .setInputMode(INPUT_MODE_TEXT)
            .setSelection(datePickerDefault)
            .setTitleText(title)
        val datePicker = datePickerBuilder.build()
        datePicker.show(parentFragmentManager, "DATE_PICKER")

        datePicker.addOnPositiveButtonClickListener { onPicked(it) }
        datePicker.addOnCancelListener { onHandled() }
        datePicker.addOnDismissListener { onHandled() }
        datePicker.addOnNegativeButtonClickListener { onHandled() }
    }


}
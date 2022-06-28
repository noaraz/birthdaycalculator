package com.example.birthdaycalculator

import android.app.Application
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel

fun AndroidViewModel.getString(@StringRes res: Int): String =
    this.getApplication<Application>().getString(res)
package com.example.lyndenflood_comp304_lab2_ex1.di

import com.example.lyndenflood_comp304_lab2_ex1.HabitViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { HabitViewModel() }
}
package com.coblin.excelexport.request

import java.time.LocalDate

data class DateRequest(
    val startDate: LocalDate,
    val endDate: LocalDate
)

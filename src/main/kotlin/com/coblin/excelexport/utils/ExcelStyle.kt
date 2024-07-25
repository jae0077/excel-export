package com.coblin.excelexport.utils

import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellStyle

interface ExcelStyle {
    fun apply(cell: Cell): CellStyle
}
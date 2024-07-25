package com.coblin.excelexport.utils.style

import com.coblin.excelexport.utils.ExcelStyle
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.IndexedColors

class DefaultHeaderStyle: ExcelStyle {
    override fun apply(cell: Cell): CellStyle {
        val workbook = cell.sheet.workbook
        val font = workbook.createFont()
            .apply {
                this.bold = true
                this.fontHeightInPoints = 16.toShort()
            }
        return workbook.createCellStyle()
            .apply {
                setFont(font)
                this.fillForegroundColor = IndexedColors.GREY_25_PERCENT.index
                this.fillPattern = FillPatternType.SOLID_FOREGROUND
                this.alignment = HorizontalAlignment.CENTER
            }
    }
}

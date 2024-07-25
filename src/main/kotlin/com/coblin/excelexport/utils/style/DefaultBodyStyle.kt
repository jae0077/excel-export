package com.coblin.excelexport.utils.style

import com.coblin.excelexport.utils.ExcelStyle
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.HorizontalAlignment

class DefaultBodyStyle: ExcelStyle {
    override fun apply(cell: Cell): CellStyle {
        val workbook = cell.sheet.workbook
        val font = workbook.createFont()
            .apply {
                this.fontHeightInPoints = 12.toShort()
            }
        return workbook.createCellStyle()
            .apply {
                setFont(font)
                this.alignment = HorizontalAlignment.LEFT
            }
    }
}

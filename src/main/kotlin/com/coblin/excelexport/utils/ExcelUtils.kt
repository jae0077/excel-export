package com.coblin.excelexport.utils

import org.apache.poi.ss.SpreadsheetVersion
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xssf.streaming.SXSSFSheet
import org.apache.poi.xssf.streaming.SXSSFWorkbook
import java.io.OutputStream
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField

object ExcelUtils {
    fun <T:Any> export(data: List<T>, type: Class<T>): SXSSFWorkbook {
        validateMaxRow(data)

        return renderExcel(data, type)
    }
    private fun <T:Any>validateMaxRow(data: List<T>) {
        if (data.size > SpreadsheetVersion.EXCEL2007.maxRows) {
            throw IllegalArgumentException("${SpreadsheetVersion.EXCEL2007.maxRows} 행을 초과하는 데이터를 지원하지 않습니다.")
        }
    }

    private fun <T:Any>renderExcel(data: List<T>, type: Class<T>): SXSSFWorkbook {
        if (data.isEmpty()) {
            throw IllegalArgumentException("export 할 데이터가 비어 있습니다.")
        }
        val wb = SXSSFWorkbook()
        val sheet = wb.createSheet()
        renderHeaders(sheet, type)
        renderBody(data, sheet)
        return wb
    }

    private fun <T:Any>renderHeaders(sheet: SXSSFSheet, type: Class<T>) {
        // 헤더를 생성 여부를 결정하기 위해 모든 속성에서 isHeader가 true인지 확인
        val hasHeader = type.kotlin.memberProperties.any { kProperty ->
            kProperty.javaField?.getAnnotation(ExcelColumn::class.java)?.isHeader ?: false
        }

        if (hasHeader) {
            val headerRow = sheet.createRow(0)
            type.kotlin.memberProperties.forEachIndexed { _, kProperty ->
                kProperty.javaField?.getAnnotation(ExcelColumn::class.java)?.let { annotation ->
                    if (annotation.isHeader) {
                        headerRow.createCell(annotation.order).apply {
                            this.setCellValue(annotation.headerName)
                            this.cellStyle = annotation.headerStyle.java.getDeclaredConstructor().newInstance().apply(this)
                        }
                    }
                }
            }
        }
    }


    private fun <T:Any>renderBody(data: List<T>, sheet: SXSSFSheet) {
        val hasHeader = sheet.physicalNumberOfRows > 0
        var lastMergedRow = if (hasHeader) 0 else -1  // 헤더가 있으면 0부터 시작, 없으면 -1부터 시작

        data.forEachIndexed { rowIndex, item ->
            val currentRow = lastMergedRow + 1  // 마지막 행 다음 행에서 시작
            val row = sheet.createRow(currentRow)
            item::class.memberProperties.forEachIndexed { _, kProperty ->
                kProperty.javaField?.getAnnotation(ExcelColumn::class.java)?.let { annotation ->
                    val cell = row.createCell(annotation.order)
                    val value = kProperty.getter.call(item)
                    renderCellValue(cell, value)
                    cell.cellStyle = annotation.bodyStyle.java.getDeclaredConstructor().newInstance().apply(cell)

                    // 셀 병합 처리
                    lastMergedRow = if (annotation.mergeRow > 0 || annotation.mergeColumn > 0) {
                        val endRow = currentRow + annotation.mergeRow - 1
                        val endCol = annotation.order + annotation.mergeColumn - 1
                        mergeCells(sheet, currentRow, endRow, annotation.order, endCol)
                        endRow  // 병합 끝 행 업데이트
                    } else {
                        currentRow // 병합이 아닐 경우 마지막 행 업데이트
                    }
                }
            }
        }
    }


    private fun renderCellValue(cell: Cell, value: Any?) {
        when (value) {
            is Number -> cell.setCellValue(value.toDouble())
            else -> cell.setCellValue(value?.toString() ?: "")
        }
    }

    private fun mergeCells(sheet: SXSSFSheet, startRow: Int, endRow: Int, startCol: Int, endCol: Int) {
        sheet.addMergedRegion(CellRangeAddress(startRow, endRow, startCol, endCol))
    }


    fun write(wb: SXSSFWorkbook, stream: OutputStream) {
        try {
            wb.write(stream)
            wb.close()
            stream.flush()
            stream.close()
        } finally {
            wb.dispose()
            stream.flush()
            stream.close()
        }
    }
}
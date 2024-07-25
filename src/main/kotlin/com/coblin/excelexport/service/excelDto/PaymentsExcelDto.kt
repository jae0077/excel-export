package com.coblin.excelexport.service.excelDto

import com.coblin.excelexport.utils.ExcelColumn

data class PaymentsExcelDto(
    @ExcelColumn(headerName = "결제IDX", order = 0)
    val idx: Long,
    @ExcelColumn(headerName = "결제한유저", order = 1)
    var userIdx: Long,
    @ExcelColumn(headerName = "결제 금액", order = 2)
    val amount: Int,
    @ExcelColumn(headerName = "결제 상태", order = 3)
    var status: String,
    @ExcelColumn(headerName = "결제 일자", order = 4)
    val createdAt: String
)

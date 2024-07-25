package com.coblin.excelexport.controller

import com.coblin.excelexport.request.DateRequest
import com.coblin.excelexport.service.ExcelExportService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ExcelExportController(
    private val excelExportService: ExcelExportService
) {
    companion object {
        private const val PAYMENT_HISTORY_DOWNLOAD_URL = "/v1/payment/history/download"
    }

    @PostMapping(value = [PAYMENT_HISTORY_DOWNLOAD_URL], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun downloadPaymentHistory(
        request: HttpServletRequest,
        response: HttpServletResponse,
        @RequestBody dateRequest: DateRequest
    ) {
        excelExportService.downloadPaymentHistory(dateRequest, request, response)
    }
}
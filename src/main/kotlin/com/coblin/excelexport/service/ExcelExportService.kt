package com.coblin.excelexport.service

import com.coblin.excelexport.jpa.PaymentsRepository
import com.coblin.excelexport.request.DateRequest
import com.coblin.excelexport.service.excelDto.PaymentsExcelDto
import com.coblin.excelexport.utils.ExcelUtils
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Service
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Service
class ExcelExportService(
    private val paymentsRepository: PaymentsRepository
) {

    fun downloadPaymentHistory(dateRequest: DateRequest, servletRequest: HttpServletRequest, servletResponse: HttpServletResponse) {
        val startDate: LocalDateTime = dateRequest.startDate.atStartOfDay()
        val endDate: LocalDateTime = dateRequest.endDate.atTime(LocalTime.MAX)

        val paymentHistory = paymentsRepository.findAllByCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual(startDate, endDate)
            .sortedByDescending { it.createdAt }
            .map {
                PaymentsExcelDto(
                    idx = it.idx!!,
                    userIdx = it.userIdx,
                    amount = it.amount,
                    status = it.status,
                    createdAt = it.createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                )
            }
        val excelFile = ExcelUtils.export(paymentHistory, PaymentsExcelDto::class.java)
        val pattern = DateTimeFormatter.ofPattern("yyyyMMdd")
        val fileName = "${dateRequest.startDate.format(pattern)}-${dateRequest.endDate.format(pattern)}-결제이력.xlsx"
        val encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString())

        servletResponse.setHeader("Content-Disposition", "attachment; filename=\"$encodedFileName\"")
        servletResponse.contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        ExcelUtils.write(excelFile, servletResponse.outputStream)
    }
}

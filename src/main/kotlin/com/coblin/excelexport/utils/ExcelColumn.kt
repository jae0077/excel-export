package com.coblin.excelexport.utils

import com.coblin.excelexport.utils.style.DefaultBodyStyle
import com.coblin.excelexport.utils.style.DefaultHeaderStyle
import kotlin.reflect.KClass

/*
 * excel로 생성할 DTO클래스에 headerName과 order를 어노테이션으로 작성
 * order = 0부터 시작(엑셀 필드 순서)
 **/
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class ExcelColumn(
    val isHeader: Boolean = true,
    val headerName: String = "",
    val order: Int,
    val headerStyle: KClass<out ExcelStyle> = DefaultHeaderStyle::class,
    val bodyStyle: KClass<out ExcelStyle> = DefaultBodyStyle::class,
    val mergeRow: Int = 0,
    val mergeColumn: Int = 0
)

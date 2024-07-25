package com.coblin.excelexport.jpa

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface PaymentsRepository: JpaRepository<Payments, Long> {
    fun findAllByCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual(createdAt: LocalDateTime, createdAt2: LocalDateTime): List<Payments>

}

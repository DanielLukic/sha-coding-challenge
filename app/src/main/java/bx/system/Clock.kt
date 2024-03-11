package bx.system

import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime

interface Clock {

    fun instant(): Instant
    fun instant(epochMillis: Long): Instant
    fun instant(instant: String): Instant
    fun durationFrom(then: Instant): Duration

    fun date(): LocalDate
    fun date(date: String): LocalDate

    fun zoned(): ZonedDateTime
    fun local(): LocalDateTime
    fun local(local: String): LocalDateTime

    companion object {
        operator fun invoke() = object : Clock {
            override fun instant(): Instant = Instant.now()
            override fun instant(epochMillis: Long): Instant = Instant.ofEpochMilli(epochMillis)
            override fun instant(instant: String): Instant = Instant.parse(instant)
            override fun durationFrom(then: Instant): Duration = Duration.between(then, Instant.now())
            override fun date(): LocalDate = LocalDate.now()
            override fun date(date: String): LocalDate = LocalDate.parse(date)
            override fun zoned(): ZonedDateTime = ZonedDateTime.now()
            override fun local(): LocalDateTime = LocalDateTime.now()
            override fun local(local: String): LocalDateTime = LocalDateTime.parse(local)
        }
    }
}

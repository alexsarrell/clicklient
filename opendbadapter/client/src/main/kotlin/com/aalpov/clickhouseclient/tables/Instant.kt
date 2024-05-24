package com.aalpov.clickhouseclient.tables

import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime

fun Instant.toRoundedInstant(): Instant =
    this.atZone(ZoneOffset.UTC).let {
        ZonedDateTime.of(it.year, it.monthValue, it.dayOfMonth, it.hour, 0, 0, 0, ZoneOffset.UTC)
            .toInstant()
    }
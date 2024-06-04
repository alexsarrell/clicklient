package com.aalpov.clickhouseclient

import com.aalpov.clickhouseclient.tables.IncomingCall
import com.aalpov.opendbadapter.service.impl.BaseClickhouseClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant
import java.util.UUID
import kotlin.random.Random
import kotlin.random.nextUBytes

@RestController
class AnalyticsService(private val clickhouseClient: BaseClickhouseClient) {

    @OptIn(ExperimentalUnsignedTypes::class)
    @PostMapping("/analytics/send")
    fun sendAnalytics() {
        clickhouseClient.insert(
            IncomingCall(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                Random.nextLong(0L, 1000000L),
                "+791944499922",
                Random.nextBoolean(),
                Random.nextUBytes(1)[0].toByte(),
                Instant.now().toEpochMilli()
            )
        )

        clickhouseClient.batchInsert(
            IncomingCall(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                Random.nextLong(0L, 1000000L),
                "+791944499922",
                Random.nextBoolean(),
                Random.nextUBytes(1)[0].toByte(),
                Instant.now().toEpochMilli()
            ),
            IncomingCall(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                Random.nextLong(0L, 1000000L),
                "+791944499922",
                Random.nextBoolean(),
                Random.nextUBytes(1)[0].toByte(),
                Instant.now().toEpochMilli()
            )
        )
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    @PostMapping("/analytics/batch")
    fun sendBatchAnalytics() {
        clickhouseClient.batchInsert(
            listOf(
                IncomingCall(
                    UUID.randomUUID().toString(),
                    UUID.randomUUID().toString(),
                    Random.nextLong(0L, 1000000L),
                    "+791944499922",
                    Random.nextBoolean(),
                    Random.nextUBytes(1)[0].toByte(),
                    Instant.now().toEpochMilli()
                ),
                IncomingCall(
                    UUID.randomUUID().toString(),
                    UUID.randomUUID().toString(),
                    Random.nextLong(0L, 1000000L),
                    "+791954229352",
                    Random.nextBoolean(),
                    Random.nextUBytes(1)[0].toByte(),
                    Instant.now().toEpochMilli()
                ),
                IncomingCall(
                    UUID.randomUUID().toString(),
                    UUID.randomUUID().toString(),
                    Random.nextLong(0L, 1000000L),
                    "+791914592352",
                    Random.nextBoolean(),
                    Random.nextUBytes(1)[0].toByte(),
                    Instant.now().toEpochMilli()
                ),
                IncomingCall(
                    UUID.randomUUID().toString(),
                    UUID.randomUUID().toString(),
                    Random.nextLong(0L, 1000000L),
                    "+7919412552922",
                    Random.nextBoolean(),
                    Random.nextUBytes(1)[0].toByte(),
                    Instant.now().toEpochMilli()
                )
            )
        )
    }
}

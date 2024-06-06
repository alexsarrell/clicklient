package com.aalpov.clickhouseclient.tables

import com.aalpov.opendbadapter.annotations.OrderedBy
import com.aalpov.opendbadapter.annotations.PartitionBy
import com.aalpov.opendbadapter.annotations.Table
import java.util.Date

@Table(name = "stateTransition")
@OrderedBy(columns = ["userId", "sessionId"])
@PartitionBy(expression = "toYYYYMM(eventDate)")
class StateTransition(
    val name: String,
    val userId: String,
    val sessionId: String,
    val eventDate: Date
)

@Table(name = "analytics_table")
@OrderedBy(columns = ["userId", "sessionId"])
class IncomingCall(
    val userId: String,
    val sessionId: String,
    val callDuration: Long,
    val telephonyId: String,
    val isFirstCall: Boolean,
    val requestCount: Byte,
    val timestamp: Long
)

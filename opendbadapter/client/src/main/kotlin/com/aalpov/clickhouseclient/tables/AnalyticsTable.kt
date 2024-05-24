package com.aalpov.clickhouseclient.tables

import com.aalpov.opendbadapter.annotations.OrderedBy
import com.aalpov.opendbadapter.annotations.Table

@Table(name = "stateTransition")
@OrderedBy(columns = ["userId", "sessionId"])
class StateTransition(
    val name: String,
    val userId: String,
    val sessionId: String
)

@Table(name = "incomingCall")
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

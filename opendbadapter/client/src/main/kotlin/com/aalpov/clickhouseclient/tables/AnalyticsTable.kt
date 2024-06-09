package com.aalpov.clickhouseclient.tables

import com.aalpov.opendbadapter.annotations.Convert
import com.aalpov.opendbadapter.annotations.OrderedBy
import com.aalpov.opendbadapter.annotations.PartitionBy
import com.aalpov.opendbadapter.annotations.Table
import com.aalpov.opendbadapter.service.Converter
import com.aalpov.opendbadapter.type.DbString
import java.lang.reflect.Field
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
    @Convert(converter = StateTransitionConverter::class)
    val state: StateTransition,
    val telephonyId: String,
    val isFirstCall: Boolean,
    val requestCount: Byte,
    val timestamp: Long
)

class StateTransitionConverter : Converter<StateTransition, String, DbString> {

    override fun convert(value: StateTransition): String {
        return value.name
    }

    override fun convertType(field: Field): DbString {
        return DbString()
    }
}

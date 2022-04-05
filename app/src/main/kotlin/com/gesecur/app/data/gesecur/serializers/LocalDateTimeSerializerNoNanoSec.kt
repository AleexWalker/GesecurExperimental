package com.gesecur.app.data.gesecur.serializers

import arrow.core.extensions.either.applicativeError.catch
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

object LocalDateTimeSerializerNoNanoSec : KSerializer<LocalDateTime> {

    private val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    private val patternNano = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.n")

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        val string = value.format(pattern)
        encoder.encodeString(string)
    }

    override fun deserialize(decoder: Decoder): LocalDateTime {
        val string = decoder.decodeString()
        return try {
            LocalDateTime.parse(string, pattern)
        } catch(e: DateTimeParseException) {
            LocalDateTime.parse(string, patternNano)
        }
    }

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)
}
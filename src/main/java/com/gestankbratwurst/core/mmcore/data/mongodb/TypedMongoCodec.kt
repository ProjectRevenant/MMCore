package com.gestankbratwurst.core.mmcore.data.mongodb

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.gestankbratwurst.core.mmcore.data.json.GsonProvider
import org.bson.BsonReader
import org.bson.BsonType
import org.bson.BsonWriter
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext
import org.bson.types.Decimal128
import java.math.BigDecimal
import java.math.BigInteger

class TypedMongoCodec<V>(private val valueClazz: Class<V>) : Codec<V> {

    override fun decode(reader: BsonReader, decoderContext: DecoderContext): V {
        val rootObject = readObject(reader)
        return GsonProvider.fromJson(rootObject.toString(), this.valueClazz)
    }

    override fun encode(writer: BsonWriter, value: V, encoderContext: EncoderContext) {
        writer.writeStartDocument()
        val jsonElement: JsonElement = GsonProvider.toJsonTree(value)
        if (jsonElement.isJsonObject) {
            jsonElement.asJsonObject.entrySet().forEach { (key, element) ->
                writer.writeName(key)
                writeJsonElement(writer, element)
            }
        }
        writer.writeEndDocument()
    }

    override fun getEncoderClass(): Class<V> {
        return this.valueClazz
    }

    private fun writeJsonElement(writer: BsonWriter, jsonElement: JsonElement) {
        if (jsonElement.isJsonObject) {
            writer.writeStartDocument()
            jsonElement.asJsonObject.entrySet().forEach { (key, element) ->
                writer.writeName(key)
                writeJsonElement(writer, element)
            }
            writer.writeEndDocument()
        } else if (jsonElement.isJsonPrimitive) {
            val jsonPrimitive = jsonElement.asJsonPrimitive
            if (jsonPrimitive.isString) {
                writer.writeString(jsonPrimitive.asString)
            } else if (jsonPrimitive.isNumber) {
                when (val number = jsonElement.asNumber) {
                    is Double -> {
                        writer.writeDouble(number.toDouble())
                    }
                    is Int -> {
                        writer.writeInt32(number.toInt())
                    }
                    is Long -> {
                        writer.writeInt64(number.toLong())
                    }
                    is Float -> {
                        writer.writeDouble(number.toFloat().toDouble())
                    }
                    is Short -> {
                        writer.writeInt32(number.toShort().toInt())
                    }
                    is Byte -> {
                        writer.writeInt32(number.toByte().toInt())
                    }
                    is BigInteger -> {
                        writer.writeString(number.toString())
                    }
                    is BigDecimal -> {
                        writer.writeDecimal128(Decimal128(number))
                    }
                }
            } else if (jsonPrimitive.isBoolean) {
                writer.writeBoolean(jsonPrimitive.asBoolean)
            }
        } else if (jsonElement.isJsonNull) {
            writer.writeNull()
        } else if (jsonElement.isJsonArray) {
            writer.writeStartArray()
            jsonElement.asJsonArray.forEach { writeJsonElement(writer, it) }
            writer.writeEndArray()
        }
    }

    private fun readElement(reader: BsonReader): JsonElement {
        return when (reader.currentBsonType) {
            BsonType.DOCUMENT -> readObject(reader)
            BsonType.INT32 -> JsonPrimitive(reader.readInt32())
            BsonType.INT64 -> JsonPrimitive(reader.readInt64())
            BsonType.BOOLEAN -> JsonPrimitive(reader.readBoolean())
            BsonType.STRING -> JsonPrimitive(reader.readString())
            BsonType.ARRAY -> readArray(reader)
            BsonType.DOUBLE -> JsonPrimitive(reader.readDouble())
            BsonType.DECIMAL128 -> JsonPrimitive(reader.readDecimal128().bigDecimalValue())
            BsonType.NULL -> {
                reader.readNull()
                JsonNull.INSTANCE
            }
            else -> {
                reader.skipValue()
                JsonNull.INSTANCE
            }
        }
    }

    private fun readArray(reader: BsonReader): JsonArray {
        reader.readStartArray()
        val jsonArray = JsonArray()
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            jsonArray.add(readElement(reader))
        }
        reader.readEndArray()
        return jsonArray
    }

    private fun readObject(reader: BsonReader): JsonObject {
        val jsonObject = JsonObject()
        reader.readStartDocument()
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            val key = reader.readName()
            jsonObject.add(key, readElement(reader))
        }
        reader.readEndDocument()
        return jsonObject
    }
}

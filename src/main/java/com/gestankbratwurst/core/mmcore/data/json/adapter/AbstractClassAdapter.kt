package com.gestankbratwurst.core.mmcore.data.json.adapter

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.gestankbratwurst.core.mmcore.data.json.GsonProvider
import java.lang.reflect.Type

class AbstractClassAdapter : JsonSerializer<Any?>, JsonDeserializer<Any?> {

    companion object {
        private const val CLASS_KEY = "@CLASS"
        private const val DATA_KEY = "@DATA"
    }

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, type: Type, context: JsonDeserializationContext): Any? {
        val jsonObject = json.asJsonObject
        val className = jsonObject[CLASS_KEY].asString
        return try {
            val clazz = Class.forName(className)
            return GsonProvider.without(this).fromJson(jsonObject[DATA_KEY], clazz)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
            null
        }
    }

    override fun serialize(src: Any?, type: Type, context: JsonSerializationContext): JsonElement {
        val jsonObject = JsonObject()
        jsonObject.addProperty(CLASS_KEY, src?.javaClass?.name)
        jsonObject.add(DATA_KEY, GsonProvider.without(this).toJsonTree(src))
        return jsonObject
    }
}

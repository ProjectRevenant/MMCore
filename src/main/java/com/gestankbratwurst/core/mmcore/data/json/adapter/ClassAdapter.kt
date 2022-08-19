package com.gestankbratwurst.core.mmcore.data.json.adapter

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

class ClassAdapter : JsonSerializer<Class<*>?>, JsonDeserializer<Class<*>?> {

    @Throws(JsonParseException::class)
    override fun deserialize(jsonElement: JsonElement, type: Type, jsonDeserializationContext: JsonDeserializationContext): Class<*>? {
        val jsonObject = jsonElement.asJsonObject
        return try {
            Class.forName(jsonObject["classname"].asString)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
            null
        }
    }

    override fun serialize(aClass: Class<*>?, type: Type, jsonSerializationContext: JsonSerializationContext): JsonElement {
        val jsonObject = JsonObject()
        jsonObject.addProperty("classname", aClass?.name)
        return jsonObject
    }
}

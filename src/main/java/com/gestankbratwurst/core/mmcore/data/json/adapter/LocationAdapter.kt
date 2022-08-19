package com.gestankbratwurst.core.mmcore.data.json.adapter

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import org.bukkit.Bukkit
import org.bukkit.Location
import java.lang.reflect.Type
import java.util.UUID

class LocationAdapter : JsonSerializer<Location>, JsonDeserializer<Location> {

    override fun serialize(src: Location, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        val jsonObject = JsonObject()
        jsonObject.addProperty("world", src.world.uid.toString())
        jsonObject.addProperty("x", src.x)
        jsonObject.addProperty("y", src.y)
        jsonObject.addProperty("z", src.z)
        jsonObject.addProperty("pitch", src.pitch)
        jsonObject.addProperty("yaw", src.yaw)
        return jsonObject
    }

    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): Location {
        val jsonObject = json.asJsonObject
        val worldId = UUID.fromString(jsonObject["world"].asString)
        val x = jsonObject["x"].asDouble
        val y = jsonObject["y"].asDouble
        val z = jsonObject["z"].asDouble
        val pitch = jsonObject["pitch"].asFloat
        val yaw = jsonObject["yaw"].asFloat
        return Location(Bukkit.getWorld(worldId), x, y, z, pitch, yaw)
    }
}

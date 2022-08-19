package com.gestankbratwurst.core.mmcore.data.json.adapter

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import org.bukkit.inventory.ItemStack
import org.bukkit.util.io.BukkitObjectInputStream
import org.bukkit.util.io.BukkitObjectOutputStream
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.lang.reflect.Type

class ItemStackAdapter : JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {

    override fun serialize(src: ItemStack, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val outputStream = ByteArrayOutputStream()
        val dataOutput = BukkitObjectOutputStream(outputStream)
        dataOutput.writeObject(src)
        dataOutput.close()
        return JsonPrimitive(Base64Coder.encodeLines(outputStream.toByteArray()))
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ItemStack {
        val inputStream = ByteArrayInputStream(Base64Coder.decodeLines(json.asJsonPrimitive.asString))
        val dataInput = BukkitObjectInputStream(inputStream)
        val itemStack = dataInput.readObject() as ItemStack
        dataInput.close()
        return itemStack
    }
}

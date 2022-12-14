package com.gestankbratwurst.core.mmcore.data.json

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.io.IOException

class PostProcessingEnabler : TypeAdapterFactory {
    override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T> {
        val delegate = gson.getDelegateAdapter(this, type)

        return object : TypeAdapter<T>() {
            @Throws(IOException::class)
            override fun write(output: JsonWriter, value: T) {
                delegate.write(output, value)
            }

            @Throws(IOException::class)
            override fun read(input: JsonReader): T {
                val obj = delegate.read(input)
                if (obj is DeserializationPostProcessable) {
                    obj.gsonPostProcess()
                }
                return obj
            }
        }
    }
}

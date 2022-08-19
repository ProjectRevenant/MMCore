package com.gestankbratwurst.core.mmcore.data.json

fun interface DeserializationPostProcessable {
    fun gsonPostProcess()
}

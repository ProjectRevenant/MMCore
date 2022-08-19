package com.gestankbratwurst.core.mmcore.data.json

import com.gestankbratwurst.core.mmcore.data.json.adapter.AbstractClassAdapter
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.nio.charset.StandardCharsets

object GsonProvider {

    val gsonPretty: Gson?
        get() {
            checkForChanges()
            return prettyGson
        }

    private val instanceCreators: MutableMap<Type, InstanceCreator<*>> = HashMap()
    private var builderChanged = true
    private var gson: Gson? = null
    private var prettyGson: Gson? = null
    private val applications: MutableMap<Any, (GsonBuilder) -> Unit> = mutableMapOf()
    private val skipCache: MutableMap<Any, Gson> = mutableMapOf()

    private fun applyApplications(builder: GsonBuilder, applications: MutableMap<Any, (GsonBuilder) -> Unit>) {
        applications.values.forEach { app -> app.invoke(builder) }
    }

    fun without(adapter: Any): Gson {
        return skipCache.computeIfAbsent(adapter) {
            val cloned: MutableMap<Any, (GsonBuilder) -> Unit> = mutableMapOf()
            for (entry in applications.entries) {
                if (entry.key != adapter) {
                    cloned[entry.key] = entry.value;
                }
            }
            val builder = GsonBuilder()
                .enableComplexMapKeySerialization()
                .disableHtmlEscaping()
            applyApplications(builder, cloned)
            builder.create()
        }
    }

    fun <T> getDelegateAdapter(skipPast: TypeAdapterFactory, type: TypeToken<T>): TypeAdapter<T> {
        checkForChanges()
        return gson!!.getDelegateAdapter(skipPast, type)
    }

    private fun checkForChanges() {
        if (!builderChanged) {
            return
        }
        skipCache.clear()
        val gsonBuilder = GsonBuilder()
            .enableComplexMapKeySerialization()
            .disableHtmlEscaping()
        val prettyGsonBuilder = GsonBuilder()
            .enableComplexMapKeySerialization()
            .disableHtmlEscaping()
            .setPrettyPrinting()

        applyApplications(gsonBuilder, applications)
        applyApplications(prettyGsonBuilder, applications)

        gson = gsonBuilder.create()
        prettyGson = prettyGsonBuilder.create()

        builderChanged = false
    }

    fun <T> toBinary(element: T): ByteArray {
        return toJson(element).toByteArray(StandardCharsets.UTF_8)
    }

    fun <T> fromBinary(bytes: ByteArray?, type: Type?): T {
        return fromJson(String(bytes!!, StandardCharsets.UTF_8), type)
    }

    fun <T> fromBinary(bytes: ByteArray?, clazz: Class<T>?): T {
        return fromJson(String(bytes!!, StandardCharsets.UTF_8), clazz)
    }

    fun <T> toJson(element: T): String {
        checkForChanges()
        return gson!!.toJson(element)
    }

    fun <T> toJsonPretty(element: T): String {
        checkForChanges()
        return prettyGson!!.toJson(element)
    }

    fun <T> toJsonTree(element: T): JsonElement {
        checkForChanges()
        return gson!!.toJsonTree(element)
    }

    fun <T> toJsonTreePretty(element: T): JsonElement {
        checkForChanges()
        return prettyGson!!.toJsonTree(element)
    }

    fun <T> fromJson(json: String?, type: Class<T>?): T {
        checkForChanges()
        return gson!!.fromJson(json, type)
    }

    fun <T> fromJson(json: String?, type: Type?): T {
        checkForChanges()
        return gson!!.fromJson(json, type)
    }

    fun <T> fromJson(json: JsonElement?, type: Type?): T {
        checkForChanges()
        return gson!!.fromJson(json, type)
    }

    fun applyChange(key: Any, builderConsumer: (GsonBuilder) -> Unit) {
        applications[key] = builderConsumer
        builderChanged = true
    }

    fun <T> registerInterfaceHierarchy(clazz: Class<T>) {
        registerTypeHierarchyAdapter(clazz, AbstractClassAdapter())
    }

    fun <T> registerAbstractClassHierarchy(clazz: Class<T>) {
        registerTypeHierarchyAdapter(clazz, AbstractClassAdapter())
    }

    fun registerInterface(type: Type) {
        registerTypeAdapter(type, AbstractClassAdapter())
    }

    fun registerAbstractClass(type: Type) {
        registerTypeAdapter(type, AbstractClassAdapter())
    }

    fun registerTypeAdapterFactory(factory: TypeAdapterFactory) {
        applyChange(factory) { builder: GsonBuilder ->
            builder.registerTypeAdapterFactory(factory)
        }
    }

    fun registerTypeAdapter(type: Type, typeAdapter: Any) {
        applyChange(typeAdapter) { builder: GsonBuilder ->
            if (typeAdapter is InstanceCreator<*>) {
                instanceCreators[type] = typeAdapter
            }
            builder.registerTypeAdapter(type, typeAdapter)
        }
    }

    fun getGson(): Gson? {
        checkForChanges()
        return gson
    }

    fun disableHtmlEscaping() {
        applyChange("HTML_ESCAPING") { obj: GsonBuilder -> obj.disableHtmlEscaping() }
    }

    fun registerTypeHierarchyAdapter(type: Class<*>, adapterFactory: Any) {
        applyChange(adapterFactory) { builder: GsonBuilder ->
            builder.registerTypeHierarchyAdapter(type, adapterFactory)
        }
    }

    fun <T> getAdapter(typeToken: TypeToken<T>): TypeAdapter<T> {
        checkForChanges()
        return gson!!.getAdapter(typeToken)
    }
}

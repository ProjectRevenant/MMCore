package com.gestankbratwurst.core.mmcore.data.mongodb

import com.mongodb.MongoClientSettings
import com.mongodb.client.FindIterable
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Indexes
import com.mongodb.client.model.ReplaceOptions
import com.mongodb.client.model.Sorts
import com.mongodb.client.result.DeleteResult
import org.bson.codecs.configuration.CodecRegistries
import org.bson.conversions.Bson
import java.util.function.Consumer

open class TypedMongoStorage<K, V>(
    mongoCollection: MongoCollection<V>,
    valueClass: Class<V>,
    private val keyFunction: (V) -> K
) {

    private val mongoCollection: MongoCollection<V>

    init {
        val codecRegistry = CodecRegistries.fromRegistries(
            CodecRegistries.fromCodecs(TypedMongoCodec(valueClass)),
            MongoClientSettings.getDefaultCodecRegistry()
        )
        this.mongoCollection = mongoCollection.withCodecRegistry(codecRegistry)
    }

    operator fun set(key: K, value: V) {
        write(key, value)
    }

    open operator fun get(key: K): V? {
        return load(key)
    }

    operator fun contains(key: K): Boolean {
        return load(key) != null
    }

    fun createHashedIndex(property: String) {
        createIndex(Indexes.hashed(property))
    }

    fun createIndex(index: Bson) {
        mongoCollection.createIndex(index)
    }

    fun write(key: K, value: V) {
        val filter = Filters.eq(key)
        val options = ReplaceOptions().upsert(true)
        mongoCollection.replaceOne(filter, value, options)
    }

    fun persist(value: V): V {
        this.write(keyFunction.invoke(value), value)
        return value
    }

    fun delete(key: K): DeleteResult {
        return mongoCollection.deleteOne(Filters.eq(key))
    }

    open fun load(key: K): V? {
        return mongoCollection.find(Filters.eq(key)).first()
    }

    fun loadAllKeys(): Set<K> {
        return mongoCollection.find().map { value: V -> keyFunction.invoke(value) }.toSet()
    }

    fun write(map: Map<K, V>) {
        map.forEach { (key: K, value: V) -> this.write(key, value) }
    }

    fun delete(keys: Collection<K>) {
        keys.forEach(Consumer { key: K -> this.delete(key) })
    }

    fun queryLazy(filter: Bson? = null): FindIterable<V> {
        return filter?.let { mongoCollection.find(filter) } ?: mongoCollection.find()
    }

    fun queryTop(property: String, limit: Int): List<V> {
        return query({ it.sort(Sorts.descending(property)).limit(limit) })
    }

    fun query(action: (FindIterable<V>) -> Unit, filter: Bson? = null): List<V> {
        val iterable = filter?.let { mongoCollection.find(filter) } ?: mongoCollection.find()
        action.invoke(iterable)
        return iterable.toList()
    }

    fun <E> findAll(property: String, value: E): List<V> {
        return this.findAll(Filters.eq(property, value))
    }

    fun findAll(filter: Bson): List<V> {
        return mongoCollection.find(filter).toList()
    }

    fun <E> deleteAll(property: String, value: E): DeleteResult {
        return this.deleteAll(Filters.eq(property, value))
    }

    fun deleteAll(filter: Bson): DeleteResult {
        return mongoCollection.deleteMany(filter)
    }
}

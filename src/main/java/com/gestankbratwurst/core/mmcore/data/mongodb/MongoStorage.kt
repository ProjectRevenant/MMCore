package com.gestankbratwurst.core.mmcore.data.mongodb

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.CreateCollectionOptions
import com.gestankbratwurst.core.mmcore.data.mongodb.annotationframework.MappedMongoStorage

class MongoStorage {

    val access: MongoAccess = MongoAccess()

    fun <T> collectionOf(
        name: String,
        type: Class<T>,
        options: CreateCollectionOptions
    ): MongoCollection<T> {
        if (!access.database.listCollectionNames().contains(name)) {
            access.database.createCollection(name, options)
        }
        return access.database.getCollection(name, type)
    }

    fun <T> collectionOfName(name: String, type: Class<T>): MongoCollection<T> {
        return access.database.getCollection(name, type)
    }

    fun <K, V> typed(
        collectionName: String,
        valueClass: Class<V>,
        keyFunction: (V) -> K
    ): TypedMongoStorage<K, V> {
        return TypedMongoStorage(
            collectionOfName(collectionName, valueClass),
            valueClass,
            keyFunction
        )
    }

    fun <K, V> mapped(collectionName: String, valueClass: Class<V>): MappedMongoStorage<K, V> {
        return MappedMongoStorage(collectionOfName(collectionName, valueClass), valueClass)
    }
}

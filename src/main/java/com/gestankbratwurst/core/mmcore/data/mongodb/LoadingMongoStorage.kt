package com.gestankbratwurst.core.mmcore.data.mongodb

import com.gestankbratwurst.core.mmcore.data.mongodb.annotationframework.MappedMongoStorage

open class LoadingMongoStorage<K, V>(collectionName: String, valueClass: Class<V>, private val creator: (K) -> V) :
    MappedMongoStorage<K, V>(collectionName, valueClass) {

    override fun get(key: K): V {
        return load(key)
    }

    override fun load(key: K): V {
        return super.load(key) ?: persist(creator.invoke(key))
    }
}

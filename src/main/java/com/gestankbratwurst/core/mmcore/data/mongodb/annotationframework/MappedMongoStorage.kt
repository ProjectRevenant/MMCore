package com.gestankbratwurst.core.mmcore.data.mongodb.annotationframework

import com.mongodb.client.MongoCollection
import com.gestankbratwurst.core.mmcore.data.mongodb.MongoStorage
import com.gestankbratwurst.core.mmcore.data.mongodb.TypedMongoStorage

open class MappedMongoStorage<K, V>(mongoCollection: MongoCollection<V>, valueClass: Class<V>) :
    TypedMongoStorage<K, V>(mongoCollection, valueClass, { value -> IdentityFetcher.fetchOrGenerate(value) }) {

    constructor(collectionName: String, valueClass: Class<V>) : this(MongoStorage().collectionOfName(collectionName, valueClass), valueClass)
}

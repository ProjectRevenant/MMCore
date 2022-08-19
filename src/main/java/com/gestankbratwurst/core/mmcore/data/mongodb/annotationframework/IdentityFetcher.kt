package com.gestankbratwurst.core.mmcore.data.mongodb.annotationframework

import org.bson.types.ObjectId
import org.reflections.ReflectionUtils
import java.lang.reflect.Field
import java.util.UUID

@Suppress("UNCHECKED_CAST")
object IdentityFetcher {

    fun <T> generateId(clazz: Class<T>): T {
        return when (clazz) {
            UUID::class.java -> UUID.randomUUID() as T
            ObjectId::class.java -> ObjectId.get() as T
            String::class.java -> UUID.randomUUID().toString() as T
            else -> throw Exception("Unsupported identity type: $clazz")
        }
    }

    fun <K, V> fetchOrGenerate(value: V): K {
        val clazz = value!!::class.java
        val fields = ReflectionUtils.getFields(clazz)
        for (field in fields) {
            val identityAnnotation = field.getAnnotation(Identity::class.java) ?: continue
            return if (identityAnnotation.generated) {
                generate(value, field)
            } else {
                fetch(value, field)
            }
        }
        throw Exception("No Identity field specified in $clazz")
    }

    private fun <K, V> generate(value: V, field: Field): K {
        field.isAccessible = true
        val fieldType = field.type as Class<K>
        val fieldValue = field.get(value)
        return if (fieldValue == null) {
            val generated = generateId(fieldType)
            field.set(value, generated)
            generated
        } else {
            fieldValue as K
        }
    }

    private fun <K, V> fetch(value: V, field: Field): K {
        if (!field.canAccess(value)) {
            field.isAccessible = true
        }
        val fieldValue = field.get(value)
        return if (fieldValue == null) {
            throw Exception("Null is not a valid identity. $field")
        } else {
            fieldValue as K
        }
    }
}

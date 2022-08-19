package com.gestankbratwurst.core.mmcore.data.mongodb.annotationframework

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Identity(val generated: Boolean = false)

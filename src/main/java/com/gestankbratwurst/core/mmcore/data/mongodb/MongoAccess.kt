package com.gestankbratwurst.core.mmcore.data.mongodb

import com.gestankbratwurst.core.mmcore.data.config.MMCoreConfiguration
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import org.bson.UuidRepresentation

class MongoAccess {

    private val client: MongoClient
    val database: MongoDatabase

    init {
        val driverProperties = MMCoreConfiguration.get().mongoDriverProperties
        val host = driverProperties.hostAddress
        val port = driverProperties.hostPort
        val user = driverProperties.user
        val password = driverProperties.password
        val databaseName = driverProperties.database
        val connection = "mongodb://$user:$password@$host:$port"
        val settings = MongoClientSettings.builder()
            .uuidRepresentation(UuidRepresentation.STANDARD)
            .applyConnectionString(ConnectionString(connection))
            .build()
        client = MongoClients.create(settings)
        database = client.getDatabase(databaseName)
    }

    fun disconnect() {
        client.close()
    }
}

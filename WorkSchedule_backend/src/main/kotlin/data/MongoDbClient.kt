package com.example.data

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.toList
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.pojo.PojoCodecProvider

object MongoDbClient {
    private const val CONNECTION_STRING =
        "mongodb+srv://nguyenminhcanhc2016:12345@cluster0.igjpv.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0"

    val database: MongoDatabase by lazy {
        try {
            val codecRegistry = CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())
            )

            val settings = MongoClientSettings.builder()
                .applyConnectionString(ConnectionString(CONNECTION_STRING))
                .codecRegistry(codecRegistry)
                .build()

            val client = MongoClient.create(settings)
            val db = client.getDatabase("WorkSchedule")

            println("✅ Connected to MongoDB successfully")
            println("📊 Database name: ${db.name}")

            db
        } catch (e: Exception) {
            println("❌ MongoDB Connection Error: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }

    // Method để test connection
    suspend fun testConnection(): Boolean {
        return try {
            // List collections để test connection
            val collections = database.listCollectionNames()
            println("📋 Available collections: ${collections.toList()}")
            true
        } catch (e: Exception) {
            println("❌ Connection test failed: ${e.message}")
            e.printStackTrace()
            false
        }
    }
}
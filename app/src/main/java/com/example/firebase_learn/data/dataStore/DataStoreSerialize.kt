package com.example.firebase_learn.data.dataStore

import androidx.datastore.core.Serializer
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.OutputStream

object DataStoreSerialize:Serializer<DataStoreHelper> {
    override val defaultValue: DataStoreHelper
        get() = DataStoreHelper()

    override suspend fun readFrom(input: InputStream): DataStoreHelper {
        return Gson().fromJson(input.readBytes().decodeToString(),DataStoreHelper::class.java)
    }

    override suspend fun writeTo(t: DataStoreHelper, output: OutputStream) {
        val gsonConverter= Gson().toJson(t)
        withContext(Dispatchers.IO) {
            output.write(gsonConverter.toByteArray())
        }
    }
}
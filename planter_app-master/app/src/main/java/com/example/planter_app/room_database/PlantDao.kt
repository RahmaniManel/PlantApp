package com.example.planter_app.room_database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantDao{
    @Upsert
    suspend fun upsertData(data:PlantTable)

    @Delete
    suspend fun deleteData(data: PlantTable)

    @Query("DELETE FROM data_table")
    suspend fun deleteAllData()


    @Query("SELECT * FROM data_table")
    fun readAllData(): Flow<List<PlantTable>>
}
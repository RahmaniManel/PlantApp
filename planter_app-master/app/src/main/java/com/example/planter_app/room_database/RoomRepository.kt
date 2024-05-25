package com.example.planter_app.room_database

import kotlinx.coroutines.flow.Flow

class RoomRepository(private val plantDao: PlantDao) {
    val readAllData: Flow<List<PlantTable>> = plantDao.readAllData()

    suspend fun upsertData(data: PlantTable) {
        plantDao.upsertData(data)
    }

    suspend fun deleteData(data: PlantTable) {
        plantDao.deleteData(data)
    }
    suspend fun deleteAllData() {
        plantDao.deleteAllData()
    }
}

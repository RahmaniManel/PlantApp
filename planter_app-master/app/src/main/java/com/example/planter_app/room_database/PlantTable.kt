package com.example.planter_app.room_database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "data_table")
data class PlantTable(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val dateAndTime: String?=null,
    val diseaseName: String?=null,
    val treatment: String?=null,
)
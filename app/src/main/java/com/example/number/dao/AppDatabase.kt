package com.example.number.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.number.dao.model.NumberModel

@Database(entities = [NumberModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun numberDao(): NumberDao
}
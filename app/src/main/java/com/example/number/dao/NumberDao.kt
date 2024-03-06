package com.example.number.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.number.dao.model.NumberModel

/**
 * Data Access Object (DAO) interface for NumberModel.
 */
@Dao
interface NumberDao {

    /**
     * Retrieve all NumberModel objects from the database.
     *
     * @return A List of NumberModel objects.
     */
    @Query("SELECT * FROM numbers")
    fun getAll(): List<NumberModel>

    /**
     * Retrieve a specific NumberModel object from the database based on its ID.
     *
     * @param number The ID of the NumberModel to retrieve.
     * @return A List containing the requested NumberModel object.
     */
    @Query("SELECT * FROM numbers WHERE id IN (:number)")
    fun getNumber(number: Int): List<NumberModel>

    /**
     * Insert one or more NumberModel objects into the database.
     *
     * @param numberModels The NumberModel objects to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg numberModel: NumberModel)

    /**
     * Delete a specific NumberModel object from the database.
     *
     * @param numberModel The NumberModel object to delete.
     */
    @Delete
    fun delete(numberModel: NumberModel)
}
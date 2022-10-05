package com.str.wardrobe.simpleMVVM.views.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.str.wardrobe.simpleMVVM.views.model.entities.NamedDress

@Dao
interface DressDao {

    @Insert
    fun insertData(data: NamedDress)

    @Transaction
    @Query("SELECT * FROM namedDress")
    fun getAllDresses(): LiveData<List<NamedDress>>

    @Transaction
    @Query("SELECT * FROM namedDress WHERE category=:category")
    fun getDressesOfCategory(category: String): LiveData<List<NamedDress>>

    @Transaction
    @Query("SELECT * FROM namedDress WHERE name=:name")
    fun getDress(name: String): LiveData<NamedDress>

    @Transaction
    @Insert
    fun addDress(dress: NamedDress)

    @Transaction
    @Update
    fun updateDress(dress: NamedDress)

}
package com.str.wardrobe.simpleMVVM.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.str.wardrobe.simpleMVVM.model.entities.NamedCategory

@Dao
interface CategoryDao {

    @Insert
    fun insertData(data: NamedCategory)

    @Transaction
    @Query("SELECT * FROM namedCategory WHERE name=:name")
    fun getDressCategory(name: String): LiveData<NamedCategory>

    @Transaction
    @Query("SELECT * FROM namedCategory")
    fun getAllCategory(): LiveData<List<NamedCategory>>

    @Transaction
    @Query("SELECT name FROM namedCategory")
    fun getAllCategoryNames(): LiveData<List<String>>

    @Transaction
    @Insert
    fun addCategory(category: NamedCategory)

    @Transaction
    @Update
    fun updateCategory(category: NamedCategory)

    @Transaction
    @Delete
    fun deleteCategory(category: NamedCategory)

}
package com.str.wardrobe.simpleMVVM.views.model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.str.wardrobe.simpleMVVM.views.model.baserepositories.CategoryBaseRepository
import com.str.wardrobe.simpleMVVM.views.model.baserepositories.CategoryListener
import com.str.wardrobe.simpleMVVM.views.model.baserepositories.DressBaseRepository
import com.str.wardrobe.simpleMVVM.views.model.baserepositories.DressListener
import com.str.wardrobe.simpleMVVM.views.model.database.CategoryDatabase
import com.str.wardrobe.simpleMVVM.views.model.database.DressDatabase
import com.str.wardrobe.simpleMVVM.views.model.entities.NamedCategory
import com.str.wardrobe.simpleMVVM.views.model.entities.NamedDress

private const val CATEGORY_DATABASE_NAME="categoryInfo-database"
private const val DRESS_DATABASE_NAME="dressInfo-database"

class WardrobeRepository (context: Context) : CategoryBaseRepository, DressBaseRepository {

    // Destructive migrations are enabled and a prepackaged database
    // is provided.
    private val categoryDatabase: CategoryDatabase = Room.databaseBuilder(
        context.applicationContext,
        CategoryDatabase::class.java,
        CATEGORY_DATABASE_NAME)
//        .createFromAsset("databases/categoryInfo-database.db")
        .fallbackToDestructiveMigration()
        .build()

    private val dressDatabase: DressDatabase = Room.databaseBuilder(
        context.applicationContext,
        DressDatabase::class.java,
        DRESS_DATABASE_NAME)
//        .createFromAsset("databases/dressInfo-database.db")
        .fallbackToDestructiveMigration()
        .build()

    private val namedCategoryDao = categoryDatabase.namedCategoryDao()
    private val namedDressDao = dressDatabase.namedCategoryDao()

    private val listenersDress = mutableSetOf<DressListener>()
    private val listenersCategory = mutableSetOf<CategoryListener>()

    companion object {
        private var INSTANCE: WardrobeRepository? = null

        fun initialize(context: Context) {
            if(INSTANCE == null) {
                INSTANCE = WardrobeRepository(context)
            }
        }

        fun get(): WardrobeRepository {
            return INSTANCE?:
            throw IllegalStateException("Авария")
        }
    }

    // С файлом [Exceptions] позже постараться реализовать  проверку на ошибки, попытку создать одежду/категорию с названием, которое уже существует
    // Пока не знаю как именно реализовать оное с LiveData

    fun getCategoryByName(name: String): LiveData<NamedCategory> {
        return namedCategoryDao.getDressCategory(name)
    }

    fun getAllCategory(): LiveData<List<NamedCategory?>> {
        return namedCategoryDao.getAllCategory()
    }

    fun getAvailableDresses(): LiveData<List<NamedDress?>> {
        return namedDressDao.getAllDresses()
    }

    fun getDressesOfCategory(category: String?): LiveData<List<NamedDress>> {

        return namedDressDao.getDressesOfCategory(category)
    }

   fun getDressByName(name: String): LiveData<NamedDress> {
        return namedDressDao.getDress(name)
    }

    fun addCategory(category: NamedCategory) {
        namedCategoryDao.addCategory(category)
    }

    fun addDress(dress: NamedDress) {
        namedDressDao.addDress(dress)
    }

    fun updateCategory(category: NamedCategory) {
        namedCategoryDao.updateCategory(category)
    }

    fun updateDress(dress: NamedDress) {
        namedDressDao.updateDress(dress)
    }

    override fun addListenerToCategory(listener: CategoryListener, category: NamedCategory) {
        listenersCategory += listener
        listener(category)
    }

    override fun removeListenerFromCategory(listener: CategoryListener) {
        listenersCategory -= listener
    }

    override fun addListenerToDress(listener: DressListener, dress: NamedDress) {
        listenersDress += listener
        listener(dress)
    }

    override fun removeListenerFromDress(listener: DressListener) {
        listenersDress -= listener
    }
}
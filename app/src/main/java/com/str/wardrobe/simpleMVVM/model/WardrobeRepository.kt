package com.str.wardrobe.simpleMVVM.model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.str.wardrobe.simpleMVVM.model.baserepositories.CategoryBaseRepository
import com.str.wardrobe.simpleMVVM.model.baserepositories.CategoryListener
import com.str.wardrobe.simpleMVVM.model.baserepositories.DressBaseRepository
import com.str.wardrobe.simpleMVVM.model.baserepositories.DressListener
import com.str.wardrobe.simpleMVVM.model.database.WardrobeDatabase
import com.str.wardrobe.simpleMVVM.model.entities.NamedCategory
import com.str.wardrobe.simpleMVVM.model.entities.NamedDress

private const val WARDROBE_DATABASE_NAME="wardrobe_database"

class WardrobeRepository(context: Context) : CategoryBaseRepository, DressBaseRepository {

    // Destructive migrations are enabled and a prepackaged database
    // is provided.
    private val wardrobeDatabase: WardrobeDatabase by lazy<WardrobeDatabase> {
        Room.databaseBuilder(
            context.applicationContext,
            WardrobeDatabase::class.java,
            WARDROBE_DATABASE_NAME
        )
            .createFromAsset("databases/wardrobe_database.db")
            .fallbackToDestructiveMigration()
            .build()
    }

        private val namedCategoryDao = wardrobeDatabase.namedCategoryDao()
        private val namedDressDao = wardrobeDatabase.namedDressDao()

        private val listenersDress = mutableSetOf<DressListener>()
        private val listenersCategory = mutableSetOf<CategoryListener>()

        override var allCategory: LiveData<List<NamedCategory>>? = null

        override var currentCategory: NamedCategory? = null

        override var currentDresses: LiveData<List<NamedDress>>? = null

        // С файлом [Exceptions] позже постараться реализовать  проверку на ошибки, попытку создать одежду/категорию с названием, которое уже существует
        // Пока не знаю как именно реализовать оное с LiveData

        fun getCategoryByName(name: String): LiveData<NamedCategory> {
            return namedCategoryDao.getDressCategory(name)
        }

        fun getCategories(): LiveData<List<NamedCategory>> {
            return namedCategoryDao.getAllCategory()
        }

        fun getAvailableDresses(): LiveData<List<NamedDress>> {
            return namedDressDao.getAllDresses()
        }

        fun getDressesOfCategory(category: String): LiveData<List<NamedDress>> {
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

}
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
import java.util.concurrent.Executors

private const val WARDROBE_DATABASE_NAME="wardrobe_database"

class WardrobeRepository(context: Context) : CategoryBaseRepository, DressBaseRepository {

    // Destructive migrations are enabled and a prepackaged database
    // is provided.
    private val wardrobeDatabase: WardrobeDatabase by lazy<WardrobeDatabase> {
        Room.databaseBuilder(
            context,
            WardrobeDatabase::class.java,
            WARDROBE_DATABASE_NAME
        )
            .createFromAsset("databases/wardrobe_database.db")
            .fallbackToDestructiveMigration()
            .build()
    }

        private val namedCategoryDao = wardrobeDatabase.namedCategoryDao()
        private val namedDressDao = wardrobeDatabase.namedDressDao()

        private val executor = Executors.newSingleThreadExecutor()

        private val listenersDress = mutableSetOf<DressListener>()
        private val listenersCategory = mutableSetOf<CategoryListener>()

        // С файлом [Exceptions] позже постараться реализовать  проверку на ошибки, попытку создать одежду/категорию с названием, которое уже существует
        // Пока не знаю как именно реализовать оное с LiveData

        fun getCategoryByName(name: String): LiveData<NamedCategory> {
            return namedCategoryDao.getDressCategory(name)
        }

        fun getCategories(): LiveData<List<NamedCategory>> {
            return namedCategoryDao.getAllCategory()
        }

        fun getCategoriesName(): LiveData<List<String>> {
            return namedCategoryDao.getAllCategoryNames()
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
            executor.execute {
                namedDressDao.addDress(dress)
            }
        }

        fun updateCategory(category: NamedCategory) {
            executor.execute {
                namedCategoryDao.updateCategory(category)
            }

        }

        fun updateDress(dress: NamedDress) {
            namedDressDao.updateDress(dress)
        }

        fun deleteCategory(category: NamedCategory) {
            namedCategoryDao.deleteCategory(category)
        }

        fun deleteDress(dress: NamedDress) {
            namedDressDao.deleteDress(dress)
        }

        fun deleteDresses(dresses: List<NamedDress>) {
            namedDressDao.deleteDresses(dresses)
        }

    override var allCategory: LiveData<List<NamedCategory>>? = null
        set(value) {
            if (field != value) {
                field = value
                listenersCategory.forEach { it(value.value) }
            }
        }

    override fun addListenerToCategory(listener: CategoryListener, category: NamedCategory) {
            listenersCategory += listener
            listener(category)
        }

        override fun removeListenerFromCategory(listener: CategoryListener) {
            listenersCategory -= listener
        }

    override var allDresses: List<NamedDress>?
        get() = TODO("Not yet implemented")
        set(value) {}

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
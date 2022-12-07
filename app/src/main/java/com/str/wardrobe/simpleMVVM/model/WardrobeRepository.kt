package com.str.wardrobe.simpleMVVM.model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.str.wardrobe.simpleMVVM.model.baserepositories.*
import com.str.wardrobe.simpleMVVM.model.database.WardrobeDatabase
import com.str.wardrobe.simpleMVVM.model.entities.NamedCategory
import com.str.wardrobe.simpleMVVM.model.entities.NamedDress
import java.io.File
import java.util.concurrent.Executors

private const val WARDROBE_DATABASE_NAME="wardrobe_database"

class WardrobeRepository(context: Context) : WardrobeBaseRepository {

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
        private val filesDir = context.applicationContext.filesDir

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

        fun getDressById(id: Int): LiveData<NamedDress> {
            return namedDressDao.getDressOfId(id)
        }

        fun addCategory(category: NamedCategory) {
            executor.execute {
                namedCategoryDao.addCategory(category)
            }
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
            executor.execute {
                namedDressDao.updateDress(dress)
            }
        }

        fun deleteCategory(category: NamedCategory) {
            executor.execute {
                namedCategoryDao.deleteCategory(category)
            }
        }

        fun deleteDress(dress: NamedDress) {
            executor.execute {
                namedDressDao.deleteDress(dress)
            }
        }

        fun deleteDresses(dresses: List<NamedDress>) {
            executor.execute {
                namedDressDao.deleteDresses(dresses)
            }
        }

    override var allCategory: LiveData<List<NamedCategory>> = getCategories()
        set(value) {
            if (field != value) {
                field = value
                listenersCategory.forEachIndexed { index, _ ->
                   (value.value?.get(index))
                }
            }
        }

    // Избавиться от этих двух
    override fun addListenerToCategory(listeners: CategoriesListener) {
        listenersCategory += (listeners) as MutableSet<CategoryListener>
        allCategory.value?.let { listeners(it) }
        }

    override fun removeListenerFromCategory(listeners: CategoriesListener) {
        listenersCategory -= (listeners) as MutableSet<CategoryListener>
    }

    override var allDresses: LiveData<List<NamedDress>>? = getAvailableDresses()
        set(value) {
            if (field != value) {
                field = value
                listenersCategory.forEachIndexed { index, _ ->
                    (value!!.value?.get(index))
                }
            }
        }

    fun getPhotoFile(dress: NamedDress): File = File(filesDir, dress.photoFileName)

    // Избавиться от этих двух
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
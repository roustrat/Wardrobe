package com.str.foundation.views

import android.app.Application
import androidx.fragment.app.viewModels
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.str.foundation.ARG_SCREEN
import com.str.foundation.BaseApplication
import com.str.wardrobe.simpleMVVM.App
import java.lang.reflect.Constructor

/**
 * Use this method for getting view-models from your fragments
 */

inline fun <reified VM : ViewModel> BaseFragment.screenViewModel() = viewModels<VM> {
    val application = requireActivity().application
    val screen = requireArguments().getSerializable(ARG_SCREEN) as BaseScreen

    val activityScopeViewModel = (requireActivity() as FragmentsHolder).getActivityScopeViewModel()

    // forming the list of available dependencies:
    // - singleton scope dependencies (repositories) -> from App class
    // - activity VM scope dependencies -> from MainViewModel
    // - screen VM scope dependencies -> screen args
//    val dependencyRepository = (application as? BaseApplication)?.repositories ?: emptyList()
    val dependencyRepository = (application as App).repositories1

    val dependencies = listOf(screen, activityScopeViewModel) + dependencyRepository

//    //Отчаянная попытка
//    val dependencyRepository = (application as App).getRepository(application.applicationContext)
//    val dependencies = listOf(screen, activityScopeViewModel) + dependencyRepository

            // creating factory
    ViewModelFactory(dependencies, this)
}

class ViewModelFactory(
    private val dependencies: List<Any>,
    owner: SavedStateRegistryOwner
) : AbstractSavedStateViewModelFactory(owner, null) {

    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        val constructors = modelClass.constructors
        val constructor = constructors.maxByOrNull { it.typeParameters.size }!!

        // - SavedStateHandle is also a dependency from screen VM scope, but we can obtain it only here,
        //   that's why merging it with the list of other dependencies:
        val dependenciesWithSavedState = dependencies + handle

        // generating the list of arguments to be passed into the view-model's constructor
        val arguments = findDependencies(constructor, dependenciesWithSavedState)
        // creating view-model
        return constructor.newInstance(*arguments.toTypedArray()) as T
    }

    private fun findDependencies(constructor: Constructor<*>, dependencies: List<Any>): List<Any> {
        val args = mutableListOf<Any>()
        // here we iterate through view-model's constructor arguments and for each
        // argument we search dependency that can be assigned to the argument
        constructor.parameterTypes.forEach { parameterClass ->
            val dependency = dependencies.first { parameterClass.isAssignableFrom(it.javaClass) }
            args.add(dependency)
        }
        return args
    }

}

//typealias ViewModelCreator<VM> = () -> VM

//class ViewModelFactory<VM : ViewModel>(
//    private val viewModelCreator: ViewModelCreator<VM>
//) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        return viewModelCreator() as T
//    }
//}
//
//inline fun <reified VM : ViewModel> Fragment.viewModelCreator(noinline creator: ViewModelCreator<VM>): Lazy<VM> {
//    return viewModels { ViewModelFactory(creator) }
//}
//
//inline fun <reified VM : ViewModel> ComponentActivity.viewModelCreator(noinline creator: ViewModelCreator<VM>): Lazy<VM> {
//    return viewModels { ViewModelFactory(creator) }
//}
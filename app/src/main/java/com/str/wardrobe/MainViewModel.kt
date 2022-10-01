package com.str.wardrobe

import android.app.Application
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import com.str.wardrobe.simpleMVVM.views.Navigator
import com.str.wardrobe.simpleMVVM.views.UiActions
import com.str.wardrobe.simpleMVVM.views.base.BaseScreen
import com.str.wardrobe.simpleMVVM.views.base.LiveEvent
import com.str.wardrobe.simpleMVVM.views.base.MutableLiveEvent
import com.str.wardrobe.simpleMVVM.views.utils.Event
import com.str.wardrobe.simpleMVVM.views.utils.ResourceActions

const val ARG_SCREEN = "ARG_SCREEN"

/**
 * Implementation of [Navigator] and [UiActions].
 * It is based on activity view-model because instances of [Navigator] and [UiActions]
 * should be available from fragments' view-models (usually they are passed to the view-model constructor).
 *
 * This view-model extends [AndroidViewModel] which means that it is not "usual" view-model and
 * it may contain android dependencies (context, bundles, etc.).
 */
class MainViewModel(
    application: Application
) : AndroidViewModel(application), Navigator, UiActions {

    val whenActivityActive = ResourceActions<MainActivity>()

    private val _result = MutableLiveEvent<Any>()
    val result: LiveEvent<Any> = _result

    override fun launch(screen: BaseScreen) = whenActivityActive {
        launchFragment(it, screen)
    }

    override fun launchWithResult(screen: BaseScreen, result: Any?) = whenActivityActive {
        // Понять, дойдет ли result до фрагмента
        if (result != null) {
            _result.value = Event(result)
        }
        launchFragment(it, screen)
    }

    override fun goBack(result: Any?) = whenActivityActive {
        if (result != null) {
            _result.value = Event(result)
        }
        it.onBackPressed()
    }

    override fun toast(message: String) {
        TODO("Not yet implemented")
    }

    override fun getString(messageRes: Int, vararg args: Any): String {
        TODO("Not yet implemented")
    }

    fun launchFragment(activity: MainActivity, screen: BaseScreen, addToBackStack: Boolean = true) {
        // as screen classes are inside fragments -> we can create fragment directly from screen
        val fragment = screen.javaClass.enclosingClass.newInstance() as Fragment
        // set screen object as fragment's argument
        fragment.arguments = bundleOf(ARG_SCREEN to screen)

        val transaction = activity.supportFragmentManager.beginTransaction()
        if (addToBackStack) transaction.addToBackStack(null)
        transaction
            .setCustomAnimations(
                R.anim.enter,
                R.anim.exit,
                R.anim.pop_enter,
                R.anim.pop_exit
            )
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    override fun onCleared() {
        super.onCleared()
        whenActivityActive.clear()
    }

}
package com.str.foundation.navigator

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.str.foundation.ARG_SCREEN
import com.str.foundation.utils.Event
import com.str.foundation.views.BaseFragment
import com.str.foundation.views.BaseScreen
import com.str.wardrobe.R
import com.str.wardrobe.simpleMVVM.MainActivity
import com.str.wardrobe.simpleMVVM.views.categorydresses.DressesCategoryFragment
import com.str.wardrobe.simpleMVVM.views.dressInfo.DressInfoFragment

class StackFragmentNavigator(
    private val activity: AppCompatActivity,
    @IdRes private val containerId: Int,
    private val initialScreenCreator: () -> BaseScreen
) : Navigator {

    private var result: Event<Any>? = null

    override fun launch(screen: BaseScreen) {
        launchFragment(screen)
    }

    override fun launchWithRemove(screen: BaseScreen) {
        launchFragmentWithRemove(screen)
    }

    override fun launchWithResult(screen: BaseScreen, result: Any?) {
        // Понять, дойдет ли result до фрагмента
        if (result != null) {
            this.result = Event(result)
        }
        launchFragment(screen)
    }

    override fun goBack(result: Any?) {
        if (result != null) {
            this.result = Event(result)
        }
        comeBack()
//        activity.onBackPressed()
    }

    fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            // define the initial screen that should be launched when app starts.
            launchFragment(
                screen = DressesCategoryFragment.Screen(),
                addToBackStack = false
            )
        }
        activity.supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentCallbacks, false)
    }

    fun onDestroy() {
        activity.supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentCallbacks)
    }

    fun comeBack() {
        val currentFragment = activity.supportFragmentManager.findFragmentById(R.id.fragmentContainer) as Fragment
        activity.supportFragmentManager.popBackStack()
        val transaction = activity.supportFragmentManager.beginTransaction()
        transaction
            .setReorderingAllowed(true)
            .remove(currentFragment)
            .commit()
    }

    fun launchFragment(screen: BaseScreen, addToBackStack: Boolean = true) {
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
            .setReorderingAllowed(true)
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    fun launchFragmentWithRemove(screen: BaseScreen, addToBackStack: Boolean = true) {
        val currentFragment = activity.supportFragmentManager.findFragmentById(R.id.fragmentContainer) as Fragment
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
            .setReorderingAllowed(true)
            .replace(R.id.fragmentContainer, fragment)
            .remove(currentFragment)
            .commit()
    }

    fun notifyScreenUpdates(fragment: Fragment) {
//        val fragment = activity.supportFragmentManager.findFragmentById(R.id.fragmentContainer)

//        if (activity.supportFragmentManager.backStackEntryCount > 0) {
//            // more than 1 screen -> show back button in the toolbar
//            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        } else {
//            activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
//        }

//        if (fragment == DressesCategoryFragment::class.java) {
//            // more than 1 screen -> show back button in the toolbar
//
//            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
//            Log.d("notifyScreenUpdate", "setDisplayHomeAsUpEnabled(true)")
//        } else {
//            activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
//            Log.d("notifyScreenUpdate", "setDisplayHomeAsUpEnabled(false)")
//        }

//        if (f is HasScreenTitle && f.getScreenTitle() != null) {
//            // fragment has custom screen title -> display it
//            supportActionBar?.title = f.getScreenTitle()
//        } else {
//            supportActionBar?.title = getString(R.string.app_name)
//        }
    }

    private fun publishResults(fragment: Fragment) {
        val result = result?.getValue() ?: return
        if (fragment is BaseFragment) {
            // has result that can be delivered to the screen's view-model
            fragment.viewModel.onResult(result)
        }
    }

    private val fragmentCallbacks = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
            notifyScreenUpdates(f)
            publishResults(f)
        }
    }


}


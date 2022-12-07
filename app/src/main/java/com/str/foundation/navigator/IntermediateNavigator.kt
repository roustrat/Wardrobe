package com.str.foundation.navigator

import androidx.fragment.app.Fragment
import com.str.foundation.utils.ResourceActions
import com.str.foundation.views.BaseScreen

class IntermediateNavigator : Navigator {

    private val targetNavigator = ResourceActions<Navigator>()

    override fun launch(screen: BaseScreen) = targetNavigator {
        it.launch(screen)
    }

    override fun launchWithRemove(screen: BaseScreen) = targetNavigator {
        it.launchWithRemove(screen)
    }

    override fun launchWithResult(screen: BaseScreen, result: Any?) = targetNavigator {
        it.launchWithResult(screen, result)
    }

    override fun goBack(result: Any?) = targetNavigator {
        it.goBack(result)
    }

    fun setTarget(navigator: Navigator?) {
        targetNavigator.resource = navigator
    }

    fun clear() {
        targetNavigator.clear()
    }
}
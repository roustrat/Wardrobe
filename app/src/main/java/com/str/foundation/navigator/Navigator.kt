package com.str.foundation.navigator

import com.str.foundation.views.BaseScreen

/**
 * Navigation for your application
 */
interface Navigator {

    /**
     * Launch a new screen at the top of back stack.
     */
    fun launch(screen: BaseScreen)

    /**
     * Go back to the previous screen and optionally send some results.
     */
    fun launchWithResult(screen: BaseScreen, result: Any? = null)

    /**
     * Go ahead to the next screen and optionally send some results.
     */
    fun goBack(result: Any? = null)

}
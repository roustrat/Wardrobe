package com.str.foundation.views

import androidx.fragment.app.Fragment

/**
 * Base class for all fragments
 */
abstract class BaseFragment : Fragment() {

    /**
     * View-model that manages this fragment
     */
    abstract val viewModel: BaseViewModel

    fun notifyScreenUpdates() {
        // if you have more than 1 activity -> you should use a separate interface instead of direct
        // cast to MainActivity
        (requireActivity() as FragmentsHolder).notifyScreenUpdates()
    }

    abstract fun startCamera()

}
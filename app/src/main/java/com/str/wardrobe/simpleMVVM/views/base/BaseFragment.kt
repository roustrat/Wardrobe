package com.str.wardrobe.simpleMVVM.views.base

import androidx.fragment.app.Fragment

/**
 * Base class for all fragments
 */
abstract class BaseFragment : Fragment() {

    /**
     * View-model that manages this fragment
     */
    abstract val viewModel: BaseViewModel

}
package com.str.wardrobe.simpleMVVM.views.categorydresses

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.str.wardrobe.R
import com.str.wardrobe.simpleMVVM.views.base.BaseFragment
import com.str.wardrobe.simpleMVVM.views.base.BaseScreen
import com.str.wardrobe.simpleMVVM.views.base.screenViewModel
import com.str.wardrobe.simpleMVVM.views.model.entities.NamedCategory

class DressesCategoryFragment : BaseFragment() {

    // no arguments for this screen
    class Screen : BaseScreen

    override val viewModel by screenViewModel<DressesCategoryViewModel>()

    private lateinit var addButton: ImageButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding =  inflater.inflate(R.layout.dresses_category, container, false)

        val adapterCategory = CategoriesAdapter(viewModel)
        val adapterDress = DressesAdapter(viewModel)
        setupLayoutManager(binding, adapterCategory, adapterDress)

        viewModel.allCategory.observe(viewLifecycleOwner) {
            if (it != null) {
                adapterCategory.items = it as List<NamedCategory>
                viewModel.currentCategory = it.first()
            }

        }

        viewModel.currentDresses.observe(viewLifecycleOwner) {
            if (it != null) {
                adapterDress.items = it
                viewModel.currentDress = it.first()
            }
        }


        return binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        addButton = view.findViewById(R.id.newDressItem)

    }

    private fun setupLayoutManager(binding: View, adapter1: CategoriesAdapter, adapter2: DressesAdapter) {
        // waiting for list width
        val categoriesRecyclerView = binding.findViewById<RecyclerView>(R.id.categoriesRecyclerView)
        val dressesRecyclerView = binding.findViewById<RecyclerView>(R.id.dressesRecyclerView)

        categoriesRecyclerView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                categoriesRecyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
//                val width = categoriesRecyclerView.width
//                val itemWidth = resources.getDimensionPixelSize(R.dimen.item_width)
//                val columns = width / itemWidth
                categoriesRecyclerView.adapter = adapter1
                categoriesRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            }
        })

        dressesRecyclerView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                dressesRecyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
//                val width = categoriesRecyclerView.width
//                val itemWidth = resources.getDimensionPixelSize(R.dimen.item_width)
//                val columns = width / itemWidth
                dressesRecyclerView.adapter = adapter2
                dressesRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            }
        })

    }

}
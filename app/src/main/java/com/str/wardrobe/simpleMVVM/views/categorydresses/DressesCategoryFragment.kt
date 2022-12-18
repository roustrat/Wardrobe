package com.str.wardrobe.simpleMVVM.views.categorydresses

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageButton
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.str.wardrobe.R
import com.str.foundation.views.BaseFragment
import com.str.foundation.views.BaseScreen
import com.str.foundation.views.screenViewModel
import com.str.wardrobe.simpleMVVM.MainActivity
import com.str.wardrobe.simpleMVVM.model.entities.NamedCategory

class DressesCategoryFragment : BaseFragment() {

    // no arguments for this screen
    class Screen : BaseScreen

    override val viewModel by screenViewModel<DressesCategoryViewModel>()

    private lateinit var addButton: ImageButton
    private lateinit var adapterCategory: CategoriesAdapter
    private lateinit var adapterDress: DressesAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding =  inflater.inflate(R.layout.dresses_category, container, false)

        adapterCategory = CategoriesAdapter(viewModel)
        adapterDress = DressesAdapter(viewModel, requireContext(), requireActivity())

//        val adapterCategory = CategoriesAdapter(viewModel)
//        val adapterDress = DressesAdapter(viewModel, requireContext(), requireActivity())
        setupCategoryLayoutManager(binding, adapterCategory)
        setupDressLayoutManager(binding, adapterDress)

        viewModel.allCategory.observe(viewLifecycleOwner) {
            adapterCategory.items = it as List<NamedCategory>
            if (it != null) {
                if (viewModel.currentDress == null) {
                    viewModel.currentCategory.value = it.first()
                    viewModel.currentDresses = viewModel.updateCurrentDressesValue()
                }
            }
        }

        viewModel.currentCategory.observe(viewLifecycleOwner) {
            if(it != null) {
                viewModel.currentDresses = viewModel.updateCurrentDressesValue()
                if (viewModel.currentDresses.value != null) {
                    adapterDress.items = viewModel.currentDresses.value!!
                    Log.d("currentCategory", adapterDress.items.first().name)
                    adapterDress.notifyDataSetChanged()
                    viewModel.currentDress = viewModel.currentDresses.value!!.first()
                } else {
                    adapterDress.items = emptyList()
                    Log.d("currentCategory", "empty")
                }
            }
            Log.d("currentCategory_1", "${viewModel.currentDresses.value == null}")
        }

        viewModel.currentDresses.observe(viewLifecycleOwner) {
            if (it != null) {
                if (it.isNotEmpty()) {
                    adapterDress.items = it
                    Log.d("currentDresses", adapterDress.items.first().name)
                    adapterDress.notifyDataSetChanged()
                    viewModel.currentDress = it.first()
                }
            }
        }
        return binding
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        (activity as MainActivity?)!!.resetActionBar(false, DrawerLayout.LOCK_MODE_UNLOCKED)
        adapterDress.notifyDataSetChanged()
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        addButton = view.findViewById(R.id.newDressItem)

        addButton.setOnClickListener {
            viewModel.addDress()
        }

    }

    private fun setupCategoryLayoutManager(binding: View, adapter: CategoriesAdapter) {
        val categoriesRecyclerView = binding.findViewById<RecyclerView>(R.id.categoriesRecyclerView)

        categoriesRecyclerView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                categoriesRecyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                categoriesRecyclerView.adapter = adapter
                categoriesRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            }
        })
    }

    private fun setupDressLayoutManager(binding: View, adapter: DressesAdapter) {
        val dressesRecyclerView = binding.findViewById<RecyclerView>(R.id.dressesRecyclerView)

        dressesRecyclerView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                dressesRecyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                dressesRecyclerView.adapter = adapter
                dressesRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            }
        })

    }

}
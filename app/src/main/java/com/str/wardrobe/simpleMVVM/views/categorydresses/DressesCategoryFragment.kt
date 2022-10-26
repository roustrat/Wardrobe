package com.str.wardrobe.simpleMVVM.views.categorydresses

import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.str.wardrobe.R
import com.str.foundation.views.BaseFragment
import com.str.foundation.views.BaseScreen
import com.str.foundation.views.screenViewModel
import com.str.wardrobe.simpleMVVM.model.entities.NamedCategory
import com.str.wardrobe.simpleMVVM.model.entities.NamedDress

class DressesCategoryFragment : BaseFragment() {

    // no arguments for this screen
    class Screen : BaseScreen

    override val viewModel by screenViewModel<DressesCategoryViewModel>()

    private lateinit var addButton: ImageButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding =  inflater.inflate(R.layout.dresses_category, container, false)

        val adapterCategory = CategoriesAdapter(viewModel)
        val adapterDress = DressesAdapter(viewModel)
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
            Toast.makeText(requireContext(), "Все", Toast.LENGTH_SHORT).show()
        }

        viewModel.currentCategory.observe(viewLifecycleOwner) {
//            if(it != null) {
//                viewModel.currentDresses.value = viewModel.allDresses.value.takeIf { it1: List<NamedDress>? -> it1 == it}
//            }
            Toast.makeText(requireContext(), "${viewModel.currentCategory.value?.name}", Toast.LENGTH_SHORT).show()
        }

        viewModel.currentDresses.observe(viewLifecycleOwner) {
            if (it != null) {
                if (it.isNotEmpty()) {
                    adapterDress.items = it
                    viewModel.currentDress = it.first()
                }
            }
        }

        return binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        addButton = view.findViewById(R.id.newDressItem)

        addButton.setOnClickListener {
            viewModel.addDress(requireContext())
        }

    }

    private fun setupCategoryLayoutManager(binding: View, adapter: CategoriesAdapter) {
        val categoriesRecyclerView = binding.findViewById<RecyclerView>(R.id.categoriesRecyclerView)

        categoriesRecyclerView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                categoriesRecyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
//                val width = categoriesRecyclerView.width
//                val itemWidth = resources.getDimensionPixelSize(R.dimen.item_width)
//                val columns = width / itemWidth
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
//                val width = categoriesRecyclerView.width
//                val itemWidth = resources.getDimensionPixelSize(R.dimen.item_width)
//                val columns = width / itemWidth
                dressesRecyclerView.adapter = adapter
                dressesRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            }
        })

    }

}
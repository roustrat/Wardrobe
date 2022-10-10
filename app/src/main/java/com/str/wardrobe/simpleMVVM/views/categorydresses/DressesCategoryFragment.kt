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
        setupLayoutManager(binding, adapterCategory, adapterDress)

        viewModel.allCategory.observe(viewLifecycleOwner) {
            adapterCategory.items = it as List<NamedCategory>
            if (it != null) {
                if (viewModel.currentDress == null) {
                    viewModel.currentCategory = it.first()
                }
            }
            Toast.makeText(requireContext(), "Observe", Toast.LENGTH_SHORT).show()
        }

        // Надо проверить необходимость
        viewModel.allDresses.observe(viewLifecycleOwner) { it1 ->
            viewModel.currentDresses = it1.takeWhile { it.category == viewModel.currentCategory?.name }
            adapterDress.items = viewModel.currentDresses
            if (viewModel.currentDresses.isNotEmpty()) {
                viewModel.currentDress = viewModel.currentDresses.first()
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
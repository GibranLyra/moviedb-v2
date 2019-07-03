package com.gibranlyra.moviedb.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gibranlyra.moviedb.MyApp
import com.gibranlyra.moviedb.R
import com.gibranlyra.moviedb.di.ViewModelFactory
import com.gibranlyra.moviedb.ui.component.BaseAdapter
import com.gibranlyra.moviedb.ui.component.error.ErrorView
import com.gibranlyra.moviedb.ui.component.movie.VerticalMovieAdapter
import com.gibranlyra.moviedb.ui.moviedetail.MovieDetailFragment
import com.gibranlyra.moviedb.util.ext.gone
import com.gibranlyra.moviedb.util.ext.requiredBundleNotFound
import com.gibranlyra.moviedb.util.ext.showSnackBar
import com.gibranlyra.moviedb.util.ext.visible
import com.gibranlyra.moviedb.util.resource.Resource
import com.gibranlyra.moviedb.util.resource.ResourceState.*
import com.gibranlyra.moviedbservice.model.Movie
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_category.*


const val EXTRA_CATEGORY = "EXTRA_CATEGORY"

class CategoryFragment : BottomSheetDialogFragment() {

    companion object {
        @JvmStatic
        fun newInstance(category: CategoryViewModel.Category): CategoryFragment {
            val fragment = CategoryFragment()
            fragment.arguments = Bundle().apply {
                putSerializable(EXTRA_CATEGORY, category)
            }
            return fragment
        }
    }

    private val viewModel: CategoryViewModel by lazy {
        ViewModelProviders
                .of(this, ViewModelFactory.getInstance(MyApp.instance))
                .get(CategoryViewModel::class.java)
    }

    private val categoryAdapter by lazy {
        VerticalMovieAdapter(mutableListOf(), object : BaseAdapter.AdapterListener<Movie> {
            override fun onAdapterItemClicked(position: Int, item: Movie, view: View) {
                MovieDetailFragment.newInstance(item)
                        .show(childFragmentManager, tag)
            }
        })
    }

    private lateinit var category: CategoryViewModel.Category

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getSerializable(EXTRA_CATEGORY)?.let {
            category = it as CategoryViewModel.Category
        } ?: run { activity?.requiredBundleNotFound(EXTRA_CATEGORY) }
        setStyle(STYLE_NORMAL, R.style.SheetDialog)
        initViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_category, container, false)
        view.findViewById<Toolbar>(R.id.fragmentCategoryToolbar).run {
            title = category.name.toLowerCase().capitalize()
            setNavigationIcon(R.drawable.ic_arrow_back)
            setNavigationOnClickListener { activity?.onBackPressed() }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryFragmentRecyclerView.adapter = categoryAdapter

        categoryFragmentErrorView.initView(object : ErrorView.ErrorViewListener {
            override fun onClick() {
                showContent()
                viewModel.start()
            }
        })
    }

    private fun initViewModel() {
        with(viewModel) {
            configurationLive.observe(this@CategoryFragment, Observer {
                when (it.status) {
                    LOADING -> when (it.loading) {
                        true -> showContent()
                        false -> {
                            //Do Nothing
                        }
                    }
                    SUCCESS -> {
                        loadCategory(it.data!!, category)
                    }
                    ERROR -> {
                        showErrorView()
                        showError(it.message!!, it.action!!)
                    }
                }
            })

            topRatedLive.observe(this@CategoryFragment, Observer { setupLiveData(it) })

            upcomingLive.observe(this@CategoryFragment, Observer { setupLiveData(it) })

            popularLive.observe(this@CategoryFragment, Observer { setupLiveData(it) })

            start()
        }
    }

    private fun showError(message: String, action: () -> Unit) {
        categoryFragmentRootView.showSnackBar(message, Snackbar.LENGTH_LONG, getString(R.string.try_again), action)
    }

    private fun showErrorView() {
        categoryFragmentLoading.gone()
        categoryFragmentErrorView.visible()
    }

    private fun showContent() {
        categoryFragmentLoading.visible()
        categoryFragmentErrorView.gone()
    }

    private fun setupLiveData(it: Resource<List<Movie>>) {
        when (it.status) {
            LOADING -> when (it.loading) {
                true -> {
                    categoryFragmentLoading.show()
                    categoryFragmentRecyclerView.gone()
                }
                false -> {
                    categoryFragmentLoading.hide()
                    categoryFragmentRecyclerView.visible()
                }
            }
            SUCCESS -> categoryAdapter.add(it.data!!.toMutableList(), true)
            ERROR -> showError(it.message!!, it.action!!)
        }
    }
}

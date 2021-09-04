package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(this, Factory(activity.application)).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setRecyclerViewAdapter()
        setHasOptionsMenu(true)
        navigateToDetail()

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_week_menu -> {
                viewModel.updateAsteroidOptionList(MainViewModel.MenuItemOptions.Week)
            }
            R.id.show_today_menu -> {
                viewModel.updateAsteroidOptionList(MainViewModel.MenuItemOptions.Today)
            }
            R.id.show_saved_menu -> {
                viewModel.updateAsteroidOptionList(MainViewModel.MenuItemOptions.Saved)
            }
        }
        return true
    }

    private fun setRecyclerViewAdapter() {
        val adapter = AsteroidInfoAdapter(AsteroidInfoAdapter.AsteroidListener { asteroid ->
            viewModel.onAsteroidClicked(asteroid)
        })
        binding.asteroidRecycler.adapter = adapter
        viewModel.asteroidList.observe(viewLifecycleOwner, {
            if (null != it) {
                (binding.asteroidRecycler.adapter as AsteroidInfoAdapter).submitList(it)
            }
        })

    }

    private fun navigateToDetail() {
        viewModel.navigateToDetailFragment.observe(viewLifecycleOwner, { theAsteroid ->
            if (theAsteroid != null) {
                findNavController().navigate(MainFragmentDirections.actionShowDetail(theAsteroid))
                viewModel.navigationReset()
            }
        })
    }
}


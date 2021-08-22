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
        ViewModelProvider(this).get(MainViewModel::class.java)
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
        return true
    }

    private fun setRecyclerViewAdapter() {
        val adapter = AsteroidInfoAdapter(AsteroidInfoAdapter.AsteroidListener { asteroid ->
            viewModel.onAsteroidClicked(asteroid)
        })
        binding.asteroidRecycler.adapter = adapter

        adapter.submitList(viewModel.asteroids)
    }

    private fun navigateToDetail() {
        viewModel.navigateToDetailFragment.observe(viewLifecycleOwner, { theAsteroid ->
            if (theAsteroid != null) {
                findNavController().navigate(MainFragmentDirections.actionShowDetail(theAsteroid))
                viewModel.navigationReset()
            }
        })
    }

//    private fun recyclerViewTestList(): MutableList<Asteroid> {
//        val asteroidList = mutableListOf<Asteroid>()
//        asteroidList.add(
//            Asteroid(
//                1, "12345 (20015 KB66", "2029-08-18",
//                3.0, 9.0, 0.0, 6.0, true
//            ))
//        asteroidList.add(
//            Asteroid(
//                1, "67890 (205699 KB67", "2020-08-19",
//                666.0, 6.0, 77.0, 48.0, true
//            ))
//        return asteroidList
//    }
}


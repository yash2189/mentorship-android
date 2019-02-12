package org.systers.mentorship.view.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_home.*
import org.systers.mentorship.R
import org.systers.mentorship.databinding.FragmentHomeBinding
import org.systers.mentorship.view.adapters.AchievementsAdapter
import org.systers.mentorship.viewmodels.HomeViewModel

/**
 * The fragment is responsible for showing a welcoming message and show some statistics of the User
 * usage of the app
 */
class HomeFragment : BaseFragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var achievementsAdapter: AchievementsAdapter

    companion object {
        /**
         * Creates an instance of HomeFragment
         */
        fun newInstance() = HomeFragment()
        val TAG: String = HomeFragment::class.java.simpleName
    }

    override fun getLayoutResourceId(): Int = R.layout.fragment_home
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutResourceId(), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        achievementsAdapter = AchievementsAdapter()
        val linearLayoutManager = LinearLayoutManager(context)
        val divider = DividerItemDecoration(context, linearLayoutManager.orientation)

        rvAchievements.apply {
            adapter = achievementsAdapter
            layoutManager = linearLayoutManager
            addItemDecoration(divider)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        with(homeViewModel) {
            userStats.observe(viewLifecycleOwner, Observer { stats ->
                binding.stats = stats
                if (stats?.achievements?.isEmpty() != false) {
                    tvNoAchievements.visibility = View.VISIBLE
                    rvAchievements.visibility = View.GONE
                } else {
                    tvNoAchievements.visibility = View.GONE
                    rvAchievements.visibility = View.VISIBLE
                    achievementsAdapter.submitList(stats.achievements)
                }
            })

            message.observe(viewLifecycleOwner, Observer { message ->
                Snackbar.make(homeContainer, message.toString(), Snackbar.LENGTH_SHORT).show()
            })
        }
    }
}


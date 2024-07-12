package com.rss.rajasri.ui.plots

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.rss.rajasri.R
import com.rss.rajasri.databinding.ActivityPlotDetailsBinding
import com.rss.rajasri.databinding.FragmentAccountBinding


class PlotDetailsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: ActivityPlotDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = ActivityPlotDetailsBinding.inflate(layoutInflater)



        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = PlotDetailsFragment().apply {}
    }
}
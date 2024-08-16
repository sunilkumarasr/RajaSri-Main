package com.rss.rajasri.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rss.rajasri.databinding.ActivityEditProfileBinding


class ProfileFragment : Fragment() {
    private lateinit var binding: ActivityEditProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = ActivityEditProfileBinding.inflate(inflater,container,false)



        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
            }
    }
}
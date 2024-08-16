package com.rss.rajasri.ui.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rss.rajasri.databinding.ActivityTransactionDetailsBinding


class TransactionDetailsFragment : Fragment() {
    private lateinit var binding: ActivityTransactionDetailsBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = ActivityTransactionDetailsBinding.inflate(layoutInflater)



        return binding.root
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = TransactionDetailsFragment().apply {}
    }
}
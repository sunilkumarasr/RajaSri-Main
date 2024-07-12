package com.rss.rajasri.ui.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.rss.rajasri.R
import com.rss.rajasri.databinding.ActivityTransactionsHistoryBinding


class TransactionFragment : Fragment() {
    private lateinit var binding: ActivityTransactionsHistoryBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = ActivityTransactionsHistoryBinding.inflate(layoutInflater)

  /*      binding.tranasctionLL.transacLL.setOnClickListener {
            val navController = Navigation.findNavController(
                requireActivity(), R.id.nav_host_fragment_activity_home
            )
            navController.navigate(R.id.transactDetailsFragment)

        }*/

        return binding.root
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = TransactionFragment().apply {}
    }
}
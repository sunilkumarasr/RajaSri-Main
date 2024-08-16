package com.rss.rajasri.ui.fragments.Bullings

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.rss.rajasri.databinding.FragmentEMIBinding
import com.rss.rajasri.ui.fragments.Bullings.Childs.EMIPaidFragment
import com.rss.rajasri.ui.activities.transaction_history.TransActionsViewPagerAdapter
import com.rss.rajasri.ui.fragments.Bullings.Childs.EMIUnpaidFragment

class EMIFragment : Fragment() {
    private lateinit var binding: FragmentEMIBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEMIBinding.inflate(inflater, container, false)
        val listOfFragments = arrayListOf<Fragment>(EMIPaidFragment(), EMIUnpaidFragment())
        val viewPagerAdapter = TransActionsViewPagerAdapter(listOfFragments, childFragmentManager, lifecycle)
        binding.viewPager.adapter = viewPagerAdapter
        binding.viewPager.currentItem = 0
        //left right swip stop
        binding.viewPager.isUserInputEnabled = false

        binding.paidCV.setOnClickListener {
            binding.viewPager.currentItem = 0
            binding.paidCV.setCardBackgroundColor(Color.parseColor("#7192B0"))
            binding.unPaidCV.setCardBackgroundColor(Color.parseColor("#FB4E45"))
        }
        binding.unPaidCV.setOnClickListener {
            binding.viewPager.currentItem = 1
            binding.paidCV.setCardBackgroundColor(Color.parseColor("#FB4E45"))
            binding.unPaidCV.setCardBackgroundColor(Color.parseColor("#7192B0"))
        }
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EMIFragment().apply {
            }
    }


}
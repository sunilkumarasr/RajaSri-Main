package com.rss.rajasri.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.rss.rajasri.R
import com.rss.rajasri.databinding.FragmentBillingBinding
import com.rss.rajasri.ui.activities.transaction_history.TransActionsViewPagerAdapter
import com.rss.rajasri.ui.fragments.Bullings.EMIFragment
import com.rss.rajasri.ui.fragments.Bullings.InvoiceFragment


class BillingFragment : Fragment() {
    private lateinit var binding: FragmentBillingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentBillingBinding.inflate(inflater, container, false)
        val listOfFragments = arrayListOf<Fragment>(EMIFragment(), InvoiceFragment())
        val viewPagerAdapter = TransActionsViewPagerAdapter(listOfFragments, childFragmentManager, lifecycle)
        binding.viewPager.adapter = viewPagerAdapter
        binding.viewPager.currentItem = 0
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    binding.paidCV.setCardBackgroundColor(Color.parseColor("#FF0000"))
                    binding.unPaidCV.setCardBackgroundColor(Color.parseColor("#D9D9D9"))
                    binding.txtEMI.setTextColor(getResources().getColorStateList(R.color.white));
                    binding.txtInvoice.setTextColor(getResources().getColorStateList(R.color.black));
                } else if (position == 1) {
                    binding.paidCV.setCardBackgroundColor(Color.parseColor("#D9D9D9"))
                    binding.unPaidCV.setCardBackgroundColor(Color.parseColor("#FF0000"))
                    binding.txtInvoice.setTextColor(getResources().getColorStateList(R.color.white));
                    binding.txtEMI.setTextColor(getResources().getColorStateList(R.color.black));
                }
                super.onPageSelected(position)
            }
        })

        binding.paidCV.setOnClickListener {
            binding.viewPager.currentItem = 0
            binding.paidCV.setCardBackgroundColor(Color.parseColor("#FF0000"))
            binding.unPaidCV.setCardBackgroundColor(Color.parseColor("#D9D9D9"))
            binding.txtEMI.setTextColor(getResources().getColorStateList(R.color.white));
            binding.txtInvoice.setTextColor(getResources().getColorStateList(R.color.black));
        }
        binding.unPaidCV.setOnClickListener {
            binding.viewPager.currentItem = 1
            binding.paidCV.setCardBackgroundColor(Color.parseColor("#D9D9D9"))
            binding.unPaidCV.setCardBackgroundColor(Color.parseColor("#FF0000"))
            binding.txtInvoice.setTextColor(getResources().getColorStateList(R.color.white));
            binding.txtEMI.setTextColor(getResources().getColorStateList(R.color.black));
        }
        return binding.root

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BillingFragment().apply {
            }
    }

}
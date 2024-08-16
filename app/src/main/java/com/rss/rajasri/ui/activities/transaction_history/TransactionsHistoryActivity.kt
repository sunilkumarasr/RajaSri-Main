package com.rss.rajasri.ui.activities.transaction_history

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.rss.rajasri.databinding.ActivityTransactionsHistoryBinding
import com.rss.rajasri.ui.fragments.Bullings.Childs.EMIPaidFragment
import com.rss.rajasri.ui.fragments.Bullings.Childs.EMIUnpaidFragment

class TransactionsHistoryActivity : AppCompatActivity() {
    private val binding: ActivityTransactionsHistoryBinding by lazy {
        ActivityTransactionsHistoryBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.includedHeaderLL.backButtonIV.setOnClickListener {
            finish()
        }
        val listOfFragments = arrayListOf<Fragment>(EMIPaidFragment(), EMIUnpaidFragment())
        val viewPagerAdapter = TransActionsViewPagerAdapter(listOfFragments,supportFragmentManager,lifecycle)
        binding.viewPager.adapter = viewPagerAdapter
        binding.viewPager.currentItem = 0
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

    }
}
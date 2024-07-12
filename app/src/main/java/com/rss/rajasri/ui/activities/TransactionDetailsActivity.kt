package com.rss.rajasri.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rss.rajasri.R
import com.rss.rajasri.databinding.ActivityEditProfileBinding
import com.rss.rajasri.databinding.ActivityTransactionDetailsBinding

class TransactionDetailsActivity : AppCompatActivity() {
    private val binding: ActivityTransactionDetailsBinding by lazy {
        ActivityTransactionDetailsBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.includedHeaderLL.backButtonIV.setOnClickListener {
            finish()
        }
    }
}
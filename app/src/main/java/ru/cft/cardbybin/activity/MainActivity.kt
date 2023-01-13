package ru.cft.cardbybin.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import ru.cft.cardbybin.databinding.ActivityMainBinding
import ru.cft.cardbybin.viewholder.CardViewHolder
import ru.cft.cardbybin.viewmodel.CardViewModel
import ru.netology.nmedia.util.AndroidUtils

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: CardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel.cardInfo.observe(this@MainActivity) {
                CardViewHolder(binding).bind(it)
            }
        binding.getCardInfoButton.setOnClickListener {
            viewModel.getCardInfo(binding.cardBin.text.toString().toInt())
            AndroidUtils.hideKeyboard(binding.cardBin)
        }
    }
}
package ru.cft.cardbybin.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import ru.cft.cardbybin.R
import ru.cft.cardbybin.databinding.ActivityMainBinding
import ru.cft.cardbybin.util.CompanionCardByBin.BIN_LENGTH
import ru.cft.cardbybin.viewholder.CardViewHolder
import ru.cft.cardbybin.viewmodel.CardViewModel
import ru.netology.nmedia.util.AndroidUtils

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: CardViewModel by viewModels()
    private var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViews()
        subscribe()
        setupListeners()
    }

    private fun initViews() {
        binding.cardBin.setText(R.string.sample_bin)
        viewModel.getCardInfo(binding.cardBin.text.toString().toInt())
    }

    private fun subscribe() {
        viewModel.cardInfo.observe(this@MainActivity) {
            CardViewHolder(binding).bind(it)
        }
    }

    private fun setupListeners() {
        binding.apply {
            getCardInfoButton.setOnClickListener {
                if (snackbar != null && snackbar?.isShown == true)
                    snackbar?.dismiss()
                AndroidUtils.hideKeyboard(cardBin)
                val bin = cardBin.text.trim()
                when {
                    bin.isBlank() -> showSnackbar(getString(R.string.empty_bin_field))
                    bin.length != BIN_LENGTH -> showSnackbar(getString(R.string.wrong_bin_length))
                    else -> viewModel.getCardInfo(cardBin.text.toString().toInt())
                }
            }
        }
    }

    private fun showSnackbar(message: String) {
        snackbar = Snackbar.make(
            binding.root,
            message,
            BaseTransientBottomBar.LENGTH_INDEFINITE
        )
            .setAction(android.R.string.ok) {}
        snackbar?.show()
    }
}
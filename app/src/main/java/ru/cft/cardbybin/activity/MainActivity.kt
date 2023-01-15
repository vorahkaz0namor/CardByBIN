package ru.cft.cardbybin.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.viewModels
import androidx.core.view.isVisible
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

    override fun onStop() {
        super.onStop()
        viewModel.saveCurrentBin(binding.cardView.dropdownCardBin.editText?.text?.trim().toString())
    }

    private fun initViews() {
        binding.cardView.dropdownCardBin.editText?.apply {
            val savedBin = viewModel.getCurrentBin()
            setText(savedBin ?: getString(R.string.sample_bin))
            if (savedBin == null)
                viewModel.getCardInfo(this.text.toString().toInt())
            else
                viewModel.getCardInfo(viewModel.getShowingBin().toInt())
        }
    }

    private fun subscribe() {
        viewModel.cardInfo.observe(this@MainActivity) { state ->
            CardViewHolder(binding).bind(state.card)
            binding.apply {
                progressBarView.progressBar.isVisible = state.loading
                errorView.errorGroup.isVisible = state.error
                cardView.cardViewGroup.isVisible = state.showing
            }
        }
    }

    private fun setupListeners() {
        binding.apply {
            cardView.apply {
                getCardInfoButton.setOnClickListener {
                    if (snackbar != null && snackbar?.isShown == true)
                        snackbar?.dismiss()
                    AndroidUtils.hideKeyboard(dropdownCardBin)
                    val bin = dropdownCardBin.editText?.text?.trim() ?: ""
                    when {
                        bin.isBlank() -> showSnackbar(getString(R.string.empty_bin_field))
                        bin.length != BIN_LENGTH -> showSnackbar(getString(R.string.wrong_bin_length))
                        else -> viewModel.getCardInfo(
                            bin.toString().toInt()
                        )
                    }
                }
                dropdownCardBin.editText?.setOnClickListener {
                    showDropDownMenu()
                }
            }
            errorView.retryButton.setOnClickListener {
                subscribe()
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

    private fun showDropDownMenu() {
        val dropDownAdapter = ArrayAdapter(
            this@MainActivity,
            R.layout.bin_list_item,
            viewModel.binListForView.value?.binList ?: emptyList()
        )
        val menu = binding.cardView.dropdownCardBin.editText as AutoCompleteTextView
        menu.setAdapter(dropDownAdapter)
    }
}
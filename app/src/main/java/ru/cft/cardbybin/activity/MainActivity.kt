package ru.cft.cardbybin.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.internal.http.HTTP_OK
import ru.cft.cardbybin.R
import ru.cft.cardbybin.databinding.ActivityMainBinding
import ru.cft.cardbybin.util.CompanionCardByBin.BIN_LENGTH
import ru.cft.cardbybin.viewholder.CardViewHolder
import ru.cft.cardbybin.viewmodel.CardViewModel
import ru.cft.cardbybin.util.AndroidUtils
import ru.cft.cardbybin.util.AndroidUtils.scopeWithRepeat
import ru.cft.cardbybin.util.CompanionCardByBin.SAMPLE_BIN

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val viewModel: CardViewModel by viewModels()
    private var snackbar: Snackbar? = null
    private val binEditText: EditText?
        get() = binding.cardView.dropdownCardBin.editText
    private val bin: String
        get() = binEditText?.text?.trim().toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
        subscribe()
        setupListeners()
    }

    override fun onStop() {
        viewModel.setCurrentBin(bin)
        super.onStop()
    }

    private fun initViews() {
        setContentView(binding.root)
        setBinEditText()
    }

    private fun subscribe() {
        viewModel.apply {
            lifecycleScope.launch {
                cardState.observe(this@MainActivity) { state ->
                    binding.apply {
                        progressBarView.progressBar.isVisible = state.loading
                        errorView.errorGroup.isVisible = state.error
                        cardView.cardViewGroup.isVisible = state.showing
                    }
                }
            }
            scopeWithRepeat {
                cardInfo.observe(this@MainActivity) {
                    CardViewHolder(binding.cardView).bind(it)
                }
            }
            lifecycleScope.launch {
                eventOccurrence.observe(this@MainActivity) { code ->
                    if (code != HTTP_OK)
                        binding.errorView.errorTitle.text =
                            getString(R.string.error_loading)
                }
            }
            lifecycleScope.launch {
                binListForView.collectLatest {
                    val dropDownAdapter = ArrayAdapter(
                        /* context = */ this@MainActivity,
                        /* resource = */ R.layout.bin_list_item,
                        /* objects = */ it
                    )
                    (binEditText as AutoCompleteTextView).setAdapter(dropDownAdapter)
                }
            }
        }
    }

    private fun setupListeners() {
        binding.apply {
            lifecycleScope.launch {
                cardView.getCardInfoButton.setOnClickListener {
                    snackbarDismiss()
                    AndroidUtils.hideKeyboard(root)
                    when {
                        bin.isBlank() ->
                            showSnackbar(getString(R.string.empty_bin_field))
                        bin.length != BIN_LENGTH ->
                            showSnackbar(getString(R.string.wrong_bin_length))
                        else -> viewModel.getCardByCurrentBin(bin)
                    }
                }
            }
            lifecycleScope.launch {
                errorView.resetButton.setOnClickListener {
                    viewModel.getCardByCurrentBin(SAMPLE_BIN.toString())
                    setBinEditText()
                }
            }
        }
    }

    private fun setBinEditText() {
        binEditText?.setText(viewModel.currentCardBin)
    }

    private fun showSnackbar(message: String) {
        snackbar = Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_INDEFINITE
        )
            .setTextMaxLines(3)
            .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
            .setAction(android.R.string.ok) {}
        snackbar?.show()
    }

    private fun snackbarDismiss() {
        if (snackbar != null && snackbar?.isShown == true) {
            snackbar?.dismiss()
            snackbar = null
        }
    }
}
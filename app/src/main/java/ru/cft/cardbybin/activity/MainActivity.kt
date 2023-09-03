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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import okhttp3.internal.http.HTTP_OK
import ru.cft.cardbybin.R
import ru.cft.cardbybin.databinding.ActivityMainBinding
import ru.cft.cardbybin.util.CompanionCardByBin.BIN_LENGTH
import ru.cft.cardbybin.viewholder.CardViewHolder
import ru.cft.cardbybin.viewmodel.CardViewModel
import ru.cft.cardbybin.util.AndroidUtils
import ru.cft.cardbybin.util.AndroidUtils.scopeWithRepeat
import ru.cft.cardbybin.util.CompanionCardByBin.overview
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
        subscribe()
        setupListeners()
    }

    override fun onStop() {
        viewModel.saveCurrentBin(binEditText?.text?.trim().toString().toInt())
        super.onStop()
    }

    private fun initViews() {
        setContentView(binding.root)
        binEditText?.setText(viewModel.currentCardBin)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
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
                cardInfo.mapLatest {
                    CardViewHolder(binding.cardView).bind(it)
                }
            }
            lifecycleScope.launch {
                eventOccurrence.observe(this@MainActivity) { code ->
                    if (code != HTTP_OK)
                        binding.errorView.errorTitle.text =
                            getString(
                                /* resId = */ R.string.error_loading,
                                /* ...formatArgs = */ overview(code)
                            )
                }
            }        }
    }

    private fun setupListeners() {
        binding.apply {
            cardView.apply {
                lifecycleScope.launch {
                    getCardInfoButton.setOnClickListener {
                        snackbarDismiss()
                        AndroidUtils.hideKeyboard(root)
                        val bin = binEditText?.text?.trim().toString()
                        when {
                            bin.isBlank() -> showSnackbar(getString(R.string.empty_bin_field))
                            bin.length != BIN_LENGTH -> showSnackbar(getString(R.string.wrong_bin_length))
                            else -> viewModel.saveCurrentBin(bin.toInt())
                        }
                    }
                }
//                dropdownCardBin.editText?.setOnClickListener {
//                    showDropDownMenu()
//                }
                lifecycleScope.launch {
                    dropdownCardBin.setEndIconOnClickListener {
                        showDropDownMenu()
                    }
                }
            }
            lifecycleScope.launch {
                errorView.resetButton.setOnClickListener {
                    viewModel.saveCurrentBin(SAMPLE_BIN)
                }
            }
        }
    }

    private fun showSnackbar(message: String) {
        snackbar = Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_INDEFINITE
        )
            .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
            .setAction(android.R.string.ok) {}
        snackbar?.show()
    }

    private fun showDropDownMenu() {
        val dropDownAdapter = ArrayAdapter(
            /* context = */ this,
            /* resource = */ R.layout.bin_list_item,
            /* objects = */ viewModel.binListForView
        )
        (binEditText as AutoCompleteTextView).setAdapter(dropDownAdapter)
    }

    private fun snackbarDismiss() {
        if (snackbar != null && snackbar?.isShown == true) {
            snackbar?.dismiss()
            snackbar = null
        }
    }
}
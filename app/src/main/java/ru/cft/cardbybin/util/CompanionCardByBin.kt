package ru.cft.cardbybin.util


object CompanionCardByBin {
    const val BIN_LENGTH = 8
    val yesOrNo: (Boolean) -> String = { mean: Boolean ->
        if (mean) "Yes" else "No"
    }
}

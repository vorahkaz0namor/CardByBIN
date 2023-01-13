package ru.cft.cardbybin.viewholder

import okhttp3.internal.format
import ru.cft.cardbybin.databinding.ActivityMainBinding
import ru.cft.cardbybin.dto.Card

class CardViewHolder(
    private val binding: ActivityMainBinding
) {
    fun bind(card: Card) {
        fillingCardFields(card)
        setupListeners(card)
    }

    private fun fillingCardFields(card: Card) {
        binding.apply {
            groupOneInclude.apply {
                scheme.text = card.scheme
                brand.text = card.brand
                length.text = card.number.length.toString()
                luhn.text = card.number.luhn.toString()
            }
            groupTwoInclude.apply {
                type.text = card.type
                prepaid.text = card.prepaid.toString()
                countryFlag.text = card.country.emoji
                countryName.text = card.country.name
                coordinates.text =
                    format(
                        "(latitude: %4d, longitude: %4d)",
                        card.country.latitude,
                        card.country.longitude
                    )
            }
        }
    }

    private fun setupListeners(card: Card) {

    }
}
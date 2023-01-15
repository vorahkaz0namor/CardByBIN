package ru.cft.cardbybin.viewholder

import ru.cft.cardbybin.databinding.ActivityMainBinding
import ru.cft.cardbybin.dto.Card
import ru.cft.cardbybin.util.CompanionCardByBin.yesOrNo

class CardViewHolder(
    private val binding: ActivityMainBinding
) {
    fun bind(card: Card) {
        fillingCardFields(card)
    }

    private fun fillingCardFields(card: Card) {
        binding.cardView.apply {
            groupOneInclude.apply {
                scheme.text = card.scheme
                brand.text = card.brand
                length.text = card.number.length.toString()
                luhn.text = yesOrNo(card.number.luhn)
            }
            groupTwoInclude.apply {
                type.text = card.type
                prepaid.text = yesOrNo(card.prepaid)
                countryFlag.text = card.country.emoji
                countryName.text = card.country.name
                latitude.text = card.country.latitude.toString()
                longitude.text =  card.country.longitude.toString()
            }
            groupThreeInclude.apply {
                bankNameAndCity.text = StringBuilder("${card.bank.name}, ${card.bank.city}")
                bankUrl.text = card.bank.url
                bankPhone.text = card.bank.phone
            }
        }
    }
}
package ru.cft.cardbybin.viewholder

import ru.cft.cardbybin.databinding.CardToViewBinding
import ru.cft.cardbybin.dto.Card
import ru.cft.cardbybin.util.CompanionCardByBin.customSetText
import ru.cft.cardbybin.util.CompanionCardByBin.yesOrNo

class CardViewHolder(
    private val binding: CardToViewBinding
) {
    fun bind(card: Card) {
        binding.apply {
            groupOneInclude.apply {
                scheme.text = customSetText(card.scheme)
                brand.text = customSetText(card.brand)
                length.text = card.number.length.toString()
                luhn.text = yesOrNo(card.number.luhn)
            }
            groupTwoInclude.apply {
                type.text = customSetText(card.type)
                prepaid.text = yesOrNo(card.prepaid)
                countryFlag.text = card.country.emoji
                countryName.text = customSetText(card.country.name)
                // ПОСМОТРИМ!
                latitude.text = card.country.latitude.toString()
                longitude.text =  card.country.longitude.toString()
            }
            groupThreeInclude.apply {
                card.bank.apply {
                    bankName.text = customSetText(name)
                    bankCity.text = customSetText(city)
                    bankUrl.text = customSetText(url)
                    bankPhone.text = customSetText(phone)
                }
            }
        }
    }
}
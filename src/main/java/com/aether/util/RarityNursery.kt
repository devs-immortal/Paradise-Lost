package com.aether.util

import net.minecraft.util.Formatting
import net.minecraft.util.Rarity
import sun.misc.Unsafe


object RarityNursery {

    private val rarityField = Rarity::class.java.getDeclaredField("formatting")
    private val ordinalField = Enum::class.java.getDeclaredField("ordinal")
    private val unsafe: Unsafe

    init {
        rarityField.isAccessible = true
        ordinalField.isAccessible = true
        val unsafeField = Unsafe::class.java.getDeclaredField("theUnsafe")
        unsafeField.isAccessible = true
        unsafe = unsafeField.get(null) as Unsafe
    }

    fun createRarity(formatting: Formatting, tier: Int): Rarity {
        val rarity = unsafe.allocateInstance(Rarity::class.java) as Rarity
        rarityField.set(rarity, formatting)
        ordinalField.setInt(rarity, tier)
        return rarity
    }
}

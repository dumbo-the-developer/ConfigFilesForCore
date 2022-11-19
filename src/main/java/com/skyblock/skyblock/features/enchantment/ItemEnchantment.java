package com.skyblock.skyblock.features.enchantment;

public class ItemEnchantment {

    private final SkyblockEnchantment baseEnchantment;
    private final int level;

    public ItemEnchantment(SkyblockEnchantment baseEnchantment, int level) {
        this.baseEnchantment = baseEnchantment;
        this.level = level;
    }

    public SkyblockEnchantment getBaseEnchantment() {
        return baseEnchantment;
    }

    public int getLevel() {
        return level;
    }

}

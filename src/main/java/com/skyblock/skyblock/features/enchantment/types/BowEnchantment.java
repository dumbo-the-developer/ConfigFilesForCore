package com.skyblock.skyblock.features.enchantment.types;

import com.skyblock.skyblock.features.enchantment.SkyblockEnchantment;

import java.util.HashMap;

public abstract class BowEnchantment extends SkyblockEnchantment {

    public BowEnchantment(String name, HashMap<Integer, String> description, int maxLevel) {
        super(name, description, maxLevel);
    }

}

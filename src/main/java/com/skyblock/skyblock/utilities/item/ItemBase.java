package com.skyblock.skyblock.utilities.item;

import com.skyblock.skyblock.Skyblock;
import com.skyblock.skyblock.enums.Item;
import com.skyblock.skyblock.enums.Rarity;
import com.skyblock.skyblock.enums.Reforge;
import com.skyblock.skyblock.enums.SkyblockStat;
import com.skyblock.skyblock.features.enchantment.ItemEnchantment;
import com.skyblock.skyblock.features.enchantment.SkyblockEnchantment;
import com.skyblock.skyblock.features.reforge.ReforgeStat;
import com.skyblock.skyblock.utilities.Util;
import de.tr7zw.nbtapi.NBTItem;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class ItemBase {

    private List<String> description;
    private Rarity rarityEnum;
    private Material material;
    private String skyblockId;
    private String rarity;
    private String name;
    private int amount;
    private Item item;

    private boolean reforgeable;
    private Reforge reforge;

    private List<ItemEnchantment> enchantments;
    private boolean enchantGlint;

    private List<String> abilityDescription;
    private String abilityCooldown;
    private String abilityName;
    private String abilityType;
    private boolean hasAbility;
    private int abilityCost;

    private int intelligence;
    private int attackSpeed;
    private int critChance;
    private int critDamage;
    private int strength;
    private int defense;
    private int health;
    private int damage;
    private int speed;

    private ItemStack stack;

    public ItemBase(ItemStack item) {
        NBTItem nbt = new NBTItem(item);

        if (!nbt.getBoolean("skyblockItem")) throw new IllegalArgumentException("Item is not a skyblock item.");

        this.material = item.getType();
        this.amount = item.getAmount();
        this.name = nbt.getString("name");
        this.rarity = nbt.getString("rarity");
        try {
            if (!ChatColor.stripColor(this.rarity).startsWith("VERY")) {
                this.rarityEnum = Rarity.valueOf(ChatColor.stripColor(rarity.split(" ")[0]).toUpperCase().replace(" ", "_"));
            } else this.rarityEnum = Rarity.VERY_SPECIAL;
        } catch (Exception ex) {
            this.rarityEnum = Rarity.COMMON;
        }
        try {
            this.reforge = Reforge.getReforge(nbt.getString("reforgeType"));
        } catch (Exception ex) {
            this.reforge = Reforge.NONE;
        }

        this.reforgeable = nbt.getBoolean("reforgeable");
        this.enchantments = new ArrayList<>();
        String enchantmentsStr = nbt.getString("enchantments");

        if (enchantmentsStr.length() > 0) {
            String[] enchantmentsList = enchantmentsStr.substring(1, enchantmentsStr.length() - 1).split(", ");
            try {
                for (String enchantment : enchantmentsList)
                    this.enchantments.add(new ItemEnchantment(Skyblock.getPlugin(Skyblock.class).getEnchantmentHandler().getEnchantment(enchantment.split(";")[1]), Integer.parseInt(enchantment.split(";")[0])));
            } catch (Exception ex) {
                this.enchantments = new ArrayList<>();
            }
        }
        this.enchantGlint = nbt.getBoolean("enchantGlint");
        String abilityDescriptionStr = nbt.getString("abilityDescription");
        this.abilityDescription = Arrays.asList(abilityDescriptionStr.substring(1, abilityDescriptionStr.length() - 1).split(", "));
        this.abilityCooldown = nbt.getString("abilityCooldown");
        this.abilityName = nbt.getString("abilityName");
        this.abilityType = nbt.getString("abilityType");
        this.hasAbility = nbt.getBoolean("hasAbility");
        this.abilityCost = nbt.getInteger("abilityCost");
        this.intelligence = nbt.getInteger("intelligence");
        this.attackSpeed = nbt.getInteger("attackSpeed");
        this.critChance = nbt.getInteger("critChance");
        this.critDamage = nbt.getInteger("critDamage");
        this.strength = nbt.getInteger("strength");
        this.defense = nbt.getInteger("defense");
        this.damage = nbt.getInteger("damage");
        this.health = nbt.getInteger("health");
        this.speed = nbt.getInteger("speed");
        this.skyblockId = nbt.getString("skyblockId");
        this.item = this.getItem(rarity);

        String descriptionStr = nbt.getString("description");
        String[] descriptionArr = descriptionStr.substring(1, descriptionStr.length() - 1).split(", ");
        String[] descriptionArrClone = Arrays.copyOf(descriptionArr, descriptionArr.length + 1);
        descriptionArrClone[descriptionArrClone.length - 1] = "";
        this.description = Arrays.asList(descriptionArrClone);

        this.stack = item;
    }

    public ItemBase(Material material, String name, Reforge reforgeType, int amount, List<String> description, List<ItemEnchantment> enchantments, boolean enchantGlint, boolean hasAbility, String abilityName, List<String> abilityDescription, String abilityType, int abilityCost, String abilityCooldown, String rarity, String skyblockId, int damage, int strength, int health, int critChance, int critDamage, int attackSpeed, int intelligence, int speed, int defense, boolean reforgeable) {
        this.description = description;
        this.material = material;
        this.name = name;
        this.rarity = rarity;
        this.amount = amount;

        this.reforge = reforgeType;
        this.reforgeable = reforgeable;

        this.enchantments = enchantments;
        this.enchantGlint = enchantGlint;

        this.abilityDescription = abilityDescription;
        this.abilityCooldown = abilityCooldown;
        this.abilityName = abilityName;
        this.abilityType = abilityType;
        this.abilityCost = abilityCost;
        this.hasAbility = hasAbility;

        this.intelligence = intelligence;
        this.attackSpeed = attackSpeed;
        this.critChance = critChance;
        this.critDamage = critDamage;
        this.strength = strength;
        this.defense = defense;
        this.health = health;
        this.damage = damage;
        this.speed = speed;

        this.skyblockId = skyblockId;

        this.createStack();
    }

    public ItemStack createStack() {
        this.stack = new ItemStack(this.material, this.amount);

        ItemMeta meta = this.stack.getItemMeta();

        List<String> lore = new ArrayList<>();

        if (this.reforge == null) this.reforge = Reforge.NONE;

        this.rarityEnum = this.getRarity(this.rarity);
        this.item = this.getItem(this.rarity);

        ReforgeStat reforgeData = this.reforge.getReforgeData(this.rarityEnum);

        int rStrength = reforgeData.get(SkyblockStat.STRENGTH);
        int rCritChance = reforgeData.get(SkyblockStat.CRIT_CHANCE);
        int rCritDamage = reforgeData.get(SkyblockStat.CRIT_DAMAGE);
        int rAttackSpeed = reforgeData.get(SkyblockStat.ATTACK_SPEED);
        int rSpeed = reforgeData.get(SkyblockStat.SPEED);
        int rMana = reforgeData.get(SkyblockStat.MANA);
        int rDefense = reforgeData.get(SkyblockStat.DEFENSE);
        int rHealth = reforgeData.get(SkyblockStat.HEALTH);

        /*
          Stats
         */
        if (damage != 0) lore.add(ChatColor.GRAY + "Damage: " + ChatColor.RED + "+" + damage);
        if (strength != 0 || rStrength > 0)
            lore.add(ChatColor.GRAY + "Strength: " + ChatColor.RED + "+" + (strength + rStrength) + (reforge != Reforge.NONE && rStrength > 0 ? " " + ChatColor.BLUE + "(+" + rStrength + ")" : ""));
        if (critChance != 0 || rCritChance > 0)
            lore.add(ChatColor.GRAY + "Crit Chance: " + ChatColor.RED + "+" + (critChance + rCritChance) + "%" + (reforge != Reforge.NONE && rCritChance > 0 ? " " + ChatColor.BLUE + "(+" + rCritChance + "%)" : ""));
        if (critDamage != 0 || rCritDamage > 0)
            lore.add(ChatColor.GRAY + "Crit Damage: " + ChatColor.RED + "+" + (critDamage + rCritDamage) + "%" + (reforge != Reforge.NONE && rCritDamage > 0 ? " " + ChatColor.BLUE + "(+" + rCritDamage + "%)" : ""));
        if (attackSpeed != 0 || rAttackSpeed > 0)
            lore.add(ChatColor.GRAY + "Attack Speed: " + ChatColor.RED + "+" + (attackSpeed + rAttackSpeed + "%" + (reforge != Reforge.NONE && rAttackSpeed > 0 ? " " + ChatColor.BLUE + "(+" + rAttackSpeed + "%)" : "")));
        if ((speed != 0 || rSpeed != 0) && (intelligence != 0 || rMana != 0) && (defense != 0 || rDefense != 0) && (health != 0 || rHealth > 0)) {
            lore.add("");
            lore.add(ChatColor.GRAY + "Health: " + ChatColor.GREEN + "+" + (health + rMana) + (reforge != Reforge.NONE && rHealth > 0 ? " HP " + ChatColor.BLUE + "(+" + rHealth + ")" : ""));
            lore.add(ChatColor.GRAY + "Defense: " + ChatColor.GREEN + "+" + (defense + rHealth) + (reforge != Reforge.NONE && rHealth > 0 ? " " + ChatColor.BLUE + "(+" + rHealth + ")" : ""));
            lore.add(ChatColor.GRAY + "Speed: " + ChatColor.GREEN + "+" + (speed + rSpeed) + (reforge != Reforge.NONE && rSpeed > 0 ? " " + ChatColor.BLUE + "(+" + rSpeed + ")" : ""));
            lore.add(ChatColor.GRAY + "Intelligence: " + ChatColor.GREEN + "+" + (intelligence + rMana) + (reforge != Reforge.NONE && rMana > 0 ? " " + ChatColor.BLUE + "(+" + rMana + ")" : ""));
        } else if (health != 0 || rHealth > 0) {
            lore.add("");
            lore.add(ChatColor.GRAY + "Health: " + ChatColor.GREEN + "+" + (health + rHealth) + (reforge != Reforge.NONE && rHealth > 0 ? " HP " + ChatColor.BLUE + "(+" + rHealth + ")" : ""));
        } else if (defense != 0 || rDefense != 0) {
            lore.add("");
            lore.add(ChatColor.GRAY + "Defense: " + ChatColor.GREEN + "+" + (defense + rHealth) + (reforge != Reforge.NONE && rDefense > 0 ? " " + ChatColor.BLUE + "(+" + rHealth + ")" : ""));
        } else if (speed != 0 || rSpeed != 0) {
            lore.add("");
            lore.add(ChatColor.GRAY + "Speed: " + ChatColor.GREEN + "+" + (speed + rSpeed) + (reforge != Reforge.NONE && rSpeed > 0 ? " " + ChatColor.BLUE + "(+" + rSpeed + ")" : ""));
        } else if (intelligence != 0 || rMana != 0) {
            lore.add("");
            lore.add(ChatColor.GRAY + "Intelligence: " + ChatColor.GREEN + "+" + (intelligence + rMana) + (reforge != Reforge.NONE && rMana > 0 ? " " + ChatColor.BLUE + "(+" + rMana + ")" : ""));
        }

        lore.add("");

        /*
        Enchantments
         */

        if (!(this.enchantments.size() < 1)) {
            if (this.enchantments.size() <= 3) {
                for (ItemEnchantment enchantment : this.enchantments) {
                    lore.add(ChatColor.BLUE + enchantment.getBaseEnchantment().getDisplayName() + " " + Util.toRoman(enchantment.getLevel()));
                    for (String s : Util.buildLore(enchantment.getBaseEnchantment().getDescription(enchantment.getLevel()))) {
                        lore.add(ChatColor.GRAY + s);
                    }
                }
            } else {
                List<String> enchantmentLore = new ArrayList<>();

                int maxCharsPerLine = 45;

                StringBuilder currentLine = new StringBuilder();

                for (ItemEnchantment enchantment : this.enchantments) {
                    if (currentLine.length() + enchantment.getBaseEnchantment().getDisplayName().length() + 1 > maxCharsPerLine) {
                        enchantmentLore.add(currentLine.toString());
                        currentLine = new StringBuilder();
                    }

                    currentLine.append(ChatColor.BLUE).append(enchantment.getBaseEnchantment().getName()).append(" ").append(Util.toRoman(enchantment.getLevel())).append(ChatColor.BLUE).append(", ");
                }

                if (currentLine.length() > 0) {
                    currentLine.delete(currentLine.length() - 2, currentLine.length());
                    enchantmentLore.add(currentLine.toString());
                }

                lore.addAll(enchantmentLore);
            }

            lore.add("");
        }

        /*
          Description
         */
        if (description != null && description.size() != 0 && !description.get(0).equals("laceholder descriptio"))
            lore.addAll(description);

        /*
          Ability
         */
        if (hasAbility) {
            lore.add(ChatColor.GOLD + "Item Ability: " + abilityName + "" + ChatColor.YELLOW + ChatColor.BOLD + abilityType);
            lore.addAll(abilityDescription);

            if (abilityCost != 0) lore.add(ChatColor.DARK_GRAY + "Mana Cost: " + ChatColor.DARK_AQUA + abilityCost);
            if (!Objects.equals(abilityCooldown, ""))
                lore.add(ChatColor.DARK_GRAY + "Cooldown: " + ChatColor.GREEN + abilityCooldown);

            lore.add("");
        }

        /*
          Reforge
         */

        if (reforgeable) lore.add(ChatColor.DARK_GRAY + "This item can be reforged!");

        /*
          Rarity
         */

        ChatColor nameColor = ChatColor.WHITE;

        if (this.rarityEnum.equals(Rarity.MYTHIC)) {
            lore.add("" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + rarity.toUpperCase());
            nameColor = ChatColor.LIGHT_PURPLE;
        } else if (this.rarityEnum.equals(Rarity.LEGENDARY)) {
            lore.add("" + ChatColor.GOLD + ChatColor.BOLD + rarity.toUpperCase());
            nameColor = ChatColor.GOLD;
        } else if (this.rarityEnum.equals(Rarity.EPIC)) {
            lore.add("" + ChatColor.DARK_PURPLE + ChatColor.BOLD + rarity.toUpperCase());
            nameColor = ChatColor.DARK_PURPLE;
        } else if (this.rarityEnum.equals(Rarity.RARE)) {
            lore.add("" + ChatColor.BLUE + ChatColor.BOLD + rarity.toUpperCase());
            nameColor = ChatColor.BLUE;
        } else if (this.rarityEnum.equals(Rarity.UNCOMMON)) {
            lore.add("" + ChatColor.GREEN + ChatColor.BOLD + rarity.toUpperCase());
            nameColor = ChatColor.GREEN;
        } else if (this.rarityEnum.equals(Rarity.COMMON))
            lore.add("" + ChatColor.WHITE + ChatColor.BOLD + rarity.toUpperCase());

        if (!(reforge == Reforge.NONE))
            meta.setDisplayName(nameColor + StringUtils.capitalize(reforge.toString().toLowerCase()) + " " + name);
        else meta.setDisplayName(name);

        meta.setLore(lore);

        if (enchantGlint || this.enchantments.size() > 0) meta.addEnchant(Enchantment.LUCK, 1, true);

        meta.spigot().setUnbreakable(true);

        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        this.stack.setItemMeta(meta);

        NBTItem nbt = new NBTItem(this.stack);

        nbt.setBoolean("skyblockItem", true);
        nbt.setString("name", name);
        nbt.setString("rarity", rarity);
        nbt.setString("reforgeType", reforge.toString());
        nbt.setString("item", item.toString());

        if (description != null) nbt.setString("description", description.toString());

        nbt.setBoolean("reforgeable", reforgeable);
        List<String> enchantmentNbt = new ArrayList<>();
        for (ItemEnchantment enchantment : this.enchantments) {
            enchantmentNbt.add(enchantment.getLevel() + ";" + enchantment.getBaseEnchantment().getName());
        }
        nbt.setString("enchantments", enchantmentNbt.toString());
        nbt.setBoolean("enchantGlint", enchantGlint);
        nbt.setBoolean("hasAbility", hasAbility);
        nbt.setString("abilityName", abilityName);
        nbt.setString("abilityType", abilityType);
        nbt.setString("abilityCooldown", abilityCooldown);
        nbt.setInteger("abilityCost", abilityCost);
        nbt.setString("abilityDescription", abilityDescription.toString());
        nbt.setInteger("damage", damage);
        nbt.setInteger("strength", strength + rStrength);
        nbt.setInteger("critChance", critChance + rCritChance);
        nbt.setInteger("critDamage", critDamage + rCritDamage);
        nbt.setInteger("attackSpeed", attackSpeed + rAttackSpeed);
        nbt.setInteger("intelligence", intelligence + rMana);
        nbt.setInteger("speed", speed + rSpeed);
        nbt.setInteger("defense", defense + rDefense);
        nbt.setInteger("health", health + rHealth);
        nbt.setString("skyblockId", skyblockId);

        this.stack = nbt.getItem();

        return this.stack;
    }

    public boolean hasEnchantment(SkyblockEnchantment enchantment) {
        for (ItemEnchantment itemEnchantment : this.enchantments) {
            System.out.println(itemEnchantment);
            System.out.println(itemEnchantment.getLevel());
            System.out.println(itemEnchantment.getBaseEnchantment());

            if (itemEnchantment.getBaseEnchantment().equals(enchantment)) return true;
        }
        return false;
    }

    public ItemEnchantment getEnchantment(String name) {
        for (ItemEnchantment enchantment : this.enchantments) {
            if (enchantment.getBaseEnchantment().getName().equalsIgnoreCase(name)) return enchantment;
        }

        return null;
    }

    public void setEnchantment(String name, int level) {
        SkyblockEnchantment enchantment = Skyblock.getPlugin(Skyblock.class).getEnchantmentHandler().getEnchantment(name);

        if (enchantment == null) return;

        ItemEnchantment itemEnchantment = new ItemEnchantment(enchantment, level);

        for (int i = 0; i < this.enchantments.size(); i++) {
            SkyblockEnchantment found = this.enchantments.get(i).getBaseEnchantment();

            if (found.getName().replace(" ", "_").equalsIgnoreCase(name)) {
                this.enchantments.set(i, itemEnchantment);

                return;
            }
        }

        this.enchantments.add(itemEnchantment);
    }

    public void addEnchantment(String name, int level) {
        SkyblockEnchantment enchantment = Skyblock.getPlugin(Skyblock.class).getEnchantmentHandler().getEnchantment(name);

        if (enchantment == null) return;

        this.enchantments.add(new ItemEnchantment(enchantment, level));
    }

    public void give(Player player) {
        player.getInventory().addItem(createStack());
    }

    public void set(Player player, int slot) {
        player.getInventory().setItem(slot, createStack());
    }

    public int getReforgeCost() {
        switch (this.rarityEnum) {
            case VERY_SPECIAL:
                return 50000;
            case SPECIAL:
                return 25000;
            case DIVINE:
                return 15000;
            case MYTHIC:
                return 10000;
            case LEGENDARY:
                return 5000;
            case EPIC:
                return 2500;
            case RARE:
                return 1000;
            case UNCOMMON:
                return 500;
            case COMMON:
                return 250;
            default:
                return 0;
        }
    }

    public Item getItem(String rarity) {
        if (Item.SWORD.containsQualifiedName(rarity)) return Item.SWORD;
        else if (Item.RANGED.containsQualifiedName(rarity)) return Item.RANGED;
        else if (Item.ARMOR.containsQualifiedName(rarity)) return Item.ARMOR;
        else return Item.NONE;
    }

    public Rarity getRarity(String rarity) {
        if (rarity.toUpperCase().contains("MYTHIC")) return Rarity.MYTHIC;
        else if (rarity.toUpperCase().contains("LEGENDARY")) return Rarity.LEGENDARY;
        else if (rarity.toUpperCase().contains("EPIC")) return Rarity.EPIC;
        else if (rarity.toUpperCase().contains("RARE")) return Rarity.RARE;
        else if (rarity.toUpperCase().contains("UNCOMMON")) return Rarity.UNCOMMON;
        else return Rarity.COMMON;
    }

    public void setStat(SkyblockStat stat, int value) {
        if (stat.equals(SkyblockStat.DAMAGE)) this.damage = value;
        else if (stat.equals(SkyblockStat.STRENGTH)) this.strength = value;
        else if (stat.equals(SkyblockStat.CRIT_CHANCE)) this.critChance = value;
        else if (stat.equals(SkyblockStat.CRIT_DAMAGE)) this.critDamage = value;
        else if (stat.equals(SkyblockStat.ATTACK_SPEED)) this.attackSpeed = value;
        else if (stat.equals(SkyblockStat.MANA)) this.intelligence = value;
        else if (stat.equals(SkyblockStat.SPEED)) this.speed = value;
        else if (stat.equals(SkyblockStat.DEFENSE)) this.defense = value;
        else if (stat.equals(SkyblockStat.HEALTH)) this.health = value;
    }

    public int getStat(SkyblockStat stat) {
        if (stat.equals(SkyblockStat.DAMAGE)) return this.damage;
        else if (stat.equals(SkyblockStat.STRENGTH)) return this.strength;
        else if (stat.equals(SkyblockStat.CRIT_CHANCE)) return this.critChance;
        else if (stat.equals(SkyblockStat.CRIT_DAMAGE)) return this.critDamage;
        else if (stat.equals(SkyblockStat.ATTACK_SPEED)) return this.attackSpeed;
        else if (stat.equals(SkyblockStat.MANA)) return this.intelligence;
        else if (stat.equals(SkyblockStat.SPEED)) return this.speed;
        else if (stat.equals(SkyblockStat.DEFENSE)) return this.defense;
        else if (stat.equals(SkyblockStat.HEALTH)) return this.health;
        else return 0;
    }

    public static ItemStack reforge(ItemStack stack, Reforge reforge) {
        ItemBase base = new ItemBase(stack);

        Rarity r = base.getRarityEnum();

        for (SkyblockStat stat : base.getReforge().getReforgeData(r).getStats().keySet()) {
            base.setStat(stat, base.getStat(stat) - base.getReforge().getReforgeData(r).getStats().get(stat));
        }

        base.createStack();

        base.setReforge(reforge);

        return base.createStack();
    }

}

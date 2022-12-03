package com.skyblock.skyblock.commands.misc;

import com.skyblock.skyblock.Skyblock;
import com.skyblock.skyblock.SkyblockPlayer;
import com.skyblock.skyblock.enums.MiningMinionType;
import com.skyblock.skyblock.enums.Rarity;
import com.skyblock.skyblock.features.pets.combat.BlueWhale;
import com.skyblock.skyblock.features.minions.MiningMinion;
import com.skyblock.skyblock.features.minions.MinionBase;
import com.skyblock.skyblock.features.pets.combat.Jerry;
import com.skyblock.skyblock.features.pets.combat.Tiger;
import com.skyblock.skyblock.features.skills.Combat;
import com.skyblock.skyblock.features.skills.Skill;
import com.skyblock.skyblock.features.slayer.SlayerHandler;
import com.skyblock.skyblock.features.slayer.SlayerType;
import com.skyblock.skyblock.features.slayer.boss.RevenantHorror;
import com.skyblock.skyblock.utilities.Util;
import com.skyblock.skyblock.utilities.command.Command;
import com.skyblock.skyblock.utilities.command.annotations.Description;
import com.skyblock.skyblock.utilities.command.annotations.RequiresPlayer;
import com.skyblock.skyblock.utilities.command.annotations.Usage;
import org.bukkit.entity.Player;

@RequiresPlayer
@Usage(usage = "/sb test")
@Description(description = "Command for testing features")
public class TestCommand implements Command {

    @Override
    public void execute(Player player, String[] args, Skyblock plugin) {
        Skill.reward(new Combat(), Integer.parseInt(args[0]), SkyblockPlayer.getPlayer(player));

        Jerry jerry = new Jerry();
        jerry.setRarity(Rarity.LEGENDARY);

        Tiger tiger = new Tiger();
        tiger.setRarity(Rarity.LEGENDARY);

        BlueWhale whale = new BlueWhale();
        whale.setRarity(Rarity.RARE);

        player.getInventory().addItem(jerry.toItemStack());
        player.getInventory().addItem(tiger.toItemStack());
        player.getInventory().addItem(whale.toItemStack());

        MinionBase cobblestoneMinion = new MiningMinion(MiningMinionType.COBBLESTONE);
        cobblestoneMinion.spawn(SkyblockPlayer.getPlayer(player), player.getLocation(), 1);
    }
}

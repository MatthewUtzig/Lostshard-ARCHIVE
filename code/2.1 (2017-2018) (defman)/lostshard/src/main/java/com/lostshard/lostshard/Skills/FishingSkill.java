package com.lostshard.lostshard.Skills;

import java.util.Random;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.inventory.ItemStack;

import com.lostshard.lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.lostshard.Utils.Output;

@Embeddable
@Access(AccessType.FIELD)
public class FishingSkill extends Skill {

	public static void callBoat(Player player) {
		final PseudoPlayer pPlayer = pm.getPlayer(player);
		final Skill skill = pPlayer.getCurrentBuild().getFishing();
		final int lvl = skill.getLvl();
		if (lvl < 500) {
			Output.simpleError(player, "You need 50 skill in fishing to create a boat out of thin air.");
			return;
		}
		final int curStam = pPlayer.getStamina();
		if (curStam < 50) {
			Output.simpleError(player, "You do not have enough stamina to create a boat.");
			return;
		}
		pPlayer.setStamina(curStam - 50);
		if (!(Math.random() * 1000 < lvl)) {
			Output.simpleError(player, "You failed to create the boat.");
			return;
		}
		boolean placedBoat = false;
		for (int x = player.getLocation().getBlockX() - 3; x < player.getLocation().getBlockX() + 3; x++) {
			for (int y = player.getLocation().getBlockY() - 3; y < player.getLocation().getBlockY() + 3; y++) {
				for (int z = player.getLocation().getBlockZ() - 3; z < player.getLocation().getBlockZ() + 3; z++) {
					final Block b = player.getWorld().getBlockAt(x, y, z);
					final Block blockAbove = player.getWorld().getBlockAt(x, y + 1, z);
					if (b != null && b.getType().equals(Material.STATIONARY_WATER)
							&& blockAbove.getType().equals(Material.AIR)) {
						Output.positiveMessage(player, "You summoned a boat.");
						placedBoat = true;
						final int gain = skill.skillGain(pPlayer);
						Output.gainSkill(player, "Fishing", gain, skill.getLvl());
						player.getWorld().spawn(new Location(b.getWorld(), b.getX() + .5, b.getY() + .5, b.getZ() + .5),
								Boat.class);
						break;
					}
					if (placedBoat)
						break;
				}
				if (placedBoat)
					break;
			}
			if (placedBoat)
				break;
		}

		if (!placedBoat)
			Output.simpleError(player, "Could not find a valid location to place a boat.");
	}

	public static ItemStack getRandomFish(int lvl) {
		final Random ran = new Random();
		final int amount = (int) Math.ceil(Math.random() * 3);
		return new ItemStack(Material.RAW_FISH, amount, (byte) ran.nextInt(3));
	}

	public static ItemStack getRandomHigh() {
		final Random ran = new Random();
		if (Math.random() < .75)
			switch (ran.nextInt(5)) {
			case 0:
			case 1:
				return new ItemStack(Material.IRON_INGOT);
			case 2:
				return new ItemStack(Material.GOLD_INGOT);
			case 3:
				return new ItemStack(Material.DIAMOND);
			default:
				return new ItemStack(Material.IRON_INGOT);
			}
		else
			switch (ran.nextInt(10)) {
			case 0:
			case 1:
			case 2:
				return new ItemStack(Material.GOLD_INGOT);
			case 3:
			case 4:
			case 5:
			case 6:
				return new ItemStack(Material.IRON_BLOCK);
			case 7:
				return new ItemStack(Material.DIAMOND_BLOCK);
			case 8:
				return new ItemStack(Material.GOLD_BLOCK);
			default:
				return new ItemStack(Material.IRON_INGOT);
			}
	}

	public static ItemStack getRandomHighst() {
		final Random ran = new Random();
		if (Math.random() < .01)
			switch (ran.nextInt(12)) {
			case 0:
				return new ItemStack(Material.RECORD_10);
			case 1:
				return new ItemStack(Material.RECORD_11);
			case 2:
				return new ItemStack(Material.RECORD_12);
			case 3:
				return new ItemStack(Material.RECORD_3);
			case 4:
				return new ItemStack(Material.RECORD_4);
			case 5:
				return new ItemStack(Material.RECORD_5);
			case 6:
				return new ItemStack(Material.RECORD_6);
			case 7:
				return new ItemStack(Material.RECORD_7);
			case 8:
				return new ItemStack(Material.RECORD_8);
			case 9:
				return new ItemStack(Material.RECORD_9);
			case 10:
				return new ItemStack(Material.RECORD_10);
			default:
				return new ItemStack(Material.DRAGON_EGG);	
			}
		else
			switch (ran.nextInt(10)) {
			case 0:
			case 1:
			case 2:
				return new ItemStack(Material.GOLD_INGOT);
			case 3:
			case 4:
			case 5:
			case 6:
				return new ItemStack(Material.IRON_BLOCK);
			case 7:
				return new ItemStack(Material.DIAMOND_BLOCK);
			case 8:
				return new ItemStack(Material.DIAMOND_BLOCK);
			default:
				return new ItemStack(Material.GOLD_BLOCK);
			}
	}

	public static ItemStack getRandomLow() {
		final Random ran = new Random();
		if (Math.random() < .33)
			switch (ran.nextInt(4)) {
			case 0:
			case 1:
				return new ItemStack(Material.IRON_INGOT);
			case 2:
				return new ItemStack(Material.GOLD_INGOT);
			default:
				return new ItemStack(Material.IRON_INGOT);
			}
		else
			switch (ran.nextInt(9)) {
			case 0:
				return new ItemStack(Material.WOOD_AXE);
			case 1:
				return new ItemStack(Material.WOOD_HOE);
			case 2:
				return new ItemStack(Material.WOOD_PICKAXE);
			case 3:
				return new ItemStack(Material.WOOD_SPADE);
			case 4:
				return new ItemStack(Material.WOOD_SWORD);
			case 5:
				return new ItemStack(Material.BOWL);
			case 6:
				return new ItemStack(Material.STRING);
			case 7:
				return new ItemStack(Material.STICK);
			default:
				return new ItemStack(Material.TRIPWIRE_HOOK);
			}
	}

	public static ItemStack getRandomMedium() {
		final Random ran = new Random();
		if (Math.random() < .33)
			switch (ran.nextInt(4)) {
			case 0:
				return new ItemStack(Material.IRON_INGOT);
			case 1:
			case 2:
				return new ItemStack(Material.GOLD_INGOT);
			case 3:
				return new ItemStack(Material.DIAMOND);
			default:
				return new ItemStack(Material.IRON_INGOT);
			}
		else
			switch (ran.nextInt(9)) {
			case 0:
				return new ItemStack(Material.WOOD_AXE);
			case 1:
				return new ItemStack(Material.WOOD_HOE);
			case 2:
				return new ItemStack(Material.WOOD_PICKAXE);
			case 3:
				return new ItemStack(Material.WOOD_SPADE);
			case 4:
				return new ItemStack(Material.WOOD_SWORD);
			case 5:
				return new ItemStack(Material.BOWL);
			case 6:
				return new ItemStack(Material.STRING);
			case 7:
				return new ItemStack(Material.STICK);
			default:
				return new ItemStack(Material.STICK);
			}
	}

	public static void onFish(PlayerFishEvent event) {
		if (event.getState().equals(State.CAUGHT_FISH)) {
			final Player player = event.getPlayer();
			final PseudoPlayer pPlayer = pm.getPlayer(player);
			final Skill skill = pPlayer.getCurrentBuild().getFishing();
			final int lvl = skill.getLvl();
			if (lvl >= 250 && Math.random() < .5) {
				if (lvl >= 1000)
					player.getLocation().getWorld().dropItemNaturally(player.getLocation(), getRandomHighst());
				else if (lvl >= 750)
					player.getLocation().getWorld().dropItemNaturally(player.getLocation(), getRandomHigh());
				else if (lvl >= 500)
					player.getLocation().getWorld().dropItemNaturally(player.getLocation(), getRandomMedium());
				else
					player.getLocation().getWorld().dropItemNaturally(player.getLocation(), getRandomLow());
			} else if (lvl >= 500)
				player.getLocation().getWorld().dropItemNaturally(player.getLocation(), getRandomFish(lvl));
			final int gain = skill.skillGain(pPlayer);
			Output.gainSkill(player, "Fishing", gain, skill.getLvl());
		}
	}

	public FishingSkill() {
		super();
		this.setName("Fishing");
		this.setBaseProb(1);
		this.setMaxGain(30);
		this.setMinGain(10);
		this.setScaleConstant(40);
		this.setMat(Material.FISHING_ROD);
	}

	public FishingSkill(int lvl, boolean locked) {
		super(lvl, locked);
	}

	@Override
	public String howToGain() {
		return "You can gain fishing by fishing";
	}
}
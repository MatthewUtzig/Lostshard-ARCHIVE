package com.lostshard.lostshard.Skills;

import javax.persistence.Embeddable;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Style;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.ItemStack;
import org.spigotmc.event.entity.EntityMountEvent;

import com.lostshard.lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.lostshard.Utils.Output;
import com.lostshard.lostshard.Utils.SpellUtils;

@Embeddable
public class TamingSkill extends Skill {

	public static void callMount(Player player, PseudoPlayer pseudoPlayer) {
		if (pseudoPlayer.getStamina() >= 10) {

			final TamingSkill skill = pseudoPlayer.getCurrentBuild().getTaming();
			final int lvl = skill.getLvl();
			if (lvl < 500) {
				Output.simpleError(player, "You are not skilled enough to have a mount.");
				return;
			}

			if (pseudoPlayer.getEngageInCombatTicks() > 0) {
				Output.simpleError(player, "You can't mount while in or shortly after combat.");
				return;
			}

			if (!skill.isMount()) {
				Output.simpleError(player, "You do not have a mount you can summon.");
				return;
			}
			if (!(pseudoPlayer.getStamina() >= 10)) {
				Output.simpleError(player, "You do not have enough stamina to summon your pets.");
				return;
			}
			final Block blockFound = player.getLocation().getBlock();
			for (int x = blockFound.getX() - 1; x <= blockFound.getX() + 1; x++)
				for (int y = blockFound.getY(); y <= blockFound.getY() + 2; y++)
					for (int z = blockFound.getZ() - 1; z <= blockFound.getZ() + 1; z++)
						if (!SpellUtils.invisibleBlocks.contains(blockFound.getWorld().getBlockAt(x, y, z).getType())) {
							Output.simpleError(player, "You can't summon a mount in rough terrain");
							return;
						}

			pseudoPlayer.setStamina(pseudoPlayer.getStamina() - 10);

			Horse horse = null;

			horse = (Horse) player.getWorld().spawnEntity(player.getLocation(), EntityType.HORSE);
			horse.setTamed(true);
			horse.setOwner(player);
			horse.setAdult();
			horse.setAge(10);
			horse.setAgeLock(true);
			horse.setJumpStrength(0.8);
			horse.setBreed(false);
			horse.setMaxHealth(20);
			horse.setHealth(20);
			// final AttributeInstance attributes = ((EntityInsentient)
			// ((CraftLivingEntity) horse)
			// .getHandle()).getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);
			// attributes.setValue(0.2);
			if (!(player.isOp() || pseudoPlayer.isSubscriber())) {
				horse.setColor(Color.BLACK);
				horse.setVariant(Variant.HORSE);
				horse.setStyle(Style.BLACK_DOTS);
				horse.getInventory().setArmor(new ItemStack(Material.DIAMOND_BARDING));
				horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
			} else {
				horse.setVariant(Variant.SKELETON_HORSE);
				horse.getInventory().setArmor(new ItemStack(Material.DIAMOND_BARDING));
				horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
			}
			horse.setPassenger(player);
		}
		Output.positiveMessage(player, "You have summoned your mount.");

	}

	public static void onDamage(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Horse && ((Horse) event.getEntity()).getPassenger() != null)
			event.getEntity().remove();
	}

	public static void onDismount(VehicleExitEvent event) {
		if (event.getVehicle() instanceof Horse)
			event.getVehicle().remove();
	}

	public static void onLave(PlayerQuitEvent event) {
		if (event.getPlayer().getVehicle() instanceof Horse)
			event.getPlayer().getVehicle().remove();
	}

	public static void onMount(EntityMountEvent event) {

	}

	public static void onTame(EntityTameEvent event) {

	}

	private int doogs = 0;

	private boolean mount = true;

	public TamingSkill() {
		super();
		this.setName("Taming");
		this.setBaseProb(.2);
		this.setScaleConstant(60);
		this.setMat(Material.IRON_BARDING);
	}

	public TamingSkill(int lvl, boolean locked) {
		super(lvl, locked);
	}

	public void callPets() {

	}

	public int getDoogs() {
		return this.doogs;
	}

	@Override
	public String howToGain() {
		return "You can gain taming by...";
	}

	public boolean isMount() {
		return this.mount;
	}

	public void setDoogs(int doogs) {
		this.doogs = doogs;
	}

	public void setMount(boolean mount) {
		this.mount = mount;
	}
}

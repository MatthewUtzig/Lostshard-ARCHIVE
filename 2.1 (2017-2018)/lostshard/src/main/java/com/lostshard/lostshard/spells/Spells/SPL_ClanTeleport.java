package com.lostshard.lostshard.Spells.Spells;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.main.Lostshard;
import com.lostshard.lostshard.Objects.Groups.Clan;
import com.lostshard.lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.lostshard.Spells.Scroll;
import com.lostshard.lostshard.Spells.Spell;
import com.lostshard.lostshard.Utils.Output;

public class SPL_ClanTeleport extends Spell {

	public SPL_ClanTeleport(Scroll scroll) {
		super(scroll);
		this.setPrompt("Who would you like to teleport to?");
	}

	@Override
	public void doAction(Player player) {
		// System.out.println("RSPNS: "+_response);
		final PseudoPlayer pseudoPlayer = this.pm.getPlayer(player);
		final Player targetPlayer = Bukkit.getPlayer(this.getResponse());

		if (targetPlayer == null || Lostshard.isVanished(targetPlayer) && player.isOp()) {
			Output.simpleError(player, "Player not found.");
			return;
		} else {
			final PseudoPlayer targetPseudoPlayer = this.pm.getPlayer(targetPlayer);
			final Clan playerClan = pseudoPlayer.getClan();
			final Clan targetPlayerClan = targetPseudoPlayer.getClan();
			if (playerClan != null && targetPlayerClan != null
					&& playerClan.getName().endsWith(targetPlayerClan.getName())) {
				if (targetPseudoPlayer.isPrivate()) {
					Output.positiveMessage(player, "Can't teleport to that player, they are set to private.");
					return;
				}

				if (targetPlayer.getHealth() <= 0) {
					Output.positiveMessage(player, "You can't teleport to that player, as they are dead.");
					return;
				}

				// check for lapis below you
				if (player.getLocation().getBlock().getRelative(0, -1, 0).getType().equals(Material.LAPIS_BLOCK)
						|| player.getLocation().getBlock().getRelative(0, -2, 0).getType()
								.equals(Material.LAPIS_BLOCK)) {
					Output.simpleError(player, "can't teleport from a Lapis Lazuli block.");
					return;
				}

				// check for lapis below your target location
				if (targetPlayer.getLocation().getBlock().getRelative(0, -1, 0).getType().equals(Material.LAPIS_BLOCK)
						|| targetPlayer.getLocation().getBlock().getRelative(0, -2, 0).getType()
								.equals(Material.LAPIS_BLOCK)) {
					Output.simpleError(player, "can't teleport to a Lapis Lazuli block.");
					return;
				}

				player.getWorld().strikeLightningEffect(player.getLocation());

				player.teleport(targetPlayer.getLocation());

				player.getWorld().strikeLightningEffect(player.getLocation());

				pseudoPlayer.setMana(0);
				pseudoPlayer.setStamina(0);
				Output.sendEffectTextNearby(player, "You hear a loud crack and the fizzle of electricity.");
				player.sendMessage(ChatColor.GRAY + "Teleporting without a rune has exausted you.");
			} else {
				Output.simpleError(player, "That player is not in your clan.");
				return;
			}
		}
	}

	@Override
	public void preAction(Player player) {
		Output.positiveMessage(player, "You begin casting Clan Teleport");
	}

	@Override
	public void runebook(Player player) {

	}

	@Override
	public boolean verifyCastable(Player player) {
		return true;
	}
}

package com.lostshard.Lostshard.Spells.Spells;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;

import com.lostshard.Lostshard.Spells.Scroll;
import com.lostshard.Lostshard.Spells.Spell;
import com.lostshard.Lostshard.Utils.Output;

public class SPL_ClearSky extends Spell {

	public static List<UUID> casting = new LinkedList<UUID>();
	
	public SPL_ClearSky(Scroll scroll) {
		super(scroll);
	}

	public void castLigningsFinish(Player player) {
		for(int f=0; f<20; f++) {
			double castX = Math.random() * 30;
			if(Math.random() < .5)
				castX = -castX;
			castX = player.getLocation().getX()+castX;
			double castZ = Math.random() * 30;
			if(Math.random() < .5)
				castZ = -castZ;
			castZ = player.getLocation().getZ()+castZ;
			
			Location strikeLocation = new Location(player.getWorld(), castX, player.getWorld().getHighestBlockYAt((int)Math.floor(castX), (int)Math.floor(castZ)), castZ);
			player.getWorld().strikeLightningEffect(strikeLocation);
		}
	}
	
	@Override
	public void doAction(Player player) {
		Output.positiveMessage(player, "You begin chanelling Clear Sky...");
		Player castingPlayer = null;
		Iterator<UUID> iterator = casting.iterator();
		while(iterator.hasNext()) {
			castingPlayer = Bukkit.getPlayer(iterator.next());
			if(!castingPlayer.isOnline())
				iterator.remove();
			if(castingPlayer.getWorld().equals(player.getWorld()))
				break;
			castingPlayer = null;
		}
		if(castingPlayer != null) {
			Output.positiveMessage(castingPlayer, "You and "+player.getName()+" successfully cast Clear Sky.");
			Output.positiveMessage(player, "You and "+castingPlayer.getName()+" successfully cast Clear Sky.");
			castLigningsFinish(player);
			castLigningsFinish(castingPlayer);
			player.getWorld().setStorm(false);
			final Player c = castingPlayer;
			casting.removeIf(u -> u.equals(player.getUniqueId()) || u.equals(c.getUniqueId()));
		}else{
			casting.add(player.getUniqueId());
		}
	}

	@Override
	public void preAction(Player player) {

	}

	@Override
	public boolean verifyCastable(Player player) {
		if(casting.equals(player.getUniqueId()))
			Output.simpleError(player, "You are already casting Clear Sky.");
		else if(SPL_Day.casting.contains(player.getUniqueId()))
			Output.simpleError(player, "You are currently casting Day.");
		else if(!player.getWorld().getWorldType().equals(WorldType.NORMAL))
			Output.simpleError(player, "You can only cast Clear Sky in the normal world.");
		return true;
	}

}

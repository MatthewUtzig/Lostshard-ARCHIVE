package com.lostshard.lostshard.Handlers;

import java.util.Random;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;

import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Spells.Scroll;
import com.lostshard.lostshard.Utils.Output;

public class ScrollHandler {

	static PlayerManager pm = PlayerManager.getManager();
	
	public static void onEntityDeathEvent(EntityDeathEvent event) {
		if(event.getEntity().getKiller() == null)
			return;
		if(!(event.getEntity().getKiller() instanceof Player))
			return;
		if(Math.random() > .2)
			return;
		Random rand = new Random();
		Player player = event.getEntity().getKiller();
		PseudoPlayer pPlayer = pm.getPlayer(player);
		Entity entity = event.getEntity();
		EntityType type = entity.getType();
		Scroll scroll = null;
		switch(type) {
			case ZOMBIE:
				switch(rand.nextInt(4)) {
				case 0:
					scroll = Scroll.GRASS;
					break;
				case 1:
//					scroll = Scroll.FLOWERS;
					break;
				case 2:
					scroll = Scroll.LIGHT;
					break;
				default:
//					scroll = Scroll.CREATEFOOD;
					break;
				}
				break;
			case SKELETON:
				switch(rand.nextInt(10)) {
				case 0:
					scroll = Scroll.ICEBALL;
					break;
				case 1:
					scroll = Scroll.FIREFIELD;
					break;
				case 2:
					scroll = Scroll.HEALSELF;
					break;
				case 3:
					scroll = Scroll.HEALOTHER;
					break;
				case 4:
//					scroll = Scroll.FIREWALK;
					break;
				case 5:
					scroll = Scroll.SUMMONANIMAL;
					break;
				case 6:
					scroll = Scroll.SLOWFIELD;
					break;
				case 7:
//					scroll = Scroll.FORCEPUSH;
					break;
				case 8:
//					scroll = Scroll.CHRONOPORT;
					break;
				default:
//					scroll = Scroll.DETECTHIDDEN;
					break;
				}
				break;
			case BLAZE:
				break;
			case CAVE_SPIDER:
			case SPIDER:
				switch(rand.nextInt(5)) {
				case 0:
					scroll = Scroll.TELEPORT;
					break;
				case 1:
					scroll = Scroll.RECALL;
					break;
				case 2:
					scroll = Scroll.MARK;
					break;
				case 3:
//					scroll = Scroll.BRIDGE;
					break;
				default:
//					scroll = Scroll.WALLOFSTONE;
					break;
				}
				break;
			case CREEPER:
				switch(rand.nextInt(27)) {
				case 0:
				case 1:
				case 2:
				case 3:
					scroll = Scroll.GATETRAVEL;
					break;
				case 5:
				case 6:
				case 7:
				case 8:
					scroll = Scroll.CLANTELEPORT;
					break;
				case 9:
				case 10:
				case 11:
				case 12:
//					scroll = Scroll.FIREBALL;
					break;
				case 13:
				case 14:
				case 15:
				case 16:
					scroll = Scroll.LIGHTNING;
					break;
				case 17:
				case 18:
				case 19:
				case 20:
//					scroll = Scroll.STONESKIN;
					break;
				case 21:
					scroll = Scroll.PERMANENTGATETRAVEL;
					break;
				case 22:
//					scroll = Scroll.DAY;
					break;
				case 23:
//					scroll = Scroll.CLEARSKY;
					break;
				case 24:
					scroll = Scroll.SUMMONMONSTER;
					break;
				case 25:
//					scroll = Scroll.MOONJUMP;
					break;
				 default:
					scroll = Scroll.ARROWBLAST;
					break;
				}
				break;
			case GHAST:
				switch(rand.nextInt(6)) {
				case 0:
					scroll = Scroll.PERMANENTGATETRAVEL;
					break;
				case 1:
//					scroll = Scroll.DAY;
					break;
				case 2:
//					scroll = Scroll.CLEARSKY;
					break;
				case 3:
//					scroll = Scroll.MOONJUMP;
					break;
				case 4:
					scroll = Scroll.SUMMONMONSTER;
					break;
				default:
					scroll = Scroll.ARROWBLAST;
					break;
				}
				break;
			case ENDERMAN:
				break;
			case ENDERMITE:
				break;
			case GUARDIAN:
				break;
			case PIG_ZOMBIE:
				break;
			case WITCH:
				break;
			case SILVERFISH:
				break;
			case ENDER_DRAGON:
				break;
			case WITHER:
				break;
		default:
			break;
		}
		if(scroll != null) {
			pPlayer.addScroll(scroll);
			Output.positiveMessage(player, "The "+type.name().toLowerCase()+" dropped a scroll of "+scroll.getSpellName()+".");
		}
	}
}

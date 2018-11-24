package com.lostshard.RPG.Spells;

import org.bukkit.entity.Player;
import org.bukkit.entity.Fireball;

import com.lostshard.RPG.PseudoPlayer;
import com.lostshard.RPG.PseudoPlayerHandler;

public class SPL_Fireball extends Spell {
	private static final String 	_name = "Fireball";
	private static final String 	_spellWords = "Charmanderous Ballicus";
	private static final int 		_castingDelay = 0;
	private static final int 		_cooldownTicks = 20;
	private static final int		_manaCost = 10;
	private static final int[]		_reagentCost = {289,331};
	private static final int 		_minMagery = 720;
	
	public String getName() 		{ return _name; }
	public String getSpellWords() 	{ return _spellWords; }
	public int getCastingDelay() 	{ return _castingDelay; }
	public int getCooldownTicks()	{ return _cooldownTicks; }
	public int getManaCost() 		{ return _manaCost; }
	public int[] getReagentCost() 	{ return _reagentCost; }
	public int getMinMagery() 		{ return _minMagery; }
	
	public int getPageNumber()		{ return 7; }
	
	/* Used to confirm that the spell can be cast, so, for example, if you were
	 * attempting to teleport to some location that was blocked, we would figure
	 * that out here and cancel the spell.
	 */
	public boolean verifyCastable(Player player, PseudoPlayer pseudoPlayer) {
		return true;
	}
	
	/* Used for anything that must be handled as soon as the spell is cast,
	 * for example targeting a location for a delayed spell.
	 */
	public void preAction(Player player) {

	}
	
	/* The meat of the spell code, this is what happens when the spell is
	 * actually activated and should be doing something.
	 */
	public void doAction(Player player) {
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
		/*
		Vector v = player.getLocation().getDirection();
		CraftPlayer craftPlayer = (CraftPlayer) player;
		EntityLiving playerEntity = craftPlayer.getHandle();
		EntityFireball fireball = new EntityFireball(((CraftWorld) player.getWorld()).getHandle(), playerEntity, v.getX(), v.getY(), v.getZ());
		((CraftWorld) player.getWorld()).getHandle().a(fireball);*/
		
		//Fireball fb = new Fireball();
		
		Fireball fb = player.getWorld().spawn(player.getEyeLocation(), Fireball.class);
		//fb.setYield(0);
		fb.setShooter(player);
		System.out.println(fb.getDirection());
//		/fb.set
		pseudoPlayer.setCantCastTicks(_cooldownTicks);
	}
}

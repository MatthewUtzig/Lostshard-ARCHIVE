package com.lostshard.lostshard.Spells.Spells;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import com.lostshard.lostshard.Spells.Scroll;
import com.lostshard.lostshard.Spells.Spell;

public class SPL_Flare extends Spell {

	public SPL_Flare(Scroll scroll) {
		super(scroll);
	}
	
	/* The meat of the spell code, this is what happens when the spell is
	 * actually activated and should be doing something.
	 */
	@Override
	public void doAction(Player player) {
		//Spawn the Firework, get the FireworkMeta.
        Firework fw = (Firework) player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta(); 
       
        //Create our effect with this
        FireworkEffect effect = FireworkEffect.builder().flicker(true).withColor(Color.RED).with(Type.STAR).trail(true).build();
       
        //Then apply the effect to the meta
        fwm.addEffect(effect);
       
        //Generate some random power and set it
        fwm.setPower(1);
       
        //Then apply this to our rocket
        fw.setFireworkMeta(fwm); 
	}
	
	/* Used for anything that must be handled as soon as the spell is cast,
	 * for example targeting a location for a delayed spell.
	 */
	@Override
	public void preAction(Player player) {

	}
	
	/* Used to confirm that the spell can be cast, so, for example, if you were
	 * attempting to teleport to some location that was blocked, we would figure
	 * that out here and cancel the spell.
	 */
	@Override
	public boolean verifyCastable(Player player) {
		return true;
	}
	
}

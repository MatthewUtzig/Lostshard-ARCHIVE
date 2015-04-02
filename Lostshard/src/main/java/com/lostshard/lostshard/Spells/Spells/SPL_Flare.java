package com.lostshard.lostshard.Spells.Spells;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import com.lostshard.lostshard.Spells.Spell;

public class SPL_Flare extends Spell {

	public SPL_Flare() {
		super();
		setName("Flare");
		setSpellWords("Beforius Flarius");
		setCastingDelay(0);
		setCooldown(5);
		setManaCost(0);
		setPage(1);
		setMinMagery(0);
		addReagentCost(new ItemStack(Material.SULPHUR));
	}
	
	/* Used to confirm that the spell can be cast, so, for example, if you were
	 * attempting to teleport to some location that was blocked, we would figure
	 * that out here and cancel the spell.
	 */
	public boolean verifyCastable(Player player) {
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
	
}

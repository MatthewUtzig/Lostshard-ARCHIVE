package com.lostshard.lostshard.Spells;

import com.lostshard.lostshard.Spells.Spells.SPL_Flare;
import com.lostshard.lostshard.Spells.Spells.SPL_GateTravel;
import com.lostshard.lostshard.Spells.Spells.SPL_Grass;
import com.lostshard.lostshard.Spells.Spells.SPL_Iceball;
import com.lostshard.lostshard.Spells.Spells.SPL_Mark;
import com.lostshard.lostshard.Spells.Spells.SPL_PermanentGateTravel;
import com.lostshard.lostshard.Spells.Spells.SPL_Recall;
import com.lostshard.lostshard.Spells.Spells.SPL_Slowfield;
import com.lostshard.lostshard.Spells.Spells.SPL_Teleport;

public enum SpellType {
	MARK,
	TELEPORT, 
	RECALL, 
	PERMANENTGATETRAVEL,
	GATETRAVEL,
	FLARE,
	SLOWFIELD,
	GRASS,
	ICEBALL;

	public Spell getSpell() {
		switch (this) {
		case MARK:
			return new SPL_Mark();
		case TELEPORT:
			return new SPL_Teleport();
		case RECALL:
			return new SPL_Recall();
		case PERMANENTGATETRAVEL:
			return new SPL_PermanentGateTravel();
		case GATETRAVEL:
			return new SPL_GateTravel();
		case FLARE:
			return new SPL_Flare();
		case SLOWFIELD:
			return new SPL_Slowfield();
		case GRASS:
			return new SPL_Grass();
		case ICEBALL:
			return new SPL_Iceball();
		default:
			return null;
		}
	}
	
	public static SpellType getByString(String string) {
		for(SpellType st : values())
			if(st.name().equalsIgnoreCase(string.trim().replace(" ", "")))
				return st;
		return null;
	}
}
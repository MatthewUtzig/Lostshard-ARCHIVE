package com.lostshard.lostshard.Spells;

import com.lostshard.lostshard.Spells.Spells.SPL_ArrowBlast;
import com.lostshard.lostshard.Spells.Spells.SPL_ClanTeleport;
import com.lostshard.lostshard.Spells.Spells.SPL_Firefield;
import com.lostshard.lostshard.Spells.Spells.SPL_Flare;
import com.lostshard.lostshard.Spells.Spells.SPL_GateTravel;
import com.lostshard.lostshard.Spells.Spells.SPL_Grass;
import com.lostshard.lostshard.Spells.Spells.SPL_HealOther;
import com.lostshard.lostshard.Spells.Spells.SPL_HealSelf;
import com.lostshard.lostshard.Spells.Spells.SPL_Iceball;
import com.lostshard.lostshard.Spells.Spells.SPL_Light;
import com.lostshard.lostshard.Spells.Spells.SPL_Lightning;
import com.lostshard.lostshard.Spells.Spells.SPL_Mark;
import com.lostshard.lostshard.Spells.Spells.SPL_PermanentGateTravel;
import com.lostshard.lostshard.Spells.Spells.SPL_Recall;
import com.lostshard.lostshard.Spells.Spells.SPL_Slowfield;
import com.lostshard.lostshard.Spells.Spells.SPL_Teleport;

public enum Scroll {
	MARK,
	TELEPORT, 
	RECALL, 
	PERMANENTGATETRAVEL,
	GATETRAVEL,
	FLARE,
	SLOWFIELD,
	GRASS,
	ICEBALL,
	ARROWBLAST,
	CLANTELEPORT,
	LIGHT,
	HEALSELF,
	HEALOTHER,
	LIGHTNING,
	FIREFIELD;

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
		case ARROWBLAST:
			return new SPL_ArrowBlast();
		case CLANTELEPORT:
			return new SPL_ClanTeleport();
		case LIGHT:
			return new SPL_Light();
		case HEALSELF:
			return new SPL_HealSelf();
		case HEALOTHER:
			return new SPL_HealOther();
		case LIGHTNING:
			return new SPL_Lightning();
		case FIREFIELD:
			return new SPL_Firefield();
		default:
			return null;
		}
	}
	
	public String getName() {
		return name();
	}
	
	public String getSpellName() {
		return getSpell().getName();
	}
	
	public static Scroll getByString(String string) {
		for(Scroll st : values())
			if(st.name().equalsIgnoreCase(string.trim().replace(" ", "")))
				return st;
		return null;
	}
}
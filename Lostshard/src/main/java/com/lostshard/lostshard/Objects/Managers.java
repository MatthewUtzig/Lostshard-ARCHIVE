package com.lostshard.lostshard.Objects;

import com.lostshard.lostshard.Manager.ChestRefillManager;
import com.lostshard.lostshard.Manager.ClanManager;
import com.lostshard.lostshard.Manager.ConfigManager;
import com.lostshard.lostshard.Manager.NPCManager;
import com.lostshard.lostshard.Manager.PartyManager;
import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Manager.PlotManager;
import com.lostshard.lostshard.Manager.SpellManager;
import com.lostshard.lostshard.Manager.StoreManager;
import com.lostshard.lostshard.Manager.TaskManager;

public interface Managers {

	public PlayerManager pm = PlayerManager.getManager();
	public PlotManager ptm = PlotManager.getManager();
	public ChestRefillManager crm = ChestRefillManager.getManager();
	public ClanManager cm = ClanManager.getManager();
	public NPCManager nm = NPCManager.getManager();
	public PartyManager partym = PartyManager.getManager();
	public SpellManager sm = SpellManager.getManager();
	public StoreManager storem = StoreManager.getManager();
	public TaskManager tm = TaskManager.getManager();
	public ConfigManager cfm = ConfigManager.getManager();
	
}

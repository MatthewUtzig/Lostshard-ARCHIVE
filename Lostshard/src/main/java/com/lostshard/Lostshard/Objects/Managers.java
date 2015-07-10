package com.lostshard.Lostshard.Objects;

import com.lostshard.Lostshard.Manager.ChestRefillManager;
import com.lostshard.Lostshard.Manager.ClanManager;
import com.lostshard.Lostshard.Manager.ConfigManager;
import com.lostshard.Lostshard.Manager.NPCManager;
import com.lostshard.Lostshard.Manager.PartyManager;
import com.lostshard.Lostshard.Manager.PlayerManager;
import com.lostshard.Lostshard.Manager.PlotManager;
import com.lostshard.Lostshard.Manager.SpellManager;
import com.lostshard.Lostshard.Manager.StoreManager;
import com.lostshard.Lostshard.Manager.TaskManager;

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

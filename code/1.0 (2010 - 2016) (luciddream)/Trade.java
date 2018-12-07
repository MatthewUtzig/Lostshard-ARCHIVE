package com.lostshard.RPG;

import org.bukkit.inventory.ItemStack;

public class Trade {
	private String _offerName;
	private String _targetName;
	private ItemStack _itemStack;
	private int _amount;
	private int _ticksRemaining = 600;
	private boolean _isDead = false;
	private int _uniqueKey;
	private int _itemSlot;
	
	public Trade(String offerName, String targetName, ItemStack itemStack, int amount, int uniqueKey, int itemSlot) {
		_offerName = offerName;
		_targetName = targetName;
		_itemStack = itemStack;
		_amount = amount;
		_uniqueKey = uniqueKey;
		_itemSlot = itemSlot;
	}
	
	public void tick() {
		if(!_isDead) {
			_ticksRemaining--;
			if(_ticksRemaining <= 0)
				_isDead = true;
		}
	}
	
	public String getOffererName() {
		return _offerName;
	}
	
	public String getTargetName() {
		return _targetName;
	}
	
	public ItemStack getItemStack() {
		return _itemStack;
	}
	
	public int getAmount() {
		return _amount;
	}
	
	public boolean isDead() {
		return _isDead;
	}
	
	public int getUniqueKey() {
		return _uniqueKey;
	}
	
	public int getItemSlot() {
		return _itemSlot;
	}
}

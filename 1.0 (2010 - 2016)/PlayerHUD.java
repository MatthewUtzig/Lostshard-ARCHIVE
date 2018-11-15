package com.lostshard.RPG;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.gui.GenericButton;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.InGameHUD;
import org.getspout.spoutapi.gui.WidgetAnchor;
import org.getspout.spoutapi.player.SpoutPlayer;

import com.lostshard.RPG.Plots.Plot;
import com.lostshard.RPG.Utils.Utils;

public class PlayerHUD {
	private boolean _initialized = false;
	private SpoutPlayer _spoutPlayer;
	private PseudoPlayer _pseudoPlayer;
	private InGameHUD _inGameHud;
	private GenericLabel _manaDisplay;
	
	public PlayerHUD(PseudoPlayer pseudoPlayer, Player player) {
		_spoutPlayer = SpoutManager.getPlayer(player);
		_pseudoPlayer = pseudoPlayer;
		_inGameHud = _spoutPlayer.getMainScreen();
	}
	
	public void init() {
		int curMana = _pseudoPlayer.getMana();
		int curStamina = _pseudoPlayer.getStamina();
		_manaDisplay = new GenericLabel("Mana: "+curMana+" - Stamina: "+curStamina);
		/*GenericItemWidget itemWidget = new GenericItemWidget(new ItemStack(288));
		itemWidget.setHeight(7).setWidth(7).setDepth(7);
		itemWidget.setAnchor(WidgetAnchor.BOTTOM_CENTER);
		itemWidget.setX(90);
		itemWidget.set
		itemWidget.setY(-20);*/
		
		_inGameHud.attachWidget(Utils.getPlugin(), _manaDisplay);
		_manaDisplay.setAnchor(WidgetAnchor.BOTTOM_CENTER);
		_manaDisplay.setAlign(WidgetAnchor.CENTER_CENTER);
		_manaDisplay.setY(-53);
		_initialized = true;
		
		//_inGameHud.attachWidget(Utils.getPlugin(), itemWidget);
	}
	
	public boolean isInitialized() {
		return _initialized;
	}
	
	public void updateMana() {
		int curMana = _pseudoPlayer.getMana();
		int curStamina = _pseudoPlayer.getStamina();
		_manaDisplay.setDirty(true);
		_manaDisplay.setText("Mana: "+curMana+"/"+_pseudoPlayer.getMaxMana()+"  - Stamina: "+curStamina+"/"+_pseudoPlayer.getMaxStamina());
	}
	
	/*public void popUpPlotInfo(Plot plot) {
		Plugin plugin = Utils.getPlugin();
		PopupScreen popUp = new GenericPopup();
		GenericLabel glTitle = new GenericLabel(" - "+plot.getName()+"'s Plot Info - ");
		glTitle.setAnchor(WidgetAnchor.TOP_CENTER);
		glTitle.setAlign(WidgetAnchor.TOP_CENTER);
		glTitle.setY(10);
		popUp.attachWidget(plugin, glTitle);
		
		if(plot.isProtected()) {
			GenericLabel glProtection = new GenericLabel("Protection: On");
			glProtection.setAnchor(WidgetAnchor.TOP_CENTER);
			glProtection.setAlign(WidgetAnchor.TOP_CENTER);
			glProtection.setY(40);
			popUp.attachWidget(plugin, glProtection);
		}
		else {
			GenericLabel glProtection = new GenericLabel("Protection: Off");
			glProtection.setAnchor(WidgetAnchor.TOP_CENTER);
			glProtection.setAlign(WidgetAnchor.TOP_CENTER);
			glProtection.setY(40);
			popUp.attachWidget(plugin, glProtection);
		}
		
		GenericButton gbProtectionOn = new GenericButton("Turn Protection On");
		gbProtectionOn.setAnchor(WidgetAnchor.TOP_CENTER);
		gbProtectionOn.setAlign(WidgetAnchor.CENTER_CENTER);
		gbProtectionOn.setX(-120);
		gbProtectionOn.setY(55);
		gbProtectionOn.setWidth(110).setHeight(20);
		popUp.attachWidget(plugin, gbProtectionOn);
		
		GenericButton gbProtectionOff = new GenericButton("Turn Protection Off");
		gbProtectionOff.setAnchor(WidgetAnchor.TOP_CENTER);
		gbProtectionOff.setAlign(WidgetAnchor.CENTER_CENTER);
		gbProtectionOff.setX(10);
		gbProtectionOff.setY(55);
		gbProtectionOff.setWidth(110).setHeight(20);
		popUp.attachWidget(plugin, gbProtectionOff);
		
		_spoutPlayer.getMainScreen().attachPopupScreen(popUp);
	}*/
}

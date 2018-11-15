package com.lostshard.RPG.Spout.HUD;

import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.gui.Button;
import org.getspout.spoutapi.gui.GenericButton;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.WidgetAnchor;
import org.getspout.spoutapi.player.SpoutPlayer;

import com.lostshard.RPG.External.Database;
import com.lostshard.RPG.Plots.Plot;
import com.lostshard.RPG.Plots.PlotHandler;
import com.lostshard.RPG.Utils.Utils;

public class PopupMainMenu extends GenericPopup {
	GenericButton _gbProtectionOn;
	GenericButton _gbProtectionOff;
	GenericLabel _glProtection;
	GenericLabel _glStatus;
	GenericButton _gbStatusPrivate;
	GenericButton _gbStatusPublic;
	GenericLabel _glExplosion;
	GenericButton _gbExplosionYes;
	GenericButton _gbExplosionNo;
	
	public PopupMainMenu(Plot plot) {
		super();
		
		Plugin plugin = Utils.getPlugin();
		
		
		GenericLabel glTitle = new GenericLabel(" - "+plot.getName()+"'s Plot Info - ");
		glTitle.setAnchor(WidgetAnchor.TOP_CENTER);
		glTitle.setAlign(WidgetAnchor.TOP_CENTER);
		glTitle.setY(10);
		this.attachWidget(plugin, glTitle);
		
		if(plot.isProtected()) {
			_glProtection = new GenericLabel("Protection: On");
			_glProtection.setAnchor(WidgetAnchor.TOP_CENTER);
			_glProtection.setAlign(WidgetAnchor.TOP_CENTER);
			_glProtection.setY(40);
			this.attachWidget(plugin, _glProtection);
		}
		else {
			_glProtection = new GenericLabel("Protection: Off");
			_glProtection.setAnchor(WidgetAnchor.TOP_CENTER);
			_glProtection.setAlign(WidgetAnchor.TOP_CENTER);
			_glProtection.setY(40);
			this.attachWidget(plugin, _glProtection);
		}
		
		_gbProtectionOn = new GenericButton("Turn Protection On");
		_gbProtectionOn.setAnchor(WidgetAnchor.TOP_CENTER);
		_gbProtectionOn.setAlign(WidgetAnchor.CENTER_CENTER);
		_gbProtectionOn.setX(-120);
		_gbProtectionOn.setY(50);
		_gbProtectionOn.setWidth(110).setHeight(20);
		this.attachWidget(plugin, _gbProtectionOn);
		
		_gbProtectionOff = new GenericButton("Turn Protection Off");
		_gbProtectionOff.setAnchor(WidgetAnchor.TOP_CENTER);
		_gbProtectionOff.setAlign(WidgetAnchor.CENTER_CENTER);
		_gbProtectionOff.setX(10);
		_gbProtectionOff.setY(50);
		_gbProtectionOff.setWidth(110).setHeight(20);
		this.attachWidget(plugin, _gbProtectionOff);
		
		if(plot.isLocked()) {
			_glStatus = new GenericLabel("Status: Private");
			_glStatus.setAnchor(WidgetAnchor.TOP_CENTER);
			_glStatus.setAlign(WidgetAnchor.TOP_CENTER);
			_glStatus.setY(80);
			this.attachWidget(plugin, _glStatus);
		}
		else {
			_glStatus = new GenericLabel("Status: Public");
			_glStatus.setAnchor(WidgetAnchor.TOP_CENTER);
			_glProtection.setAlign(WidgetAnchor.TOP_CENTER);
			_glProtection.setY(80);
			this.attachWidget(plugin, _glStatus);
		}
		
		_gbStatusPrivate = new GenericButton("Set Private");
		_gbStatusPrivate.setAnchor(WidgetAnchor.TOP_CENTER);
		_gbStatusPrivate.setAlign(WidgetAnchor.CENTER_CENTER);
		_gbStatusPrivate.setX(-120);
		_gbStatusPrivate.setY(90);
		_gbStatusPrivate.setWidth(110).setHeight(20);
		this.attachWidget(plugin, _gbStatusPrivate);
		
		_gbStatusPublic = new GenericButton("Set Public");
		_gbStatusPublic.setAnchor(WidgetAnchor.TOP_CENTER);
		_gbStatusPublic.setAlign(WidgetAnchor.CENTER_CENTER);
		_gbStatusPublic.setX(120);
		_gbStatusPublic.setY(90);
		_gbStatusPublic.setWidth(110).setHeight(20);
		this.attachWidget(plugin, _gbStatusPublic);
		
		if(plot.isExplosionAllowed()) {
			_glExplosion = new GenericLabel("Allow Owner/Co-Owner Explosions: Allow");
			_glExplosion.setAnchor(WidgetAnchor.TOP_CENTER);
			_glExplosion.setAlign(WidgetAnchor.TOP_CENTER);
			_glExplosion.setY(120);
			this.attachWidget(plugin, _glExplosion);
		}
		else {
			_glExplosion = new GenericLabel("Allow Owner/Co-Owner Explosions: Don't Allow");
			_glExplosion.setAnchor(WidgetAnchor.TOP_CENTER);
			_glExplosion.setAlign(WidgetAnchor.TOP_CENTER);
			_glExplosion.setY(120);
			this.attachWidget(plugin, _glExplosion);
		}
		
		_gbExplosionYes = new GenericButton("Allow");
		_gbExplosionYes.setAnchor(WidgetAnchor.TOP_CENTER);
		_gbExplosionYes.setAlign(WidgetAnchor.CENTER_CENTER);
		_gbExplosionYes.setX(-120);
		_gbExplosionYes.setY(130);
		_gbExplosionYes.setWidth(110).setHeight(20);
		this.attachWidget(plugin, _gbExplosionYes);
		
		_gbExplosionNo = new GenericButton("Don't Allow");
		_gbExplosionNo.setAnchor(WidgetAnchor.TOP_CENTER);
		_gbExplosionNo.setAlign(WidgetAnchor.CENTER_CENTER);
		_gbExplosionNo.setX(120);
		_gbExplosionNo.setY(130);
		_gbExplosionNo.setWidth(110).setHeight(20);
		this.attachWidget(plugin, _gbExplosionNo);
	}
	
	public void clickedButton(SpoutPlayer spoutPlayer, Button button) {
		System.out.println("Clicked Butan");
		if(button.equals(_gbProtectionOn)) {
			Plot plot = PlotHandler.findPlotAt(spoutPlayer.getLocation());
			if(plot != null) {
				if(plot.isOwner(spoutPlayer.getName()) || plot.isCoOwner(spoutPlayer.getName())) {
					plot.setProtect(true);
					Database.updatePlot(plot);
					_glProtection.setText("Protection: On");
					_glProtection.setDirty(true);
				}
			}
		}
		else if(button.equals(_gbProtectionOff)) {
			Plot plot = PlotHandler.findPlotAt(spoutPlayer.getLocation());
			if(plot != null) {
				if(plot.isOwner(spoutPlayer.getName()) || plot.isCoOwner(spoutPlayer.getName())) {
					plot.setProtect(false);
					Database.updatePlot(plot);
					_glProtection.setText("Protection: Off");
					_glProtection.setDirty(true);
				}
			}
		}
	}
}
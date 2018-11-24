package com.lostshard.RPG.Spout.HUD;

import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.gui.Button;
import org.getspout.spoutapi.gui.GenericButton;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.WidgetAnchor;
import org.getspout.spoutapi.player.SpoutPlayer;

import com.lostshard.RPG.Utils.Utils;

public class PopupSpellbook extends GenericPopup {
	private GenericButton _gbFirstCircle;
	private GenericButton _gbSecondCircle;
	private GenericButton _gbThirdCircle;
	private GenericButton _gbFourthCircle;
	
	public PopupSpellbook() {
		super();
		
		/*Plugin plugin = Utils.getPlugin();
		GenericLabel glTitle = new GenericLabel(" - "+this.getPlayer().getName()+"'s Spellbook - ");
		glTitle.setAnchor(WidgetAnchor.TOP_CENTER);
		glTitle.setAlign(WidgetAnchor.TOP_CENTER);
		glTitle.setY(10);
		this.attachWidget(plugin, glTitle);
		
		_gbFirstCircle = new GenericButton("First Circle");
		_gbFirstCircle.setAnchor(WidgetAnchor.TOP_CENTER);
		_gbFirstCircle.setAlign(WidgetAnchor.CENTER_CENTER);
		_gbFirstCircle.setX(-120);
		_gbFirstCircle.setY(90);
		_gbFirstCircle.setWidth(110).setHeight(20);
		this.attachWidget(plugin, _gbFirstCircle);
		
		_gbSecondCircle = new GenericButton("Second Circle");
		_gbSecondCircle.setAnchor(WidgetAnchor.TOP_CENTER);
		_gbSecondCircle.setAlign(WidgetAnchor.CENTER_CENTER);
		_gbSecondCircle.setX(-120);
		_gbSecondCircle.setY(100);
		_gbSecondCircle.setWidth(110).setHeight(20);
		this.attachWidget(plugin, _gbSecondCircle);
		
		_gbThirdCircle = new GenericButton("Third Circle");
		_gbThirdCircle.setAnchor(WidgetAnchor.TOP_CENTER);
		_gbThirdCircle.setAlign(WidgetAnchor.CENTER_CENTER);
		_gbThirdCircle.setX(-120);
		_gbThirdCircle.setY(110);
		_gbThirdCircle.setWidth(110).setHeight(20);
		this.attachWidget(plugin, _gbThirdCircle);
		
		_gbFourthCircle = new GenericButton("Fourth Circle");
		_gbFourthCircle.setAnchor(WidgetAnchor.TOP_CENTER);
		_gbFourthCircle.setAlign(WidgetAnchor.CENTER_CENTER);
		_gbFourthCircle.setX(-120);
		_gbFourthCircle.setY(120);
		_gbFourthCircle.setWidth(110).setHeight(20);
		this.attachWidget(plugin, _gbFourthCircle);*/
	}
	
	public void clickedButton(SpoutPlayer spoutPlayer, Button button) {
		/*System.out.println("Clicked Butan");
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
		}*/
	}
}

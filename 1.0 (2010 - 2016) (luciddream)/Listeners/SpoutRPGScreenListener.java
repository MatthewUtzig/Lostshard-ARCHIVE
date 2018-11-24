package com.lostshard.RPG.Listeners;

import org.getspout.spoutapi.event.screen.ButtonClickEvent;
import org.getspout.spoutapi.event.screen.ScreenCloseEvent;
import org.getspout.spoutapi.event.screen.ScreenListener;
import org.getspout.spoutapi.event.screen.ScreenOpenEvent;
import org.getspout.spoutapi.event.screen.SliderDragEvent;
import org.getspout.spoutapi.event.screen.TextFieldChangeEvent;
import org.getspout.spoutapi.gui.Button;
import org.getspout.spoutapi.gui.Screen;
import org.getspout.spoutapi.gui.ScreenType;
import org.getspout.spoutapi.player.SpoutPlayer;

import com.lostshard.RPG.PseudoPlayer;
import com.lostshard.RPG.PseudoPlayerHandler;
import com.lostshard.RPG.Skills.Meditation;
import com.lostshard.RPG.Spout.HUD.PopupPlotInfo;

public class SpoutRPGScreenListener extends ScreenListener {

    @Override
    public void onScreenOpen(ScreenOpenEvent event) {
               
    }
   
    @Override
    public void onButtonClick(ButtonClickEvent event) {
    	Screen screen = event.getScreen();
    	SpoutPlayer spoutPlayer = event.getPlayer();
    	
    	if(screen instanceof PopupPlotInfo) {
    		PopupPlotInfo popupPlotInfo = (PopupPlotInfo)screen;
    		Button button = event.getButton();
    		popupPlotInfo.clickedButton(spoutPlayer, button);
    	}
    }
       
    @Override
    public void onSliderDrag(SliderDragEvent event) {
               
    }
       
    @Override
    public void onTextFieldChange(TextFieldChangeEvent event) {
               
    }
       
    @Override
    public void onScreenClose(ScreenCloseEvent event) {
    	if(event.getScreenType().equals(ScreenType.PLAYER_INVENTORY)) {
    		SpoutPlayer player = event.getPlayer();
    		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
    		pseudoPlayer.setMaxMana(Meditation.getMaxMana(pseudoPlayer));
    	}
    }
}

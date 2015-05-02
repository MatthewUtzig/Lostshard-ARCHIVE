package com.lostshard.lostshard.Objects.Plot;

import com.lostshard.lostshard.Utils.IntPoint;

public enum Capturepoint {

	HOST("Host", new IntPoint(0, 0, 0));
	
	public static Capturepoint getByName(String name) {
		for(Capturepoint cp : values())
			if(cp.getPlotName().equalsIgnoreCase(name))
				return cp;
		return null;
	}
	public static boolean isCapturepoint(Plot plot) {
		for(Capturepoint pc : values())
			if(pc.name().equalsIgnoreCase(plot.getName()))
				return true;
		return false;
	}
	
	private String plotName;

	private IntPoint point;
	
	private Capturepoint(String name, IntPoint point) {
		this.plotName = name;
	}
	
	public String getPlotName() {
		return plotName;
	}

	public IntPoint getPoint() {
		return point;
	}
}

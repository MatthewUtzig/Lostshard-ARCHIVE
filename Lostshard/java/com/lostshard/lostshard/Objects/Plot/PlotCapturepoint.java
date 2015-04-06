package com.lostshard.lostshard.Objects.Plot;

public enum PlotCapturepoint {

	HOST("Host");
	
	private String plotName;
	
	private PlotCapturepoint(String name) {
		this.setPlotName(name);
	}

	public String getPlotName() {
		return plotName;
	}

	public void setPlotName(String plotName) {
		this.plotName = plotName;
	}
	
	public boolean isCapturepoint(Plot plot) {
		for(PlotCapturepoint pc : values())
			if(pc.name().equalsIgnoreCase(plot.getName()))
				return true;
		return false;
	}
}

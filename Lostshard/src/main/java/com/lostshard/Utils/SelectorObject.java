package com.lostshard.Utils;

public class SelectorObject {

	private final double weight;
	private final Object object;
	
	public SelectorObject(double weight, Object object) {
		super();
		this.weight = weight;
		this.object = object;
	}

	public double getWeight() {
		return weight;
	}

	public Object getObject() {
		return object;
	}
}

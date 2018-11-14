package com.lostshard.Utils;

public class SelectorObject {

	private final int weight;
	private final Object object;

	public SelectorObject(int weight, Object object) {
		super();
		this.weight = weight;
		this.object = object;
	}

	public Object getObject() {
		return this.object;
	}

	public double getWeight() {
		return this.weight;
	}
}

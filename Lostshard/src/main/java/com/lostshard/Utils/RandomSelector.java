package com.lostshard.Utils;

import java.util.ArrayList;
import java.util.List;

public class RandomSelector {

	private final List<SelectorObject> objects;
	
	public RandomSelector() {
		this.objects = new ArrayList<SelectorObject>();
	}
	
	public boolean add(double weight, Object object) {
		return objects.add(new SelectorObject(weight, object));
	}
	
	public Object getRandomObject() {
		double total_weight = 0;
		for(SelectorObject o : objects)
			total_weight += o.getWeight();
		double weight = Math.random()*total_weight;
		double currentWeight = 0;
		for(SelectorObject o : objects) {
			if(o.getWeight()+currentWeight >= weight) {
				return o;
			}
			currentWeight += o.getWeight();
		}
		return null;
	}

	public List<SelectorObject> getObjects() {
		return objects;
	}
	
}

package com.lostshard.Utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class RandomSelector {

	private final List<SelectorObject> objects;
	
	public RandomSelector() {
		this.objects = new ArrayList<SelectorObject>();
	}
	
	public boolean add(int weight, Object object) {
		return objects.add(new SelectorObject(weight, object));
	}
	
	public double getTotalWeight() {
		double total_weight = 0;
		for(SelectorObject o : objects)
			total_weight += o.getWeight();
		return total_weight;
	}
	
	public Object getRandomObject() {
		double total_weight = getTotalWeight();
		double weight = Math.random()*total_weight;
		double currentWeight = 0;
		for(SelectorObject o : objects) {
			if(o.getWeight()+currentWeight >= weight) {
				return o.getObject();
			}
			currentWeight += o.getWeight();
		}
		return null;
	}

	public List<SelectorObject> getObjects() {
		return objects;
	}
	
	public String getAsString() {
		double tw = getTotalWeight();
		String[] strings = new String[objects.size()];
		for(int i=0; i<objects.size(); i++) {
			SelectorObject o = objects.get(i);
			strings[i] = ""+tw+"/"+o.getWeight()+"="+(tw/o.getWeight());
		}
		return StringUtils.join(strings, ", ");
	}
}

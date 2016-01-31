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
		return this.objects.add(new SelectorObject(weight, object));
	}

	public String getAsString() {
		final double tw = this.getTotalWeight();
		final String[] strings = new String[this.objects.size()];
		for (int i = 0; i < this.objects.size(); i++) {
			final SelectorObject o = this.objects.get(i);
			strings[i] = "" + tw + "/" + o.getWeight() + "=" + tw / o.getWeight();
		}
		return StringUtils.join(strings, ", ");
	}

	public List<SelectorObject> getObjects() {
		return this.objects;
	}

	public Object getRandomObject() {
		final double total_weight = this.getTotalWeight();
		final double weight = Math.random() * total_weight;
		double currentWeight = 0;
		for (final SelectorObject o : this.objects) {
			if (o.getWeight() + currentWeight >= weight) {
				return o.getObject();
			}
			currentWeight += o.getWeight();
		}
		return null;
	}

	public double getTotalWeight() {
		double total_weight = 0;
		for (final SelectorObject o : this.objects)
			total_weight += o.getWeight();
		return total_weight;
	}
}

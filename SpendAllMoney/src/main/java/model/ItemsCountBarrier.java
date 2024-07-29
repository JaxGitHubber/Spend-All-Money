package model; 

import java.util.ArrayList;
import java.util.List;

public class ItemsCountBarrier {
	private List<Item> itemsWithBarrier;
	
	@SuppressWarnings("serial")
	public ItemsCountBarrier() {
		itemsWithBarrier = new ArrayList<Item>() {{
			add(new Item("bugatti", 300));
			add(new Item("Bugatti Centodieci", 300));
			add(new Item("Mercedes Benz 300 SLR Uhlenhaut", 1));
			add(new Item("Ferrari 250 GTO", 1));
			add(new Item("Capitals Tower - Park Tower - flat", 100));
			add(new Item("Aaron Spellings Manor", 1));
			add(new Item("Villa Leopolda", 1));
			add(new Item("V-22 Osprey", 300));
			add(new Item("Grumman E-2D Advanced Hawkeye", 150));
			add(new Item("Arleigh Burke DDG-51 Destroyer", 5));
			add(new Item("Charles de Gaulle Aircraft Carrier", 5));
			add(new Item("HMS Astute", 5));
		}};
	}
	
	public int getCountBarrier(String name) {
		for(Item item : itemsWithBarrier) {
			if(item.getName().equalsIgnoreCase(name)) {
				return item.getCount();
			}
		}
		return -1;
	}
	
	public List<Item> getItems() {
		return itemsWithBarrier;
	}
}

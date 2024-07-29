package model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class ItemsStorage {
	private List<Item> storage = new ArrayList<>();
 	
	public int getCount(String name) {
		if(!storage.isEmpty()) {
			for(Item storageItem : storage) {
				if(storageItem.getName().equals(name)) {
					return storageItem.getCount();
				}
			}
		}
		return 0;
	}
	
	public void incrementItem(Item item) {
		boolean isFound = false;
		for(Item storageItem : storage) {
			if(storageItem.getName().equals(item.getName())) {
				storageItem.incrementCount();
				isFound = true;
				break;
			}
		}
		if(!isFound) {
			Item storageItem = new Item();
			storageItem.setName(item.getName());
			storageItem.setCount(1);
			storage.add(storageItem);
		}
	}
	
	public void decrementItem(Item item) {
		Item foundItem = null;
		for(Item storageItem : storage) {
			if(storageItem.getName().equals(item.getName())) {
				foundItem = storageItem;
				break;
			}
		}
		if(foundItem != null) {
			if(foundItem.getCount() == 1) {
				storage.remove(foundItem);
			} else {
				foundItem.decrementCount();
			}
		}
	}
	
	public void deleteItem(String name) {
		for(Item storageItem : storage) {
			if(storageItem.getName().equals(name)) {
				storage.remove(storageItem);
				break;
			}
		}
	}
	
	public void clear() {
		storage = new ArrayList<>();
	}
}

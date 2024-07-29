package model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class ShoppingCart {
	private List<Item> items = new ArrayList<>();
	
	public List<Item> getItems() {
		return items;
	}
	
	public void clear() {
		items = new ArrayList<>();
	}
	
	public String getTotalPrice() {
		long totalPriceNumber = 0;
		for(Item item : items) {
			totalPriceNumber += PriceParser.parseToLong(item.getPrice());
		}
	    return PriceParser.parseToPrice(totalPriceNumber);
	}
	
	public int getTotalCount() {
		int totalCount = 0;
		for(Item item : items) {
			totalCount += item.getCount();
		}
		return totalCount;
	}
	
	public void addItem(Item item) {
		Item findenItem = findItem(item.getName());
		if(findenItem == null) {
			items.add(item);
		} else {
			findenItem.incrementCount();
		    findenItem.setPrice(PriceParser.parseToPrice(increasePriceInNumber(findenItem, item)));
		}
	}
	
	public void removeItem(Item item) {
		Item findenItem = findItem(item.getName());
		if(findenItem.getCount() == 1) {
			items.remove(findenItem);
		} else {
			findenItem.decrementCount();
		    findenItem.setPrice(PriceParser.parseToPrice(decreasePriceInNumber(findenItem, item)));
		}
	}
	
	public void removeItem(String name) {
		Item findenItem = findItem(name);
		if(findenItem != null) {
			items.remove(findenItem);
		}
	}
	
	public void updateCountBarriers(ItemsCountBarrier itemsCountBarrier) {
		for(Item itemWithBarrier : itemsCountBarrier.getItems()) {
			for(Item shoppingCartItem : items) {
				if(shoppingCartItem.getName().equalsIgnoreCase(itemWithBarrier.getName())) {
					itemWithBarrier.setCount(itemWithBarrier.getCount() - shoppingCartItem.getCount());
					break;
				}
			}
		}	
	}
	
	private Item findItem(String name) {
		Item findenItem = null;
		for(Item secondItem : items) {
			if(secondItem.getName().equals(name)) {
				findenItem = secondItem;
				break;
			}
		}
		return findenItem;
	}
	
	private Long increasePriceInNumber(Item findenItem, Item item) {
		return PriceParser.parseToLong(findenItem.getPrice()) + PriceParser.parseToLong(item.getPrice());
	}
	
	private Long decreasePriceInNumber(Item findenItem, Item item) {
		return PriceParser.parseToLong(findenItem.getPrice()) - PriceParser.parseToLong(item.getPrice());
	}
}

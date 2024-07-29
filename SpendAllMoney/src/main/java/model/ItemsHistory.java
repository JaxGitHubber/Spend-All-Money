package model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class ItemsHistory {
	List<Item> items = new ArrayList<Item>();
	
	public List<Item> getItems() {
		return items;
	}
	
	public void clear() {
		items = new ArrayList<Item>();
	}
	
	public void addItems(List<Item> items) {
		for(Item item : items) {
			Item finddenItem = null;
			for(Item historyItem : this.items) {
				if(item.getName().equals(historyItem.getName())) {
					finddenItem = historyItem;
				}
			}
			if(finddenItem != null) {
				finddenItem.setPrice(PriceParser.parseToPrice(increasePriceInNumber(finddenItem, item)));
				finddenItem.setCount(finddenItem.getCount() + item.getCount());
			} else {
				if(this.items.size() > 30) {
					this.items.remove(0);
				}
				this.items.add(item);
			}
		}
	}
	
	private Long increasePriceInNumber(Item findenItem, Item item) {
		return PriceParser.parseToLong(findenItem.getPrice()) + PriceParser.parseToLong(item.getPrice());
	}
}

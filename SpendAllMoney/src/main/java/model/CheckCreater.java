package model;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@Scope("singleton")
public class CheckCreater {
	private int totalCount = 0;
	private String totalPrice = "0$";
	
	public void calculateTotalCount(List<Item> shoppingCart) {
		int totalCount = 0;
		for(Item item : shoppingCart) {
			totalCount += item.getCount();
		}
		this.totalCount = totalCount;
	}
	
	public void calculateTotalPrice(List<Item> shoppingCart) {
		int totalPriceNumber = 0;
		for(Item item : shoppingCart) {
			totalPriceNumber += Integer.parseInt(item.getPrice().replace("$", "").replace(".", ""));
		}
		String totalPrice = Integer.toString(totalPriceNumber);
		String priceWithpoints = "";
	    int step = 1;
	      for(int i = totalPrice.length()-1; i >= 0; i--) {
	        if(step == 3 && i-1 != -1) {
	            step = 1;
	            priceWithpoints = totalPrice.charAt(i) + priceWithpoints;
	            priceWithpoints = "." + priceWithpoints;
	        } else {
	        	priceWithpoints = totalPrice.charAt(i) + priceWithpoints;
	            step++;
	        }
	      }
	      this.totalPrice = priceWithpoints + "$";
	}
}

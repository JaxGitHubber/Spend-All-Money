package model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonAutoDetect
public class Item {
	@JsonIgnore
	private int count = 1;
	
	private String name;
	
	private String image;
	
	private String price;
	
	private Boolean isFavorite;
	
	public Item(String name, String image, String price, Boolean isFavorite) {
		this.name = name;
		this.image = image;
		this.price = price;
		this.isFavorite = isFavorite;
	}
	
	public Item(String name, int count) {
		this.name = name;
		this.count = count;
	}
	
	public void incrementCount() {
		count++;
	}
	
	public void decrementCount() {
		count--;
	}
	
	public String getCountToString() {
		return "count. " + count;
	}
	
	public String getItemIdToString() {
		return image.replaceAll("resources/images/", "").replaceAll(".png", "");
	}
}

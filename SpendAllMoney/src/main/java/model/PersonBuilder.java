package model;

import org.springframework.context.ApplicationContext;

import lombok.Getter;

@Getter
public class PersonBuilder {
	private String fullName;
	private String image;
	private String state;
	private String startLabel;
	private String bio;
	
	public PersonBuilder fullName(String fullName) {
		this.fullName = fullName;
		return this;
	}
	
	public PersonBuilder image(String image) {
		this.image = "resources/images/" + image + ".png";
		return this;
	}
	
	public PersonBuilder state(String state) {
		this.state = state;
		return this;
	}
	
	public PersonBuilder startLabel(String startLabel) {
		this.startLabel = startLabel;
		return this;
	}
	
	public PersonBuilder bio(String bio) {
		this.bio = bio;
		return this;
	}
	
	public Person build(ApplicationContext context) {
		return new Person(this, context);
	}
}

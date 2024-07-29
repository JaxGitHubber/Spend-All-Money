package model;

import org.springframework.context.ApplicationContext;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="person")
public class Person {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	@Column(name="full_name")
	private String fullName;
	@Transient
	private String image;
	@Column(name="state")
	private String state;
	@Transient
	private String startLabel;
	@Transient
	private String bio;
	@Column(name="spent_money")
	private String spentMoney = "$0";
	@Transient
	private String fixedState;
	@Transient
	private ItemsCountBarrier itemsCountBarrier;
	@Transient
	private ItemsStorage itemsStorage;
	@Transient
	private ShoppingCart shoppingCart;
	@ManyToOne(optional=true, cascade=CascadeType.ALL)
	@JoinColumn(name="account_id")
	private Account account;
	
	public Person(final PersonBuilder personBuilder, final ApplicationContext context) {
		this.fullName = personBuilder.getFullName();
		this.image = personBuilder.getImage();
		this.state = personBuilder.getState();
		this.startLabel = personBuilder.getStartLabel();
		this.bio = personBuilder.getBio();
		this.fixedState = state;
		this.itemsCountBarrier = new ItemsCountBarrier();
		this.itemsStorage = context.getBean(ItemsStorage.class);
		this.shoppingCart = context.getBean(ShoppingCart.class);
	}
}

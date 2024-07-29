package model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonAutoDetect
@Entity
@Table(name="leaderboard")
public class LeaderBoard {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	@JsonIgnore
	@Transient
	private Long topNumber = 0L;
	@Column(name="total_spent_money")
	private Long totalSpentMoney;
	@JsonIgnore
	@Transient
	private String parsedTotalSpentMoney;
	@OneToOne(mappedBy="leaderboard")
	private Account account;
	
	public LeaderBoard(final Long totalSpentMoney) {
		this.totalSpentMoney = totalSpentMoney;
	}
	
	@JsonIgnore
	public String getTopNumberByPattern() {
		String parsedTopNumber = "";
		if(this.topNumber < 10) {
			parsedTopNumber += "0" + topNumber;
		} else {
			parsedTopNumber += topNumber;
		}
		return parsedTopNumber + ".";
	}
}

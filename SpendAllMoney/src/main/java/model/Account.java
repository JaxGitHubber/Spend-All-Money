package model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
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
@Table(name="account")
public class Account {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	@Column(name="login", nullable=false, length=30, unique=true)
	private String login;
	@JsonIgnore
	@Column(name="password", nullable=false)
	private String password;
	@JsonIgnore
	@Transient
	private String repeatedPassword;
	@JsonIgnore
	@OneToMany(mappedBy="account", fetch=FetchType.EAGER)
	private Set<Person> people = new HashSet<>();
	@JsonIgnore
	@OneToOne(optional=true, cascade=CascadeType.ALL)
	@JoinColumn(name="leaderboard_id")
	private LeaderBoard leaderboard = new LeaderBoard(0L);
}

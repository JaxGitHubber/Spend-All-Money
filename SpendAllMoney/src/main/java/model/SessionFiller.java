package model;

import java.util.List;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import dao.DAOFactory;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.Setter;

@Setter
@Component
@Scope(value="singleton")
public class SessionFiller {
	private ApplicationContext applicationContext;
	
	public void fillAccountSession(String login, HttpSession session) {
		session.setAttribute("status", "SignIn");
		People people = getPeople(session);
		loadPeople(login, people);
		session.setAttribute("people", people);
		baseFilling(session, people.getPerson("PAVEL DUROV"));
	}
	
	public void fillStartSession(HttpSession session) {
		session.setAttribute("status", "SignOut");
		People people = applicationContext.getBean(People.class);
		session.setAttribute("people", people);
		baseFilling(session, people.getPerson("PAVEL DUROV"));
	}
	
	public void loadPeople(String login, People people) {
		Account account = getAccount(login);
		if(account != null) {
			if(account.getPeople().size() != 0) {
				changePeopleOnSaveAccountPeople(account.getPeople(), people.getPeopleList());
			} else {
				savePeopleOnAccount(account.getId(), people);
			}
		}
	}
	
	private People getPeople(HttpSession session) {
		People people = (People) session.getAttribute("people");
		if(people == null) {
			people = applicationContext.getBean(People.class);
		}
		return people;
	}
	
	@Transactional
	private Account getAccount(String login) {
		return DAOFactory.getAccountDAO().getAccount(login);
	}
	
	private void changePeopleOnSaveAccountPeople(Set<Person> savePeople, List<Person> people) {
		for(Person savePerson : savePeople) {
			for(Person person : people) {
				if(person.getFullName().equalsIgnoreCase(savePerson.getFullName())) {
					person.setState(savePerson.getState());
					person.setSpentMoney(savePerson.getSpentMoney());
				}
			}
		}
	}
	
	private void savePeopleOnAccount(Long accountID, People people) {
		DAOFactory.getPersonDAO().addPeople(accountID, people);
	}
	
	private void baseFilling(HttpSession session, Person person) {
		session.setAttribute("shoppingCart", person.getShoppingCart());
		session.setAttribute("storage", person.getItemsStorage());
		session.setAttribute("history", applicationContext.getBean(ItemsHistory.class));
		session.setAttribute("countBarrier", person.getItemsCountBarrier());
		session.setAttribute("person", person);
	}
}

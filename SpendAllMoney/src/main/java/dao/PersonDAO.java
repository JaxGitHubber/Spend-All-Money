package dao;

import jakarta.annotation.PreDestroy;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import model.Account;
import model.People;
import model.Person;

public class PersonDAO {
	private EntityManagerFactory emf;
	private EntityManager em;
	
	{
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		emf = Persistence.createEntityManagerFactory("spend-all-money-jpa");
		em = emf.createEntityManager();
	}
	
	public void addPeople(Long accountID, People people) {
		em.getTransaction().begin();
		for(Person person : people.getPeopleList()) {
			Query query = em.createQuery("INSERT INTO Person(fullName, state, spentMoney, account) VALUES(:fullName, :state, :spentMoney, :accountID)");
			query.setParameter("fullName", person.getFullName());
			query.setParameter("state", person.getState());
			query.setParameter("spentMoney", person.getSpentMoney());
			query.setParameter("accountID", accountID);
			query.executeUpdate();
		}
		em.getTransaction().commit();
	}
	
	public void updatePerson(Account account, Person person) {
		em.getTransaction().begin();
		Query query = em.createQuery("UPDATE Person p SET p.state = :state, p.spentMoney = :spentMoney WHERE p.account = :account AND p.fullName = :fullName");
		query.setParameter("state", person.getState());
		query.setParameter("spentMoney", person.getSpentMoney());
		query.setParameter("account", account);
		query.setParameter("fullName", person.getFullName());
		query.executeUpdate();
		em.getTransaction().commit();
	}
	
	@PreDestroy
	private void disconnect() {
		emf.close();
		em.close();
	}
}

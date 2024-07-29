package dao;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

import jakarta.annotation.PreDestroy;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import model.Account;

@Component
public class AccountDAO {
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
	
	public void addAccount(Account account) {
		account.setPassword(BCrypt.hashpw(account.getPassword(), BCrypt.gensalt()));
		em.getTransaction().begin();
		em.persist(account);
		em.getTransaction().commit();
	}
	
	public Account getAccount(String login) {
		try {
		  return em.createQuery("SELECT a FROM Account a WHERE a.login = :login", Account.class).setParameter("login", login).getSingleResult();
	      } catch(Exception e) {
		    e.printStackTrace();
		    return null;
	      }
	}
	
	public String getPw(String login) {
		Query query = em.createQuery("SELECT a.password FROM Account a WHERE a.login = :login");
		query.setParameter("login", login);
		try {
		  return (String) query.getSingleResult();
		} catch(Exception e) {
			return null;
		}
	}
	
	public boolean isLoginExists(String login) {
		Query query = em.createQuery("SELECT a.login FROM Account a WHERE login = :login");
		query.setParameter("login", login);
		try {
			String existsLogin = (String) query.getSingleResult();
			if(existsLogin != null) {
				return true;
			}
			return false;
		} catch(Exception e) {
			return false;
		}
	}
	
	@PreDestroy
	private void disconnect() {
		emf.close();
		em.close();
	}
}

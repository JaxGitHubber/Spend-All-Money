package dao;

import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.PreDestroy;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import model.LeaderBoard;

public class LeaderBoardDAO {
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
	
	@SuppressWarnings("unchecked")
	public List<LeaderBoard> getAllSortedTop() {
		try {
			return (List<LeaderBoard>) em.createQuery("SELECT l FROM LeaderBoard l ORDER BY l.totalSpentMoney DESC").getResultList();
		} catch(Exception e) {
			return new ArrayList<>();
		}
	}
	
	public Long getTotalSpentMoney(Long leaderBoardID) {
		return em.createQuery("SELECT l.totalSpentMoney FROM LeaderBoard l WHERE l.id = :id", Long.class).setParameter("id", leaderBoardID).getSingleResult();
	}
	
	public void updateTotalSpentMoney(Long leaderBoardID, Long totalSpentMoney) {
		em.getTransaction().begin();
		Query query = em.createQuery("UPDATE LeaderBoard l SET l.totalSpentMoney = :totalSpentMoney WHERE l.id = :id");
		query.setParameter("totalSpentMoney", totalSpentMoney);
		query.setParameter("id", leaderBoardID);
		query.executeUpdate();
		em.getTransaction().commit();
	}
	
	@PreDestroy
	private void disconnect() {
		emf.close();
		em.close();
	}
}

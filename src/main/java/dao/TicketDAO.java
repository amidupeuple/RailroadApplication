package dao;

import entity.Passenger;
import entity.Ticket;
import entity.Train;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * DAO for Ticket entity.
 */
public class TicketDAO {
    private EntityManagerFactory entityManagerFactory;

    public TicketDAO(EntityManagerFactory factory) {
        entityManagerFactory = factory;
    }

    /**
     * Method add new ticket to the corresponding table in DB
     * @param train
     * @param passenger
     */
    public void addNewTicket(Train train, Passenger passenger) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        Ticket newTicket = new Ticket(train, passenger);
        entityManager.persist(newTicket);

        entityManager.getTransaction().commit();
    }
}

package dao;

import dto.PassengerDTO;
import entity.Passenger;
import dto.OrderDTO;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class for Passenger entity.
 */
public class PassengerDAO {
    private static final Logger log = Logger.getLogger(Passenger.class);

    private EntityManagerFactory entityManagerFactory;

    public PassengerDAO(EntityManagerFactory factory) {
        entityManagerFactory = factory;
        log.debug("Instance of PassengerDAO is created.");
    }

    /**
     * Check if user whose data pass in order parameter is already registered on the appropriate train.
     * @param order - details of user's order
     * @return true - is registered, false - absent
     */
    public boolean isPassengerAlreadyRegistered(OrderDTO order) {
        log.debug("Start method isPassengerAlreadyRegistered(...)");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        List<Integer> passenger = entityManager.createQuery(
                "select p.id from Passenger p, Ticket t " +
                "where p.firstName = ?1 and p.secondName = ?2 and p.dateOfBirth = ?3 and " +
                "p.id = t.passenger.id and t.train.number = ?4"
        ).setParameter(1, order.getFirstName())
         .setParameter(2, order.getSecondName())
         .setParameter(3, order.getDateOfBirth())
         .setParameter(4, order.getTrainNumber())
         .getResultList();

        entityManager.getTransaction().commit();

        if (passenger.isEmpty()) {
            log.debug("Required passenger hasn't been found.");
            log.debug("Finish method isPassengerAlreadyRegistered(...)");
            return false;
        }
        else {
            log.debug("Required passenger found.");
            log.debug("Finish method isPassengerAlreadyRegistered(...)");
            return true;
        }
    }

    /**
     * Method add new passenger in corresponding table in DB.
     * @param order - parameters of new passenger.
     */
    public Passenger addPassenger(OrderDTO order) {
        log.debug("Start method addPassenger(...)");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        Passenger newPassenger = new Passenger(order.getFirstName(),
                                               order.getSecondName(),
                                               order.getDateOfBirth());
        entityManager.persist(newPassenger);

        entityManager.getTransaction().commit();

        log.debug("New passenger was successfully added. Method addPassenger() is finished.");
        return newPassenger;
    }

    /**
     * Get passenger with given first name, second name and date of birth.
     * @param first
     * @param second
     * @param dateOfBirth
     * @return null if no match, else Passenger instance.
     */
    public Passenger getPassenger(String first, String second, Date dateOfBirth) {
        log.debug("Start method getPassenger(...)");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        List<Passenger> buf = entityManager.createQuery(
                "select p from Passenger p " +
                "where p.firstName = ?1 and p.secondName = ?2 and p.dateOfBirth = ?3"
        ).setParameter(1, first)
         .setParameter(2, second)
         .setParameter(3, dateOfBirth)
         .getResultList();

        entityManager.getTransaction().commit();

        if (buf.isEmpty()) {
            log.debug("Required passenger hasn't been found.");
            log.debug("Finish method getPassenger(...)");
            return null;
        }
        else {
            log.debug("Required passenger was found.");
            log.debug("Finish method getPassenger(...)");
            return buf.get(0);
        }
    }

    public List<PassengerDTO> getPassengersByTrain(int trainNumber) {
        log.debug("Start: getPassengersByTrain()");
        ArrayList<PassengerDTO> passengers = new ArrayList<PassengerDTO>();

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        List<Passenger> bufPassengers = entityManager.createQuery(
                "select p from Passenger p, Ticket t, Train tr " +
                "where p.id = t.passenger.id and tr.number = ?1 and t.train.id = tr.id "
        ).setParameter(1, trainNumber).getResultList();

        entityManager.getTransaction().commit();

        for (int i = 0; i < bufPassengers.size(); i++) {
            PassengerDTO curPas = new PassengerDTO(bufPassengers.get(i).getFirstName(),
                                                   bufPassengers.get(i).getSecondName(),
                                                   bufPassengers.get(i).getDateOfBirth(),
                                                   trainNumber);
            passengers.add(curPas);
        }

        log.debug("Finish: getPassengersByTrain()");
        return passengers;
    }

}

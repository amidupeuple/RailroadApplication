package dao;

import entity.Passenger;
import entity.Station;
import org.apache.log4j.Logger;
import server.exceptions.EntityUpdateException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * DAO for Station entity
 */
public class StationDAO {
    private static final Logger log = Logger.getLogger(Passenger.class);

    private EntityManagerFactory entityManagerFactory;

    public StationDAO(EntityManagerFactory factory) {
        entityManagerFactory = factory;
    }

    public boolean addStation(String name) throws EntityUpdateException{
        log.debug("Start: addStation()");

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        Station newStation = new Station(name);
        entityManager.persist(newStation);

        try {
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            log.error("Exception station with the given name already exists");
            throw new EntityUpdateException("Ошибка! Станция с таким названием уже существует");
        }

        log.debug("Finish: addStation()");
        return true;

    }
}

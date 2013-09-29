package dao;

import entity.Passenger;
import entity.Station;
import org.apache.log4j.Logger;
import server.exceptions.AddStationException;
import server.exceptions.EntityUpdateException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: danya_000
 * Date: 9/29/13
 * Time: 1:50 AM
 * To change this template use File | Settings | File Templates.
 */
public class StationDAO {
    private static final Logger log = Logger.getLogger(Passenger.class);

    private EntityManagerFactory entityManagerFactory;

    public StationDAO(EntityManagerFactory factory) {
        entityManagerFactory = factory;
    }

    public void addStation(String name) throws AddStationException{
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        Station newStation = new Station(name);
        entityManager.persist(newStation);

        try {
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            throw new AddStationException("Ошибка! Станция с таким названием уже существует.");
        }

    }
}

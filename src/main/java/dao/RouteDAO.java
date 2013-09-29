package dao;

import entity.Passenger;
import entity.Route;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * DAO for Route entity.
 */
public class RouteDAO {
    private static final Logger log = Logger.getLogger(Passenger.class);

    private EntityManagerFactory entityManagerFactory;

    public RouteDAO(EntityManagerFactory factory) {
        entityManagerFactory = factory;
    }

    public void addRoute() {
        log.debug("Start: addRoute()");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        Route newRoute = new Route();
        entityManager.persist(newRoute);

        log.debug("Finish: addRoute()");
        entityManager.getTransaction().commit();
    }
}

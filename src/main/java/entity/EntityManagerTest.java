package entity;

import junit.framework.TestCase;

import static protocol.Constants.POINT_OF_REFERENCE;
import static protocol.Constants.HOUR;
import static protocol.Constants.MINUTE;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: danya_000
 * Date: 9/21/13
 * Time: 1:29 AM
 * To change this template use File | Settings | File Templates.
 */
public class EntityManagerTest extends TestCase {
    private EntityManagerFactory entityManagerFactory;

    public EntityManagerTest() {
        entityManagerFactory = Persistence.createEntityManagerFactory("org.hibernate.ejb.HibernatePersistence");
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    @Override
    protected void setUp() throws Exception {
        entityManagerFactory = Persistence.createEntityManagerFactory("org.hibernate.ejb.HibernatePersistence");
    }

    @Override
    protected void tearDown() throws Exception {
        entityManagerFactory.close();
    }

    /*public void testStationInRoute() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        Train train = new Train(215, 150);
        Set<StationInRoute> set = new HashSet<StationInRoute>();
        StationInRoute stationInRoute = new StationInRoute(new StationInRoutePK(3, 1),
                                                           "Бологое",
                                                           new Timestamp(POINT_OF_REFERENCE+18*HOUR+30*MINUTE),
                                                           null);
        StationInRoute stationInRoute1 = new StationInRoute(new StationInRoutePK(3, 2),
                                                            "Москва",
                                                            new Timestamp(POINT_OF_REFERENCE+21*HOUR+15*MINUTE),
                                                            new Timestamp(POINT_OF_REFERENCE+21*HOUR+0*MINUTE));
        StationInRoute stationInRoute2 = new StationInRoute(new StationInRoutePK(3, 3),
                "Ярославль",
                null,
                new Timestamp(POINT_OF_REFERENCE+22*HOUR+12*MINUTE));

        set.add(stationInRoute);
        set.add(stationInRoute1);
        set.add(stationInRoute2);

        train.setStations(set);
        stationInRoute.setTrain(train);
        stationInRoute1.setTrain(train);
        stationInRoute2.setTrain(train);

        entityManager.persist(train);
        entityManager.persist(stationInRoute);
        entityManager.persist(stationInRoute1);
        entityManager.persist(stationInRoute2);


        entityManager.getTransaction().commit();
        entityManager.close();

    }*/


    public void testBasicUsage() {
        // create a couple of events...
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        /*Train t = new Train(5, 123);
        entityManager.persist(t);*/

        Passenger p = new Passenger("Ed", "Krot", null);
        Ticket t = new Ticket();
        Train train = new Train(567, 123);

        t.setPassenger(p);
        t.setTrain(train);
        Set<Ticket> setTickets = new HashSet<Ticket>(1);
        setTickets.add(t);
        p.setTickets(setTickets);
        train.setTickets(setTickets);

        entityManager.persist(train);
        entityManager.persist(t);
        entityManager.persist(p);


        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public static void main(String[] args) {
        EntityManagerTest entity = new EntityManagerTest();
        entity.testBasicUsage();
        entity.getEntityManagerFactory().close();
    }
}

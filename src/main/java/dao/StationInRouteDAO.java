package dao;

import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.sql.Time;
import java.util.List;

/**
 * DAO for StationInRoute class.
 */
public class StationInRouteDAO {
    private static final Logger log = Logger.getLogger(StationInRouteDAO.class);

    private EntityManagerFactory entityManagerFactory;

    public StationInRouteDAO(EntityManagerFactory factory) {
        entityManagerFactory = factory;
        log.debug("Instance of StationInRouteDAO was created.");
    }

    /**
     * Get departure time of certain train from the certain station.
     * @param trainNumber
     * @param station
     * @return
     */
    public Time getDepartureTime(int trainNumber, String station) {
        log.debug("Start method getDepartureTime(...)");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        List<Time> buffer = entityManager.createQuery(
                "select s.departureTime from StationInRoute s " +
                "where s.train.number = ?1 and s.name = ?2"
        ).setParameter(1, trainNumber)
         .setParameter(2, station)
         .getResultList();

        entityManager.getTransaction().commit();

        log.debug("Finish method getDepartureTime(...)");
        return (Time) buffer.get(0);
    }

    /**
     * Get arrival time of certain train to the certain station.
     * @param trainNumber
     * @param station
     * @return
     */
    public Time getArrivalTime(int trainNumber, String station) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        List<Time> buffer = entityManager.createQuery(
                "select s.arrivalTime from StationInRoute s " +
                "where s.train.number = ?1 and s.name = ?2"
        ).setParameter(1, trainNumber)
         .setParameter(2, station)
         .getResultList();

        entityManager.getTransaction().commit();

        return (Time) buffer.get(0);
    }

    /**
     * Method gets from DB from table "station_in_route" number(s) of train(s) which pass from station A to station B
     * and departures from A in the given interval.
     * @param stationA
     * @param stationB
     * @param timeFrom - start of the given interval
     * @param timeTo - end of the given interval
     * @return array of train number(s)
     */
    public List<Integer> getTrainNumbers(String stationA, String stationB, Time timeFrom, Time timeTo) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        List<Integer> resultList = entityManager.createQuery(
                        "select s1.train.number from StationInRoute s1, StationInRoute s2 " +
                        "where s1.name = ?1 and s2.name = ?2 and s1.id.routeId = s2.id.routeId and " +
                        "s1.id.numberInRoute < s2.id.numberInRoute and s1.departureTime > ?3 and " +
                        "s1.departureTime < ?4"
        ).setParameter(1, stationA)
         .setParameter(2, stationB)
         .setParameter(3, timeFrom)
         .setParameter(4, timeTo)
         .getResultList();

        entityManager.getTransaction().commit();

        return resultList;
    }
}

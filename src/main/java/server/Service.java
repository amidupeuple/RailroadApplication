package server;

import protocol.ScheduleObject;
import protocol.RequestObject;
import protocol.ResponseObject;
import protocol.Constants;
import server.entity.StationInRoute;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * It encapsulates methods which execute all possible services provided by db for clients
 */
public class Service {
    private static long POINT_OF_REFERENCE = 75600000;
    private static long HOUR = 3600000;
    private static long MINUTE = 60000;


    // Entity manager factory is single for every particular service method.
    private static EntityManagerFactory ENTITY_MANAGER_FACTORY;

    static {
        ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("org.hibernate.ejb.HibernatePersistence");
    }

    public static void shutDownEntityManagerFactory() {
        ENTITY_MANAGER_FACTORY.close();
    }

    /**
     * Method determines which kind of service should be execute. It depends on parameter is passed in this
     * method. And after analyse, execute needed service method.
     * @param reqObj - instructions and required data to execute appropriate service, passed from client.
     * @return Data which are requested by user.
     */
    public static ResponseObject execute(RequestObject reqObj) {
        Constants.ClientService reqService = reqObj.getService();

        if (reqService == Constants.ClientService.getScheduleFromAtoB)     return scheduleFromAtoB(reqObj);
        else if (reqService == Constants.ClientService.scheduleForStation) return scheduleForStation();
        else if (reqService == Constants.ClientService.ticketPurchase)     return purchaseTicket();
        else if (reqService == Constants.ClientService.addEntity)          return addNewEntity();
        else if (reqService == Constants.ClientService.viewPassangers)     return viewPassengersOnTrain();
        else if (reqService == Constants.ClientService.viewTrains)         return viewTrains();
        else if (reqService == Constants.ClientService.saleTicket)         return saleTicket();
        else return null;
    }

    private static ResponseObject addNewEntity() {

        return new ResponseObject();
    }

    private static ResponseObject purchaseTicket() {
        return new ResponseObject();
    }

    private static ResponseObject saleTicket() {
        return new ResponseObject();
    }

    /**
     * Get trains schedule from station A to station B in the given time interval. All required data for this service
     * is encapsulated in parameter reqObj.
     * @param reqObj
     * @return Train schedule as list of ScheduleObject objects.
     */
    private static ResponseObject scheduleFromAtoB(RequestObject reqObj) {
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        entityManager.getTransaction().begin();

        ScheduleObject scheduleObj = (ScheduleObject) reqObj.getObject();
        List<StationInRoute> resultList = entityManager.createQuery(
                "select s1 from StationInRoute s1, StationInRoute s2 " +
                "where s1.name = ?1 and s2.name = ?2 and s1.id.routeId = s2.id.routeId and " +
                "s1.id.numberInRoute < s2.id.numberInRoute and s1.departureTime > ?3 and " +
                "s1.departureTime < ?4"
        ).setParameter(1, scheduleObj.getFromStation())
         .setParameter(2, scheduleObj.getToStation())
         .setParameter(3, scheduleObj.getDepartureTime())
         .setParameter(4, scheduleObj.getArrivalTime())
         .getResultList();

        // Packaging response list of objects based on data retrieved from data base.
        int size = resultList.size();
        ArrayList<ScheduleObject> responseList = new ArrayList<ScheduleObject>(size);
        for (int i = 0; i < size; i++) {
            StationInRoute station = resultList.get(i);
            responseList.add(new ScheduleObject(station.getTrain().getNumber(),
                                                scheduleObj.getFromStation(),
                                                scheduleObj.getToStation(),
                                                station.getDepartureTime(),
                                                station.getDepartureTime(),
                                                station.getTrain().getVacancies()));
        }

        return new ResponseObject(Constants.StatusOfExecutedService.success, responseList);
    }

    public static void main(String[] args) {
        scheduleFromAtoB(new RequestObject(Constants.ClientService.getScheduleFromAtoB, new ScheduleObject(123,
                                                                                                           "Псков",
                                                                                                           "Москва",
                                                                                                           new Timestamp(POINT_OF_REFERENCE),
                                                                                                           new Timestamp(POINT_OF_REFERENCE + 23*HOUR),
                                                                                                           100)));
    }

    private static ResponseObject scheduleForStation() {
        return new ResponseObject();
    }

    private static ResponseObject viewPassengersOnTrain() {
        return new ResponseObject();
    }

    private static ResponseObject viewTrains() {
        return new ResponseObject();
    }
}

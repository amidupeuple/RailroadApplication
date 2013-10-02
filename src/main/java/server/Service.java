package server;

import dao.*;
import dto.RequestDTO;
import dto.ResponseDTO;
import org.apache.log4j.Logger;
import common.*;
import server.exceptions.BookingTicketException;
import server.services.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Access point to various services
 */
public class Service {
    private static final Logger log = Logger.getLogger(Service.class);

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
    public static ResponseDTO execute(RequestDTO reqObj)  {
        log.debug("Start: execute()");

        PassengerDAO passengerDAO = new PassengerDAO(ENTITY_MANAGER_FACTORY);
        StationInRouteDAO sirDAO = new StationInRouteDAO(ENTITY_MANAGER_FACTORY);
        StationDAO stationDAO = new StationDAO(ENTITY_MANAGER_FACTORY);
        TicketDAO ticketDAO = new TicketDAO(ENTITY_MANAGER_FACTORY);
        TrainDAO trainDAO = new TrainDAO(ENTITY_MANAGER_FACTORY);

        //Determines what kind of server was required.
        Constants.ClientService reqService = reqObj.getService();

        if (reqService == Constants.ClientService.getScheduleFromAtoB) {
            log.debug("Start: getScheduleFromAtoB");
            return StationInRouteService.scheduleFromAtoB(reqObj, sirDAO);
        }
        else if (reqService == Constants.ClientService.scheduleForStation) {
            log.debug("Start: scheduleForStation");
            return StationInRouteService.scheduleForStation(reqObj, sirDAO);
        }
        else if (reqService == Constants.ClientService.buyTicket) {
            log.debug("Start: buyTicket");
            return TicketService.bookTicket(reqObj, passengerDAO, ticketDAO, trainDAO, sirDAO);
        }
        else if (reqService == Constants.ClientService.addTrain) {
            log.debug("Start: buyTicket");
            return TrainService.addTrain(reqObj, trainDAO);
        }
        else if (reqService == Constants.ClientService.addStation) {
            log.debug("Start: addStation");
            return StationService.addStation(reqObj, stationDAO);
        }
        else if (reqService == Constants.ClientService.addRoute) {
            log.debug("Start: addRoute");
            return StationInRouteService.addRoute(reqObj, sirDAO);
        }
        else if (reqService == Constants.ClientService.viewPassangers) {
            log.debug("Start: viewPassangers");
            return PassengerService.showPassengers(reqObj, passengerDAO);
        }
        else if (reqService == Constants.ClientService.viewTrains) {
            log.debug("Start: viewTrains");
            return TrainService.viewAllTrains(trainDAO);
        }
        else return null;
    }

}

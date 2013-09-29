package server;

import dto.RequestDTO;
import dto.ResponseDTO;
import org.apache.log4j.Logger;
import protocol.*;
import server.exceptions.BookingTicketException;
import server.services.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * It encapsulates methods which execute all possible services provided by db for clients
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
    public static ResponseDTO execute(RequestDTO reqObj) throws BookingTicketException {
        TicketService ticketService = new TicketService(ENTITY_MANAGER_FACTORY);
        StationInRouteService sirService = new StationInRouteService(ENTITY_MANAGER_FACTORY);
        TrainService trainService = new TrainService(ENTITY_MANAGER_FACTORY);
        StationService stationService = new StationService(ENTITY_MANAGER_FACTORY);
        PassengerService pasService = new PassengerService(ENTITY_MANAGER_FACTORY);


        //Determines what kind of server was required.
        Constants.ClientService reqService = reqObj.getService();

        if (reqService == Constants.ClientService.getScheduleFromAtoB)     return sirService.scheduleFromAtoB(reqObj);
        else if (reqService == Constants.ClientService.scheduleForStation) return sirService.scheduleForStation(reqObj);
        else if (reqService == Constants.ClientService.buyTicket)          return ticketService.bookTicket(reqObj);
        else if (reqService == Constants.ClientService.addTrain)           return trainService.addTrain(reqObj);
        else if (reqService == Constants.ClientService.addStation)         return stationService.addStation(reqObj);
        else if (reqService == Constants.ClientService.addRoute)           return sirService.addRoute(reqObj);
        else if (reqService == Constants.ClientService.viewPassangers)     return pasService.showPassengers(reqObj);
        else if (reqService == Constants.ClientService.viewTrains)         return trainService.viewAllTrains();
        else if (reqService == Constants.ClientService.saleTicket)         return saleTicket();
        else return null;
    }




    private static ResponseDTO saleTicket() {
        return new ResponseDTO();
    }

    private static ResponseDTO addNewEntity() {

        return new ResponseDTO();
    }


    private static ResponseDTO viewPassengersOnTrain() {
        return new ResponseDTO();
    }

    private static ResponseDTO viewTrains() {
        return new ResponseDTO();
    }
}

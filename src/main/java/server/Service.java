package server;

import protocol.RequestObject;
import protocol.ResponseObject;
import protocol.Constants;

import org.hibernate.annotations.GenericGenerator;

/**
 * It encapsulates methods which execute all possible services provided by server for clients
 */
public class Service {
    /**
     * Method determines which kind of service should be execute. It depends on parameter is passed in this
     * method. And after analyse, execute needed service method.
     * @param reqObj - instructions and required data to execute appropriate service, passed from client.
     * @return Data which are requested by user.
     */
    public static ResponseObject execute(RequestObject reqObj) {
        Constants.ClientService reqService = reqObj.getService();

        if (reqService == Constants.ClientService.getScheduleFromAtoB)     return scheduleFromAtoB();
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

    private static ResponseObject scheduleFromAtoB() {
        return new ResponseObject();
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

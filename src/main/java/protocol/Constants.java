package protocol;

/**
 * It contains some auxiliary enums for protocol's objects.
 */
public class Constants {
    //Possible types of clients
    public enum ClientType {user, admin};

    //Possible services that may be requested by the client
    public enum ClientService {getScheduleFromAtoB, scheduleForStation, ticketPurchase,
                        addEntity, viewPassangers, viewTrains, saleTicket};

    //Status of executing service
    public enum StatusOfExecutedService {success, error};
}

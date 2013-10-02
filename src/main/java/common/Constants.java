package common;


public class Constants {
    //Possible types of clients
    public enum ClientType {user, admin};

    //Possible services that may be requested by the client
    public enum ClientService {getScheduleFromAtoB, scheduleForStation, buyTicket,
                               addTrain, addStation, addRoute, viewPassangers, viewTrains, saleTicket};

    //Status of executing service
    public enum StatusOfExecutedService {success, error};


    /**
     * This constants required for time setting when u create new instance of java.util.Timestamp class. Since this
     * application requires only time, date may be disregarded.
     */
    public final static long POINT_OF_REFERENCE = 75600000;
    public final static long HOUR = 3600000;
    public final static long MINUTE = 60000;
}

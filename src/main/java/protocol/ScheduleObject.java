package protocol;

import java.io.Serializable;
import java.sql.Timestamp;

public class ScheduleObject implements Serializable{
    int number;
    String fromStation;
    String toStation;
    Timestamp departureTime;
    Timestamp arrivalTime;
    int ticketsAmount;

    public int getNumber() {
        return number;
    }

    public String getFromStation() {
        return fromStation;
    }

    public String getToStation() {
        return toStation;
    }

    public Timestamp getDepartureTime() {
        return departureTime;
    }

    public Timestamp getArrivalTime() {
        return arrivalTime;
    }

    public int getTicketsAmount() {
        return ticketsAmount;
    }

    public ScheduleObject() {}

    public ScheduleObject(int numb,
                          String from,
                          String to,
                          Timestamp fromTime,
                          Timestamp toTime,
                          int tickets) {
        number = numb;
        fromStation = from;
        toStation = to;
        departureTime = fromTime;
        arrivalTime = toTime;
        ticketsAmount = tickets;
    }
}

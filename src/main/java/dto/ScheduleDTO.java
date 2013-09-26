package dto;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;

public class ScheduleDTO implements Serializable{
    int number;
    String fromStation;
    String toStation;
    Time departureTime;
    Time arrivalTime;
    int ticketsAmount;

    public void setNumber(int number) {
        this.number = number;
    }

    public void setFromStation(String fromStation) {
        this.fromStation = fromStation;
    }

    public void setToStation(String toStation) {
        this.toStation = toStation;
    }

    public void setDepartureTime(Time departureTime) {
        this.departureTime = departureTime;
    }

    public void setArrivalTime(Time arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setTicketsAmount(int ticketsAmount) {
        this.ticketsAmount = ticketsAmount;
    }

    public int getNumber() {
        return number;
    }

    public String getFromStation() {
        return fromStation;
    }

    public String getToStation() {
        return toStation;
    }

    public Time getDepartureTime() {
        return departureTime;
    }

    public Time getArrivalTime() {
        return arrivalTime;
    }

    public int getTicketsAmount() {
        return ticketsAmount;
    }

    public ScheduleDTO() {
        /*number = 34;
        fromStation = "Псков";
        toStation = "Москва";
        departureTime = new Timestamp(Constants.POINT_OF_REFERENCE+12*Constants.HOUR);
        arrivalTime = new Timestamp(Constants.POINT_OF_REFERENCE+12*Constants.HOUR + 13*Constants.MINUTE);;
        ticketsAmount = 123;*/
    }

    public ScheduleDTO(int numb,
                       String from,
                       String to,
                       Time fromTime,
                       Time toTime,
                       int tickets) {
        number = numb;
        fromStation = from;
        toStation = to;
        departureTime = fromTime;
        arrivalTime = toTime;
        ticketsAmount = tickets;
    }
}

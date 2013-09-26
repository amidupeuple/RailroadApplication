package entity;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: danya_000
 * Date: 9/21/13
 * Time: 2:48 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "station_in_route")
public class StationInRoute {
    @EmbeddedId
    private StationInRoutePK id;

    @Column(name = "departure_time")
    private Time departureTime;

    @Column(name = "arrival_time")
    private Time arrivalTime;

    private String name;

    @ManyToOne
    private Train train;

    public StationInRoute() {}

    public StationInRoute(StationInRoutePK stationID, String n, Time dep, Time arr) {
        id = stationID;
        name = n;
        departureTime = dep;
        arrivalTime = arr;
    }

    public boolean isBiggerThan(Timestamp date) {
        Date date1 = new Date(departureTime.getTime());
        Date date2 = new Date(date.getTime());

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        cal2.set(Calendar.DAY_OF_MONTH, cal1.get(Calendar.DAY_OF_MONTH));
        cal2.set(Calendar.MONTH, cal1.get(Calendar.MONTH));
        cal2.set(Calendar.YEAR, cal1.get(Calendar.YEAR));

        if (cal1.getTimeInMillis() - cal2.getTimeInMillis() < 0) return false;
        else return true;
    }

    public StationInRoutePK getId() {
        return id;
    }

    public void setId(StationInRoutePK id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    public Time getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Time departureTime) {
        this.departureTime = departureTime;
    }

    public Time getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Time arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
}

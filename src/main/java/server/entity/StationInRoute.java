package server.entity;

import javax.persistence.*;
import java.sql.Timestamp;

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
    private Timestamp departureTime;

    @Column(name = "arrival_time")
    private Timestamp arrivalTime;

    private String name;

    @ManyToOne
    private Train train;

    public StationInRoute() {}

    public StationInRoute(StationInRoutePK stationID, String n, Timestamp dep, Timestamp arr) {
        id = stationID;
        name = n;
        departureTime = dep;
        arrivalTime = arr;
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

    public Timestamp getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Timestamp departureTime) {
        this.departureTime = departureTime;
    }

    public Timestamp getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Timestamp arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
}

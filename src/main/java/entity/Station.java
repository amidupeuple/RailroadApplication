package entity;

import javax.persistence.*;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: danya_000
 * Date: 9/27/13
 * Time: 5:54 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "station")
public class Station {
    private int id;
    private String name;
    private Set<StationInRoute> stationsInRoute;

    public Station() {}

    public Station(String n) {
        name = n;
    }

    @Id
    @Column(name = "id", precision = 0)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "station")
    public Set<StationInRoute> getStationsInRoute() {
        return stationsInRoute;
    }

    public void setStationsInRoute(Set<StationInRoute> stationsInRoute) {
        this.stationsInRoute = stationsInRoute;
    }
}

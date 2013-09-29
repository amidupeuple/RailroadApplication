package entity;

import javax.persistence.*;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: danya_000
 * Date: 9/27/13
 * Time: 5:45 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "route")
public class Route {
    private int id;
    private Set<StationInRoute> stationsInRoute;

    public Route() {}

    @Id
    @Column(name="id", precision=0)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @OneToMany(mappedBy = "route")
    public Set<StationInRoute> getStationsInRoute() {
        return stationsInRoute;
    }

    public void setStationsInRoute(Set<StationInRoute> stationsInRoute) {
        this.stationsInRoute = stationsInRoute;
    }
}

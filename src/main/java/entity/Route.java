package entity;

import javax.persistence.*;
import java.util.Set;


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

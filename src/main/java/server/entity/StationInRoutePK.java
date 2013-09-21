package server.entity;

import javax.persistence.Basic;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: danya_000
 * Date: 9/21/13
 * Time: 4:20 PM
 * To change this template use File | Settings | File Templates.
 */
@Embeddable
public class StationInRoutePK implements Serializable{
    @Basic
    private int routeId;

    @Basic
    private int numberInRoute;

    public StationInRoutePK() {}

    public StationInRoutePK(int rId, int numb) {
        routeId = rId;
        numberInRoute = numb;
    }

    public boolean equals(Object object) {
        if (object instanceof StationInRoutePK) {
            StationInRoutePK pk = (StationInRoutePK)object;
            return routeId == pk.routeId && numberInRoute == pk.numberInRoute;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return routeId + numberInRoute;
    }
}

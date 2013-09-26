package entity;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: danya_000
 * Date: 9/22/13
 * Time: 1:31 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "ticket")
public class Ticket {
    private int id;
    private Train train;
    private Passenger passenger;

    public Ticket() {}

    public Ticket(Train t, Passenger p) {
        train = t;
        passenger = p;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne
    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    @ManyToOne
    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }
}

package entity;

import javax.persistence.*;


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

    @ManyToOne(targetEntity = Train.class)
    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    @ManyToOne(targetEntity = Passenger.class)
    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }
}

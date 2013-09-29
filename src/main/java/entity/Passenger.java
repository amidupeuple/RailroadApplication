package entity;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: danya_000
 * Date: 9/22/13
 * Time: 1:46 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "passenger")
public class Passenger {
    private int id;
    private String firstName;
    private String secondName;
    private Date dateOfBirth;
    private Set<Ticket> tickets;

    public Passenger() {}

    public Passenger(String first, String second, Date date) {
        firstName = first;
        secondName = second;
        dateOfBirth = date;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "first_name")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "second_name")
    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    @Column(name = "date_of_birth")
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @OneToMany(mappedBy = "passenger")
    public Set<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(Set<Ticket> tickets) {
        this.tickets = tickets;
    }
}

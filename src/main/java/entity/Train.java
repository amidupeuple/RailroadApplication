package entity;

import javax.persistence.*;
import java.util.Set;


@Entity
@Table(name = "train")
public class Train {
    private int id;
    private int number;
    private int vacancies;
    private Set<Ticket> tickets;
    private Set<StationInRoute> stationsInRoute;

    public Train(){}

    public Train(int numb, int vac) {
        number = numb;
        vacancies = vac;
    }

    @Id
    @Column(name="id", precision=0)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "number")
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Column(name = "vacancies")
    public int getVacancies() {
        return vacancies;
    }

    public void setVacancies(int vacancies) {
        this.vacancies = vacancies;
    }

    @OneToMany(mappedBy = "train")
    public Set<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(Set<Ticket> tickets) {
        this.tickets = tickets;
    }

    @OneToMany(mappedBy = "train")
    public Set<StationInRoute> getStationsInRoute() {
        return stationsInRoute;
    }

    public void setStationsInRoute(Set<StationInRoute> stationsInRoute) {
        this.stationsInRoute = stationsInRoute;
    }
}

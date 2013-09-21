package server.entity;

import javax.persistence.*;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: danya_000
 * Date: 9/21/13
 * Time: 1:24 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "train")
public class Train {
    private int number;
    private int vacancies;
    private Set<StationInRoute> stations;

    public Train(){}

    public Train(int numb, int vac) {
        number = numb;
        vacancies = vac;
    }

    @Id
    @Column(name="number", precision=0)
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
    public Set<StationInRoute> getStations() {
        return stations;
    }

    public void setStations(Set<StationInRoute> s) {
        this.stations = s;
    }


}

package dto;

import java.io.Serializable;
import java.sql.Date;

/**
 * DTO object to transfer data about ticket order.
 */
public class OrderDTO implements Serializable{
    String firstName;
    String secondName;
    Date dateOfBirth;
    int trainNumber;
    String fromStation;

    public OrderDTO() {}

    public OrderDTO(String first, String second, Date date, int numb, String fromSt) {
        firstName = first;
        secondName = second;
        dateOfBirth = date;
        trainNumber = numb;
        fromStation = fromSt;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(int trainNumber) {
        this.trainNumber = trainNumber;
    }

    public String getFromStation() {
        return fromStation;
    }

    public void setFromStation(String fromStation) {
        this.fromStation = fromStation;
    }

}

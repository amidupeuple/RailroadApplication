package dto;

import java.io.Serializable;
import java.sql.Date;

/**
 * Created with IntelliJ IDEA.
 * User: danya_000
 * Date: 9/28/13
 * Time: 8:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class PassengerDTO implements Serializable{
    private String firstName;
    private String secondName;
    private Date dateOfBirth;
    private int trainNumber;

    public PassengerDTO(String first, String second, Date date, int numb) {
        firstName = first;
        secondName = second;
        dateOfBirth = date;
        trainNumber = numb;
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
}

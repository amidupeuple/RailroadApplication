package client.gui;

import dto.PassengerDTO;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: danya_000
 * Date: 9/28/13
 * Time: 9:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class PassengersTableModel extends AbstractTableModel {
    List<PassengerDTO> passengers;
    final String[] columnNames = {"№ поезда",
                                  "Имя",
                                  "Фамилия",
                                  "Дата рождения"};

    public PassengersTableModel() {
        passengers = new ArrayList<PassengerDTO>();
    }

    public PassengersTableModel(ArrayList<PassengerDTO> passengersList) {
        passengers = passengersList;
    }

    @Override
    public int getRowCount() {
        return passengers.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0: return passengers.get(rowIndex).getTrainNumber();
            case 1: return passengers.get(rowIndex).getFirstName();
            case 2: return passengers.get(rowIndex).getSecondName();
            case 3: return passengers.get(rowIndex).getDateOfBirth();
            default: return null;
        }
    }

    public List<PassengerDTO> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<PassengerDTO> passengers) {
        this.passengers = passengers;
    }
}

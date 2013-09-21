package client.gui;

import protocol.ScheduleObject;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 * This is model for jtable in user's gui panel. It displays schedule of trains.
 */
public class TrainsTableModel extends AbstractTableModel{
    
    ArrayList<ScheduleObject> trains;
    final String[] columnNames = {"№", 
                                 "От", 
                                 "До", 
                                 "Отправление", 
                                 "Прибытие", 
                                 "Билеты"};
    
    public TrainsTableModel() {
        trains = new ArrayList<ScheduleObject>();
        trains.add(new ScheduleObject());
    }
    
    public TrainsTableModel(ArrayList<ScheduleObject> trs) {
        trains = trs;
    }
    
    @Override
    public int getRowCount() {
        return trains.size();
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
        switch(columnIndex) {
            case 0: return trains.get(rowIndex).getNumber();
            case 1: return trains.get(rowIndex).getFromStation();
            case 2: return trains.get(rowIndex).getToStation();
            case 3: return trains.get(rowIndex).getDepartureTime().toString();
            case 4: return trains.get(rowIndex).getArrivalTime().toString();
            case 5: return trains.get(rowIndex).getTicketsAmount();
            default: return null;
        }
    }
    
}

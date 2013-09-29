package client.gui;

import dto.ScheduleDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 * This is model for jtable in user's gui panel. It displays schedule of trains.
 */
public class TrainsTableModel extends AbstractTableModel{

    public void setTrains(ArrayList<ScheduleDTO> trains) {
        this.trains = trains;
    }

    public ArrayList<ScheduleDTO> getTrains() {
        return trains;
    }

    ArrayList<ScheduleDTO> trains;
    final String[] columnNames = {"№", 
                                 "От", 
                                 "До", 
                                 "Отправление", 
                                 "Прибытие", 
                                 "Места"};
    
    public TrainsTableModel() {
        trains = new ArrayList<ScheduleDTO>();
        //trains.add(new ScheduleDTO());
    }
    
    public TrainsTableModel(ArrayList<ScheduleDTO> trs) {
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
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");

        switch(columnIndex) {
            case 0: return trains.get(rowIndex).getNumber();
            case 1: return trains.get(rowIndex).getFromStation();
            case 2: return trains.get(rowIndex).getToStation();
            case 3: if (trains.get(rowIndex).getDepartureTime() == null) return "";
                    else return format.format(trains.get(rowIndex).getDepartureTime());
            case 4: if (trains.get(rowIndex).getArrivalTime() == null) return "";
                    else return format.format(trains.get(rowIndex).getArrivalTime());
            case 5: return trains.get(rowIndex).getTicketsAmount();
            default: return null;
        }
    }


    
}

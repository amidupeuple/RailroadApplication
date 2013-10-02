package client.gui;

import client.ClientConnectionManager;
import dto.RequestDTO;
import dto.ResponseDTO;
import dto.ScheduleDTO;

import static common.Constants.HOUR;
import static common.Constants.MINUTE;
import static common.Constants.POINT_OF_REFERENCE;

import client.exception.ConnectToServerException;
import common.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * GUI panel for user
 */
public class ClientGUIPanel extends javax.swing.JPanel {

    public ClientGUIPanel() {
        initComponents();
        requestObject = new RequestDTO();
        scheduleObject = new ScheduleDTO();

        //Set default time interval
        scheduleObject.setDepartureTime(new Time(POINT_OF_REFERENCE));
        scheduleObject.setArrivalTime(new Time(POINT_OF_REFERENCE + 23*HOUR + 59*MINUTE));
    }

    private void initComponents() {

        optionsButtonGroup = new javax.swing.ButtonGroup();
        scheduleForStationRadioButton = new javax.swing.JRadioButton();
        stationTextField = new javax.swing.JTextField();
        stationLabel = new javax.swing.JLabel();
        scheduleFromAtoBRadioButton = new javax.swing.JRadioButton();
        fromStationLabel = new javax.swing.JLabel();
        toStationLabel = new javax.swing.JLabel();
        fromStationTextField = new javax.swing.JTextField();
        toStationTextField = new javax.swing.JTextField();
        timeIntervalLabel = new javax.swing.JLabel();
        timeFromSpinner = new javax.swing.JSpinner();
        colonLabel = new javax.swing.JLabel();
        timeToSpinner = new javax.swing.JSpinner();
        showButton = new javax.swing.JButton();
        buyTicketButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        trainsTable = new javax.swing.JTable();
        resetButton = new javax.swing.JButton();
        trainsTableModel = new TrainsTableModel();

        disableScheduleForStationWidgets();
        disableTrainSearchWidgets();

        optionsButtonGroup.add(scheduleForStationRadioButton);
        scheduleForStationRadioButton.setText("Расписание поездов по станции:");
        scheduleForStationRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scheduleForStationRadioButtonActionPerformed(evt);
            }
        });

        stationTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                scheduleObject.setFromStation(stationTextField.getText());
            }
        });

        stationLabel.setText("Станция:");

        optionsButtonGroup.add(scheduleFromAtoBRadioButton);
        scheduleFromAtoBRadioButton.setText("Поиск поезда:");
        scheduleFromAtoBRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scheduleFromAtoBRadioButtonActionPerformed(evt);
            }
        });

        fromStationLabel.setText("От:");

        toStationLabel.setText("До:");

        fromStationTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                scheduleObject.setFromStation(fromStationTextField.getText());
            }
        });

        toStationTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                scheduleObject.setToStation(toStationTextField.getText());
            }
        });

        timeIntervalLabel.setText("Временной интервал:");
        colonLabel.setText(":");

        setSpinnerModels();
        timeFromSpinner.addChangeListener(new TimeFromListener());
        timeToSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                scheduleObject.setArrivalTime(new Time(((Date) timeToSpinner.getValue()).getTime()));
            }
        });

        showButton.setText("Показать");
        showButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showButtonActionPerformed();
            }
        });


        buyTicketButton.setText("Заказать билет");
        buyTicketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = trainsTable.getSelectedRow();

                //If row isn't selected
                if (row == -1) return;

                JDialog dialog = new OrderDialog(null, true, trainsTableModel.getTrains(), row);
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);
            }
        });

        trainsTable.setModel(trainsTableModel);
        trainsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        trainsTable.setDefaultRenderer( Object.class, new BorderLessTableCellRenderer() );

        jScrollPane1.setViewportView(trainsTable);

        jScrollPane2.setViewportView(jScrollPane1);

        resetButton.setText("Сбросить");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                trainsTableModel.setTrains(new ArrayList<ScheduleDTO>());
                trainsTableModel.fireTableDataChanged();

                fromStationTextField.setText("");
                toStationTextField.setText("");

                stationTextField.setText("");
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(showButton)
                        .addGap(18, 18, 18)
                        .addComponent(buyTicketButton)
                        .addGap(18, 18, 18)
                        .addComponent(resetButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(scheduleForStationRadioButton)
                            .addComponent(scheduleFromAtoBRadioButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(stationLabel)
                                .addGap(18, 18, 18)
                                .addComponent(stationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(fromStationLabel)
                                .addGap(18, 18, 18)
                                .addComponent(fromStationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(toStationLabel)
                                .addGap(18, 18, 18)
                                .addComponent(toStationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 492, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(timeIntervalLabel)
                        .addGap(18, 18, 18)
                        .addComponent(timeFromSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(colonLabel)
                        .addGap(18, 18, 18)
                        .addComponent(timeToSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(scheduleForStationRadioButton)
                    .addComponent(stationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(stationLabel))
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(scheduleFromAtoBRadioButton)
                    .addComponent(fromStationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fromStationLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(toStationLabel)
                    .addComponent(toStationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(timeIntervalLabel)
                    .addComponent(timeFromSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colonLabel)
                    .addComponent(timeToSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(45, 45, 45)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(showButton)
                    .addComponent(buyTicketButton)
                    .addComponent(resetButton))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(36, Short.MAX_VALUE))
        );
    }


    /**
     * Executes when "showButton" is pressed. It checks correctness of formed data to server - instance of
     * ScheduleDTO, calls method to connect to server. After, displays in gui table received data from server.
     */
    private void showButtonActionPerformed() {
        //Clear gui table.
        trainsTableModel.setTrains(new ArrayList<ScheduleDTO>());
        trainsTableModel.fireTableDataChanged();


        //Check correctness of data prepared to send to server.
        if (mode == SelectedOption.scheduleForStation) {
            if (scheduleObject.getFromStation() == null || scheduleObject.getFromStation().equals("")) {
                JOptionPane.showMessageDialog(null, "Некорректно введена станция. Повторите ввод");
                return;
            }
        } else if (mode == SelectedOption.scheduleFromAtoB){
            if (scheduleObject.getFromStation() == null || scheduleObject.getFromStation().equals("")) {
                JOptionPane.showMessageDialog(null, "Некорректно введена станция отправления. Повторите ввод");
                return;
            } else if (scheduleObject.getToStation() == null || scheduleObject.getToStation().equals("")) {
                JOptionPane.showMessageDialog(null, "Некорректно введена станция прибытия. Повторите ввод");
                return;
            } else if (scheduleObject.getDepartureTime().after(scheduleObject.getArrivalTime())) {
                JOptionPane.showMessageDialog(null, "Некорректно введен временной интервал. Повторите ввод");
                return;
            }
        } else if (mode == SelectedOption.noSelection) {
            JOptionPane.showMessageDialog(null, "Не выбрана ни одна из опций");
            return;
        }

        //Packaging data in instance of RequestDTO to send it to server
        if (mode == SelectedOption.scheduleForStation) requestObject.setService(Constants.ClientService.scheduleForStation);
        else if (mode == SelectedOption.scheduleFromAtoB) requestObject.setService(Constants.ClientService.getScheduleFromAtoB);

        requestObject.setObject(scheduleObject);

        //Sending data to the server and receiving response data.
        try {
            responseObject = ClientConnectionManager.connect(requestObject);
        } catch (ConnectToServerException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            return;
        }

        if (responseObject.getStatus() == Constants.StatusOfExecutedService.error) {
            JOptionPane.showMessageDialog(null, responseObject.getObject());
        } else if (responseObject.getStatus() == Constants.StatusOfExecutedService.success) {
            //Refresh contents of client's gui-table.
            trainsTableModel.setTrains((ArrayList<ScheduleDTO>) responseObject.getObject());
            trainsTableModel.fireTableDataChanged();
        }

    }

    private void setSpinnerModels() {
        Date date = new Date(POINT_OF_REFERENCE);

        SpinnerDateModel sm1 = new SpinnerDateModel(date, null, null, Calendar.MINUTE);
        SpinnerDateModel sm2 = new SpinnerDateModel(date, null, null, Calendar.MINUTE);

        timeFromSpinner.setModel(sm1);
        timeToSpinner.setModel(sm2);
        timeFromSpinner.setEditor(new JSpinner.DateEditor(timeFromSpinner, "HH:mm"));
        timeToSpinner.setEditor(new JSpinner.DateEditor(timeToSpinner, "HH:mm"));
    }


    private void disableScheduleForStationWidgets() {
        stationTextField.setEnabled(false);
        stationTextField.setText("");
    }

    private void enableScheduleForStationWidgets() {
        stationTextField.setEnabled(true);
    }

    private void disableTrainSearchWidgets() {
        fromStationTextField.setEnabled(false);
        toStationTextField.setEnabled(false);
        timeFromSpinner.setEnabled(false);
        timeToSpinner.setEnabled(false);

        fromStationTextField.setText("");
        toStationTextField.setText("");

        buyTicketButton.setEnabled(false);
    }

    private void enableTrainSearchWidgets() {
        fromStationTextField.setEnabled(true);
        toStationTextField.setEnabled(true);
        timeFromSpinner.setEnabled(true);
        timeToSpinner.setEnabled(true);

        buyTicketButton.setEnabled(true);
    }

    private void scheduleForStationRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {
        clearScheduleResults();
        mode = SelectedOption.scheduleForStation;
        enableScheduleForStationWidgets();
        disableTrainSearchWidgets();
    }

    private void scheduleFromAtoBRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {
        clearScheduleResults();
        mode = SelectedOption.scheduleFromAtoB;
        enableTrainSearchWidgets();
        disableScheduleForStationWidgets();
    }

    private javax.swing.JButton buyTicketButton;
    private javax.swing.JLabel colonLabel;
    private javax.swing.JLabel fromStationLabel;
    private javax.swing.JTextField fromStationTextField;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.ButtonGroup optionsButtonGroup;
    private javax.swing.JButton resetButton;
    private javax.swing.JButton showButton;
    private javax.swing.JLabel stationLabel;
    private javax.swing.JTextField stationTextField;
    private javax.swing.JSpinner timeFromSpinner;
    private javax.swing.JLabel timeIntervalLabel;
    private javax.swing.JSpinner timeToSpinner;
    private javax.swing.JLabel toStationLabel;
    private javax.swing.JTextField toStationTextField;
    private javax.swing.JRadioButton scheduleForStationRadioButton;
    private javax.swing.JRadioButton scheduleFromAtoBRadioButton;
    private javax.swing.JTable trainsTable;
    private TrainsTableModel trainsTableModel;
    // End of variables declaration//GEN-END:variables

    RequestDTO requestObject;
    ScheduleDTO scheduleObject;
    ResponseDTO responseObject;

    private SelectedOption mode = SelectedOption.noSelection;

    int rowNumber;                              //Number of selected row in the trainsTable.
    
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                JFrame frame = new JFrame("Client");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(575, 600);
                ClientGUIPanel pan = new ClientGUIPanel();
                frame.getContentPane().add(new JScrollPane(new ClientGUIPanel()));
                frame.setVisible(true);
            }
            
        });
    }

    class TimeFromListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            scheduleObject.setDepartureTime(new Time(((Date) timeFromSpinner.getValue()).getTime()));
        }
    }


    private void clearScheduleResults() {
        trainsTableModel.setTrains(new ArrayList<ScheduleDTO>());
        trainsTableModel.fireTableDataChanged();
    }

    private enum SelectedOption {noSelection, scheduleForStation, scheduleFromAtoB};




}

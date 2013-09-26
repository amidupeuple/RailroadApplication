package client.gui;

import client.ClientConnectionManager;
import dto.RequestDTO;
import dto.ResponseDTO;
import dto.ScheduleDTO;

import static protocol.Constants.POINT_OF_REFERENCE;

import protocol.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
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
    }

    private void initComponents() {

        optionsButtonGroup = new javax.swing.ButtonGroup();
        trainScheduleRadioButton = new javax.swing.JRadioButton();
        stationTextField = new javax.swing.JTextField();
        stationLabel = new javax.swing.JLabel();
        trainSearchRadioButton = new javax.swing.JRadioButton();
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

        //Initialization - hasn't yet been user's action - set widgets disabled
        disableScheduleForStationWidgets();
        disableTrainSearchWidgets();

        optionsButtonGroup.add(trainScheduleRadioButton);
        trainScheduleRadioButton.setText("Расписание поездов по станции:");
        trainScheduleRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                trainScheduleRadioButtonActionPerformed(evt);
            }
        });

        stationTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stationTextFieldActionPerformed(evt);
            }
        });

        stationLabel.setText("Станция:");

        optionsButtonGroup.add(trainSearchRadioButton);
        trainSearchRadioButton.setText("Поиск поезда:");
        trainSearchRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                trainSearchRadioButtonActionPerformed(evt);
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
                JDialog dialog = new OrderDialog(null, true, trainsTableModel.getTrains(), rowNumber);
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);
            }
        });

        trainsTable.setModel(trainsTableModel);
        trainsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    Point p = e.getPoint();
                    rowNumber = trainsTable.rowAtPoint(p);
                    ListSelectionModel model = trainsTable.getSelectionModel();
                    model.setSelectionInterval(rowNumber, rowNumber);
                }
                /*else if (SwingUtilities.isRightMouseButton(e)) {
                    Point p = e.getPoint();
                    int rowNumber = trainsTable.rowAtPoint(p);
                    ListSelectionModel model = trainsTable.getSelectionModel();
                    model.setSelectionInterval(rowNumber, rowNumber);

                    JPopupMenu popupMenu = new JPopupMenu();
                    JMenuItem menuItemBuyTicket = new JMenuItem("Купить билет");
                    menuItemBuyTicket.addActionListener(new BuyTicketButtonListener(trainsTableModel.getTrains(),
                                                                                      rowNumber));

                    popupMenu.add(menuItemBuyTicket);
                    popupMenu.show(trainsTable,e.getX(),e.getY());

                }*/

            }
        });


        jScrollPane1.setViewportView(trainsTable);

        jScrollPane2.setViewportView(jScrollPane1);

        resetButton.setText("Сбросить");

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
                            .addComponent(trainScheduleRadioButton)
                            .addComponent(trainSearchRadioButton))
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
                    .addComponent(trainScheduleRadioButton)
                    .addComponent(stationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(stationLabel))
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(trainSearchRadioButton)
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
    }// </editor-fold>//GEN-END:initComponents

    private void showButtonActionPerformed() {

        //Check correctness of data prepared to send to server.
        if (isScheduleForStation) {
            if (scheduleObject.getFromStation().equals("")) {
                System.out.println("no station");
                return;
            }
        } else {
            if (scheduleObject.getFromStation().equals("")) {
                System.out.println("no from station");
                return;
            } else if (scheduleObject.getToStation().equals("")) {
                System.out.println("no to station");
                return;
            } else if (scheduleObject.getDepartureTime().after(scheduleObject.getArrivalTime())) {
                System.out.println("uncorrect time interval");
                return;
            }
        }

        //Packaging data in instance of RequestDTO to send it to server
        if (isScheduleForStation) requestObject.setService(Constants.ClientService.scheduleForStation);
        else requestObject.setService(Constants.ClientService.getScheduleFromAtoB);
        requestObject.setObject(scheduleObject);

        //Sending data to the server and receiving response data.
        responseObject = ClientConnectionManager.connect(requestObject);

        //Refresh contents of client's gui-table.
        trainsTableModel.setTrains((ArrayList<ScheduleDTO>) responseObject.getObject());
        trainsTableModel.fireTableDataChanged();
    }

    private void setSpinnerModels() {
        Date date = new Date(POINT_OF_REFERENCE);

        SpinnerDateModel sm1 = new SpinnerDateModel(date, null, null, Calendar.MINUTE);
        SpinnerDateModel sm2 = new SpinnerDateModel(date, null, null, Calendar.MINUTE);



        timeFromSpinner.setModel(sm1);
        timeToSpinner.setModel(sm2);
        timeFromSpinner.setEditor(new JSpinner.DateEditor(timeFromSpinner, "HH:mm"));
        timeToSpinner.setEditor(new JSpinner.DateEditor(timeToSpinner, "HH:mm"));
        /*JSpinner.DateEditor de1 = new JSpinner.DateEditor(timeFromSpinner, "HH:mm");
        //de1.getTextField().setEditable(false);
        JSpinner.DateEditor de2 = new JSpinner.DateEditor(timeToSpinner, "HH:mm");
        //de2.getTextField().setEditable(false);

        timeFromSpinner.setEditor(de1);
        timeToSpinner.setEditor(de2);*/
    }


    private void disableScheduleForStationWidgets() {
        stationTextField.setEnabled(false);
        stationTextField.setText("");
    }

    private void enableScheduleForStationWidgets() {
        stationTextField.setEnabled(true);
    }

    /**
     * Disable widgets which corresponding "search train" radiobutton.
     */
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

    private void trainScheduleRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {
        clearScheduleResults();
        isScheduleForStation = true;
        enableScheduleForStationWidgets();
        disableTrainSearchWidgets();
    }

    private void stationTextFieldActionPerformed(java.awt.event.ActionEvent evt) {
        scheduleObject.setFromStation(stationTextField.getText());
    }

    private void fromStationTextFieldActionPerformed(java.awt.event.ActionEvent evt) {
        scheduleObject.setFromStation(fromStationTextField.getText());
    }

    private void toStationTextFieldActionPerformed(java.awt.event.ActionEvent evt) {
        scheduleObject.setToStation(toStationTextField.getText());
    }

    private void trainSearchRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {
        clearScheduleResults();
        isScheduleForStation = false;
        enableTrainSearchWidgets();
        disableScheduleForStationWidgets();
    }

    private void buyTicketButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buyTicketButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_buyTicketButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
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
    private javax.swing.JRadioButton trainScheduleRadioButton;
    private javax.swing.JRadioButton trainSearchRadioButton;
    private javax.swing.JTable trainsTable;
    private TrainsTableModel trainsTableModel;
    // End of variables declaration//GEN-END:variables

    RequestDTO requestObject;
    ScheduleDTO scheduleObject;
    ResponseDTO responseObject;

    private boolean isScheduleForStation;       //if true, user selected "schedule for station" service, otherwise
                                                //train searching from station A to B in given time interval.

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

    class TimeToListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            scheduleObject.setArrivalTime(new Time(((Date) timeToSpinner.getValue()).getTime()));
            int i = 0;
        }
    }

    class BuyTicketButtonListener implements ActionListener {
        ArrayList<ScheduleDTO> schedule;
        int row;

        public BuyTicketButtonListener(ArrayList<ScheduleDTO> s, int r) {
            schedule = s;
            row = r;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JDialog dialog = new OrderDialog(null, true, schedule, row);
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        }
    }

    private void clearScheduleResults() {
        trainsTableModel.setTrains(new ArrayList<ScheduleDTO>());
        trainsTableModel.fireTableDataChanged();
    }

}

package client.gui;

import client.ClientConnectionManager;
import dto.PassengerDTO;
import dto.RequestDTO;
import dto.ResponseDTO;
import dto.ScheduleDTO;
import protocol.Constants;
import server.exceptions.AdministratorRouteInputException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.PatternSyntaxException;

/**
 * Panel for administrator's gui.
 */
public class AdminPanel extends javax.swing.JPanel {

    public AdminPanel(JFrame f) {
        dataForUpdateList = new ArrayList<ScheduleDTO>();
        dataForUpdate = new ScheduleDTO();
        request = new RequestDTO();
        frame = f;
        initComponents();
    }

    private void initComponents() {

        optionsButtonGroup = new javax.swing.ButtonGroup();
        tabbedPane = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        addTrainRadioButton = new javax.swing.JRadioButton();
        addStationRadioButton = new javax.swing.JRadioButton();
        addRouteRadioButton = new javax.swing.JRadioButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        inputTextArea = new javax.swing.JTextArea();
        addNewEntitiesCloseButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        trainNumberLabel = new javax.swing.JLabel();
        addTrainNumberTextField = new javax.swing.JTextField();
        capacityLabel = new javax.swing.JLabel();
        capacityTextField = new javax.swing.JTextField();
        stationNameLabel = new javax.swing.JLabel();
        stationNameTextField = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        inputTrainNumberLabel = new javax.swing.JLabel();
        trainNumberTextField = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        passengersTable = new javax.swing.JTable();
        viewPassengersCloseButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        showTrainsButton = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        trainsTable = new javax.swing.JTable();
        trainsViewCloseButton = new javax.swing.JButton();
        saleTicketButton = new javax.swing.JButton();
        passengersTableModel = new PassengersTableModel();
        trainsTableModel = new TrainsTableModel();

        disableAddRouteWidgets();
        disableAddStationWidgets();
        disableAddTrainWidgets();

        optionsButtonGroup.add(addTrainRadioButton);
        addTrainRadioButton.setText("Добавить поезд");
        addTrainRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enableAddTrainWidgets();
                disableAddRouteWidgets();
                disableAddStationWidgets();
            }
        });

        optionsButtonGroup.add(addStationRadioButton);
        addStationRadioButton.setText("Добавить станцию");
        addStationRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enableAddStationWidgets();
                disableAddTrainWidgets();
                disableAddRouteWidgets();
            }
        });

        optionsButtonGroup.add(addRouteRadioButton);
        addRouteRadioButton.setText("Добавить маршрут");
        addRouteRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enableAddRouteWidgets();
                disableAddTrainWidgets();
                disableAddStationWidgets();
            }
        });

        inputTextArea.setColumns(20);
        inputTextArea.setRows(5);
        inputTextArea.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                routeInfo = inputTextArea.getText();
            }
        });

        jScrollPane1.setViewportView(inputTextArea);

        addNewEntitiesCloseButton.setText("Закрыть");
        addNewEntitiesCloseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                frame.setVisible(false);
            }
        });

        saveButton.setText("Сохранить");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (addTrainRadioButton.isSelected()) {
                    dataForUpdateList.add(dataForUpdate);
                    request.setService(Constants.ClientService.addTrain);
                } else if (addStationRadioButton.isSelected()) {
                    dataForUpdateList.add(dataForUpdate);
                    request.setService(Constants.ClientService.addStation);
                } else if (addRouteRadioButton.isSelected()) {
                    request.setService(Constants.ClientService.addRoute);
                    try {
                        routeInfoHandler(routeInfo);
                    } catch (AdministratorRouteInputException ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    }
                }

                request.setObject(dataForUpdateList);

                if (!dataForUpdateList.isEmpty()) {
                    ResponseDTO responce = ClientConnectionManager.connect(request);

                    JOptionPane.showMessageDialog(null, (String) responce.getObject());

                    dataForUpdateList.clear();
                    clearWidgetsContent();
                }
            }
        });

        trainNumberLabel.setText("Номер поезда:");
        addTrainNumberTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                dataForUpdate.setNumber(Integer.parseInt(addTrainNumberTextField.getText()));
            }
        });

        capacityLabel.setText("Вместимость:");
        capacityTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                dataForUpdate.setTicketsAmount(Integer.parseInt(capacityTextField.getText()));
            }
        });

        stationNameLabel.setText("Название станции:");
        stationNameTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                dataForUpdate.setFromStation(stationNameTextField.getText());
            }
        });

        trainNumberTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dataForUpdate.setNumber(Integer.parseInt(trainNumberTextField.getText()));
                dataForUpdateList.add(dataForUpdate);
                request.setService(Constants.ClientService.viewPassangers);
                request.setObject(dataForUpdateList);

                ResponseDTO responce = ClientConnectionManager.connect(request);

                if (responce.getStatus() == Constants.StatusOfExecutedService.success) {
                    passengersTableModel.setPassengers((List<PassengerDTO>) responce.getObject());
                    passengersTableModel.fireTableDataChanged();
                }
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(addStationRadioButton)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(21, 21, 21)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addComponent(stationNameLabel)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(stationNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addComponent(trainNumberLabel)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(addTrainNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(64, 64, 64)
                                                                .addComponent(capacityLabel)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(capacityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addComponent(jScrollPane1)))
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                        .addComponent(saveButton)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(addNewEntitiesCloseButton))
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                        .addComponent(addTrainRadioButton)
                                                        .addGap(455, 455, 455))
                                                .addComponent(addRouteRadioButton, javax.swing.GroupLayout.Alignment.LEADING)))
                                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(addTrainRadioButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(trainNumberLabel)
                                        .addComponent(addTrainNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(capacityLabel)
                                        .addComponent(capacityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(40, 40, 40)
                                .addComponent(addStationRadioButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(stationNameLabel)
                                        .addComponent(stationNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(48, 48, 48)
                                .addComponent(addRouteRadioButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(45, 45, 45)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(addNewEntitiesCloseButton)
                                        .addComponent(saveButton))
                                .addContainerGap(26, Short.MAX_VALUE))
        );

        tabbedPane.addTab("Добавить новые сущности", jPanel1);

        inputTrainNumberLabel.setText("Введите номер поезда:");

        passengersTable.setModel(passengersTableModel);
        jScrollPane2.setViewportView(passengersTable);

        viewPassengersCloseButton.setText("Закрыть");
        viewPassengersCloseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 570, Short.MAX_VALUE)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(inputTrainNumberLabel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(trainNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addComponent(viewPassengersCloseButton)))
                                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(inputTrainNumberLabel)
                                        .addComponent(trainNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 339, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(viewPassengersCloseButton)
                                .addGap(29, 29, 29))
        );

        tabbedPane.addTab("Просмотр зарегистрированных пассажиров", jPanel2);

        showTrainsButton.setText("Показать поезда");
        showTrainsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ResponseDTO responce = ClientConnectionManager.connect(new RequestDTO(Constants.ClientService.viewTrains,
                                                                       null));
                if (responce.getStatus() == Constants.StatusOfExecutedService.success) {
                    trainsTableModel.setTrains((ArrayList<ScheduleDTO>) responce.getObject());
                    trainsTableModel.fireTableDataChanged();
                }
            }
        });

        trainsTable.setModel(trainsTableModel);
        jScrollPane3.setViewportView(trainsTable);
        trainsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    Point p = e.getPoint();
                    rowNumber = trainsTable.rowAtPoint(p);
                    ListSelectionModel model = trainsTable.getSelectionModel();
                    model.setSelectionInterval(rowNumber, rowNumber);
                }
            }
        });

        trainsViewCloseButton.setText("Закрыть");
        trainsViewCloseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
            }
        });

        saleTicketButton.setText("Продажа билета");
        saleTicketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog dialog = new OrderDialog(null, true, trainsTableModel.getTrains(), rowNumber);
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 570, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(showTrainsButton)
                        .addGap(18, 18, 18)
                        .addComponent(saleTicketButton)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(trainsViewCloseButton)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(showTrainsButton)
                    .addComponent(saleTicketButton))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(trainsViewCloseButton)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        tabbedPane.addTab("Просмотр поездов", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPane)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPane)
        );
    }

    /**
     * Parse and analyse string which represents info about ne route.
     * @param info - info about new route, inserted by administrator
     * @throws AdministratorRouteInputException - exception of this type about incorrect entered route information.
     */
    private void routeInfoHandler(String info) throws AdministratorRouteInputException {
        String[] arrOfStations = null;
        DateFormat formatter = new SimpleDateFormat("HH:mm");
        Date tmpDateDep = null;
        Date tmpDateArr = null;
        int trainNumber = 0;

        if (info == null) {
            throw new AdministratorRouteInputException("Некорректно введен маршрут");
        }

        try {
            arrOfStations = info.split("\n");
        } catch (PatternSyntaxException e) {
            throw new AdministratorRouteInputException("Некорректно введен маршрут");
        }

        //Try to extract train number from administrator's input
        try {
            trainNumber = Integer.parseInt(arrOfStations[0]);
        } catch (NumberFormatException e) {
            throw new AdministratorRouteInputException("Некорректно введен номер поезда");
        }

        for (int i = 1; i < arrOfStations.length; i++) {
            String[] stationInfo = null;

            try {
                stationInfo = arrOfStations[i].split("_");
            } catch (PatternSyntaxException ex) {
                throw new AdministratorRouteInputException("Некорректно введена информация по станции");
            }

            ScheduleDTO stationData = new ScheduleDTO();
            stationData.setFromStation(stationInfo[0]);

            try {

                if (i == 1) {
                    tmpDateArr = null;
                    tmpDateDep = (Date) formatter.parse(stationInfo[1]);
                } else if (i == arrOfStations.length-1) {
                    tmpDateDep = null;
                    tmpDateArr = (Date) formatter.parse(stationInfo[1]);
                } else {
                    tmpDateDep = (Date) formatter.parse(stationInfo[2]);
                    tmpDateArr = (Date) formatter.parse(stationInfo[1]);
                }

            } catch (ParseException e1) {
                throw new AdministratorRouteInputException("Некорректно введено время");
            }

            if (i == 1) stationData.setArrivalTime(null);
            else stationData.setArrivalTime(new Time(tmpDateArr.getTime()));

            if (i == arrOfStations.length-1) stationData.setDepartureTime(null);
            else stationData.setDepartureTime(new Time(tmpDateDep.getTime()));

            dataForUpdateList.add(stationData);
        }

        dataForUpdateList.get(0).setNumber(trainNumber);
    }

    private void clearWidgetsContent() {
        addTrainNumberTextField.setText("");
        capacityTextField.setText("");
        stationNameTextField.setText("");
        inputTextArea.setText("");
    }

    private void disableAddTrainWidgets() {
        addTrainNumberTextField.setEnabled(false);
        capacityTextField.setEnabled(false);
    }

    private void enableAddTrainWidgets() {
        addTrainNumberTextField.setEnabled(true);
        capacityTextField.setEnabled(true);
    }

    private void disableAddStationWidgets() {
        stationNameTextField.setEnabled(false);
    }

    private void enableAddStationWidgets() {
        stationNameTextField.setEnabled(true);
    }

    private void disableAddRouteWidgets() {
        inputTextArea.setEnabled(false);
    }

    private void enableAddRouteWidgets() {
        inputTextArea.setEnabled(true);
    }

    private javax.swing.JRadioButton addRouteRadioButton;
    private javax.swing.JRadioButton addStationRadioButton;
    private javax.swing.JTextField addTrainNumberTextField;
    private javax.swing.JRadioButton addTrainRadioButton;
    private javax.swing.JButton addNewEntitiesCloseButton;
    private javax.swing.JLabel capacityLabel;
    private javax.swing.JTextField capacityTextField;
    private javax.swing.JButton viewPassengersCloseButton;
    private javax.swing.JButton trainsViewCloseButton;
    private javax.swing.JTextArea inputTextArea;
    private javax.swing.JLabel inputTrainNumberLabel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable passengersTable;
    private javax.swing.JTable trainsTable;
    private javax.swing.ButtonGroup optionsButtonGroup;
    private javax.swing.JButton saleTicketButton;
    private javax.swing.JButton saveButton;
    private javax.swing.JButton showTrainsButton;
    private javax.swing.JLabel stationNameLabel;
    private javax.swing.JTextField stationNameTextField;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JLabel trainNumberLabel;
    private javax.swing.JTextField trainNumberTextField;
    private PassengersTableModel passengersTableModel;
    private TrainsTableModel trainsTableModel;

    private List<ScheduleDTO> dataForUpdateList;    //Instances of ScheduleDTO will fill the list depending on the
                                                    //selected option. If administrator will choose "add train" option,
                                                    //this list will consist of one ScheduleDTO instance, in which
                                                    //will be specified parameters of new train (number and vacancies).
                                                    //If administrator will choose "add station", in the list also will
                                                    //be one element (only with name of station). And if "add route"
                                                    //option - list will consist of elements each of them represents
                                                    //station in route (name, time of arrival and departure).

    private ScheduleDTO dataForUpdate;              //In this variable we will put some information when one of the
                                                    //"add new entity" option is selected. Which information depends on
                                                    //what option is selected.

    private String routeInfo;                       //This string contains information about route entered by admin
                                                    //in appropriate text pane.

    private RequestDTO request;

    private JFrame frame;                           //Frame in which this instance of panel is embeded.

    private int rowNumber;                          //Row, selected in trainsTable



    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(650, 570);
                frame.getContentPane().add(new AdminPanel(frame));
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

}

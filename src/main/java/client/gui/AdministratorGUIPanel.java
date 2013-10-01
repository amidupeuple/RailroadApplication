package client.gui;

import client.ClientConnectionManager;
import dto.*;
import protocol.Constants;
import server.exceptions.EntityUpdateException;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.PatternSyntaxException;

import static protocol.Constants.HOUR;
import static protocol.Constants.MINUTE;
import static protocol.Constants.POINT_OF_REFERENCE;

public class AdministratorGUIPanel extends javax.swing.JPanel {

    public AdministratorGUIPanel(JFrame f) {
        dataForUpdateList = new ArrayList<ScheduleDTO>();
        dataForUpdate = new ScheduleDTO();
        request = new RequestDTO();
        frame = f;
        order = new OrderDTO();
        initComponents();

        //Set default time interval
        dataForUpdate.setDepartureTime(new Time(POINT_OF_REFERENCE));
        dataForUpdate.setArrivalTime(new Time(POINT_OF_REFERENCE + 23*HOUR + 59*MINUTE));
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
        saveButton = new javax.swing.JButton();
        trainNumberLabel = new javax.swing.JLabel();
        addTrainNumberTextField = new javax.swing.JTextField();
        capacityLabel = new javax.swing.JLabel();
        capacityTextField = new javax.swing.JTextField();
        stationNameLabel = new javax.swing.JLabel();
        stationNameTextField = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        inputTrainNumberLabel = new javax.swing.JLabel();
        inputTrainNumberTextField = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        passengersTable = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        showTrainsButton = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        trainsTable = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        fromLabel = new javax.swing.JLabel();
        fromTextField = new javax.swing.JTextField();
        toLabel = new javax.swing.JLabel();
        toTextField = new javax.swing.JTextField();
        intervalLabel = new javax.swing.JLabel();
        fromTimeSpinner = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        toTimeSpinner = new javax.swing.JSpinner();
        saleTicketTrainsTable = new javax.swing.JScrollPane();
        saleTrainsTable = new javax.swing.JTable();
        firstNameLabel = new javax.swing.JLabel();
        firstNameTextField = new javax.swing.JTextField();
        secondNameLabel = new javax.swing.JLabel();
        secondNameTextField = new javax.swing.JTextField();
        dateOfBirthLabel = new javax.swing.JLabel();
        dateOfBirthSpinner = new javax.swing.JSpinner();
        registerButton = new javax.swing.JButton();
        saleShowButton = new javax.swing.JButton();
        passengersTableModel = new PassengersTableModel();
        trainsTableModel = new TrainsTableModel();
        saleTrainsTableModel = new TrainsTableModel();

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

        saveButton.setText("Сохранить");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (addTrainRadioButton.isSelected()) {
                    if (dataForUpdate.getNumber() <= 0 || dataForUpdate.getTicketsAmount() <= 0) {
                        JOptionPane.showMessageDialog(null, "Некорректно введена информация о добавляемом поезде");
                        return;
                    }

                    dataForUpdateList.add(dataForUpdate);
                    request.setService(Constants.ClientService.addTrain);
                } else if (addStationRadioButton.isSelected()) {
                    if (dataForUpdate.getFromStation() == null || dataForUpdate.getFromStation().equals("")) {
                        JOptionPane.showMessageDialog(null, "Некорректно введена информация о добавляемй станции");
                        return;
                    }

                    dataForUpdateList.add(dataForUpdate);
                    request.setService(Constants.ClientService.addStation);
                } else if (addRouteRadioButton.isSelected()) {
                    request.setService(Constants.ClientService.addRoute);
                    try {
                        routeInfoHandler(routeInfo);
                    } catch (EntityUpdateException ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    }
                } else JOptionPane.showMessageDialog(null, "Не выбрана ни одна из опций");

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
                int trainNumber = 0;

                try {
                    trainNumber = Integer.parseInt(addTrainNumberTextField.getText());
                } catch (NumberFormatException ex) {
                    trainNumber = 0;
                }

                dataForUpdate.setNumber(trainNumber);
            }
        });

        capacityLabel.setText("Вместимость:");
        capacityTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                int capacity = 0;

                try {
                    capacity = Integer.parseInt(capacityTextField.getText());
                } catch (NumberFormatException ex) {
                    capacity = 0;
                }

                dataForUpdate.setTicketsAmount(capacity);
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                                .addComponent(addRouteRadioButton)
                                                .addGap(0, 461, Short.MAX_VALUE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                                                .addComponent(addTrainRadioButton)
                                                                .addGap(55, 55, 55)
                                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(trainNumberLabel)
                                                                        .addComponent(capacityLabel))
                                                                .addGap(40, 40, 40)
                                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(capacityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(addTrainNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                .addComponent(saveButton)
                                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                                        .addComponent(addStationRadioButton)
                                                                        .addGap(41, 41, 41)
                                                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                                                        .addComponent(stationNameLabel)
                                                                                        .addGap(18, 18, 18)
                                                                                        .addComponent(stationNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(addTrainRadioButton)
                                        .addComponent(trainNumberLabel)
                                        .addComponent(addTrainNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(capacityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(capacityLabel))
                                .addGap(28, 28, 28)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(addStationRadioButton)
                                        .addComponent(stationNameLabel)
                                        .addComponent(stationNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(27, 27, 27)
                                                .addComponent(addRouteRadioButton))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(37, 37, 37)
                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(50, 50, 50)
                                .addComponent(saveButton)
                                .addContainerGap(87, Short.MAX_VALUE))
        );

        tabbedPane.addTab("Добавить новые сущности", jPanel1);

        inputTrainNumberLabel.setText("Введите номер поезда:");

        inputTrainNumberTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int trainNumber = 0;

                try {
                    trainNumber = Integer.parseInt(inputTrainNumberTextField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Некорректно введен номер поезда");
                    return;
                }
                dataForUpdate.setNumber(trainNumber);
                dataForUpdateList.add(dataForUpdate);

                request.setService(Constants.ClientService.viewPassangers);
                request.setObject(dataForUpdateList);

                ResponseDTO responce = ClientConnectionManager.connect(request);

                if (responce.getStatus() == Constants.StatusOfExecutedService.success) {
                    passengersTableModel.setPassengers((List<PassengerDTO>) responce.getObject());
                    passengersTableModel.fireTableDataChanged();
                } else if (responce.getStatus() == Constants.StatusOfExecutedService.error) {
                    passengersTableModel.setPassengers(new ArrayList<PassengerDTO>());
                    passengersTableModel.fireTableDataChanged();
                    JOptionPane.showMessageDialog(null, "На указанный поезд пассажиров нет");
                }

            }
        });


        passengersTable.setModel(passengersTableModel);
        jScrollPane2.setViewportView(passengersTable);

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
                                                .addComponent(inputTrainNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(inputTrainNumberLabel)
                                        .addComponent(inputTrainNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                                .addGap(70, 70, 70))
        );

        tabbedPane.addTab("Просмотр пассажиров", jPanel2);

        showTrainsButton.setText("Показать поезда");
        showTrainsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ResponseDTO responce = ClientConnectionManager.connect(new RequestDTO(Constants.ClientService.viewTrains,
                        null));
                if (responce.getStatus() == Constants.StatusOfExecutedService.success) {
                    trainsTableModel.setTrains((ArrayList<ScheduleDTO>) responce.getObject());
                    trainsTableModel.fireTableDataChanged();
                } else if (responce.getStatus() == Constants.StatusOfExecutedService.error) {
                    JOptionPane.showMessageDialog(null, (String) responce.getObject());
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
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(showTrainsButton)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(54, Short.MAX_VALUE))
        );

        tabbedPane.addTab("Просмотр поездов", jPanel3);

        fromLabel.setText("От:");

        fromTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                dataForUpdate.setFromStation(fromTextField.getText());
            }
        });

        toLabel.setText("До:");

        toTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                dataForUpdate.setToStation(toTextField.getText());
            }
        });

        intervalLabel.setText("Интервал:");

        jLabel1.setText(":");

        setSpinnerModels();
        fromTimeSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                dataForUpdate.setDepartureTime(new Time(((Date) fromTimeSpinner.getValue()).getTime()));
            }
        });
        toTimeSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                dataForUpdate.setArrivalTime((new Time(((Date) toTimeSpinner.getValue()).getTime())));
            }
        });

        saleTrainsTable.setModel(saleTrainsTableModel);
        saleTrainsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    Point p = e.getPoint();
                    rowNumber = saleTrainsTable.rowAtPoint(p);
                    ListSelectionModel model = saleTrainsTable.getSelectionModel();
                    model.setSelectionInterval(rowNumber, rowNumber);
                }
            }
        });
        saleTicketTrainsTable.setViewportView(saleTrainsTable);

        saleShowButton.setText("Показать ");
        saleShowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (dataForUpdate.getFromStation() == null || dataForUpdate.getFromStation().equals("")) {
                    JOptionPane.showMessageDialog(null, "Некорректно введена станция отправления. Повторите ввод.");
                    return;
                } else if (dataForUpdate.getToStation() == null || dataForUpdate.getToStation().equals("")) {
                    JOptionPane.showMessageDialog(null, "Некорректно введена станция прибытия. Повторите ввод.");
                    return;
                } else if (dataForUpdate.getDepartureTime().after(dataForUpdate.getArrivalTime())) {
                    JOptionPane.showMessageDialog(null, "Некорректно введен временной интервал. Повторите ввод.");
                    return;
                }

                request.setService(Constants.ClientService.getScheduleFromAtoB);
                request.setObject(dataForUpdate);

                ResponseDTO responce = ClientConnectionManager.connect(request);

                if (responce.getStatus() == Constants.StatusOfExecutedService.success) {
                    saleTrainsTableModel.setTrains((ArrayList<ScheduleDTO>) responce.getObject());
                    saleTrainsTableModel.fireTableDataChanged();
                }
            }
        });

        firstNameLabel.setText("Имя:");

        firstNameTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                order.setFirstName(firstNameTextField.getText());
            }
        });

        secondNameLabel.setText("Фамилия:");

        secondNameTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                order.setSecondName(secondNameTextField.getText());
            }
        });

        dateOfBirthLabel.setText("Дата рождения:");

        dateOfBirthSpinner.setModel(new SpinnerDateModel(new Date(POINT_OF_REFERENCE), null, null, Calendar.DAY_OF_MONTH));
        dateOfBirthSpinner.setEditor(new JSpinner.DateEditor(dateOfBirthSpinner,"dd.MM.yyyy"));
        dateOfBirthSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                order.setDateOfBirth(new java.sql.Date(((java.util.Date) dateOfBirthSpinner.getValue()).getTime()));
            }
        });

        registerButton.setText("Зарегистрировать");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<ScheduleDTO> schedule = saleTrainsTableModel.getTrains();

                request.setService(Constants.ClientService.buyTicket);

                if (!schedule.isEmpty()) {
                    order.setTrainNumber(schedule.get(rowNumber).getNumber());
                    order.setFromStation(schedule.get(rowNumber).getFromStation());
                }

                if (order.getFirstName() == null || order.getFirstName() == "") {
                    JOptionPane.showMessageDialog(null, "Некорректно введено имя. Повторите ввод.");
                    return;
                } else if (order.getSecondName() == null || order.getSecondName() == "") {
                    JOptionPane.showMessageDialog(null, "Некорректно введена фамилия. Повторите ввод.");
                    return;
                }

                request.setObject(order);

                ResponseDTO response = ClientConnectionManager.connect(request);

                if (response.getStatus() == Constants.StatusOfExecutedService.success) {
                    JOptionPane.showMessageDialog(null, "Пассажир успешно зарегистрирован");
                } else if (response.getStatus() == Constants.StatusOfExecutedService.error) {
                    JOptionPane.showMessageDialog(null, ((Exception)response.getObject()).getMessage());
                }

                clearSaleWidgetsContent();
            }
        });


        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(saleTicketTrainsTable)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(fromLabel)
                                        .addGap(18, 18, 18)
                                        .addComponent(fromTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(toLabel)
                                        .addGap(18, 18, 18)
                                        .addComponent(toTextField)))
                                .addGap(37, 37, 37)
                                .addComponent(intervalLabel)
                                .addGap(18, 18, 18)
                                .addComponent(fromTimeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(toTimeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                        .addComponent(firstNameLabel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(firstNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                        .addComponent(secondNameLabel)
                                        .addGap(18, 18, 18)
                                        .addComponent(secondNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(42, 42, 42)
                                .addComponent(dateOfBirthLabel)
                                .addGap(18, 18, 18)
                                .addComponent(dateOfBirthSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(saleShowButton)
                            .addComponent(registerButton))
                        .addGap(0, 106, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fromLabel)
                    .addComponent(fromTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(intervalLabel)
                    .addComponent(fromTimeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(toTimeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(toLabel)
                    .addComponent(toTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(saleShowButton)
                .addGap(37, 37, 37)
                .addComponent(saleTicketTrainsTable, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(firstNameLabel)
                    .addComponent(firstNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dateOfBirthLabel)
                    .addComponent(dateOfBirthSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(secondNameLabel)
                    .addComponent(secondNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(registerButton)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        tabbedPane.addTab("Продажа билета", jPanel4);

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
     * @throws EntityUpdateException - exception of this type about incorrect entered route information.
     */
    private void routeInfoHandler(String info) throws EntityUpdateException {
        String[] arrOfStations = null;
        DateFormat formatter = new SimpleDateFormat("HH:mm");
        Date tmpDateDep = null;
        Date tmpDateArr = null;
        int trainNumber = 0;

        if (info == null) {
            throw new EntityUpdateException("Некорректно введен маршрут");
        }

        try {
            arrOfStations = info.split("\n");
        } catch (PatternSyntaxException e) {
            throw new EntityUpdateException("Некорректно введен маршрут");
        }


        //Try to extract train number from administrator's input
        try {
            trainNumber = Integer.parseInt(arrOfStations[0]);
        } catch (NumberFormatException e) {
            throw new EntityUpdateException("Некорректно введен номер поезда");
        }

        if (arrOfStations.length < 3) throw new EntityUpdateException("Число станций в маршруте должно быть не " +
                                                                      "меньше двух");

        for (int i = 1; i < arrOfStations.length; i++) {
            String[] stationInfo = null;

            try {
                stationInfo = arrOfStations[i].split("_");
            } catch (PatternSyntaxException ex) {
                throw new EntityUpdateException("Некорректно введена информация по станции");
            }

            ScheduleDTO stationData = new ScheduleDTO();
            stationData.setFromStation(stationInfo[0]);

            if ((i == 1 || i == arrOfStations.length-1) && stationInfo.length != 2) {
                throw new EntityUpdateException("Некорректно введена информация по станции " + stationInfo[0]);
            } else if ((i > i && i < arrOfStations.length-1) && stationInfo.length != 3) {
                throw new EntityUpdateException("Некорректно введена информация по станции " + stationInfo[0]);
            }

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
                throw new EntityUpdateException("Некорректно введено время");
            }

            if (i == 1) stationData.setArrivalTime(null);
            else stationData.setArrivalTime(new Time(tmpDateArr.getTime()));

            if (i == arrOfStations.length-1) stationData.setDepartureTime(null);
            else stationData.setDepartureTime(new Time(tmpDateDep.getTime()));

            dataForUpdateList.add(stationData);
        }

        dataForUpdateList.get(0).setNumber(trainNumber);
    }

    private void setSpinnerModels() {
        Date date = new Date(POINT_OF_REFERENCE);

        SpinnerDateModel sm1 = new SpinnerDateModel(date, null, null, Calendar.MINUTE);
        SpinnerDateModel sm2 = new SpinnerDateModel(date, null, null, Calendar.MINUTE);

        fromTimeSpinner.setModel(sm1);
        toTimeSpinner.setModel(sm2);
        fromTimeSpinner.setEditor(new JSpinner.DateEditor(fromTimeSpinner, "HH:mm"));
        toTimeSpinner.setEditor(new JSpinner.DateEditor(toTimeSpinner, "HH:mm"));
    }

    private void clearSaleWidgetsContent() {
        firstNameTextField.setText("");
        secondNameTextField.setText("");
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
    private javax.swing.JLabel capacityLabel;
    private javax.swing.JTextField capacityTextField;
    private javax.swing.JLabel dateOfBirthLabel;
    private javax.swing.JSpinner dateOfBirthSpinner;
    private javax.swing.JLabel firstNameLabel;
    private javax.swing.JTextField firstNameTextField;
    private javax.swing.JLabel fromLabel;
    private javax.swing.JTextField fromTextField;
    private javax.swing.JSpinner fromTimeSpinner;
    private javax.swing.JTextArea inputTextArea;
    private javax.swing.JLabel inputTrainNumberLabel;
    private javax.swing.JLabel intervalLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable passengersTable;
    private javax.swing.JTable trainsTable;
    private javax.swing.JTable saleTrainsTable;
    private javax.swing.ButtonGroup optionsButtonGroup;
    private javax.swing.JButton registerButton;
    private javax.swing.JButton saleShowButton;
    private javax.swing.JScrollPane saleTicketTrainsTable;
    private javax.swing.JButton saveButton;
    private javax.swing.JLabel secondNameLabel;
    private javax.swing.JTextField secondNameTextField;
    private javax.swing.JButton showTrainsButton;
    private javax.swing.JLabel stationNameLabel;
    private javax.swing.JTextField stationNameTextField;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JLabel toLabel;
    private javax.swing.JTextField toTextField;
    private javax.swing.JSpinner toTimeSpinner;
    private javax.swing.JLabel trainNumberLabel;
    private javax.swing.JTextField inputTrainNumberTextField;

    private PassengersTableModel passengersTableModel;
    private TrainsTableModel trainsTableModel;
    private TrainsTableModel saleTrainsTableModel;

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
                                                    //what option is selected. Or when we need receive schedule
                                                    //to sale ticket.

    private String routeInfo;                       //This string contains information about route entered by admin
    //in appropriate text pane.

    private RequestDTO request;

    private JFrame frame;                           //Frame in which this instance of panel is embedded.

    private int rowNumber;                          //Row, selected in trainsTable

    private OrderDTO order;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(650, 520);
                frame.getContentPane().add(new AdministratorGUIPanel(frame));
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }


}

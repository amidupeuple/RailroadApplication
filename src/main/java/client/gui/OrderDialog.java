package client.gui;

import client.ClientConnectionManager;
import dto.OrderDTO;
import dto.RequestDTO;
import dto.ResponseDTO;
import dto.ScheduleDTO;
import org.apache.log4j.Logger;
import protocol.Constants;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static protocol.Constants.POINT_OF_REFERENCE;

public class OrderDialog extends javax.swing.JDialog {

    private static final Logger log = Logger.getLogger(OrderDialog.class);

    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel dateOfBirthLabel;
    private javax.swing.JSpinner dateOfBirthSpinner;
    private javax.swing.JTextField firstNameTextField;
    private javax.swing.JTextPane infoTextPane;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton makeOrderButton;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JTextField secondNameTextField;

    ArrayList<ScheduleDTO> scheduleList;    //List of schedule objects from trainsTable
    int row;                                //Chosen row in trainsTable ~ number of element in list

    SimpleDateFormat dateFormatter;         //Formatter for represent time in text field

    OrderDTO orderFromUser;

    RequestDTO request;

    ResponseDTO response;


    public OrderDialog(java.awt.Frame parent, boolean modal, ArrayList<ScheduleDTO> s, int r) {
        super(parent, modal);
        scheduleList = s;
        row = r;
        orderFromUser = new OrderDTO();
        request = new RequestDTO();
        initComponents();
    }

    private void initComponents() {
        this.setTitle("Заказ билета");

        nameLabel = new javax.swing.JLabel();
        firstNameTextField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        secondNameTextField = new javax.swing.JTextField();
        dateOfBirthLabel = new javax.swing.JLabel();
        dateOfBirthSpinner = new javax.swing.JSpinner();
        makeOrderButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        infoTextPane = new javax.swing.JTextPane();

        dateFormatter = new SimpleDateFormat("HH:mm");

        infoTextPane.setEditable(false);
        if (!scheduleList.isEmpty()) {
            infoTextPane.setText("Заказ билета\n" +
                             "От: " + scheduleList.get(row).getFromStation() + "\n" +
                             "До: " + scheduleList.get(row).getToStation() + "\n" +
                             "Номер поезда: " + scheduleList.get(row).getNumber() + "\n" +
                             "Отправление: " + dateFormatter.format(scheduleList.get(row).getDepartureTime()) + "\n" +
                             "Прибытие: " + dateFormatter.format(scheduleList.get(row).getArrivalTime()));
        }

        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        nameLabel.setText("Имя:");

        firstNameTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                orderFromUser.setFirstName(firstNameTextField.getText());
            }
        });

        jLabel1.setText("Фамилия:");

        secondNameTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                orderFromUser.setSecondName(secondNameTextField.getText());
            }
        });

        dateOfBirthLabel.setText("Дата рождения:");

        dateOfBirthSpinner.setModel(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH));
        dateOfBirthSpinner.setEditor(new JSpinner.DateEditor(dateOfBirthSpinner,"dd.MM.yyyy"));
        dateOfBirthSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                orderFromUser.setDateOfBirth(new java.sql.Date(((java.util.Date) dateOfBirthSpinner.getValue()).getTime()));
            }
        });

        makeOrderButton.setText("Оформить заказ");
        makeOrderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                makeOrderButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Закрыть");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setVisible(false);
            }
        });

        jScrollPane1.setViewportView(infoTextPane);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dateOfBirthLabel)
                            .addComponent(jLabel1)
                            .addComponent(nameLabel))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(secondNameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                            .addComponent(firstNameTextField)
                            .addComponent(dateOfBirthSpinner, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(makeOrderButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                        .addComponent(cancelButton)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameLabel)
                    .addComponent(firstNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(secondNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dateOfBirthLabel)
                    .addComponent(dateOfBirthSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(makeOrderButton)
                    .addComponent(cancelButton))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }

    private void makeOrderButtonActionPerformed(java.awt.event.ActionEvent evt) {
        request.setService(Constants.ClientService.buyTicket);

        if (orderFromUser.getFirstName() == null || orderFromUser.getFirstName().equals("")) {
            JOptionPane.showMessageDialog(null, "Некорректно введено имя. Повторите ввод");
            return;
        } else if (orderFromUser.getSecondName() == null || orderFromUser.getSecondName().equals("")) {
            JOptionPane.showMessageDialog(null, "Некорректно введена фамилия. Повторите ввод");
            return;
        } else if (orderFromUser.getDateOfBirth() == null) {
            JOptionPane.showMessageDialog(null, "Некорректно введена дата рождения. Повторите ввод");
            return;
        }

        orderFromUser.setTrainNumber(scheduleList.get(row).getNumber());
        orderFromUser.setFromStation(scheduleList.get(row).getFromStation());

        request.setObject(orderFromUser);

        log.debug("Request object is prepared -> send data to server.");

        response = ClientConnectionManager.connect(request);

        log.debug("Response object from server is obtained.");

        if (response.getStatus() == Constants.StatusOfExecutedService.success)
            infoTextPane.setText(((String) response.getObject()));
        else if (response.getStatus() == Constants.StatusOfExecutedService.error)
            infoTextPane.setText(((Exception) response.getObject()).getMessage());

    }

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cancelButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                OrderDialog dialog = new OrderDialog(null, true, new ArrayList<ScheduleDTO>(), 0);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

}

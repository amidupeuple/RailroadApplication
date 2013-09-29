package server.services;

import dao.PassengerDAO;
import dao.TicketDAO;
import dao.TrainDAO;
import dto.RequestDTO;
import dto.ResponseDTO;
import dto.ScheduleDTO;
import org.apache.log4j.Logger;
import protocol.Constants;
import server.exceptions.EntityUpdateException;

import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: danya_000
 * Date: 9/29/13
 * Time: 1:06 AM
 * To change this template use File | Settings | File Templates.
 */
public class TrainService {
    private static final Logger log = Logger.getLogger(TicketService.class);

    private EntityManagerFactory entityManagerFactory;

    public TrainService(EntityManagerFactory factory) {
        entityManagerFactory = factory;
    }

    public ResponseDTO addTrain(RequestDTO reqObj) {
        log.debug("addTrain() start");
        TrainDAO train = new TrainDAO(entityManagerFactory);

        List<ScheduleDTO> trainInfo = (List<ScheduleDTO>) reqObj.getObject();

        try {
            train.addTrain(trainInfo.get(0).getNumber(), trainInfo.get(0).getTicketsAmount());
        } catch (EntityUpdateException e) {
            return new ResponseDTO(Constants.StatusOfExecutedService.error, e.getMessage());
        }

        log.debug("addTrain() finish");
        return new ResponseDTO(Constants.StatusOfExecutedService.success, "Новый поезд упешно добавлен");
    }

    public ResponseDTO viewAllTrains() {
        log.debug("viewAllTrains() start");
        TrainDAO train = new TrainDAO(entityManagerFactory);

        ArrayList<ScheduleDTO> trains = train.getAllTrains();

        log.debug("viewAllTrains() finish");
        return new ResponseDTO(Constants.StatusOfExecutedService.success, trains);
    }
}

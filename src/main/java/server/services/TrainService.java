package server.services;

import dao.TrainDAO;
import dto.RequestDTO;
import dto.ResponseDTO;
import dto.ScheduleDTO;
import org.apache.log4j.Logger;
import common.Constants;
import server.exceptions.EntityUpdateException;

import java.util.ArrayList;
import java.util.List;

/**
 * Services that interact with TrainDAO.
 */
public class TrainService {
    private static final Logger log = Logger.getLogger(TicketService.class);

    public static ResponseDTO addTrain(RequestDTO reqObj, TrainDAO trainDAO) {
        log.debug("addTrain() start");

        List<ScheduleDTO> trainInfo = (List<ScheduleDTO>) reqObj.getObject();

        try {
            trainDAO.addTrain(trainInfo.get(0).getNumber(), trainInfo.get(0).getTicketsAmount());
        } catch (EntityUpdateException e) {
            log.warn("Exception: " + e.getMessage());
            return new ResponseDTO(Constants.StatusOfExecutedService.error, e.getMessage());
        }

        log.debug("addTrain() finish");
        return new ResponseDTO(Constants.StatusOfExecutedService.success, "Новый поезд упешно добавлен");
    }

    public static ResponseDTO viewAllTrains(TrainDAO trainDAO) {
        log.debug("viewAllTrains() start");

        ArrayList<ScheduleDTO> trains = trainDAO.getAllTrains();

        log.debug("viewAllTrains() finish");
        return new ResponseDTO(Constants.StatusOfExecutedService.success, trains);
    }
}

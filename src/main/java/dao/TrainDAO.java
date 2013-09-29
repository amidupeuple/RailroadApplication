package dao;

import dto.ScheduleDTO;
import entity.Train;
import server.exceptions.AddTrainException;
import server.exceptions.EntityUpdateException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: danya_000
 * Date: 9/24/13
 * Time: 1:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class TrainDAO {
    private EntityManagerFactory entityManagerFactory;

    public TrainDAO(EntityManagerFactory factory) {
        entityManagerFactory = factory;
    }

    /**
     * Get amount of vacancies in train by train number.
     * @param numb - number of desired train.
     * @return amount of vacancies int train.
     */
    public int getVacanciesInTrain(int numb) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        List<Integer> vacancies = entityManager.createQuery(
                "select t.vacancies from Train t " +
                "where t.number = ?1"
        ).setParameter(1, numb)
         .getResultList();

        entityManager.getTransaction().commit();

        return vacancies.get(0);
    }

    public Train getTrain(int number) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        List<Train> buf = entityManager.createQuery(
                "select t from Train t " +
                        "where t.number = ?1"
        ).setParameter(1, number).getResultList();

        entityManager.getTransaction().commit();

        return buf.get(0);
    }

    public void decrementVacancies(int trainNumber) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        Query decrementVacanciesQuery = entityManager.createQuery(
                "update Train t " +
                "set t.vacancies = (t.vacancies - 1) " +
                "where t.number = ?1").setParameter(1, trainNumber);

        decrementVacanciesQuery.executeUpdate();

        entityManager.getTransaction().commit();
    }

    public void addTrain(int number, int vacancies) throws EntityUpdateException{
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        Train newTrain = new Train(number, vacancies);

        entityManager.persist(newTrain);
        try {
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            throw new AddTrainException("Ошибка! Такой поезд уже существует.");
        }
    }

    public ArrayList<ScheduleDTO> getAllTrains() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        ArrayList<ScheduleDTO> trainsList = new ArrayList<ScheduleDTO>();

        List<Object[]> bufTrainsInRoutes = entityManager.createQuery(
                "select tr.number, sir1.station.name, sir2.station.name, sir1.departureTime," +
                "       sir2.arrivalTime, tr.vacancies " +
                "from Train tr, StationInRoute sir1, StationInRoute sir2  " +
                "where tr.id = sir1.train.id and tr.id = sir2.train.id and " +
                "      sir1.departureTime = (select min(sir3.departureTime) " +
                "                            from StationInRoute sir3 " +
                "                            where sir3.route.id = sir1.route.id) and " +
                "      sir2.arrivalTime = (select max(sir4.arrivalTime) " +
                "                          from StationInRoute sir4 " +
                "                          where sir4.route.id = sir2.route.id)"
        ).getResultList();

        List<Train> bufTrainsNotInRoutes = entityManager.createQuery(
                "select t from Train t " +
                "where t.id not in (select distinct s.train.id from StationInRoute s)"
        ).getResultList();

        entityManager.getTransaction().commit();

        for (int i = 0; i < bufTrainsInRoutes.size(); i++) {
            Object[] trainInfo = bufTrainsInRoutes.get(i);
            ScheduleDTO train = new ScheduleDTO((Integer) trainInfo[0],
                                                (String) trainInfo[1],
                                                (String) trainInfo[2],
                                                (Time) trainInfo[3],
                                                (Time) trainInfo[4],
                                                (Integer) trainInfo[5]);
            trainsList.add(train);
        }

        for (int i = 0; i < bufTrainsNotInRoutes.size(); i++) {
            Train trainInfo = bufTrainsNotInRoutes.get(i);
            ScheduleDTO train = new ScheduleDTO();
            train.setNumber(trainInfo.getNumber());
            train.setTicketsAmount(trainInfo.getVacancies());
            trainsList.add(train);
        }

        return trainsList;
    }

}

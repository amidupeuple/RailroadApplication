package dao;

import entity.Train;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
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

}

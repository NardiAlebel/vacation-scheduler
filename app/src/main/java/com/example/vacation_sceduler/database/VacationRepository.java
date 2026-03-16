package com.example.vacation_sceduler.database;

import android.app.Application;

import com.example.vacation_sceduler.dao.ExcursionDao;
import com.example.vacation_sceduler.dao.VacationDao;
import com.example.vacation_sceduler.entities.Excursion;
import com.example.vacation_sceduler.entities.Vacation;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VacationRepository {

    private VacationDao vacationDao;
    private ExcursionDao excursionDao;
    private List<Vacation> allVacations;
    private List<Excursion> allExcursions;

    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public VacationRepository(Application application) {
        VacationDatabase db = VacationDatabase.getDatabase(application);
        vacationDao = db.vacationDao();
        excursionDao = db.excursionDao();
    }

    public List<Vacation> getAllVacations() {
        databaseExecutor.execute(() -> {
            allVacations = vacationDao.getAllVacations();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return allVacations;
    }

    public List<Vacation> searchVacations(String query) {
        databaseExecutor.execute(() -> {
            allVacations = vacationDao.searchVacations(query);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return allVacations;
    }

    public void insert(Vacation vacation) {
        databaseExecutor.execute(() -> {
            vacationDao.insert(vacation);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update(Vacation vacation) {
        databaseExecutor.execute(() -> {
            vacationDao.update(vacation);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void delete(Vacation vacation) {
        databaseExecutor.execute(() -> {
            vacationDao.delete(vacation);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public List<Excursion> getExcursionsForVacation(int vacationId) {
        databaseExecutor.execute(() -> {
            allExcursions = excursionDao.getExcursionsForVacation(vacationId);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return allExcursions;
    }

    public int getExcursionCountForVacation(int vacationId) {
        final int[] count = new int[1];
        databaseExecutor.execute(() -> {
            count[0] = excursionDao.getExcursionCountForVacation(vacationId);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return count[0];
    }

    public void insert(Excursion excursion) {
        databaseExecutor.execute(() -> {
            excursionDao.insert(excursion);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update(Excursion excursion) {
        databaseExecutor.execute(() -> {
            excursionDao.update(excursion);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void delete(Excursion excursion) {
        databaseExecutor.execute(() -> {
            excursionDao.delete(excursion);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

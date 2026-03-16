package com.example.vacation_sceduler.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.vacation_sceduler.entities.Excursion;

import java.util.List;

@Dao
public interface ExcursionDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Excursion excursion);

    @Update
    void update(Excursion excursion);

    @Delete
    void delete(Excursion excursion);

    @Query("SELECT * FROM excursions WHERE vacationId = :vacationId ORDER BY id ASC")
    List<Excursion> getExcursionsForVacation(int vacationId);

    @Query("SELECT COUNT(*) FROM excursions WHERE vacationId = :vacationId")
    int getExcursionCountForVacation(int vacationId);

    @Query("SELECT * FROM excursions WHERE id = :id")
    Excursion getExcursionById(int id);
}

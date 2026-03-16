package com.example.vacation_sceduler.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.vacation_sceduler.entities.Vacation;

import java.util.List;

@Dao
public interface VacationDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Vacation vacation);

    @Update
    void update(Vacation vacation);

    @Delete
    void delete(Vacation vacation);

    @Query("SELECT * FROM vacations ORDER BY id ASC")
    List<Vacation> getAllVacations();

    @Query("SELECT * FROM vacations WHERE id = :id")
    Vacation getVacationById(int id);
}

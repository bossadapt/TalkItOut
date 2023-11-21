package com.example.talkitout;

import java.util.List;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
@Dao
public interface ItemListDao {
    @Query("SELECT * FROM presets")
    List<Preset> getAll();

    @Query("SELECT * FROM presets WHERE name LIKE :name LIMIT 1")
    Preset findByName(String name);

    @Insert
    void insert(Preset preset);

    @Delete
    void delete(Preset preset);
}

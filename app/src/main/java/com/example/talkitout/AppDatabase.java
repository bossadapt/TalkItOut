package com.example.talkitout;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
@Database(entities = {Preset.class}, version = 1)
@TypeConverters({DataConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract ItemListDao itemListDao();

}

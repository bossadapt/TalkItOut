package com.example.talkitout;
import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class DataConverter {
    @TypeConverter
    public String fromItemList(List<String> itemsString) {
        if (itemsString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {}.getType();
        String json = gson.toJson(itemsString, type);
        return json;
    }

    @TypeConverter
    public List<String> toItemList(String optionsString) {
        if (optionsString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {}.getType();
        return gson.fromJson(optionsString, type);
    }
}
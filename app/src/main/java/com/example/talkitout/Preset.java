package com.example.talkitout;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "presets")
public class Preset implements Parcelable {
    public void setName(@NonNull String name) {
        this.name = name;
    }
    public void setItems(List<String> items){
        this.items = items;
    }

    @PrimaryKey
    public @NonNull String name;
    @ColumnInfo(name = "items")
    public List<String> items;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeStringList(this.items);
    }

    public void readFromParcel(Parcel source) {
        this.name = source.readString();
        this.items = source.createStringArrayList();
    }

    public Preset() {
    }

    protected Preset(Parcel in) {
        this.name = in.readString();
        this.items = in.createStringArrayList();
    }

    public static final Parcelable.Creator<Preset> CREATOR = new Parcelable.Creator<Preset>() {
        @Override
        public Preset createFromParcel(Parcel source) {
            return new Preset(source);
        }

        @Override
        public Preset[] newArray(int size) {
            return new Preset[size];
        }
    };
}

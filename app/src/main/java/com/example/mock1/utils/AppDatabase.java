package com.example.mock1.utils;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mock1.dao.PhuongTienDao;
import com.example.mock1.entity.PhuongTien;

@Database(entities = {PhuongTien.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase appDatabase;
    public static AppDatabase getInstance(Context context){
        if(appDatabase == null){
            appDatabase = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "ThongTinPhuongTien"
            ).allowMainThreadQueries().build();
        }
        return appDatabase;
    }
    public abstract PhuongTienDao phuongTienDao();
}

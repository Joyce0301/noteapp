package com.example.noteapp;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Note.class}, version = 2, exportSchema = false)
public abstract class NoteDatabase extends RoomDatabase {

    private static NoteDatabase INSTANCE;

    public abstract NoteDao noteDao();

    public static synchronized NoteDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            NoteDatabase.class,
                            "note_db"
                    )
                    .allowMainThreadQueries()
                    // 版本升级时回退重建数据库（简单方案，生产环境建议用 migration）
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}

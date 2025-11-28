package com.example.noteapp; // 换成你的包名

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Note.class}, version = 1, exportSchema = false)
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
                    // 考试图省事，允许主线程访问（正式项目一般用子线程）
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
}


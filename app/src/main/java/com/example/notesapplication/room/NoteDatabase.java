package com.example.notesapplication.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {NoteItem.class}, version = 1)
@TypeConverters(DateConverter.class)
public abstract class NoteDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "NotesDatabase.db";

    private static NoteDatabase instance;

    public abstract NoteDao noteDao();

    public static synchronized NoteDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    NoteDatabase.class,
                    DATABASE_NAME).fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}

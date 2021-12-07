package com.example.notesapplication;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.notesapplication.room.NoteDao;
import com.example.notesapplication.room.NoteDatabase;
import com.example.notesapplication.room.NoteItem;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {

    public static final String TAG = "test";
    private NoteDatabase mDb;
    private NoteDao mDao;
    private CompositeDisposable mDisposable;
    private ArrayList<NoteItem> list;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        mDb = Room.inMemoryDatabaseBuilder(context, NoteDatabase.class).build();
        mDao = mDb.noteDao();
        mDisposable = new CompositeDisposable();
        list = new ArrayList<>();
        list.add(new NoteItem("title1", "subshtenie 1 kuso"));
        list.add(new NoteItem("title2", "subshtenie 2 dulgo subshtenie 2 dulgo subshtenie 2 dulgo subshtenie 2 dulgo subshtenie 2 dulgo subshtenie 2 dulgo subshtenie 2 dulgo subshtenie 2 dulgo subshtenie 2 dulgo subshtenie 2 dulgo subshtenie 2 dulgo subshtenie 2 dulgo subshtenie 2 dulgo subshtenie 2 dulgo subshtenie 2 dulgo "));
        Log.i(TAG, "createDb");
    }

    @After
    public void closeDb() {
        mDb.close();
        mDisposable.clear();
        mDisposable = null;
        Log.i(TAG, "closeDb");
    }

    @Test
    public void createAndRetrieveNotes() {
        mDisposable.add(mDao.insertOrUpdateList(list).subscribe());
        mDisposable.add(mDao.getCount().subscribe(integer -> assertEquals(list.size(), integer.intValue())));
    }

    @Test
    public void compareStrings() {
        mDisposable.add(mDao.insertOrUpdateList(list).subscribe());
        NoteItem note = list.get(0);
        mDisposable.add(mDao.getNoteById(1).subscribe(fromDb -> {
            assertEquals(note.getMessage(), fromDb.getMessage());
            assertEquals(1, fromDb.getId());
        }));
    }
}

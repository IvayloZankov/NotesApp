package com.example.notesapplication.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertOrUpdate(NoteItem noteItem);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertOrUpdateList(List<NoteItem> notes);

    @Update
    Completable update(NoteItem noteItem);

    @Delete
    Completable delete(NoteItem noteItem);

    @Query("SELECT * FROM notes_table ORDER BY date DESC")
    Flowable<List<NoteItem>> getAllNotes();

    @Query("SELECT COUNT(*) FROM notes_table")
    Single<Integer> getCount();

    @Query("SELECT * FROM notes_table WHERE id = :id")
    Single<NoteItem> getNoteById(int id);
}

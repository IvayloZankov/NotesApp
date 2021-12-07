package com.example.notesapplication.room;

import android.app.Application;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class NoteRepo {

    private final NoteDao mNoteDao;
    private final Flowable<List<NoteItem>> mNotesFlowable;

    public NoteRepo(Application application) {
        NoteDatabase database = NoteDatabase.getInstance(application);
        this.mNoteDao = database.noteDao();
        this.mNotesFlowable = mNoteDao.getAllNotes();
    }

    public Completable insertOrUpdate(NoteItem noteItem) {
        return mNoteDao.insertOrUpdate(noteItem);
    }

    public Completable delete(NoteItem noteItem) {
        return mNoteDao.delete(noteItem);
    }

    public Flowable<List<NoteItem>> getAllNotes() {
        return mNotesFlowable;
    }

    public Single<Integer> getNotesCount() {
        return mNoteDao.getCount();
    }

    public Single<NoteItem> getNoteById(int mNoteId) {
        return mNoteDao.getNoteById(mNoteId);
    }
}

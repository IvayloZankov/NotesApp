package com.example.notesapplication.list;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;

import com.example.notesapplication.room.NoteItem;
import com.example.notesapplication.room.NoteRepo;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ListViewModel extends AndroidViewModel {

    private final NoteRepo mRepo;
    private final CompositeDisposable mDisposable;

    public ListViewModel(@NonNull Application application) {
        super(application);
        mRepo = new NoteRepo(application);
        mDisposable = new CompositeDisposable();
    }

    public void deleteNote(NoteItem noteItem) {
        mDisposable.add(mRepo.delete(noteItem).subscribeOn(Schedulers.io()).subscribe());
    }

    public LiveData<List<NoteItem>> getAllNotes() {
        return LiveDataReactiveStreams.fromPublisher(mRepo.getAllNotes().subscribeOn(Schedulers.io()));
    }

    public Single<Integer> getNotesCount() {
        return mRepo.getNotesCount();
    }

    public Completable addTestItem(String title) {
        return mRepo.insertOrUpdate(new NoteItem(title, "test message"));
    }

    @Override
    protected void onCleared() {
        mDisposable.clear();
        super.onCleared();
    }
}

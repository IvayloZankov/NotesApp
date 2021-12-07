package com.example.notesapplication.edit;

import static com.example.notesapplication.utils.Constants.*;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.notesapplication.room.NoteItem;
import com.example.notesapplication.room.NoteRepo;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class EditorViewModel extends AndroidViewModel {

    public MutableLiveData<NoteItem> mMutableNote = new MutableLiveData<>();
    private final NoteRepo mRepo;
    private final CompositeDisposable mDisposable;

    public EditorViewModel(@NonNull Application application) {
        super(application);
        mRepo = new NoteRepo(application);
        mDisposable = new CompositeDisposable();
    }

    public void loadData(int mNoteId) {
        mRepo.getNoteById(mNoteId).subscribeOn(Schedulers.io()).subscribe(new SingleObserver<NoteItem>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                mDisposable.add(d);
            }

            @Override
            public void onSuccess(@NonNull NoteItem noteItem) {
                mMutableNote.postValue(noteItem);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                e.printStackTrace();
            }
        });

    }

    @Override
    protected void onCleared() {
        mDisposable.clear();
        super.onCleared();
    }

    public void saveNote(String title, String message) {
        NoteItem noteItem = mMutableNote.getValue();
        if (noteItem == null) {
            if ((title.trim().isEmpty() && message.trim().isEmpty())  ||
                    title.equals(NEW_NOTE_TITLE) && message.trim().isEmpty()) {
                return;
            }
            noteItem = new NoteItem(title.trim(), message.trim());
        } else {
            noteItem.setTitle(title.trim());
            noteItem.setMessage(message.trim());
        }
        mDisposable.add(mRepo.insertOrUpdate(noteItem).subscribeOn(Schedulers.io()).subscribe());
    }
}

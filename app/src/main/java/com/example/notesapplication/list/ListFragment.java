package com.example.notesapplication.list;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesapplication.R;
import com.example.notesapplication.room.NoteItem;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class ListFragment extends Fragment implements ListAdapter.OnNoteItemListener {

    private ListAdapter mAdapter;
    private ListViewModel mViewModel;
    private List<NoteItem> mArrayNotes;

    private final CompositeDisposable mDisposable = new CompositeDisposable();

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArrayNotes = new ArrayList<>();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes_list, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        handleBackButton();
        initRecyclerView(view);
        initViewModel();
        mViewModel.getAllNotes().observe(getViewLifecycleOwner(), noteItems -> {
            mArrayNotes.clear();
            mArrayNotes.addAll(noteItems);
            mArrayNotes.add(new NoteItem(getString(R.string.add_new_note), ""));
            mAdapter.notifyDataSetChanged();
        });

        Button testButton = view.findViewById(R.id.testButton);
        testButton.setOnClickListener(v ->
                mViewModel.getNotesCount().subscribeOn(Schedulers.io()).subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Integer integer) {
                        addNewTestNote(integer);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }
                }));
    }

    private void addNewTestNote(Integer integer) {
        mDisposable.add(mViewModel.addTestItem(String.valueOf(integer)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe());
    }

    private void initViewModel() {
        mViewModel = new ViewModelProvider(this).get(ListViewModel.class);
    }

    private void initRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewNotes);
        mAdapter = new ListAdapter(mArrayNotes, this);
        recyclerView.setAdapter(mAdapter);
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        mDisposable.clear();
        super.onDestroy();
    }

    @Override
    public void onNoteItemClick(int position) {
        if (position != mArrayNotes.size() - 1) {
            Bundle bundle = new Bundle();
            bundle.putInt(getString(R.string.note_id), mArrayNotes.get(position).getId());
            NavHostFragment.findNavController(ListFragment.this)
                    .navigate(R.id.action_NotesList_to_SelectedNote, bundle);
        } else {
            NavHostFragment.findNavController(ListFragment.this)
                    .navigate(R.id.action_NotesList_to_SelectedNote);
        }

    }

    @Override
    public void onNoteItemLongClick(int position) {
        if (position != mArrayNotes.size() - 1) {
            showDeleteAlertDialog(position);
        }
    }

    private void showDeleteAlertDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.alert_detele_title))
                .setMessage(getString(R.string.alert_delete_text))
                .setPositiveButton(getString(R.string.alert_delete_yes), (dialog, which) ->
                        mViewModel.deleteNote(mArrayNotes.get(position)))
                .setNegativeButton(getString(R.string.alert_delete_cancel), null).show();
    }

    private void handleBackButton() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (doubleBackToExitPressedOnce) {
                    requireActivity().finish();
                    return;
                }
                doubleBackToExitPressedOnce = true;
                Toast.makeText(getContext(), R.string.exit_toast, Toast.LENGTH_SHORT).show();

                new Handler(Looper.getMainLooper()).postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }
}
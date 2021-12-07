package com.example.notesapplication.edit;

import static com.example.notesapplication.utils.Constants.*;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.notesapplication.R;

public class EditorFragment extends Fragment {

    private int mNoteId = -1;
    private EditText editTextTitle;
    private EditText editTextMessage;
    private EditorViewModel mViewModel;
    private boolean isEditing;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            mNoteId = arguments.getInt(getString(R.string.note_id));
        }
        return inflater.inflate(R.layout.fragment_selected_note, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editTextTitle = view.findViewById(R.id.editTextTitle);
        editTextMessage = view.findViewById(R.id.editTextMessage);
        if (savedInstanceState != null) isEditing = savedInstanceState.getBoolean(EDITING_KEY);
        initViewModel();
        handleBackButton();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editTextTitle, InputMethodManager.SHOW_IMPLICIT);
    }

    private void initViewModel() {
        mViewModel = new ViewModelProvider(this).get(EditorViewModel.class);
        mViewModel.mMutableNote.observe(getViewLifecycleOwner(), noteItem -> {
            if (noteItem != null && !isEditing) {
                editTextTitle.setText(noteItem.getTitle());
                editTextMessage.setText(noteItem.getMessage());
                editTextTitle.requestFocus();
            }
        });
        if (mNoteId == -1) {
            editTextTitle.setText(getString(R.string.add_new_note));
            editTextTitle.setSelectAllOnFocus(true);
            editTextTitle.requestFocus();
        } else {
            mViewModel.loadData(mNoteId);
        }
    }

    private void handleBackButton() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                String title = editTextTitle.getText().toString();
                String message = editTextMessage.getText().toString();
                mViewModel.saveNote(title, message);
                NavHostFragment.findNavController(EditorFragment.this)
                        .navigate(R.id.action_SelectedNote_to_NotesList);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(EDITING_KEY, true);
        super.onSaveInstanceState(outState);
    }
}
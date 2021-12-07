package com.example.notesapplication.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesapplication.R;
import com.example.notesapplication.room.NoteItem;

import java.util.List;

public class ListAdapter extends
        RecyclerView.Adapter<ListAdapter.NoteViewHolder> {

    private final List<NoteItem> mNotesList;
    private final OnNoteItemListener mListener;

    public ListAdapter(List<NoteItem> notesList, OnNoteItemListener listener) {
        this.mNotesList = notesList;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_note, parent, false);
        return new NoteViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(final NoteViewHolder holder, int position) {
        String title = mNotesList.get(position).getTitle();
        String message = mNotesList.get(position).getMessage();
        holder.mTextViewNoteTitle.setText(title);
        holder.mTextViewNoteText.setText(message);
    }

    public interface OnNoteItemListener {
        void onNoteItemClick(int position);
        void onNoteItemLongClick(int position);
    }

    @Override
    public int getItemCount() {
        return mNotesList.size();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {
        public final View mView;
        public final TextView mTextViewNoteTitle;
        public final TextView mTextViewNoteText;
        public OnNoteItemListener mListener;

        public NoteViewHolder(View view, OnNoteItemListener onNoteItemListener) {
            super(view);
            this.mListener = onNoteItemListener;
            mView = view;
            mTextViewNoteTitle = view.findViewById(R.id.textViewTitle);
            mTextViewNoteText =  view.findViewById(R.id.textViewText);
            mView.setOnClickListener(this);
            mView.setOnLongClickListener(this);
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString();
        }

        @Override
        public void onClick(View v) {
            mListener.onNoteItemClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            mListener.onNoteItemLongClick(getAdapterPosition());
            return true;
        }
    }
}
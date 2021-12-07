package com.example.notesapplication.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "notes_table")
public class NoteItem implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String message;
    private Date date;

    public NoteItem() {
    }

    public NoteItem(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "NoteItem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}

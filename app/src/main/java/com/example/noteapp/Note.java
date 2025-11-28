package com.example.noteapp; // 换成你自己项目的包名

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes")
public class Note {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "content")
    private String content;

    public Note(String content) {
        this.content = content;
    }

    // getter / setter
    public int getId() {
        return id;
    }

    public void setId(int id) {   // 插入后设置主键用
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // 让 ListView 显示这段文字
    @Override
    public String toString() {
        return content;
    }
}


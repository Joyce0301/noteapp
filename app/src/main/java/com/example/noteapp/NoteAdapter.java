package com.example.noteapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

/**
 * 自定义适配器，用于在列表中显示备忘内容和时间
 */
public class NoteAdapter extends ArrayAdapter<Note> {

    public NoteAdapter(@NonNull Context context, List<Note> notes) {
        super(context, android.R.layout.simple_list_item_2, notes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(android.R.layout.simple_list_item_2, parent, false);
        }

        Note note = getItem(position);

        TextView text1 = convertView.findViewById(android.R.id.text1);
        TextView text2 = convertView.findViewById(android.R.id.text2);

        if (note != null) {
            text1.setText(note.getContent());
            text2.setText("创建：" + note.getFormattedDate());
        }

        return convertView;
    }
}

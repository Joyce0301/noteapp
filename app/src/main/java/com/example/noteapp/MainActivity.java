package com.example.noteapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    private EditText etNote;
    private Button btnAbout;
    private Button btnAdd;
    private ListView listView;

    private NoteDatabase db;
    private NoteDao noteDao;

    private ArrayList<Note> noteList;
    private NoteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNote = findViewById(R.id.et_note);
        btnAdd = findViewById(R.id.btn_add);
        btnAbout = findViewById(R.id.btn_about);
        listView = findViewById(R.id.list_notes);

        db = NoteDatabase.getInstance(this);
        noteDao = db.noteDao();

        List<Note> allNotes = noteDao.getAll();
        noteList = new ArrayList<>(allNotes);

        // 使用自定义适配器显示时间和内容
        adapter = new NoteAdapter(this, noteList);
        listView.setAdapter(adapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = etNote.getText().toString().trim();

                if (text.isEmpty()) {
                    Toast.makeText(MainActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                Note note = new Note(text);
                long newId = noteDao.insert(note);
                note.setId((int) newId);
                noteList.add(0, note); // 添加到列表开头（最新的在前）
                adapter.notifyDataSetChanged();
                etNote.setText("");
            }
        });

        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showEditDialog(position);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteDialog(position);
                return true;
            }
        });
    }

    private void showEditDialog(final int position) {
        final Note note = noteList.get(position);

        final EditText editText = new EditText(this);
        editText.setText(note.getContent());
        editText.setSelection(editText.getText().length());

        new AlertDialog.Builder(this)
                .setTitle("编辑备忘")
                .setView(editText)
                .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newText = editText.getText().toString().trim();

                        if (newText.isEmpty()) {
                            Toast.makeText(MainActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        note.setContent(newText);
                        noteDao.update(note);
                        
                        // 更新后刷新列表（可能需要重新排序）
                        noteList.remove(position);
                        noteList.add(0, note);
                        adapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void showDeleteDialog(final int position) {
        final Note note = noteList.get(position);

        new AlertDialog.Builder(this)
                .setTitle("删除备忘")
                .setMessage("确定要删除这条备忘吗？")
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        noteDao.delete(note);
                        noteList.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }
}

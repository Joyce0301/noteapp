package com.example.noteapp;  // ⚠️ 用你自己项目的包名！比如 com.example.myapp

// ====== 这些是用到的类，从 Android 和 Java 中导入 ======
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


// 这个类就是我们 App 的主页面（唯一一个 Activity）
public class MainActivity extends AppCompatActivity {

    // ====== 界面上的控件（上面的输入框和按钮，下面的列表） ======
    private EditText etNote;   // 输入备忘内容的文本框

    private Button btnAbout; //跳转按钮

    private Button btnAdd;     // “添加”按钮
    private ListView listView; // 显示所有备忘的列表

    // ====== Room 相关对象 ======
    private NoteDatabase db;   // 数据库对象（整个 App 共用一个实例，单例）
    private NoteDao noteDao;   // DAO：对 Note 表进行增删改查的接口

    // ====== 用来给 ListView 显示数据 ======
    // Room 查询返回 List<Note>，这里转成 ArrayList 方便增删
    private ArrayList<Note> noteList;
    // ArrayAdapter 负责把 noteList 显示到 ListView（每一行显示 Note.toString()）
    private ArrayAdapter<Note> adapter;

    // Activity 生命周期入口：创建时会执行这个方法（类似 main）
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 调用父类的 onCreate，完成系统级的初始化（必须写在最前面）
        super.onCreate(savedInstanceState);

        // 把这个 Activity 和我们写的布局文件 activity_main.xml 关联起来
        // 也就是：这句决定了屏幕上长什么样
        setContentView(R.layout.activity_main);

        // ===================== 1. 找到布局中的控件 =====================

        // findViewById 根据 id 在布局文件中找到对应的控件，并赋值给我们在类里定义的变量
        // 这样后面就可以通过 etNote / btnAdd / btnAbout / listView 来操作界面
        etNote = findViewById(R.id.et_note);         // 对应顶部的输入框 EditText
        btnAdd = findViewById(R.id.btn_add);         // 对应“添加”按钮
        btnAbout = findViewById(R.id.btn_about);     // 对应“About”按钮（用来跳转）
        listView = findViewById(R.id.list_notes);    // 对应显示备忘列表的 ListView

        // ===================== 2. 初始化数据库和 DAO =====================

        // 通过我们写的 NoteDatabase.getInstance(this) 获取数据库单例对象
        // 第一次调用时 Room 会帮我们创建数据库文件，以后会一直复用同一个实例
        db = NoteDatabase.getInstance(this);

        // 通过数据库对象拿到 NoteDao
        // NoteDao 里封装了对 notes 表的增删改查操作（getAll / insert / update / delete）
        noteDao = db.noteDao();

        // ===================== 3. 从数据库读取历史数据，填充列表 =====================

        // 调用 DAO 的 getAll() 方法，从数据库中取出所有 Note 记录（SELECT * FROM notes）
        List<Note> allNotes = noteDao.getAll();

        // Room 返回的是 List<Note> 接口，这里转成 ArrayList，方便后面 add/remove
        noteList = new ArrayList<>(allNotes);

        // ===================== 4. 创建适配器，并绑定到 ListView =====================

        // ArrayAdapter 的作用：把 noteList 里的每一个 Note 映射成 ListView 上的一行
        // 这里用的是系统自带的 simple_list_item_1 布局（一行一个 TextView）
        // 显示的文字来自 Note.toString()，我们在 Note 类里已经让 toString() 返回 content
        adapter = new ArrayAdapter<>(
                this,                                   // 上下文：当前的 Activity
                android.R.layout.simple_list_item_1,    // 每一行使用的布局
                noteList                                // 要显示的数据源
        );

        // 给 ListView 设置适配器，这样 ListView 就能把 noteList 展示出来了
        listView.setAdapter(adapter);

        // ===================== 5. 设置“添加”按钮的点击事件（负责往数据库里新增一条 Note） =====================

        // 当用户点击“添加”按钮时，就会执行 onClick 方法里的代码
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // ① 从输入框中取出用户输入的文本，并去掉前后空格
                String text = etNote.getText().toString().trim();

                // ② 如果输入内容为空，就给一个 Toast 提示，不允许添加空内容
                if (text.isEmpty()) {
                    Toast.makeText(MainActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
                    return; // 直接结束这个点击事件，不再往下执行
                }

                // ③ 根据输入内容创建一个 Note 对象
                //    构造函数里只传 content，id 主键让 Room 自动生成
                Note note = new Note(text);

                // ④ 调用 DAO 的 insert() 方法，把 Note 插入到数据库中
                //    insert() 会返回新插入那一行的主键 id（long 类型）
                long newId = noteDao.insert(note);

                // ⑤ 把生成的 id 设置回这个 note 对象里，方便以后更新 / 删除时使用
                note.setId((int) newId);

                // ⑥ 把这个 note 加入到内存中的列表 noteList 里
                noteList.add(note);

                // ⑦ 通知适配器：数据已经发生变化，请刷新 ListView 的显示
                adapter.notifyDataSetChanged();

                // ⑧ 清空输入框内容，方便用户继续输入下一条备忘
                etNote.setText("");
            }
        });

        // ===================== 6. 设置“About”按钮的点击事件（负责 Activity 跳转） =====================

        // 这个按钮的作用：从主界面跳转到 AboutActivity
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 创建一个 Intent，“意图”：从 MainActivity 跳转到 AboutActivity
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);

                // 调用 startActivity，真正发出跳转请求，系统会打开 AboutActivity 界面
                startActivity(intent);
            }
        });

        // ===================== 7. 设置列表项“点击”事件：点击某一条，弹出编辑对话框 =====================

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // position 表示用户点击的是第几行（从 0 开始）
                // 我们调用自己写的 showEditDialog(position) 方法，弹出编辑对话框
                showEditDialog(position);
            }
        });

        // ===================== 8. 设置列表项“长按”事件：长按某一条，弹出删除确认框 =====================

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // 长按时调用 showDeleteDialog(position)，弹出“是否删除”的确认对话框
                showDeleteDialog(position);

                // 返回 true 表示我们已经处理了这个长按事件，后续不会再触发点击事件
                return true;
            }
        });
    }


    // ================== 下边是两个辅助方法：编辑、删除 ==================

    /**
     * 弹出一个对话框，让用户修改某一条备忘的内容
     * @param position 要修改的是列表中的第几个元素
     */
    private void showEditDialog(final int position) {
        // 1）先拿到当前位置对应的 Note 对象
        final Note note = noteList.get(position);

        // 2）创建一个新的 EditText，作为对话框里的输入框
        final EditText editText = new EditText(this);
        // 把原来的内容填进去，方便用户在原来的基础上修改
        editText.setText(note.getContent());
        // 光标放到文本的最后
        editText.setSelection(editText.getText().length());

        // 3）使用 AlertDialog.Builder 构建一个对话框
        new AlertDialog.Builder(this)
                .setTitle("编辑备忘")   // 对话框标题
                .setView(editText)     // 把刚才创建的 EditText 放到对话框里
                // “保存”按钮
                .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 用户点“保存”后执行这里的代码

                        // 读取最新输入内容
                        String newText = editText.getText().toString().trim();

                        // 如果内容为空，提示错误
                        if (newText.isEmpty()) {
                            Toast.makeText(MainActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // 修改内存中 Note 对象的内容
                        note.setContent(newText);

                        // 调用 DAO 的 update() 方法，更新数据库中的这一条记录
                        noteDao.update(note);

                        // 通知适配器刷新界面
                        adapter.notifyDataSetChanged();
                    }
                })
                // “取消”按钮什么都不做，传 null 即可
                .setNegativeButton("取消", null)
                // show() 表示真正显示这个对话框
                .show();
    }

    /**
     * 弹出一个确认对话框，询问用户是否删除某一条备忘
     * @param position 要删除的是列表中的第几个元素
     */
    private void showDeleteDialog(final int position) {
        // 1）拿到要删除的 Note 对象
        final Note note = noteList.get(position);

        // 2）构建确认删除对话框
        new AlertDialog.Builder(this)
                .setTitle("删除备忘")           // 标题
                .setMessage("确定要删除这条备忘吗？") // 提示内容
                // “删除”按钮
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 先从数据库中删除这条记录
                        noteDao.delete(note);
                        // 再从内存列表中移除
                        noteList.remove(position);
                        // 通知适配器刷新界面
                        adapter.notifyDataSetChanged();
                    }
                })
                // “取消”按钮：什么都不做
                .setNegativeButton("取消", null)
                .show();
    }
}


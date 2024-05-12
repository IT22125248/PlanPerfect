package com.example.planperfect

import android.content.DialogInterface
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.planperfect.Adapter.PlanAdapter
import com.example.planperfect.Model.PlanModel
import com.example.planperfect.Utils.DataBaseHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), OnDialogCloseListener {

    private lateinit var mRecyclerview: RecyclerView
    private lateinit var fab: FloatingActionButton
    private lateinit var myDB: DataBaseHelper
    private lateinit var mList: MutableList<PlanModel>
    private lateinit var adapter: PlanAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mRecyclerview = findViewById(R.id.recyclerview)
        fab = findViewById(R.id.fab)
        myDB = DataBaseHelper(this)
        mList = mutableListOf()
        adapter = PlanAdapter(myDB, this)

        mRecyclerview.setHasFixedSize(true)
        mRecyclerview.layoutManager = LinearLayoutManager(this)
        mRecyclerview.adapter = adapter

        mList.addAll(myDB.getAllTasks())
        mList.reverse()
        adapter.setTasks(mList)

        fab.setOnClickListener {
            AddNewTask.newInstance().show(supportFragmentManager, AddNewTask.TAG)
        }
        val itemTouchHelper = ItemTouchHelper(RecyclerViewTouchHelper(this, adapter))
        itemTouchHelper.attachToRecyclerView(mRecyclerview)

    }

    override fun onDialogClose(dialogInterface: DialogInterface) {
        mList.clear()
        mList.addAll(myDB.getAllTasks())
        mList.reverse()
        adapter.setTasks(mList)
        adapter.notifyDataSetChanged()
    }
}

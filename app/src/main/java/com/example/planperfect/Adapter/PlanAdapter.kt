package com.example.planperfect.Adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.planperfect.AddNewTask
import com.example.planperfect.MainActivity
import com.example.planperfect.Model.PlanModel
import com.example.planperfect.R
import com.example.planperfect.Utils.DataBaseHelper

class PlanAdapter(private val myDB: DataBaseHelper, private val activity: MainActivity) :
    RecyclerView.Adapter<PlanAdapter.MyViewHolder>() {

    private var mList: MutableList<PlanModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.task_layout, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = mList[position]
        holder.mCheckBox.text = item.task
        holder.mCheckBox.isChecked = item.status != 0
        holder.mCheckBox.setOnCheckedChangeListener { _, isChecked ->
            val status = if (isChecked) 1 else 0
            myDB.updateStatus(item.id, status)
        }
    }

    override fun getItemCount(): Int = mList.size

    fun setTasks(tasks: List<PlanModel>) {
        mList.clear()
        mList.addAll(tasks)
        notifyDataSetChanged()
    }

    fun deleteTask(position: Int): PlanModel {
        val item = mList.removeAt(position)
        myDB.deleteTask(item.id)
        notifyItemRemoved(position)
        return item
    }

    fun editItem(position: Int) {
        val item = mList[position]

        val bundle = Bundle().apply {
            putInt("id", item.id)
            putString("task", item.task)
        }

        val task = AddNewTask().apply {
            arguments = bundle
        }
        task.show(activity.supportFragmentManager, task.tag)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mCheckBox: CheckBox = itemView.findViewById(R.id.mcheckbox)
    }
}
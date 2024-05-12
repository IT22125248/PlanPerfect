package com.example.planperfect

import android.app.AlertDialog
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.planperfect.Adapter.PlanAdapter
import androidx.core.content.ContextCompat
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

class RecyclerViewTouchHelper(private val context: Context, private val adapter: PlanAdapter) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        if (direction == ItemTouchHelper.RIGHT) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Delete Task")
            builder.setMessage("Are You Sure ?")
            builder.setPositiveButton("Yes") { dialog, which ->
                val deletedTask = adapter.deleteTask(position)
                // Notify the adapter immediately after deleting the task from the database
                adapter.notifyItemRemoved(position)
                // Optionally, you can also show an Undo option here
            }
            builder.setNegativeButton("Cancel") { dialog, which -> adapter.notifyItemChanged(position) }
            val dialog = builder.create()
            dialog.show()
        } else {
            adapter.editItem(position)
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        RecyclerViewSwipeDecorator.Builder(
            c,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
            .addSwipeLeftBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
            .addSwipeLeftActionIcon(R.drawable.baseline_edit_24)
            .addSwipeRightBackgroundColor(Color.RED)
            .addSwipeRightActionIcon(R.drawable.baseline_delete_24)
            .create()
            .decorate()
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}
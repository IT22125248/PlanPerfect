package com.example.planperfect

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.planperfect.Model.PlanModel
import com.example.planperfect.Utils.DataBaseHelper
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddNewTask : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "AddNewTask"

        fun newInstance(taskId: Int? = null, taskText: String? = null): AddNewTask {
            val bundle = Bundle().apply {
                taskId?.let { putInt("id", it) }
                taskText?.let { putString("task", it) }
            }
            return AddNewTask().apply {
                arguments = bundle
            }
        }
    }

    private lateinit var mEditText: EditText
    private lateinit var mSaveButton: Button
    private lateinit var myDb: DataBaseHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.add_newtask, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mEditText = view.findViewById(R.id.edittext)
        mSaveButton = view.findViewById(R.id.button_save)

        myDb = DataBaseHelper(requireActivity())

        var isUpdate = false

        arguments?.let { bundle ->
            val taskText = bundle.getString("task")
            mEditText.setText(taskText)
            if (!taskText.isNullOrEmpty()) {
                isUpdate = true
            }
        }

        mEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    mSaveButton.isEnabled = false
                    mSaveButton.setBackgroundColor(Color.GRAY)
                } else {
                    mSaveButton.isEnabled = true
                    mSaveButton.setBackgroundColor(resources.getColor(R.color.colorPrimary))
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        val finalIsUpdate = isUpdate
        mSaveButton.setOnClickListener {
            val text = mEditText.text.toString()
            if (finalIsUpdate) {
                arguments?.getInt("id")?.let { id ->
                    myDb.updateTask(id, text)
                }
            } else {
                val item = PlanModel().apply {
                    task = text
                    status = 0
                }
                myDb.insertTask(item)
            }
            dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        val activity = activity
        if (activity is OnDialogCloseListener) {
            activity.onDialogClose(dialog)
        }
    }
}
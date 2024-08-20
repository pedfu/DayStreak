package com.pedfu.daystreak.presentation.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.button.MaterialButton
import com.pedfu.daystreak.R

class SelectCreateTypeDialogFragment : DialogFragment() {
    private var onItemCreatedListener: OnItemCreatedListener? = null
    fun setOnItemCreatedListener(listener: OnItemCreatedListener) {
        this.onItemCreatedListener = listener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonCategory = view.findViewById<MaterialButton>(R.id.buttonCategory)
        val buttonStreak = view.findViewById<MaterialButton>(R.id.buttonStreak)

        buttonCategory.setOnClickListener {
            showCategoryCreationModal()
        }
        buttonStreak.setOnClickListener {
            showStreakCreationModal()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_select_create_type_dialog, container, false)
    }

    private fun showCategoryCreationModal() {
        val categoryCreationDialog = CategoryCreationDialogFragment(::dismiss)
        categoryCreationDialog.setOnItemCreatedListener(object : OnItemCreatedListener {
            override fun onItemCreated(itemType: String) {
                onItemCreatedListener?.onItemCreated(itemType)
                dismiss()
            }
        })
        categoryCreationDialog.show(childFragmentManager, "CreateItemDialog")
    }

    private fun showStreakCreationModal() {

    }
}
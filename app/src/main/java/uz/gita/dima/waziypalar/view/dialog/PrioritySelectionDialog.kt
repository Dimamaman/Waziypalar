package uz.gita.dima.waziypalar.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import uz.gita.dima.waziypalar.R
import uz.gita.dima.waziypalar.databinding.LayoutPrioritySelectionDialogBinding
import uz.gita.dima.waziypalar.utils.Constants.LOW_PRIORITY
import uz.gita.dima.waziypalar.utils.Constants.MEDIUM_PRIORITY
import uz.gita.dima.waziypalar.utils.Constants.TOP_PRIORITY

@ExperimentalCoroutinesApi
class PrioritySelectionDialog : BottomSheetDialogFragment() {

    private var binding: LayoutPrioritySelectionDialogBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.layout_priority_selection_dialog,
            container,
            false
        )
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }


    private fun setListeners() {
        val savedStateHandle = findNavController().previousBackStackEntry?.savedStateHandle
        binding?.apply {
            tvPriorityHeader.setOnClickListener { dismiss() }

            tvTopPriority.setOnClickListener {
                savedStateHandle?.set("PRIORITY", TOP_PRIORITY)
                dismiss()
            }

            tvMediumPriority.setOnClickListener {
                savedStateHandle?.set("PRIORITY", MEDIUM_PRIORITY)
                dismiss()
            }

            tvLowPriority.setOnClickListener {
                savedStateHandle?.set("PRIORITY", LOW_PRIORITY)
                dismiss()
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
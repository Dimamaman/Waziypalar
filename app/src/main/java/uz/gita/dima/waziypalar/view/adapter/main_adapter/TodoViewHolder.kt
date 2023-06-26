package uz.gita.dima.waziypalar.view.adapter.main_adapter

import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import uz.gita.dima.waziypalar.databinding.LayoutMainTaskListBinding
import uz.gita.dima.waziypalar.model.Todo
import uz.gita.dima.waziypalar.view.fragment.TaskFragmentDirections


class TodoViewHolder(
    private val binding: LayoutMainTaskListBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(todoItem: Todo) {
        binding.apply {
            todo = todoItem
            executePendingBindings()
            clItemListContainer.setOnClickListener {
                val action =
                    TaskFragmentDirections.actionTaskFragmentToTaskEditFragment(todoItem.docId)
                it?.findNavController()?.navigate(action)
            }
        }
    }
}

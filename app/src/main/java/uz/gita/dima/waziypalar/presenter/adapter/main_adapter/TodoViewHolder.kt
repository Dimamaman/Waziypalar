package uz.gita.dima.waziypalar.presenter.adapter.main_adapter

import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import tech.androidplay.sonali.todo.view.fragment.TaskFragmentDirections
import uz.gita.dima.waziypalar.data.model.Todo
import uz.gita.dima.waziypalar.databinding.LayoutMainTaskListBinding


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

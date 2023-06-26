package uz.gita.dima.waziypalar.view.adapter.main_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import uz.gita.dima.waziypalar.model.Todo
import uz.gita.dima.waziypalar.view.adapter.DiffUtilCallback
import uz.gita.dima.waziypalar.databinding.LayoutMainTaskListBinding

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class TodoAdapter : ListAdapter<Todo, TodoViewHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LayoutMainTaskListBinding.inflate(layoutInflater, parent, false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val todoItem = getItem(position)
        holder.bind(todoItem)
    }
}
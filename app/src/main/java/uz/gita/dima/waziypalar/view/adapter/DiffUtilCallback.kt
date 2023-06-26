package uz.gita.dima.waziypalar.view.adapter

import androidx.recyclerview.widget.DiffUtil
import uz.gita.dima.waziypalar.model.Todo

class DiffUtilCallback : DiffUtil.ItemCallback<Todo>() {
    override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean {
        return oldItem.docId == newItem.docId
    }

    override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }
}
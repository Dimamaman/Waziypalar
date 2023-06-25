package uz.gita.dima.waziypalar.presenter.adapter.viewpager_adapter

import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import tech.androidplay.sonali.todo.view.fragment.TaskFragmentDirections
import uz.gita.dima.waziypalar.data.model.Todo
import uz.gita.dima.waziypalar.databinding.LayoutAssignedCardListBinding

class ViewPagerViewHolder(private val binding: LayoutAssignedCardListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(todoItem: Todo) {
        binding.todo = todoItem
        binding.executePendingBindings()

        binding.llAssignedCardItemList.setOnClickListener {
            val action = TaskFragmentDirections.actionTaskFragmentToTaskViewFragment(todoItem.docId)
            it?.findNavController()?.navigate(action)
        }
    }
}
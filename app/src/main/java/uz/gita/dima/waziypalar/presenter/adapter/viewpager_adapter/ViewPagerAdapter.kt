package uz.gita.dima.waziypalar.presenter.adapter.viewpager_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import uz.gita.dima.waziypalar.presenter.adapter.DiffUtilCallback
import uz.gita.dima.waziypalar.data.model.Todo
import uz.gita.dima.waziypalar.databinding.LayoutAssignedCardListBinding

class ViewPagerAdapter : ListAdapter<Todo, ViewPagerViewHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LayoutAssignedCardListBinding.inflate(layoutInflater, parent, false)
        return ViewPagerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        val todoItem = getItem(position)
        holder.bind(todoItem)
    }


}
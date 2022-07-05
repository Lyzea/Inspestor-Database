package com.example.sqlitedemoapplication

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sqlitedemoapplication.databinding.RecyclerviewRowBinding

class ItemAdapter(val context: Context, val items: ArrayList<MyModelClass>): RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    class ViewHolder(private val binding: RecyclerviewRowBinding): RecyclerView.ViewHolder(binding.root){
        val llMain = binding.llMain
        val tvPest_Name = binding.tvPestName
        val tvRecommended_Pesticide = binding.tvREcommendedPesticide
        val buttonEdit = binding.imageButtonEdit
        val buttonDelete = binding.imageButtonDelete
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerviewRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.tvPest_Name.text = item.pest_name
        holder.tvRecommended_Pesticide.text = item.recommended_pesticide

        //updating background according to odd/even position
        if (position % 2 == 0){
            holder.llMain.setBackgroundColor(Color.GRAY)
        } else{
            holder.llMain.setBackgroundColor(Color.WHITE)
        }

        holder.buttonEdit.setOnClickListener {
            if (context is MainActivity){
                context.getEditDialog(item)
            }
        }

        holder.buttonDelete.setOnClickListener {
            if (context is MainActivity){
                context.deleteRecordAlertDialog(item)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
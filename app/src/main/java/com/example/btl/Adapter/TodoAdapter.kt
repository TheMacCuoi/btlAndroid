package com.example.btl.Adapter

import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.btl.Model.Todo
import com.example.btl.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.item_todo.view.*

class TodoAdapter(
    private val todos: ArrayList<Todo>
) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    private lateinit var dbref: DatabaseReference

    class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        return TodoViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_todo,
                parent,
                false
            )
        )
    }

//    fun addTodo(todo: Todo){
//        todos.add(todo)
//        notifyItemInserted(todos.size - 1)
//    }

//    fun delDoneTodos(){
//        todos.removeAll { todo ->
//            todo.paid
//        }
//        notifyDataSetChanged()
//    }

    private fun toggleStrikeThrough(tvTodoTitle: TextView, isChecked: Boolean){
        if (isChecked){
            tvTodoTitle.paintFlags = tvTodoTitle.paintFlags or STRIKE_THRU_TEXT_FLAG
        } else{
            tvTodoTitle.paintFlags = tvTodoTitle.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val curTodo = todos[position]
        holder.itemView.apply {
            tvTodoTitle.text = resources.getString(R.string.todo_title,curTodo.name, curTodo.money)
            cbDone.isChecked = curTodo.paid!!
            toggleStrikeThrough(tvTodoTitle, curTodo.paid!!)
            cbDone.setOnCheckedChangeListener { _, isChecked ->
                toggleStrikeThrough(tvTodoTitle, isChecked)
                curTodo.paid = !curTodo.paid!!
                updatePaid(curTodo)
            }
        }
    }

    private fun updatePaid(todo: Todo) {
        dbref = FirebaseDatabase.getInstance().getReference("DanhSachNo")

        dbref.child(todo.id!!).setValue(todo)
    }

    override fun getItemCount(): Int {
        return todos.size
    }
}
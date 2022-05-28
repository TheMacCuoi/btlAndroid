package com.example.btl.View

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.btl.Adapter.TodoAdapter
import com.example.btl.Model.Todo
import com.example.btl.R
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_todo.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var todoList: ArrayList<Todo>
    private lateinit var dbref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvTodoItems.layoutManager = LinearLayoutManager(this)
        getData()


        btnAdd.setOnClickListener{
            val name = etName.text.toString()
            val money = etMoney.text.toString()
            if(name.isNotEmpty() && money.isNotEmpty()) {
                val todo = Todo(UUID.randomUUID().toString(), name, money, false)
                addData(todo)
                etName.text.clear()
                etMoney.text.clear()
            }
        }
        btnDel.setOnClickListener{
            delData()
        }
//        cbDone.setOnClickListener{
//            if (cbDone.isChecked){
//                updateData()
//            }
//        }
    }

    private fun getData(){
        dbref = FirebaseDatabase.getInstance().getReference("DanhSachNo")

        dbref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                todoList = arrayListOf()
                if (snapshot.exists()){
                    for (n in snapshot.children){
                        val conNo = n.getValue(Todo::class.java)
                        conNo?.let { todoList.add(it) }
                    }
                    rvTodoItems.adapter = TodoAdapter(todoList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    private fun addData(todo: Todo){
        dbref = FirebaseDatabase.getInstance().getReference("DanhSachNo")

        dbref.child(todo.id!!).setValue(todo)

    }

    private fun delData(){
        dbref = FirebaseDatabase.getInstance().getReference("DanhSachNo")

        for (conNo in todoList){
            if (conNo.paid == true){
                dbref.child(conNo.id!!).removeValue()
            }
        }
    }

    private fun updateData(i: Int){
        dbref = FirebaseDatabase.getInstance().getReference("DanhSachNo")
    }
}



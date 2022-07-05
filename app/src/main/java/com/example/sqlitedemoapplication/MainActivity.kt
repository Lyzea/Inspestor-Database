package com.example.sqlitedemoapplication

import android.app.AlertDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sqlitedemoapplication.databinding.ActivityMainBinding
import com.example.sqlitedemoapplication.databinding.UpdateDialogBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonAdd.setOnClickListener {view->
            addPesticideData(view)
        }

        setUpListofDataIntoRecyclerView()
    }

    private fun addPesticideData(view: View) {
        val pest_name = binding.etPestName.text.toString()
        val recommended_pesticide = binding.etRecommendedPesticide.text.toString()
        val databaseHandler = DatabaseHandler(this)
        if (!pest_name.isEmpty() && !recommended_pesticide.isEmpty()){
            //id is primary key so system will assign right id itself
            val status = databaseHandler.addPesticideData(MyModelClass(0, pest_name, recommended_pesticide))
            if (status > -1){
                Toast.makeText(applicationContext, "Record saved", Toast.LENGTH_LONG).show()
                binding.etPestName.text.clear()
                binding.etRecommendedPesticide.text.clear()

                setUpListofDataIntoRecyclerView()
            }
        } else{
            Toast.makeText(this, "cant be empty", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setUpListofDataIntoRecyclerView() {
        if (getItemList().size > 0){
            binding.rvList.visibility = View.VISIBLE

            binding.rvList.layoutManager = LinearLayoutManager(this)
            binding.rvList.adapter = ItemAdapter(this, getItemList())
        } else{
            binding.rvList.visibility = View.GONE
        }
    }

    private fun getItemList(): ArrayList<MyModelClass> {
        //creating the instance of DataBaseHandler class
        val databaseHandler = DatabaseHandler(this)
        val myList = databaseHandler.viewPesticideData()
        return myList
    }

    fun getEditDialog(item: MyModelClass) {
        val editDialog = Dialog(this)
        val binding2 = UpdateDialogBinding.inflate(layoutInflater)
        editDialog.setContentView(binding2.root)

        editDialog.setCancelable(false)

        binding2.apply {
            etPestName.setText(item.pest_name)
            etRecommendedPesticide.setText(item.recommended_pesticide)
            buttonUpdate.setOnClickListener {
                val newPest_Name = etPestName.text.toString()
                val newRecommended_Pesticide = etRecommendedPesticide.text.toString()
                val databaseHandler = DatabaseHandler(this@MainActivity)
                if (newPest_Name.isNotEmpty() && newRecommended_Pesticide.isNotEmpty()){
                    val status = databaseHandler.updatePesticideData(MyModelClass(item.id,newPest_Name,newRecommended_Pesticide))

                    if (status>-1){
                        Toast.makeText(applicationContext, "Updated", Toast.LENGTH_SHORT).show()

                        setUpListofDataIntoRecyclerView()

                        editDialog.dismiss()
                    }
                } else{
                    Toast.makeText(applicationContext, "cant be empty", Toast.LENGTH_SHORT).show()
                }
            }

            buttonCancel.setOnClickListener {
                editDialog.dismiss()
            }
        }
        editDialog.show()
    }

    fun deleteRecordAlertDialog(item: MyModelClass) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Record")
        builder.setMessage("Are you sure you want to delete ${item.id}?")
        builder.setPositiveButton("Yes"){dialogInterface, which ->

            val databaseHandler = DatabaseHandler(this)

            val status =  databaseHandler.deletePesticideData(item)
            if (status>-1){
                Toast.makeText(applicationContext, "Deleted", Toast.LENGTH_SHORT).show()

                setUpListofDataIntoRecyclerView()
            }
            dialogInterface.dismiss()
        }
        builder.setNegativeButton("No"){dialogInterface, which ->
            dialogInterface.dismiss()
        }
        val alertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}
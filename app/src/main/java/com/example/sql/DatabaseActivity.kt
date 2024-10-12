package com.example.sql


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class DatabaseActivity : AppCompatActivity() {

    private var dbHelper: DatabaseHelper = DatabaseHelper(this, null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_database)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val nameInput: EditText = findViewById(R.id.nameInput)
        val positionSpinner: Spinner = findViewById(R.id.positionSpinner)
        val phoneInput: EditText = findViewById(R.id.phoneInput)
        val outputText: TextView = findViewById(R.id.outputText)

        ArrayAdapter.createFromResource(
            this,
            R.array.positions_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            positionSpinner.adapter = adapter
        }

        findViewById<Button>(R.id.saveButton).setOnClickListener {
            saveData(
                nameInput.text.toString(),
                positionSpinner.selectedItem.toString(),
                phoneInput.text.toString()
            )
            nameInput.text.clear()
            phoneInput.text.clear()
        }

        findViewById<Button>(R.id.getButton).setOnClickListener {
            getData(outputText)
        }

        findViewById<Button>(R.id.deleteButton).setOnClickListener {
            deleteData(nameInput.text.toString())
            getData(outputText)
            nameInput.text.clear() // Очистка поля ввода после удаления
        }
    }

    private fun saveData(name: String, position: String, phone: String) {
        if (name.isNotEmpty() && phone.isNotEmpty()) {
            dbHelper.insertUser(name, position, phone)
            Toast.makeText(this, getString(R.string.save), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, getString(R.string.is_correct), Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("Range", "SetTextI18n")
    private fun getData(outputText: TextView) {
        val cursor = dbHelper.getAllUsers()

        if (cursor.moveToFirst()) {
            val stringBuilder = StringBuilder()
            do {
                val name = cursor.getString(cursor.getColumnIndex("name"))
                val position = cursor.getString(cursor.getColumnIndex("position"))
                val phone = cursor.getString(cursor.getColumnIndex("phone"))
                stringBuilder.append("Имя: $name\nДолжность: $position\nТелефон: $phone\n\n")
            } while (cursor.moveToNext())

            outputText.text = stringBuilder.toString()

            cursor.close()
        } else {
            outputText.text = getString(R.string.no_data)
        }
    }

    private fun deleteData(name: String) {
        if (dbHelper.userExists(name)) {
            dbHelper.deleteUser()
            Toast.makeText(this, getString(R.string.delete), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, getString(R.string.not_found), Toast.LENGTH_SHORT).show()
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_exit -> {
                finishAffinity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
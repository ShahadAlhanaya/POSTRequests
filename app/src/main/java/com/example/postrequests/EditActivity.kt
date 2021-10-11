package com.example.postrequests

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditActivity : AppCompatActivity() {

    var name = ""
    var location = ""
    var pk = 0

    lateinit var saveButton: Button
    lateinit var deleteButton: Button
    lateinit var nameEditText: EditText
    lateinit var locationEditText: EditText

    lateinit var progressBar: ProgressBar
    lateinit var progressBarTextView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        //actionbar
        val actionbar = supportActionBar!!
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)
        //set actionbar title
        actionbar.title = "Edit entry"


        saveButton = findViewById(R.id.btn_save)
        deleteButton = findViewById(R.id.btn_delete)
        nameEditText = findViewById(R.id.edt_nameEdit)
        locationEditText = findViewById(R.id.edt_locationEdit)

        progressBar = findViewById(R.id.progressBar3)
        progressBarTextView = findViewById(R.id.progressBarText3)
        setProgressBar(false)



        //getting info from previous activity
        name = intent.extras?.getString("userName").toString()
        location = intent.extras?.getString("userLocation").toString()
        pk = intent.extras?.getInt("pk")!!

        nameEditText.setText(name)
        locationEditText.setText(location)

        saveButton.setOnClickListener {
            val updatedName = nameEditText!!.text.toString()
            val updatedLocation = locationEditText!!.text.toString()
            if (updatedName != name || updatedLocation != location) {
                if (name.trim().length < 2 || location.trim().length < 2) {
                    Toast.makeText(this, "Please enter valid information", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    var newUser = Users.User(updatedName, updatedLocation)
                    updateUser(newUser, onResult = {
                        nameEditText.clearFocus()
                        locationEditText.clearFocus()
                        Toast.makeText(applicationContext, "Saved Successfully!", Toast.LENGTH_SHORT)
                            .show()
                    })
                }
            }
        }

        deleteButton.setOnClickListener {
            val builder = AlertDialog.Builder(this@EditActivity)
            builder.setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton("Delete") { _, _ ->
                    deleteUser(onResult = {
                        Toast.makeText(applicationContext, "Deleted Successfully!", Toast.LENGTH_SHORT)
                            .show()
                        finish()
                    })
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.red));
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }



    private fun updateUser(user: Users.User, onResult: () -> Unit) {
        setProgressBar(true)
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
        if (apiInterface != null) {
            apiInterface.updateUser(pk, user).enqueue(object : Callback<Users.User> {
                override fun onResponse(call: Call<Users.User>, response: Response<Users.User>) {
                    setProgressBar(false)
                    onResult()
                }

                override fun onFailure(call: Call<Users.User>, t: Throwable) {
                    setProgressBar(false)
                    onResult()
                }
            })
        }

    }

    private fun deleteUser(onResult: () -> Unit) {
        setProgressBar(true)
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
        if (apiInterface != null) {
            apiInterface.deleteUser(pk).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    setProgressBar(false)
                    onResult()
                }
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    setProgressBar(false)
                    onResult()
                }
            })
        }

    }

    private fun setProgressBar(visibility: Boolean) {
        progressBar.isVisible = visibility
        progressBarTextView.isVisible = visibility
        locationEditText.isEnabled = !visibility
        nameEditText.isEnabled = !visibility
        saveButton.isClickable = !visibility
        deleteButton.isClickable = !visibility
    }
}
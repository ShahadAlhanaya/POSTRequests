package com.example.postrequests

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var floatingActionButton: FloatingActionButton
    lateinit var recyclerView: RecyclerView
    lateinit var progressBar: ProgressBar
    lateinit var progressBarTextView: TextView

    var usersList = arrayListOf<Users.User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBar = findViewById(R.id.progressBar)
        progressBarTextView = findViewById(R.id.progressBarText)
        setProgressBar(false)


        //initialize recyclerView
        recyclerView = findViewById(R.id.rv_users)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = UsersAdapter(usersList)

        floatingActionButton = findViewById(R.id.floatingActionButton)
        floatingActionButton.setOnClickListener {
            showBottomSheetDialog(this)
        }


        CoroutineScope(IO).launch {
            getUsersList()
        }
    }

    private fun getUsersList() {
        setProgressBar(true)
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
        if (apiInterface != null) {
            apiInterface.getUser()?.enqueue(object : Callback<List<Users.User>> {
                override fun onResponse(
                    call: Call<List<Users.User>>,
                    response: Response<List<Users.User>>
                ) {
                    setProgressBar(false)
                    Log.d(
                        "GET Response:",
                        response.code().toString() + " " + response.message()
                    )
                    for (User in response.body()!!) {
                        usersList.add(User)
                    }
                    recyclerView.adapter!!.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<List<Users.User>>, t: Throwable) {
                    setProgressBar(false)
                    Toast.makeText(this@MainActivity, "" + t.message, Toast.LENGTH_SHORT)
                        .show()
                }
            })
        }
    }

    private fun setProgressBar(visibility: Boolean) {
        progressBar.isVisible = visibility
        progressBarTextView.isVisible = visibility
    }

    private fun showBottomSheetDialog(mainActivity: MainActivity) {
        val bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetStyle)
        bottomSheetDialog.setContentView(R.layout.add_user_bottom_sheet)
        val submitButton = bottomSheetDialog.findViewById<Button>(R.id.btn_submit)
        val nameEditText = bottomSheetDialog.findViewById<EditText>(R.id.edt_name)
        val locationEditText = bottomSheetDialog.findViewById<EditText>(R.id.edt_location)

        submitButton!!.setOnClickListener {
            val name = nameEditText!!.text.toString()
            val location = locationEditText!!.text.toString()
            if (name.trim().length < 2 || location.trim().length < 2) {
                Toast.makeText(this, "Please enter a valid information", Toast.LENGTH_SHORT).show()
            } else {

                var newUser = Users.User(name, location)
                postUser(newUser,onResult = {
                    nameEditText.text.clear()
                    locationEditText.text.clear()
                    Toast.makeText(applicationContext, "Save Success!", Toast.LENGTH_SHORT).show()
                    bottomSheetDialog.dismiss()
                    getUsersList()
                })
            }
        }

        bottomSheetDialog.show()
    }

    private fun postUser(user: Users.User, onResult: ()-> Unit) {
        setProgressBar(true)
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
        if (apiInterface != null) {
            apiInterface.addUser(user).enqueue(object : Callback<Users.User> {
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
}



package com.example.postrequests

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.user_item.view.*

class UsersAdapter(private val usersList: ArrayList<Users.User>, private val context: Context) :
    RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {
    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userNameTextView: TextView = itemView.tv_userName
        val userLocationTextView: TextView = itemView.tv_userLocation
        val pkTextView: TextView = itemView.tv_pk
        val userItemLinearLayout : LinearLayout = itemView.ll_userItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.user_item,
            parent,
            false
        )
        return UserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val userName = usersList[position].name
        val userLocation = usersList[position].location
        val pk = usersList[position].pk
        holder.userNameTextView.text = userName
        holder.userLocationTextView.text = userLocation
        holder.pkTextView.text = pk.toString()
        holder.userItemLinearLayout.setOnClickListener {
            val intent = Intent(context, EditActivity::class.java)
            intent.putExtra("userName", userName)
            intent.putExtra("userLocation", userLocation)
            intent.putExtra("pk", pk)
            context.startActivity(intent)
        }

    }

    override fun getItemCount() = usersList.size
}
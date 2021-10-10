package com.example.postrequests

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.user_item.view.*

class UsersAdapter(private val usersList: ArrayList<Users.User>) :
    RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {
    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userNameTextView: TextView = itemView.tv_userName
        val userLocationTextView: TextView = itemView.tv_userLocation
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
        holder.userNameTextView.text = userName
        holder.userLocationTextView.text = userLocation
    }

    override fun getItemCount() = usersList.size
}
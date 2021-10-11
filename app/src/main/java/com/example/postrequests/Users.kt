package com.example.postrequests

import com.google.gson.annotations.SerializedName

class Users(){
    var data: List<User>? = null

    class User {

        @SerializedName("name")
        var name: String? = null

        @SerializedName("location")
        var location: String? = null

        @SerializedName("pk")// primary key
        var pk: Int? = null

        constructor(name: String?, location: String?) {
            this.name = name
            this.location = location
//            this.pk = pk
        }
    }
}



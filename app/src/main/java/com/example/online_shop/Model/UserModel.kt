package com.example.online_shop.Model

data class UserModel(
    var uid: String? = null, // Đây sẽ là UID từ Firebase Authentication
    var username: String? = null, // Đây sẽ là email của người dùng (từ Auth)
    var name: String? = null,
    var address: String? = null,
    var phonenumber: String? = null
// Không bao gồm trường 'password' ở đây
)

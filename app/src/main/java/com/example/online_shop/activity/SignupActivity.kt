package com.example.online_shop.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.online_shop.Model.UserModel
import com.example.online_shop.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Khởi tạo Firebase instances
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("Users")
        auth = FirebaseAuth.getInstance()

        binding.signupBtn.setOnClickListener {
            val signupUsername = binding.signupUsername.text.toString().trim()
            val signupPassword = binding.signupPassword.text.toString().trim()
            val signupName = binding.signupName.text.toString().trim()
            val signupAddress = binding.signupAddress.text.toString().trim()
            val signupPhoneNumber = binding.signupPhoneNumber.text.toString().trim()
            val signupConfirmPassword = binding.signupConfirmPassword.text.toString().trim()

            if (signupUsername.isNotEmpty() && signupPassword.isNotEmpty() && signupName.isNotEmpty() && signupAddress.isNotEmpty() && signupPhoneNumber.isNotEmpty() && signupConfirmPassword.isNotEmpty()) {
                if (signupPassword == signupConfirmPassword) {
                    auth.createUserWithEmailAndPassword(signupUsername, signupPassword)
                        .addOnCompleteListener(this) {
                            if (it.isSuccessful) {
                                val firebaseUser =
                                    auth.currentUser //lấy đối tượng người dùng từ firebase hiện tại
                                val uid =
                                    firebaseUser?.uid  //lấy ÚID duy nhất của người dùng từ auth

                                if (uid != null) {
                                    //Lưu thông tin chi tiết người dùng vào Realtime Database
                                    val user = UserModel(
                                        uid = uid,
                                        username = signupUsername,
                                        name = signupName,
                                        address = signupAddress,
                                        phonenumber = signupPhoneNumber
                                    )
                                    // Lưu đối tượng User vào Realtime Database
                                    // usersRef.child(uid) tạo một nút con với tên là UID của người dùng
                                    databaseReference.child(uid).setValue(user)
                                        .addOnCompleteListener {
                                            Toast.makeText(
                                                this,
                                                "Đăng ký thành công!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            val intent = Intent(
                                                this,
                                                SigninActivity::class.java
                                            ) // Chuyển hướng sau khi đăng ký
                                            startActivity(intent)
                                            finish() // Kết thúc SignupActivity
                                        }
                                        .addOnFailureListener { e ->
                                            // Lưu database thất bại
                                            Toast.makeText(
                                                this,
                                                "Lỗi lưu thông tin: ${e.message}",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            // Nếu lưu database lỗi, xóa tài khoản Auth vừa tạo
                                            // để tránh tình trạng có tài khoản Auth mà không có profile đầy đủ
                                            firebaseUser.delete()
                                                .addOnCompleteListener { deleteTask ->
                                                    if (deleteTask.isSuccessful) {
                                                        println("Tài khoản Auth đã bị xóa do lỗi database.")
                                                    } else {
                                                        println("Lỗi khi cố gắng xóa tài khoản Auth lỗi: ${deleteTask.exception?.message}")
                                                    }
                                                }
                                        }
                                } else {
                                    Toast.makeText(
                                        this,
                                        "Lỗi: Không lấy được UID người dùng Auth.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Toast.makeText(
                                    this,
                                    it.exception?.localizedMessage ?: "Đăng ký thất bại",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                } else {
                    Toast.makeText(this@SignupActivity, "Password not match", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(
                    this@SignupActivity, "All field are mandatory ", Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.SigninTxt.setOnClickListener {
            startActivity(Intent(this@SignupActivity, SigninActivity::class.java))
            finish()
        }

    }

}
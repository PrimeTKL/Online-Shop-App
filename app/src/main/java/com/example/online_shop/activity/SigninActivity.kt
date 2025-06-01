package com.example.online_shop.activity

import android.content.Intent
import android.os.Bundle
import android.service.autofill.UserData
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.online_shop.R
import com.example.online_shop.databinding.ActivitySigninBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SigninActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySigninBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        binding.loginBtn.setOnClickListener {
            val email = binding.loginUsername.text.toString().trim()
            val pass = binding.loginPassword.text.toString().trim()

            // 1. Kiểm tra các trường đầu vào
            if (email.isEmpty() || pass.isEmpty()) { // Đổi thành OR để kiểm tra cả hai
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener // Dùng return@setOnClickListener để thoát khỏi lambda
            }

            // Tùy chọn: Hiển thị ProgressBar hoặc vô hiệu hóa nút để báo hiệu đang xử lý
            // showLoadingIndicator()

            // 2. Thực hiện đăng nhập với Firebase Authentication
            firebaseAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this) { task ->
                    // Tùy chọn: Ẩn ProgressBar hoặc kích hoạt lại nút
                    // hideLoadingIndicator()

                    if (task.isSuccessful) {
                        // Đăng nhập thành công
                        Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish() // Kết thúc LoginActivity
                    } else {
                        // Đăng nhập thất bại, xử lý lỗi cụ thể
                        val exception = task.exception
                        when (exception) {
                            is FirebaseAuthInvalidUserException -> {
                                Toast.makeText(this, "Tài khoản không tồn tại. Vui lòng đăng ký.", Toast.LENGTH_LONG).show()
                            }
                            is FirebaseAuthInvalidCredentialsException -> {
                                Toast.makeText(this, "Mật khẩu không chính xác.", Toast.LENGTH_LONG).show()
                            }
                            else -> {
                                // Các lỗi khác (ví dụ: lỗi mạng)
                                val errorMessage = exception?.localizedMessage ?: "Đăng nhập thất bại. Vui lòng thử lại."
                                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
        }
        binding.signupTxt.setOnClickListener{
            startActivity(Intent(this,SignupActivity::class.java))
        }

    }
    private fun checkCurrentUserAndNavigate() {
        if (firebaseAuth.currentUser != null) {
            // Người dùng đã đăng nhập, chuyển hướng đến MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Đóng SigninActivity để không thể quay lại bằng nút Back
        }
    }
}
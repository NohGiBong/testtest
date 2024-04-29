package com.example.testtest

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.testtest.databinding.ActivityRegBinding

class RegActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegBinding
    private lateinit var dbHelper: MemberDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = MemberDbHelper(this)

        binding.btnRegister.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val name = binding.etName.text.toString().trim()
        val ageText = binding.etAge.text.toString().trim()
        val mobile = binding.etMobile.text.toString().trim()

        if (name.isEmpty()) {
            binding.etName.error = "이름을 입력해주세요."
            return
        }

        val age = ageText.toIntOrNull()
        if (age == null) {
            binding.etAge.error = "나이를 올바르게 입력해주세요."
            return
        } else if (age <= 0) {
            binding.etAge.error = "나이를 0보다 큰 값으로 입력해주세요."
            return
        }

        if (mobile.isEmpty()) {
            binding.etMobile.error = "휴대폰 번호를 입력해주세요."
            return
        }

        // 데이터베이스에 사용자 정보 저장
        saveUserToDatabase(name, age, mobile)

        // 메인 액티비티로 이동
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // 현재 액티비티 종료

        Toast.makeText(this, "사용자 등록 완료: $name, $age, $mobile", Toast.LENGTH_SHORT).show()
    }

    private fun saveUserToDatabase(name: String, age: Int, mobile: String) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(MemberDbHelper.COLUMN_NAME, name)
            put(MemberDbHelper.COLUMN_AGE, age)
            put(MemberDbHelper.COLUMN_MOBILE, mobile)
        }
        db.insert(MemberDbHelper.TABLE_NAME, null, values)
        db.close()
    }
}
package com.example.testtest

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.testtest.databinding.ActivityEditBinding

class EditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBinding
    private lateinit var member: Member
    private lateinit var listActivity: ListActivity
    private lateinit var detailActivity: DetailActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 전달받은 Member 객체 설정
        member = (intent.getSerializableExtra("member") as? Member)!!
        if (member != null) {
            setupViews()
            setupButtons()
        } else {
            // 전달받은 Member 객체가 null인 경우 처리
            // 예: 토스트 메시지 표시, 화면 종료 등
            Toast.makeText(this, "Member 객체가 전달되지 않았습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setupViews() {
        binding.etName.setText(member.name)
        binding.etAge.setText(member.age.toString())
        binding.etMobile.setText(member.mobile)
    }

    private fun setupButtons() {
        binding.btnSave.setOnClickListener { saveMember() }
    }

    private fun saveMember() {
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

        // 데이터베이스에 사용자 정보 업데이트
        updateMemberInDatabase(member.id, name, age, mobile)

        Toast.makeText(this, "사용자 정보가 수정되었습니다.", Toast.LENGTH_SHORT).show()

        val updatedMember = Member(member.id, name, age, mobile)
        val intent = Intent()
        intent.putExtra("updatedMember", updatedMember)
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun updateMemberInDatabase(id: Long, name: String, age: Int, mobile: String) {
        val dbHelper = MemberDbHelper(this)
        dbHelper.updateMember(id, name, age, mobile)
    }

}

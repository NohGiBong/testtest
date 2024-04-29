package com.example.testtest

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.testtest.databinding.ActivityDetailBinding


class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var member: Member
    private lateinit var listActivity: ListActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
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
        binding.tvName.text = member.name
        binding.tvAge.text = member.age.toString()
        binding.tvMobile.text = member.mobile
    }

    private fun setupButtons() {
        binding.btnEdit.setOnClickListener { editMember() }
        binding.btnDelete.setOnClickListener { deleteMember() }
        binding.btnBack.setOnClickListener { finish() }
    }

    private fun editMember() {
        // 수정 화면으로 이동
        val intent = Intent(this, EditActivity::class.java)
        intent.putExtra("member", member)
        startActivityForResult(intent, REQUEST_CODE_EDIT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_EDIT && resultCode == RESULT_OK) {
            // 수정된 회원 정보 가져오기
            val updatedMember = data?.getSerializableExtra("updatedMember") as? Member
            if (updatedMember != null) {
                // 데이터베이스 업데이트
                val dbHelper = MemberDbHelper(this)
                dbHelper.updateMember(
                    updatedMember.id,
                    updatedMember.name,
                    updatedMember.age,
                    updatedMember.mobile
                )

                // 화면 업데이트
                member = updatedMember
                setupViews()
                Toast.makeText(this, "회원 정보가 수정되었습니다.", Toast.LENGTH_SHORT).show()

                // ListActivity 인스턴스 가져오기
                listActivity = (intent.getSerializableExtra("listActivity") as? ListActivity)!!
                listActivity.refreshMemberList()
            }
        }
    }


    private fun deleteMember() {
        // 삭제 확인 AlertDialog 생성
        val builder = AlertDialog.Builder(this)
        builder.setTitle("회원 삭제")
        builder.setMessage("정말 이 회원을 삭제하시겠습니까?")
        builder.setPositiveButton("삭제") { _, _ ->
            // 삭제 확인 버튼 클릭 시
            val dbHelper = MemberDbHelper(this)
            dbHelper.deleteMember(member)
            Toast.makeText(this, "멤버가 삭제되었습니다.", Toast.LENGTH_SHORT).show()

            // ListActivity 인스턴스 가져오기
            listActivity = (intent.getSerializableExtra("listActivity") as? ListActivity)!!
            listActivity.refreshMemberList()
            finish()
        }
        builder.setNegativeButton("취소") { dialog, _ ->
            // 취소 버튼 클릭 시
            dialog.dismiss()
        }

        // AlertDialog 표시
        builder.show()
    }
    companion object {
        private const val REQUEST_CODE_EDIT = 1
    }
}
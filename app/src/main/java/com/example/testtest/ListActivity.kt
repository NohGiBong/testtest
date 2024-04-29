package com.example.testtest

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testtest.databinding.ActivityListBinding

class ListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListBinding
    private lateinit var dbHelper: MemberDbHelper
    private lateinit var members: List<Member>
    private lateinit var adapter: MemberAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = MemberDbHelper(this)
        loadMembersFromDatabase()
        setupRecyclerView()

        binding.btnBackList.setOnClickListener {
            finish() // 이전 화면으로 돌아가기
        }
    }

    private fun loadMembersFromDatabase() {
        members = dbHelper.getAllMembers()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = MemberAdapter(members) { member ->
            // 아이템 클릭 시 상세 페이지로 이동
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("member", member)
            startActivity(intent)
        }
        binding.recyclerView.adapter = adapter
    }

    fun refreshMemberList() {
        loadMembersFromDatabase()
        adapter.notifyDataSetChanged()
    }
}

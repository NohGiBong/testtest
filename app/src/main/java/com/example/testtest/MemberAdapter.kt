package com.example.testtest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MemberAdapter(private val members: List<Member>,
                    private val onItemClick: (Member) -> Unit) : RecyclerView.Adapter<MemberAdapter.MemberViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_member, parent, false)
        return MemberViewHolder(view)
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        val member = members[position]
        holder.bind(member)
        holder.itemView.setOnClickListener { onItemClick(member) }
    }

    override fun getItemCount(): Int {
        return members.size
    }

    inner class MemberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.tv_name)
        private val ageTextView: TextView = itemView.findViewById(R.id.tv_age)
        private val mobileTextView: TextView = itemView.findViewById(R.id.tv_mobile)

        fun bind(member: Member) {
            nameTextView.text = member.name
            ageTextView.text = member.age.toString()
            mobileTextView.text = member.mobile
        }
    }
}
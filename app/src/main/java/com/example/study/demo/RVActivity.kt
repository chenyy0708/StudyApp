package com.example.study.demo

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.study.databinding.ActivityRecyclerviewBinding

/**
 * Created by chenyy on 2021/5/28.
 */

class RVActivity : AppCompatActivity() {

    val items = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityRecyclerviewBinding.inflate(layoutInflater).apply {
            setContentView(root)
            recyclerView.layoutManager = LinearLayoutManager(this@RVActivity)
            recyclerView.adapter = Adapter().apply {
                items.add("sfwe")
                items.add("sdfwef")
                items.add("sfwewerwwe")
                items.add("sferwwe")
                items.add("sfbwebwwe")
                notifyDataSetChanged()
            }
        }
    }


    inner class Adapter : RecyclerView.Adapter<Adapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(TextView(parent.context))
        }

        override fun getItemCount(): Int = items.size

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val textView = itemView as? TextView
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.textView?.text = items[position]
        }
    }
}
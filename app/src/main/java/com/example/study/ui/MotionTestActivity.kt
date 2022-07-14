package com.example.study.ui

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.tracing.Trace
import com.example.study.databinding.ActivityMotionTestBinding
import com.sankuai.waimai.router.annotation.RouterUri

/**
 * Created by chenyy on 2021/5/28.
 */

@RouterUri(path = ["/motionTest"])
class MotionTestActivity : AppCompatActivity() {

    val items = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Trace.beginSection("MotionTest")
        ActivityMotionTestBinding.inflate(layoutInflater).apply {
            setContentView(root)
            recyclerView.layoutManager = LinearLayoutManager(this@MotionTestActivity)
            recyclerView.adapter = Adapter().apply {
                for (i in 0..200) {
                    items.add("测试item:${i}")
                }
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
            if (position == 100) {
                setItemData()
            }
        }

        private fun setItemData() {
            Trace.beginSection("setItemData")
            Thread.sleep(500L)
            Trace.beginSection("setItemData")
        }
    }

    override fun onDestroy() {
        Trace.endSection()
        super.onDestroy()
    }
}
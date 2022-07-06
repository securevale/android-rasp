package com.securevale.rasp.android.sample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChecksAdapter : RecyclerView.Adapter<ChecksAdapter.ViewHolder>() {

    var items = listOf<TestCheck>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_check, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(data: TestCheck) = with(itemView) {
            findViewById<TextView>(R.id.check_name).apply {
                text = data.name
            }

            setOnClickListener {
                context.startActivity(TestDetailsActivity.intent(context, data.checkData))
            }
        }
    }

    data class TestCheck(
        val name: String,
        val checkData: TestData
    )
}
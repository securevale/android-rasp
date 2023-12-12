package com.securevale.rasp.android.sample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

class AvailableChecksListAdapter : RecyclerView.Adapter<AvailableChecksListAdapter.ViewHolder>() {

    var items = listOf<Check>()

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
        fun bind(data: Check) = with(itemView) {
            findViewById<Button>(R.id.check_name).apply {
                text = data.name
            }

            setOnClickListener {
                context.startActivity(CheckDetailsActivity.intent(context, data.checkData))
            }
        }
    }

    data class Check(
        val name: String,
        val checkData: TestType
    )
}
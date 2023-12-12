package com.securevale.rasp.android.sample.check

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.securevale.rasp.android.api.result.ExtendedResult
import com.securevale.rasp.android.sample.R

class CheckResultAdapter : RecyclerView.Adapter<CheckResultAdapter.ViewHolder>() {

    private val items = mutableListOf<ExtendedResult>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_check_result, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun updateItems(extendedResult: ExtendedResult) {
        items.add(extendedResult)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(extendedResult: ExtendedResult) = with(itemView) {
            findViewById<TextView>(R.id.check).text = extendedResult.checkType.toString()
            val drawableResId =
                if (extendedResult.vulnerable) R.drawable.red_check else R.drawable.green_check
            findViewById<ImageView>(R.id.result).setImageDrawable(
                AppCompatResources.getDrawable(context, drawableResId)
            )
        }
    }
}

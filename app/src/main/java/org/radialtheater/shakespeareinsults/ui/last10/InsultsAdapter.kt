package org.radialtheater.shakespeareinsults.ui.last10

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import org.radialtheater.shakespeareinsults.R
import org.radialtheater.shakespeareinsults.db.Insult

class InsultsAdapter(private val insults: List<Insult>) :
    RecyclerView.Adapter<InsultsAdapter.ViewHolder>() {

    private lateinit var insultItemListener: InsultItemListener

    override fun getItemCount() = insults.size

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.insult_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, position: Int) {
        val insult = insults[position]
        holder.textView?.text = insult.insultText.replace("\n", " ")
        holder.itemView.setOnClickListener {
            insultItemListener.onInsultItemClick(insult.insultText)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView = itemView.findViewById<TextView?>(R.id.insult_text)
    }

    /**
     * Interface to handle button clicks
     */
    interface InsultItemListener {
        fun onInsultItemClick(insult: String)
    }

    /**
     * Receives reference to fragment implementing the listener interface
     */
    fun setItemListener(listener: InsultItemListener) {
        this.insultItemListener = listener
    }

}
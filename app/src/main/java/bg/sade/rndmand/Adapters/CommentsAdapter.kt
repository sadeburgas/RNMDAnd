package bg.sade.rndmand.Adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import bg.sade.rndmand.Model.Comment
import bg.sade.rndmand.R
import java.text.SimpleDateFormat
import java.util.*



class CommentsAdapter(val comments: ArrayList<Comment>) : RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.comment_list_view, p0, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return comments.count()
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.bindComment(comments[p1])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val username = itemView.findViewById<TextView>(R.id.commentListCommentTxt)
        val timestamp = itemView.findViewById<TextView>(R.id.commentListTimestamp)
        val commentTxt = itemView.findViewById<TextView>(R.id.commentListCommentTxt)

        fun bindComment(comment: Comment) {

            username.text = comment.username
            commentTxt.text = comment.commentTxt


            val dateFormatter = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())
            val dateString = dateFormatter.format(comment.timestamp)
            timestamp.text = dateString

        }

    }
}

package bg.sade.rndmand

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class ThoughtsAdapter(val thoughts: ArrayList<Thought>) : RecyclerView.Adapter<ThoughtsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.thought_list_view, p0, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return thoughts.count()
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.bindThought(thoughts[p1])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val username = itemView.findViewById<TextView>(R.id.listViewUsername)
        val timestamp = itemView.findViewById<TextView>(R.id.listViewTimestamp)
        val thoughtTxt = itemView.findViewById<TextView>(R.id.listViewThoughtTxt)
        val num_Likes = itemView.findViewById<TextView>(R.id.listViewNumLikesLbl)
        val likesImage = itemView.findViewById<ImageView>(R.id.listViewLikesImage)

        fun bindThought(thought: Thought){

            username.text = thought.username
            thoughtTxt.text = thought.thoughtTxt
            num_Likes.text = thought.num_Likes.toString()


            val dateFormatter = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())
            val dateString = dateFormatter.format(thought.timestamp)
            timestamp.text = dateString

            likesImage.setOnClickListener {
                FirebaseFirestore.getInstance().collection(THOUGHTS_REF).document(thought.documentId)
                        .update(NUM_LIKES, thought.num_Likes?.plus(1))
            }

        }
    }

}
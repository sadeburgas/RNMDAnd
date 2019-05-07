package bg.sade.rndmand.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import bg.sade.rndmand.R
import bg.sade.rndmand.Utilities.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_comments.*

class CommentsActivity : AppCompatActivity() {

    lateinit var thoughtDocumentId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)
        thoughtDocumentId = intent.getStringExtra(DOCUMENT_KEY)
        println(thoughtDocumentId)
    }

    fun addCommentClicked(view: View) {

        val commentTxt = enterCommentTxt.text.toString()
        val thoughtRef = FirebaseFirestore.getInstance().collection(THOUGHTS_REF).document(thoughtDocumentId)

        FirebaseFirestore.getInstance().runTransaction { transaction ->

            val thought = transaction.get(thoughtRef)
            val numComments = thought.getLong(NUM_COMMENTS)?.plus(1)
            transaction.update(thoughtRef, NUM_COMMENTS, numComments)

            val newCommentRef = FirebaseFirestore.getInstance().collection(THOUGHTS_REF)
                    .document(thoughtDocumentId).collection(COMMENTS_REF).document()
            val data = HashMap<String, Any>()
            data.put(COMMENT_TXT, commentTxt)
            data.put(TIMESTAMP, FieldValue.serverTimestamp())
            data.put(USERNAME, FirebaseAuth.getInstance().currentUser?.displayName.toString())

            transaction.set(newCommentRef, data)

        }
                .addOnSuccessListener {
                    enterCommentTxt.setText("")
                }
                .addOnFailureListener { exception ->
                    Log.e("Exception", "Could not add comment ${exception.localizedMessage}")
                }
    }
}

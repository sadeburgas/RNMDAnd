package bg.sade.rndmand.Activities

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import bg.sade.rndmand.Adapters.CommentsAdapter
import bg.sade.rndmand.Interfaces.CommentOptionsClickListener
import bg.sade.rndmand.Model.Comment
import bg.sade.rndmand.R
import bg.sade.rndmand.Utilities.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_comments.*
import java.util.*

class CommentsActivity : AppCompatActivity(), CommentOptionsClickListener {

    lateinit var thoughtDocumentId: String
    lateinit var commentsAdapter: CommentsAdapter
    val comments = arrayListOf<Comment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)
        thoughtDocumentId = intent.getStringExtra(DOCUMENT_KEY)
        println(thoughtDocumentId)

        commentsAdapter = CommentsAdapter(comments, this)
        commentListView.adapter = commentsAdapter
        val layoutManager = LinearLayoutManager(this)
        commentListView.layoutManager = layoutManager

        FirebaseFirestore.getInstance().collection(THOUGHTS_REF).document(thoughtDocumentId)
                .collection(COMMENTS_REF)
                .orderBy(TIMESTAMP, Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, exception  ->
                    if (exception != null) {
                        Log.e("Exception", "Could not retrieve comment ${exception.localizedMessage}")
                    }
                    if (snapshot != null) {
                        comments.clear()

                        for (document in snapshot.documents ) {
                            val data = document.data
                            val name = data!![USERNAME] as String
                            val timestamp = document.getDate(TIMESTAMP)
                            val commentTxt = data[COMMENT_TXT] as String
                            val documentId = data[COMMENT_TXT] as String
                            val userId = data[USER_ID] as String

                            val newComment = Comment(name, timestamp, commentTxt, documentId, userId)
                            comments.add(newComment)
                        }

                        commentsAdapter.notifyDataSetChanged()
                    }
                }
    }

    override fun optionMenuClicked(comment: Comment) {
        // this is where I present alert dialog.
        println(comment.commentTxt)
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
            data.put(USER_ID, FirebaseAuth.getInstance().currentUser?.uid.toString())
            transaction.set(newCommentRef, data)

        }
                .addOnSuccessListener {
                    enterCommentTxt.setText("")
                    hideKeyboard()
                }
                .addOnFailureListener { exception ->
                    Log.e("Exception", "Could not add comment ${exception.localizedMessage}")
                }
    }


    private fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if (inputManager.isAcceptingText) {
            inputManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }
    }



}


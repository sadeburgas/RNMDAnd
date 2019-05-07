package bg.sade.rndmand.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import bg.sade.rndmand.Utilities.DATE_CREATED
import bg.sade.rndmand.R
import bg.sade.rndmand.Utilities.USERNAME
import bg.sade.rndmand.Utilities.USERS_REF
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_create_user.*

class CreateUserActivity : AppCompatActivity() {

    lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)
        auth = FirebaseAuth.getInstance()
    }

    fun createCreateClicked(view: View) {
        val email = createEmailTxt.text.toString()
        val password = createPasswordTxt.text.toString()
        val username = createUsernameTxt.text.toString()

        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { result ->
                    // user is created
                    val changeRequest = UserProfileChangeRequest.Builder()
                            .setDisplayName(username)
                            .build()
                    result.user.updateProfile(changeRequest)
                            .addOnFailureListener {exception ->
                                Log.e("Exception", "Could not update display name: ${exception.localizedMessage}")
                            }
                    val data = HashMap<String, Any>()
                    data.put(USERNAME, username)
                    data.put(DATE_CREATED, FieldValue.serverTimestamp())

                    FirebaseFirestore.getInstance().collection(USERS_REF).document(result.user.uid)
                            .set(data)
                            .addOnSuccessListener {
                                finish()
                            }
                            .addOnFailureListener {exception ->
                                Log.e("Exception", "Could not add user document: ${exception.localizedMessage}")
                            }
                }
                .addOnFailureListener { exception ->
                    Log.e("Exception", "Could not create user: ${exception.localizedMessage}")
                }


    }

    fun createCanselClicked(view: View) {
        finish()
    }
}

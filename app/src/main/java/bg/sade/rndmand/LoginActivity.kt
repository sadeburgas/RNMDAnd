package bg.sade.rndmand

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun loginLoginClicked(view: View) {
        // Perform login
    }

    fun loginCreateClicked(view: View) {
        // segue to the create user
    }
}

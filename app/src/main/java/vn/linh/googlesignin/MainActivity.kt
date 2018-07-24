package vn.linh.googlesignin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.android.gms.auth.api.signin.GoogleSignIn
import android.content.Intent
import android.util.Log
import android.widget.Button
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    private val RC_SIGN_IN = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button_sign_in).setOnClickListener {
            signIn()
        }

        findViewById<Button>(R.id.button_sign_out).setOnClickListener {
            signOut()
        }
    }

    private fun getSignInOptions(): GoogleSignInOptions {
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail()
                .build()
    }

    private fun signIn() {
        val signInClient = GoogleSignIn.getClient(this, getSignInOptions())
        val signInIntent = signInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun signOut() {
        val signInClient = GoogleSignIn.getClient(this, getSignInOptions())
        signInClient.signOut()
        Log.i(TAG, "signOut ")
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            Log.i(TAG, "signInResult: email: " + account.email!!)
            Log.i(TAG, "signInResult: email: " + account.email!!)

            Toast.makeText(this, "Sign in successfully", Toast.LENGTH_SHORT).show()
        } catch (e: ApiException) {
            // GooglePlayService Disable => error code 12500 (GoogleSignInStatusCodes.html#SIGN_IN_FAILED)
            // GooglePlayService OutUpdate => error code 12500 (GoogleSignInStatusCodes.html#SIGN_IN_FAILED)
            // No Internet connection => still able to sign in (no error throw)

            // Service Missing: error 8 (CommonStatusCodes#INTERNAL_ERROR)
            // API unavailable => look like when don't need to  handle (consider API not connect)
            // Start resolution => look like when don't need to  handle
            //
            Toast.makeText(this,
                    "signInResult:failed code = " + e.statusCode, Toast.LENGTH_SHORT).show()
        }

    }

}

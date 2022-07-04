package com.example.libocean.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.InputType
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.libocean.R
import com.example.libocean.models.RegisterModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    lateinit var authen: FirebaseAuth
    lateinit var dbStore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val username: EditText = findViewById(R.id.register_username)
        username.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        val password: EditText = findViewById(R.id.register_password)
//        password.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        val name: EditText = findViewById(R.id.register_name)
        name.inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME
        val age: EditText = findViewById(R.id.register_age)
        age.inputType = InputType.TYPE_CLASS_NUMBER

        val register: Button = findViewById(R.id.button)
        initialized()
        register.setOnClickListener {
            registerUser(username, password, name, age)
        }
    }

    private fun initialized() {
        authen = FirebaseAuth.getInstance()
        dbStore = FirebaseFirestore.getInstance()
    }

    private fun registerUser(username: EditText, password: EditText, edit_name: EditText, edit_age: EditText) {

        var user: String = username.text.toString()
        var pass: String = password.text.toString()
        var name: String = edit_name.text.toString()
        var age: String = edit_age.text.toString()

        if (!validate(username, password))
            return
        else {
            if (age.isEmpty()) {
                age = 0.toString()
            }
            createUser(user, pass, Integer.parseInt(age), name)
        }
    }

    private fun createUser(user: String, pass: String, age: Int, name: String) {
        authen.createUserWithEmailAndPassword(user, pass).addOnCompleteListener(this) {
                task ->
            if (task.isSuccessful) {
                var id: String? = authen.currentUser?.uid
                id?.let { updateUser(id, name, Integer.parseInt(age.toString())) }
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Register Fail!!!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validate(username: EditText, password: EditText): Boolean {
        var user: String = username.text.toString()
        var pass: String = password.text.toString()
        var handler = Handler()

        if (user.isEmpty()) {
            username.error = "Must enter Email!!!"
            handler.postDelayed({username.error = null}, 5000)
            username.requestFocus()
            return false
        }else if (!Patterns.EMAIL_ADDRESS.matcher(user).matches()) {
            username.error = "Email is invalid!!!"
            handler.postDelayed({username.error = null}, 5000)
            username.requestFocus()
            return false
        }else if (pass.isEmpty()) {
            password.error = "Must enter Password!!!"
            handler.postDelayed({password.error = null}, 5000)
            password.requestFocus()
            return false
        }else if (pass.length < 6) {
            password.error = "Minimum length must be 6!!"
            handler.postDelayed({username.error = null}, 5000)
            password.requestFocus()
            return false
        }
        return true
    }

    private fun updateUser(id: String, edit_name: String, edit_age: Int) {
        dbStore.collection("UserId").document(id).set(RegisterModel(name = edit_name, age = edit_age))
    }
}
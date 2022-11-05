package com.stoe.wallpapers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_register.view.*

//2
class RegisterFragment : Fragment() {

    //puteam sa folosim si FirebaseRepository aici dar oricum ne trebuie auth pt register
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var navController: NavController? = null  //ca sa trecem de la un fragment la altul

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //setup nav controller
        navController = Navigation.findNavController(view)

        //check if user is already logged in
        if(firebaseAuth.currentUser == null){
            //not logged in, create acc
            //sign in user anonymously using firebase auth - fara detalii de la user
            register_text.text = "Syncing data"
            firebaseAuth.signInAnonymously().addOnCompleteListener {
                if (it.isSuccessful){
                    //acc created, go home
                    register_text.text = "Finishing up"
                    navController!!.navigate(R.id.action_registerFragment_to_homeFragment)
                } else {
                    //show error
                    register_text.text = "Error: ${it.exception!!.message}"
                }
            }
        } else {
            //logged in, send to home
            navController!!.navigate(R.id.action_homeFragment_to_registerFragment)
        }
    }

}
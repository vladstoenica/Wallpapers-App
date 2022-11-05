package com.stoe.wallpapers

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot

//1 dupa fragments
//e elegant sa il facem asa, kinda global in aplicatie ca sa nu trebuiasca sa declaram auth si firestore in fiecare clasa
//parte din MVVM
//chemem funtionalitatea in alte clase prin obiect firebaseRepository
class FirebaseRepository {

    //initialize auth, firestore

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firebaseFireStore: FirebaseFirestore = FirebaseFirestore.getInstance()

    var lastVisible: DocumentSnapshot? = null
    private val pagesize: Long = 6

    //return the currently logged in user
    fun getUser(): FirebaseUser? {     //? pt ca returneaza null daca nu e nimeni logat
        return firebaseAuth.currentUser
    }

    //3 - query the data from firestore and show it in the app
    fun queryWallpapers(): Task<QuerySnapshot> {    //returns query snapshot
        if(lastVisible == null){   //inseamna ca incarcam prima pagina
            //load first page
            return firebaseFireStore.collection("Wallpapers")
                .orderBy("date", Query.Direction.DESCENDING)
                .limit(9)
                .get()  //9 ca o sa avem 9 poze per pag
        } else {
            //load next page
            return firebaseFireStore.collection("Wallpapers")
                .orderBy("date", Query.Direction.DESCENDING)
                .startAfter(lastVisible!!)
                .limit(pagesize)
                .get()
        }
    }

}
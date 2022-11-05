package com.stoe.wallpapers

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentSnapshot

//5
class WallpapersViewModel: ViewModel(){

    //initialize wallpaper repository to load wallpaper data
    private val firebaseRepository: FirebaseRepository = FirebaseRepository()

    private val wallpapersList: MutableLiveData<List<WallpapersModel>> by lazy {
        MutableLiveData<List<WallpapersModel>>().also {
            loadWallpaperData()      //as soon as variable is initialised, it will start loading from firebase
        }
    }

    //used to observe the data from View class
    fun getWallpapersList(): LiveData<List<WallpapersModel>> {
        return wallpapersList
    }

    fun loadWallpaperData(){
        //query data from repo
        firebaseRepository.queryWallpapers().addOnCompleteListener {
            if (it.isSuccessful){
                val result = it.result
                if(result!!.isEmpty){
                    //no more wallpapers to load, reached end of list
                } else {
                    //results are ready to load
                    if(wallpapersList.value == null){     //fara if-ul asta, ne inlocuia pag 1 cu pag 2 cand dadeam scroll in jos, in loc sa adauge pag 2 la 1
                        //loading first page
                        wallpapersList.value = result.toObjects(WallpapersModel::class.java)
                    } else {
                        //load next page
                        wallpapersList.value = wallpapersList.value!!.plus(result.toObjects(WallpapersModel::class.java))
                    }
                    //once the results are ready, we can set the results to wallpapersList LiveData
//                    wallpapersList.value = result.toObjects(WallpapersModel::class.java)    //avem rezultatele gata -> cream un adaptor pt recycler wallListAdapter

                    //get the last document
                    var lastItem: DocumentSnapshot = result.documents[result.size() - 1]
                    firebaseRepository.lastVisible = lastItem
                }
            } else {
                //Error
                Log.d("ViewModelLog", "Error: ${it.exception!!.message}")
            }
        }
    }
}
package com.stoe.wallpapers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(), (WallpapersModel) -> Unit {

    //imediat dupa ce am creat repository class
    private val firebaseRepository = FirebaseRepository()
    private var navController: NavController? = null  //ca sa trecem de la un fragment la altul

    //7 initialize the adapter for recycler
    private var wallpapersList: List<WallpapersModel> = ArrayList()  //initializata dar goala
    private val wallpapersListAdapter: WallpapersListAdapter = WallpapersListAdapter(wallpapersList, this)

    private var isLoading: Boolean = true    //pt recycler - incarcat urmatoarele pagini

    val wallpapersViewModel: WallpapersViewModel by viewModels()  //pt onActivityCreated

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //initialize the action bar (declarata in xml)
        (activity as AppCompatActivity).setSupportActionBar(main_toolbar);

        //we can assign custom title to action bar
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar!!.title = "Wallpapers"

        //initialize nav controller
        navController = Navigation.findNavController(view)  //view din onViewCreated

        //check if user is logged in
        if (firebaseRepository.getUser() == null){
            //user not logged in, go to register page
            navController!!.navigate(R.id.action_homeFragment_to_registerFragment)
        }

        //initialize the recyclerview
        wallpapers_list_view.setHasFixedSize(true)
        wallpapers_list_view.layoutManager = GridLayoutManager(context, 3)
        wallpapers_list_view.adapter = wallpapersListAdapter

        //reached bottom of recycler - load the next set of wallpapers
        wallpapers_list_view.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                //if the user cant scroll anymore
                if(!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE){    //state idle inseamna ca e la sfarsit si nu mai poate sa dea in jos (nu se mai misca)
                    if(!isLoading){
                        //load next page
                        wallpapersViewModel.loadWallpaperData()
                        isLoading = true
                    }
                }
            }
        })

    }

    //ca sa luam data din ViewModel si sa o bagam in adapter, override-uim onActivityCreated
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //we can now observe the livedata obj, if anything changes in the livedata, this method fires up and returns the data that is added to the obj
        wallpapersViewModel.getWallpapersList().observe(viewLifecycleOwner, Observer {
            wallpapersList = it
            //add the list of data to the adapter and notify the adaptor that the list has been changed
            wallpapersListAdapter.wallpapersList = wallpapersList
            wallpapersListAdapter.notifyDataSetChanged()

            //Loading complete
            isLoading = false
        })
    }

    //implemented pt wallpapersListAdapter
    override fun invoke(wallpaper: WallpapersModel) {
        //functia asta e legata de clickListener si e called de fiecare data cand apasam un item din lista
        //ne trimite la details fragment
        val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(wallpaper.image)   //functia e facuta automat atunci cand in nav graph specificam ca vrem ceva transmis intre fragmente
        navController!!.navigate(action)
    }

}
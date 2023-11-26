package com.xperiencelabs.armenu.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.xperiencelabs.armenu.R
import com.xperiencelabs.armenu.adapter.DashboardRecyclerAdapter
import com.xperiencelabs.armenu.model.Event
import com.xperiencelabs.armenu.util.ConnectionManager
import java.util.*


class Dashboard : Fragment() {
    lateinit var recyclerDashboard: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var button: Button
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar

    val booklist = arrayListOf<Event>()
    //last expression is considered the return value in lambda functions
//    var ratingComparator= Comparator<Event>{book1,book2->
//        var result=book1.bookRating.compareTo(book2.bookRating,true)
//        if(result==0){
//            result=book1.bookName.compareTo(book2.bookName)
//        }
//        result
//
//
//        //if strings are identical the result is zero
//        //if string1 is greater than string2 then result is positive
//
//    }
//    val booklist = arrayListOf<Book>(
//        Book("P.S. I love You", "Cecelia Ahern", "Rs. 299", "4.5", R.drawable.ps_ily),
//        Book("The Great Gatsby", "F. Scott Fitzgerald", "Rs. 399", "4.1", R.drawable.great_gatsby),
//        Book("Anna Karenina", "Leo Tolstoy", "Rs. 199", "4.3", R.drawable.anna_kare),
//        Book("Madame Bovary", "Gustave Flaubert", "Rs. 500", "4.0", R.drawable.madame),
//        Book("War and Peace", "Leo Tolstoy", "Rs. 249", "4.8", R.drawable.war_and_peace),
//        Book("Lolita", "Vladimir Nabokov", "Rs. 349", "3.9", R.drawable.lolita),
//        Book("Middlemarch", "George Eliot", "Rs. 599", "4.2", R.drawable.middlemarch),
//        Book(
//            "The Adventures of Huckleberry Finn",
//            "Mark Twain",
//            "Rs. 699",
//            "4.5",
//            R.drawable.adventures_finn
//        ),
//        Book("Moby-Dick", "Herman Melville", "Rs. 499", "4.5", R.drawable.moby_dick),
//        Book("The Lord of the Rings", "J.R.R Tolkien", "Rs. 749", "5.0", R.drawable.lord_of_rings)
//    )

    lateinit var recyclerAdapter: DashboardRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        var view = inflater.inflate(
            R.layout.fragment_dashboard,
            container,
            false
        )//similar to setcontentView
        var connection: ConnectionManager = ConnectionManager()
        button = view.findViewById(R.id.btncheckConnectivity)
        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressBar)
        progressLayout.visibility = View.VISIBLE
        setHasOptionsMenu(true)
        button.setOnClickListener {
            if (connection.checkConnectivity(activity as Context)) {
                //display message using dialog box
                val dialog = AlertDialog.Builder(activity as Context)
                dialog.setTitle("Success")
                dialog.setMessage("Internet Connection Found")
                dialog.setPositiveButton("Ok") { text, listener ->
                    //do nothing
                }
                dialog.setNegativeButton("Cancel") { text, listener ->
                    //do nothing
                }
                dialog.create()
                dialog.show()

            } else {
                val dialog = AlertDialog.Builder(activity as Context)
                dialog.setTitle("Failure")
                dialog.setMessage("Internet Connection Not Found")
                dialog.setPositiveButton("Ok") { text, listener ->


                }
                dialog.setNegativeButton("Cancel") { text, listener ->

                }
                dialog.create()
                dialog.show()


            }


        }

        //recyclerDashboard=getView()?.findViewById(R.id.recyclerDashboard)
        recyclerDashboard = view.findViewById(R.id.recyclerDashboard)
        // Inflate the layout for this fragment

        layoutManager = LinearLayoutManager(activity)


        val queue = Volley.newRequestQueue(activity as Context)

        val url = "http://172.20.10.5:3000/"
        if (ConnectionManager().checkConnectivity(activity as Context)) {
            val jsonObjectRequest =
                object : JsonObjectRequest(Method.GET, url, null, Response.Listener {
                    try {
                        val success = it.getBoolean("success")
                        if (success) {
                            progressLayout.visibility = View.INVISIBLE
                            val data = it.getJSONArray("data")
                            for (i in 0 until data.length()) {
                                val bookJsonObject = data.getJSONObject(i)
                                val bookobj = Event(

                                    bookJsonObject.getInt("id"),
                                    bookJsonObject.getString("venue"),
                                    bookJsonObject.getString("event"),
                                    bookJsonObject.getString("event_desc"),
                                    bookJsonObject.getString("date"),
                                    bookJsonObject.getString("price"),
                                    bookJsonObject.getString("location"),
                                    bookJsonObject.getDouble("distance")

                                )
                                booklist.add(bookobj)


                            }
                            recyclerAdapter = DashboardRecyclerAdapter(
                                activity as Context,
                                booklist
                            ) //has to be typecasted as this is a user defined class and therefore (does not have many constructors)
                            //there is no dynamic type casting in kotlin


                            //initializing recycler view with all its components

                            recyclerDashboard.adapter = recyclerAdapter
                            recyclerDashboard.layoutManager = layoutManager
//                                recyclerDashboard.addItemDecoration(
//                                    DividerItemDecoration(
//                                        activity,
//                                        (layoutManager as LinearLayoutManager).orientation
//                                    )
//                                )
                        } else {
                            if (activity != null) {
                                Toast.makeText(
                                    activity,
                                    "Some Error Occurred!",
                                    Toast.LENGTH_SHORT
                                )//when unsuccessful
                                    .show()
                            }

                        }
                    } catch (e: Exception) {//for JSON exception i.e the resources can be corrupted or due to server issues
                        Toast.makeText(
                            activity,
                            "Some Unexpected Error Occurred!",
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                },
                    Response.ErrorListener {
                        //for handling Volley Errors
                        Toast.makeText(
                            activity,
                            it.message,
                            Toast.LENGTH_SHORT
                        ).show()

                    })

                {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "6bee6da45559ef"
                        return headers
                    }


                }




            queue.add(jsonObjectRequest)
        } else {
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Failure")
            dialog.setMessage("Internet Connection Not Found")
            dialog.setPositiveButton("Open Settings") { text, listener ->
                val settingsIntent = Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit") { text, listener ->
                ActivityCompat.finishAffinity(activity as Activity) //closes apps even when there are more than 1 activity are running
            }
            dialog.create()
            dialog.show()
        }


//        val stringJsonobj=object:StringRequest(Method.GET,url,Response.Listener {
//
//        },Response.ErrorListener {  }
//
//        )

        return view
    }
    //false because we do not want any one fragment to attach permanently

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_dashboard, menu)


    }


}
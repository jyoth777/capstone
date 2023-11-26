package com.xperiencelabs.armenu.adapter





import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.xperiencelabs.armenu.R
import com.xperiencelabs.armenu.activity.Recommendation
import com.xperiencelabs.armenu.fragment.Dashboard
import com.xperiencelabs.armenu.fragment.Favourite
import com.xperiencelabs.armenu.model.Event
import org.json.JSONObject


class FragmentRecyclerAdapter(val context: Context, val itemList: ArrayList<Event>) :
    RecyclerView.Adapter<FragmentRecyclerAdapter.DashboardViewHolder>() {//links adapter class and the recycler view together also provides view holder to the recycler view

    //arraylist contains the data
    //parent is the recyclerview here
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_dashboard_single_row, parent, false)

        return DashboardViewHolder(view)
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        //here we should ensure the correct data goes into the correct position
        //data can be fetched from:1.data locally and display static list 2.from the internet 3.from the user's device (eg:list of songs stored in the user's phone)
        //data can be sent from the activity or the fragment
        holder.fav.setImageResource(R.drawable.ic_favouriteselected)
        val book = itemList[position]
        holder.venue.text = book.venue//giving new item with different value each time

        holder.event.text = book.event
        holder.eventDesc.text = book.eventDesc
        holder.location.text = book.location
        holder.date.text = book.date
        holder.location.text = book.location
        holder.price.text = book.price
        holder.distance.text = book.distance.toString()
        val queue = Volley.newRequestQueue(context)
        val url = "http://172.20.10.5:3000/remove"
        holder.fav.setOnClickListener {

            val jsonobj = JSONObject()
            System.out.println("i");
            jsonobj.put("likedEventId", book.id);

            val reqobj =
                object : JsonObjectRequest(Request.Method.POST, url, jsonobj, {
                    try {

                        val success = it.getBoolean("success")
                        if (success) {

                            Toast.makeText(
                                context,
                                "Event removed from favourites",
                                Toast.LENGTH_SHORT
                            ).show()

                            (context as Recommendation?)?.supportFragmentManager?.beginTransaction()?.replace(
                                R.id.frame,
                                Favourite()
                            )?.commit()


                        } else {
                            Toast.makeText(
                                context,
                                "${it.getJSONObject("data").getString("errorMessage")}",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    } catch (e: Exception) {
                        Toast.makeText(
                            context,
                            "Unexpected error",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }, {
                    Toast.makeText(
                        context,
                        "Unexpected Volley error",
                        Toast.LENGTH_SHORT
                    ).show()
                }) {



                    override fun getBody(): ByteArray {
                        val params: MutableMap<String, String> = java.util.HashMap()
                        // on below line we are passing our key
                        // and value pair to our parameters.
                        // on below line we are passing our key
                        // and value pair to our parameters.
                        params["likedEventId"] = book.id.toString();

                        return JSONObject(params as Map<*, *>?).toString().toByteArray()
                    }
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "6bee6da45559ef"
                        return headers


                    }

                    override fun getParams(): MutableMap<String, String>? {
                        val params: MutableMap<String, String> = java.util.HashMap()
                        // on below line we are passing our key
                        // and value pair to our parameters.
                        // on below line we are passing our key
                        // and value pair to our parameters.
                        params["likedEventId"] = book.id.toString();

                        return params
                    }

                }
            reqobj.retryPolicy = DefaultRetryPolicy(
                3000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
            queue.add(reqobj);
//            Toast.makeText(context, "Clicked on ${book.bookName}", Toast.LENGTH_SHORT).show()
//            var intent = Intent(context, DescriptionActivity::class.java)
//            intent.putExtra("book_id",book.bookId)
//            startActivity(context, intent, null)


        }

    }

    override fun getItemCount(): Int {
        return itemList.size//returns number of items in the list
    }

    class DashboardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //view is the view which is contained inside the view holder here it is the recycler_dashboard_single_line
        val llContent: RelativeLayout = view.findViewById(R.id.llContent)
        val venue: TextView = view.findViewById(R.id.txtVenue)
        val event: TextView = view.findViewById(R.id.txtEvent)

        val price: TextView = view.findViewById(R.id.txtPrice)
        val fav: ImageView = view.findViewById(R.id.imgstar)
        val eventDesc: TextView = view.findViewById(R.id.txtEventDesc)
        val date: TextView = view.findViewById(R.id.txtDate)
        val location: TextView = view.findViewById(R.id.txtLocation)
        val distance: TextView = view.findViewById(R.id.txtDistance)


    }

}


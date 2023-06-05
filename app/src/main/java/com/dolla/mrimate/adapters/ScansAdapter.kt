package com.dolla.mrimate.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.dolla.mrimate.databinding.ItemListBinding
import com.dolla.mrimate.pojo.Scan
import com.dolla.mrimate.util.deserializeBitmap

/**
 * @created 05/06/2023 - 3:18 PM
 * @project MRIMate
 * @author adell
 */

class ScansAdapter :
    RecyclerView.Adapter<ScansAdapter.ScansViewHolder>() { // Adapter class for the RecyclerView in the ListFragment

    // lambda function that will be used to handle the click event on the Scan  in the RecyclerView
    lateinit var onScanClicked: (Scan) -> Unit // This function will be called when a Scan is clicked
    private var scansList = ArrayList<Scan>() // This list will hold the list of scans

    fun setScansList(scansList: ArrayList<Scan>) { // This function will be used to set the list of scans to the adapter and notify the adapter that the data set has changed
        this.scansList = scansList
        notifyDataSetChanged() // Notify the adapter that the data set has changed
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ScansViewHolder { // This function will be called to create a new ViewHolder object instance and return it to the RecyclerView when it needs a new item view to display.
        // Initialize the view binding object instance using the layout file name
        return ScansViewHolder(
            ItemListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: ScansViewHolder,
        position: Int
    ) { // This function will be called to bind the data to the view holder object instance

        // set animation to the view holder Patient item view (card view) when it is created for the first time only
        holder.itemView.startAnimation(
            AnimationUtils.loadAnimation(
                holder.itemView.context,
                android.R.anim.slide_in_left
            )
        )

        holder.binding.ivItemImage.setImageBitmap(deserializeBitmap(scansList[position].scanImage))
        holder.binding.tvItemTitle.text =
            scansList[position].classType // Set the Scan Item name to the TextView
        holder.binding.tvItemSubtitle.text =
            "${scansList[position].percentage} %" // Set the Scan Item name to the TextView

        holder.itemView.setOnClickListener { // Set the click listener on the root view of the Scan item view in the RecyclerView (.xml)
            onScanClicked(scansList[position]) // Call the lambda function to handle the click event on the Scan Item Scan
        }
    }

    override fun getItemCount(): Int =
        scansList.size // This function will return the number of scans in the list

    // ViewHolder class that takes in the view binding object instance of the .xml layout file and extends the RecyclerView.ViewHolder class to hold the view binding object instance
    inner class ScansViewHolder(val binding: ItemListBinding) :
        RecyclerView.ViewHolder(binding.root) // ViewHolder class that takes in the view binding object instance of the .xml layout file and extends the RecyclerView.ViewHolder class to hold the view binding object instance

}

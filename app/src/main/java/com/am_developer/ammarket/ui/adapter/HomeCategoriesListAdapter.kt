package com.am_developer.ammarket.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.am_developer.ammarket.R

class HomeCategoriesListAdapter : RecyclerView.Adapter<HomeCategoriesListAdapter.MyViewHolder>() {

    private val listCategoryTitle = arrayOf(
        "Bakery", "Meats", "Grocery", "Mornings", "PetShop",
        "Beverages", "Cleaning", "Hygiene", "Fruits&Vegetables", "Dairy"
    )

    private val listCategoryImg = arrayOf(
        R.drawable.bread, R.drawable.meat, R.drawable.grocery,
        R.drawable.breakfast, R.drawable.pets, R.drawable.softdrinks,
        R.drawable.bucket, R.drawable.shampoo, R.drawable.healthy_food,
        R.drawable.dairy_products
    )

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var homeCategoryTitle: TextView = view.findViewById(R.id.tv_home_category_title)
        var homeCategoryIMG: ImageView = view.findViewById(R.id.iv_home_category_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_category_holder, parent, false)
        return MyViewHolder(v)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.homeCategoryTitle.text = listCategoryTitle[position]
        holder.homeCategoryIMG.setImageResource(listCategoryImg[position])
    }

    override fun getItemCount(): Int = listCategoryTitle.size
}
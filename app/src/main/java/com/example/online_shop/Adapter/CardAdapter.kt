package com.example.online_shop.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.online_shop.Helper.ManagmentCart
import com.example.online_shop.Model.ItemModel
import com.example.online_shop.databinding.ViewholderCartBinding
import com.example.online_shop.databinding.ViewholderSizeBinding
import com.example.online_shop.Helper.ChangeNumberItemsListener

class CardAdapter(private var listItemSelected:ArrayList<ItemModel>,
    context: Context,
    var changeNumberItemsListener:ChangeNumberItemsListener?=null):RecyclerView.Adapter<CardAdapter.ViewHolder>() {
    class ViewHolder (val binding: ViewholderCartBinding):RecyclerView.ViewHolder(binding.root){

    }
    private val managementCart= ManagmentCart(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardAdapter.ViewHolder {
        val binding=ViewholderCartBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardAdapter.ViewHolder, position: Int) {
        val item = listItemSelected[position]
        holder.binding.titleTxt.text=item.title
        holder.binding.feeEachItem.text="$${item.price}"
        holder.binding.totalEachItem.text="$${Math.round(item.price*item.numberInCart)}"
        holder.binding.numberItemTxt.text=item.numberInCart.toString()

        Glide.with(holder.itemView.context)
            .load(item.picUrl[0])
            .apply(RequestOptions().transform(CenterCrop()))
            .into(holder.binding.pic)

        holder.binding.plusCartBtn.setOnClickListener{
            managementCart.plusItem(listItemSelected,position,object : ChangeNumberItemsListener{
                override fun onChanged() {
                    notifyDataSetChanged()
                    changeNumberItemsListener?.onChanged()
                }

            })
        }

        holder.binding.minusCartBtn.setOnClickListener{
            managementCart.minusItem(listItemSelected,position,object : ChangeNumberItemsListener{
                override fun onChanged() {
                    notifyDataSetChanged()
                    changeNumberItemsListener?.onChanged()
                }

            })
        }
    }

    override fun getItemCount(): Int = listItemSelected.size
}
package com.example.online_shop.activity

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.online_shop.Adapter.ColorAdapter
import com.example.online_shop.Adapter.SizeAdapter
import com.example.online_shop.Adapter.SliderAdapter
import com.example.online_shop.Helper.ManagmentCart
import com.example.online_shop.Model.ItemModel
import com.example.online_shop.Model.SliderModel
import com.example.online_shop.R
import com.example.online_shop.databinding.ActivityDetailBinding
import com.example.online_shop.databinding.ViewholderColorBinding

class DetailActivity : BaseActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var item:ItemModel
    private var numberOrder=1
    private lateinit var managmentCart: ManagmentCart
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        managmentCart=ManagmentCart(this)

        getBundle()
        banners()
        intitLists()
    }

    private fun intitLists() {
        var sizeList = ArrayList<String>()
        for(size in item.size){
            sizeList.add(size.toString())
        }
        binding.sizeList.adapter=SizeAdapter(sizeList)
        binding.sizeList.layoutManager=LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)

        val colorList = ArrayList<String>()
        for (imageUrl in item.picUrl){
            colorList.add(imageUrl)
        }
        binding.colorList.adapter=ColorAdapter(colorList)
        binding.colorList.layoutManager=LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
    }

    private fun banners() {
        val sliderItems=ArrayList<SliderModel>()
        for(imagesUrl in item.picUrl ){
            sliderItems.add(SliderModel(imagesUrl))
        }
        binding.slider.adapter=SliderAdapter(sliderItems,binding.slider)
        binding.slider.clipToPadding = true
        binding.slider.clipChildren = true
        binding.slider.offscreenPageLimit = 1

        if(sliderItems.size>1){
            binding.dotIndicator.visibility=View.VISIBLE
            binding.dotIndicator.attachTo(binding.slider)
        }
    }

    private fun getBundle(){
        item = intent.getParcelableExtra("object")!!
        binding.titleTxt.text=item.title
        binding.descriptionTxt.text=item.description
        binding.priceTxt.text="$"+  item.price
        binding.ratingTxt.text="${item.rating} Rating"
        binding.addToCartBtn.setOnClickListener{
            item.numberInCart=numberOrder
            managmentCart.insertFood(item)
        }
        binding.backBtn.setOnClickListener{finish()}
        binding.cartBtn.setOnClickListener{

        }
    }
}
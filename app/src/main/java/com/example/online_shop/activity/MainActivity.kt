package com.example.online_shop.activity

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.online_shop.Model.SliderModel
import com.example.online_shop.R
import com.example.online_shop.SliderAdapter
import com.example.online_shop.ViewModel.MainViewModel
import com.example.online_shop.databinding.ActivityIntroBinding
import com.example.online_shop.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val viewModel = MainViewModel()
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)


        initBanner()
    }

    private fun initBanner() {
        binding.progessBarBanner.visibility = View.VISIBLE
        viewModel.banner.observe(this, Observer { items ->
        banners(items)
        binding.progressBarBrand.visibility=View.GONE
        })
        viewModel.loadBanner()
    }

    private fun banners(images: List<SliderModel>) {
        binding.viewpageSlider.adapter = SliderAdapter(images, binding.viewpageSlider)
        binding.viewpageSlider.clipToPadding = false
        binding.viewpageSlider.clipChildren = false
        binding.viewpageSlider.offscreenPageLimit = 3
        binding.viewpageSlider.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER


        val compositePageTransformer = CompositePageTransformer().apply {
            addTransformer(MarginPageTransformer(40))
        }
        binding.viewpageSlider.setPageTransformer(compositePageTransformer)
        if (images.size > 1) {
            binding.dotIndicator.visibility = View.VISIBLE
            binding.dotIndicator.attachTo(binding.viewpageSlider)
        }
    }
}
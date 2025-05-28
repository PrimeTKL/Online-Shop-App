package com.example.online_shop.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.online_shop.Adapter.BrandAdapter
import com.example.online_shop.Adapter.PopularAdapter
import com.example.online_shop.Model.SliderModel
import com.example.online_shop.Adapter.SliderAdapter
import com.example.online_shop.ViewModel.MainViewModel
import com.example.online_shop.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val viewModel = MainViewModel()
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBottomMenu()
        initBanner()
        initBrand()
        initPopular()
    }

    private fun initBottomMenu() {
        binding.cartBtn.setOnClickListener {
            startActivity(
                Intent(
                    this@MainActivity,
                    CartActivity::class.java
                )
            )
        }
    }

    private fun initBanner() {
        binding.progessBarBanner.visibility = View.VISIBLE
        viewModel.banners.observe(this, Observer { items ->
            banners(items)
            binding.progressBarBrand.visibility = View.GONE
        })
        viewModel.loadBanner()
    }

    private fun banners(images: List<SliderModel>) {
        binding.viewpagerSlider.adapter = SliderAdapter(images, binding.viewpagerSlider)
        binding.viewpagerSlider.clipToPadding = false
        binding.viewpagerSlider.clipChildren = false
        binding.viewpagerSlider.offscreenPageLimit = 3
        binding.viewpagerSlider.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER


        val compositePageTransformer = CompositePageTransformer().apply {
            addTransformer(MarginPageTransformer(40))
        }
        binding.viewpagerSlider.setPageTransformer(compositePageTransformer)
        if (images.size > 1) {
            binding.dotIndicator.visibility = View.VISIBLE
            binding.dotIndicator.attachTo(binding.viewpagerSlider)
        }
    }

    private fun initBrand() {
        binding.progressBarPopular.visibility = View.VISIBLE
        viewModel.brands.observe(this, Observer {
            binding.viewBrand.layoutManager = LinearLayoutManager(
                this@MainActivity,
                LinearLayoutManager.HORIZONTAL, false
            )
            binding.viewBrand.adapter = BrandAdapter(it)
            binding.progressBarPopular.visibility = View.GONE
        })
        viewModel.loadBrand()
    }

    private fun initPopular() {
        binding.progressBarPopular.visibility = View.VISIBLE
        viewModel.popular.observe(this, Observer {
            binding.viewPopular.layoutManager = GridLayoutManager(this@MainActivity, 2)
            binding.viewPopular.adapter = PopularAdapter(it)
            binding.progressBarPopular.visibility = View.GONE
        })
        viewModel.loadPopular()
    }
}
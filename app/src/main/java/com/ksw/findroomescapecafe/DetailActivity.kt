package com.ksw.findroomescapecafe

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.ksw.findroomescapecafe.databinding.ActivityDetailBinding
import com.ksw.findroomescapecafe.model.RoomsModel
import com.ksw.findroomescapecafe.util.BaseAppCompatActivity

/**
 * Created by KSW on 2021-09-08
 */

class DetailActivity : BaseAppCompatActivity() {

    private var binding: ActivityDetailBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val data: RoomsModel = intent.getSerializableExtra("data") as RoomsModel

        Glide.with(binding!!.ivDetail.context)
            .load(data.imgUrl)
            .into(binding!!.ivDetail)


           /*if (binding!!.tvDetailTitle.text.isEmpty()) {
               "정보 없음"
           } else {
               data.title
           }

           if (binding!!.tvAddressInfo.text.isEmpty()) {
               "정보 없음"
           } else {
               data.address
           }

           if (binding!!.tvPhoneInfo.text.isEmpty()) {
               "정보 없음"
           } else {
               data.tel
           }*/

        binding!!.tvDetailTitle.text = data.title ?: "정보 없음"
        binding!!.tvAddressInfo.text = data.address ?: "정보 없음"
        binding!!.tvPhoneInfo.text = data.tel ?: "정보 없음"

        if (!data.price.isNullOrBlank()) binding!!.tvPriceInfo.text = data.price.replace(" | ", "\n")
        if (!data.hourInfo.isNullOrBlank()) binding!!.tvOpenTimeInfo.text = data.hourInfo.replace(" | ", "\n")
        if (data.hasBooking) {
            binding!!.reserveButton.visibility = View.VISIBLE
            binding!!.reserveButton.setOnClickListener {
                goReserveSite(data.bookingUrl)
            }
        }

        binding!!.shareButton.setOnClickListener {
            shareInfo(data)
        }

    }

    private fun goReserveSite(bookingUrl: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(bookingUrl)
        startActivity(intent)
    }

    private fun shareInfo(data: RoomsModel) {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                "[방탈출 같이가자! \uD83C\uDFC3] 지금 \"${data.title}\"를 확인해보세요. https://m.place.naver.com/place/${data.id} GoGO!"
            )
            type = "text/plain"
        }
        startActivity(Intent.createChooser(intent, null))
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}
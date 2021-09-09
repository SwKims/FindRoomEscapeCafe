package com.ksw.findroomescapecafe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.ksw.findroomescapecafe.api.RoomService
import com.ksw.findroomescapecafe.databinding.ActivityMainBinding
import com.ksw.findroomescapecafe.model.RoomsModel
import com.ksw.findroomescapecafe.model.RoomsResponse
import com.ksw.findroomescapecafe.util.BaseAppCompatActivity
import com.ksw.findroomescapecafe.util.Constant.Companion.BASE_URL
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : BaseAppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding!!.searchButton.setOnClickListener {
            if (binding!!.searchText.text.isNullOrEmpty()) {
                binding!!.textInput.error = "검색어를 입력해주세요"
            } else {
                val searchWord = binding!!.searchText.text.toString()
                getRoomCafeList(searchWord)
            }
        }

    }


    private fun getRoomCafeList(searchWord: String) {
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(RoomService::class.java).also {
            it.getRoomCafeList(query = (searchWord.replace(" ", "") + " 방탈출카페"))
                .enqueue(object : Callback<RoomsResponse> {
                    override fun onResponse(
                        call: Call<RoomsResponse>,
                        response: Response<RoomsResponse>
                    ) {
                        if (response.isSuccessful.not()) {
                            Toast.makeText(
                                this@MainActivity, "에러", Toast.LENGTH_SHORT
                            ).show()
                            return
                        }
                        response.body()?.let { noRoomCafe ->
                            if (noRoomCafe.roomsResult.place == null) {
                                binding!!.textInput.error = "검색 결과가 없습니다."
                                return
                            }
                            val dataList = noRoomCafe.roomsResult.place.items
                            val intent = Intent(this@MainActivity, ResultActivity::class.java)
                            val list: ArrayList<RoomsModel> = ArrayList<RoomsModel>(dataList)
                            intent.putExtra("dataList", list)
                            startActivity(intent)
                        }
                    }

                    override fun onFailure(call: Call<RoomsResponse>, t: Throwable) {

                    }

                })


        }
    }
}
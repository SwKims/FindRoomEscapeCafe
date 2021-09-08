package com.ksw.findroomescapecafe

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.ksw.findroomescapecafe.adapter.RoomListAdapter
import com.ksw.findroomescapecafe.adapter.RoomViewPagerAdapter
import com.ksw.findroomescapecafe.databinding.ActivityResultBinding
import com.ksw.findroomescapecafe.databinding.BottomSheetBinding
import com.ksw.findroomescapecafe.model.RoomsModel
import com.ksw.findroomescapecafe.util.Constant.Companion.LOCATION_PERMISSION_REQUEST_CODE
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import com.naver.maps.map.widget.LocationButtonView

/**
 * Created by KSW on 2021-09-08
 */

class ResultActivity : AppCompatActivity(), OnMapReadyCallback, Overlay.OnClickListener {

    private var binding: ActivityResultBinding? = null

    //    private var binding2: BottomSheetBinding? = null
    private lateinit var locationSource: FusedLocationSource

    private val mapView: MapView by lazy {
        binding!!.mapView
    }

    private val viewPager2: ViewPager2 by lazy {
        binding!!.escapeRoomViewPager
    }

    private val currentLocationButton: LocationButtonView by lazy {
        binding!!.currentLocation
    }

    private val bottomSheetTextView: TextView by lazy {
//        binding2!!.tvBottomSheet
        findViewById(R.id.tv_bottomSheet)
    }

    private val viewPagerAdapter = RoomViewPagerAdapter(itemClicked = {
        DetailActivityGo(it)
    })

    private val recyclerViewPagerAdapter = RoomListAdapter(itemClicked = {
        DetailActivityGo(it)
    })

    private val recyclerView: RecyclerView by lazy {
//        binding2!!.bottomRecyclerView
        findViewById(R.id.bottom_recyclerView)
    }

    lateinit var dataList: ArrayList<RoomsModel>

    private lateinit var naverMap: NaverMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        mapView.onCreate(savedInstanceState)
        dataList = intent.getSerializableExtra("dataList") as ArrayList<RoomsModel>


        mapView.getMapAsync(this)
        viewPager2.adapter = viewPagerAdapter
        recyclerView.adapter = recyclerViewPagerAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                val selectedRoomsModel = viewPagerAdapter.currentList[position]
                val cameraUpdate =
                    CameraUpdate.scrollTo(LatLng(selectedRoomsModel.lat, selectedRoomsModel.lng))
                        .animate(CameraAnimation.Easing)

                naverMap.moveCamera(cameraUpdate)

            }
        })

    }

    private fun DetailActivityGo(roomsModel: RoomsModel) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("data", roomsModel)
        startActivity(intent)
    }

    override fun onMapReady(map: NaverMap) {
        naverMap = map

        naverMap.maxZoom = 18.0
        naverMap.minZoom = 10.0

        val uiSettings = naverMap.uiSettings

        uiSettings.isLocationButtonEnabled = false
        uiSettings.setLogoMargin(30, 0, 0, 230)

        currentLocationButton.map = naverMap

        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        naverMap.locationSource = locationSource

        setListWithData(dataList)

    }

    @SuppressLint("SetTextI18n")
    private fun setListWithData(dataList: java.util.ArrayList<RoomsModel>) {
        updateMarker(dataList)
        viewPagerAdapter.submitList(dataList)
        recyclerViewPagerAdapter.submitList(dataList)
        bottomSheetTextView.text = "${dataList.size}개의 결과"
    }

    private fun updateMarker(rooms: java.util.ArrayList<RoomsModel>) {
        rooms.forEach { roomsModel ->
            val marker = Marker()
            marker.position = LatLng(roomsModel.lat, roomsModel.lng)
            marker.onClickListener = this
            marker.map = naverMap
            marker.tag = roomsModel.id
            marker.icon = MarkerIcons.RED
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return
        }
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated) {
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
        binding = null
//        binding2 = null
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onClick(overlay: Overlay): Boolean {
        val selectedModel = viewPagerAdapter.currentList.firstOrNull() {
            it.id == overlay.tag
        }
        selectedModel?.let {
            val position = viewPagerAdapter.currentList.indexOf(it)
            viewPager2.currentItem = position
        }
        return true
    }


}
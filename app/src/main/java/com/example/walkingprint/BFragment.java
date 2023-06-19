package com.example.walkingprint;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import java.util.ArrayList;
import java.util.List;


public class BFragment extends Fragment {

    private MapView mapView;
    private MapboxMap map;
    private int count = 0;
    private List<Object> locationDataList;
    private Button startButton;
    private Button endButton;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final long LOCATION_UPDATE_INTERVAL = 5000;
    private FusedLocationProviderClient fusedLocationClient;
    private Handler handler;
    private Runnable locationUpdateRunnable;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_b, container, false);

        // 맵박스 사용하기 위한 접근 토큰 지정
        Mapbox.getInstance(requireContext(), getString(R.string.access_token));

        // Setup the MapView
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        locationDataList = new ArrayList<>();
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                map = mapboxMap;
                startLocationUpdates();
            }
        });

        startButton = view.findViewById(R.id.startButton);
        endButton = view.findViewById(R.id.endButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startButton.setVisibility(View.GONE);
                endButton.setVisibility(View.VISIBLE);
                // Add your code to start the timer and marker placement here.
            }
        });

        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endButton.setVisibility(View.GONE);
                startButton.setVisibility(View.VISIBLE);
                // Add your code to stop the timer and marker placement here.
            }
        });

        return view;
    }


    private void startLocationUpdates() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            requestLocation();
            startLocationUpdateRunnable();
        } else {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void startLocationUpdateRunnable() {
        handler = new Handler(Looper.getMainLooper());
        locationUpdateRunnable = new Runnable() {
            @Override
            public void run() {
                requestLocation();
                handler.postDelayed(this, LOCATION_UPDATE_INTERVAL);
            }
        };
        handler.postDelayed(locationUpdateRunnable, LOCATION_UPDATE_INTERVAL);
    }

    private void requestLocation() {
        // 위치 권한이 허용되어 있는지 확인
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // 위치 권한이 허용된 경우 위치 업데이트 요청
            getLastKnownLocation();
        } else {
            // 위치 권한 요청
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 위치 권한이 허용된 경우 위치 업데이트 요청
                getLastKnownLocation();
            } else {
                // 위치 권한이 거부된 경우 처리할 작업 추가
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void getLastKnownLocation() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            updateCurrentLocation(location);
                        } else {
                            // 위치 정보가 null인 경우 처리할 작업 추가
                        }
                    }
                });
    }

    private void updateCurrentLocation(@NonNull Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        long currentTime = System.currentTimeMillis();

        count++;

        Object[] locationData = new Object[]{latitude, longitude, currentTime};
        // 예시로 현재 위치 출력
        System.out.println("[" + count + "] 현재 위치: " + latitude + ", " + longitude);
        System.out.println("현재 시간" + currentTime);
        locationDataList.add(locationData);

        // 위치 정보를 기반으로 마커 설정 등의 작업 수행
        LatLng currentLocation = new LatLng(latitude, longitude);
        map.addMarker(new MarkerOptions().position(currentLocation).title("현재 위치"));
        map.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
        map.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}

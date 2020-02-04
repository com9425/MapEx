package com.example.mapex;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener {
    Spinner spSeoulTour;
    Button btnMyPosition;
    private GoogleMap mMap;
    String seoul[] = {"국립중앙박물관", "남산골 한옥마을", "예술의 전당", "청계천", "63빌딩",
            "서울타워", "경복궁", "김치문화체험관", "서울올림픽기념관",
            "국립민속박물관", "서대문형무소역사관", "창덕궁"};
    String tourName;
    Double lat[] = {37.5240867, 37.5591447, 37.4785361, 37.5696512, 37.5198158, 37.5511147,
            37.5788408, 37.5629457, 37.5202976, 37.5815645, 37.5742887, 37.5826041};
    Double lng[] = {126.9803881, 126.9936826, 127.0107423, 127.0056375, 126.9403139,
            126.9878596, 126.9770162, 126.9851652, 127.1159236, 126.9789313,
            126.9562269, 126.9919376};
    Double tourLatLng[] = new Double[2];
    Double myLatLng[] = new Double[2];
    int pos;
    FusedLocationProviderApi providerApi;
    GoogleApiClient apiClient;
    /*LocationManager myLocation;
    LocationListener listener;
    String locationProvider;*/
    boolean chk = false;
    int pCheck;

    //onCreate 메서드 시작
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        spSeoulTour = (Spinner) findViewById(R.id.spSeoulTour);
        btnMyPosition = (Button) findViewById(R.id.btnMyPosition);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, seoul);
        spSeoulTour.setAdapter(adapter);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        pCheck = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (pCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }
        //setMyLocation();
        apiClient=new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        providerApi=LocationServices.FusedLocationApi;
        spSeoulTour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                chk = false;
                tourLatLng[0] = lat[position];
                tourLatLng[1] = lng[position];
                pos = position;
                tourMove(tourLatLng);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnMyPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chk = true;
                apiClient.connect();
                tourMove(myLatLng);
            }
        });
    }
    //onResume메서드 시작


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();
        apiClient.connect();
        /*if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        myLocation.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                1, 1000, (android.location.LocationListener) listener);*/
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    //map 콜백 함수 호출에 의한 onMapReady메서드 시작
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
       /* LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
    }

    //옵션 메뉴 생성 메서드 시작
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, 1, 0, "일반지도");
        menu.add(0, 2, 0, "위성지도");
        return true;
    }

    //옵션 메뉴 선택시 수행할 작업 메서드 시작
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case 2:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //위도, 경도를 받아 해당 위치로 이동할 지도표시 부분
    private void tourMove(Double locationLatLng[]) {
        LatLng tourPos = new LatLng(locationLatLng[0], locationLatLng[1]);
        String address[] = {"서울특별시 용산구 서빙고로 137",
                "서울특별시 중구 퇴계로34길 28",
                "서울특별시 서초구 남부순환로 2364",
                "서울특별시 종로구 창신동",
                "서울특별시 영등포구 63로 50 한화금융센터",
                "서울특별시 용산구 남산공원길 105",
                "서울특별시 종로구 삼청로 37",
                "서울특별시 중구 명동2가 32-2",
                "서울특별시 송파구 올림픽로 448",
                "서울특별시 종로구 삼청로 37",
                "서울특별시 서대문구 통일로 251 독립공원",
                "서울특별시 종로구 율곡로 99"};
        final String tel[] = {"02-2077-9000", "02-2264-4412", "02-580-1300",
                "02-2290-6114", "02-789-5663", "02-3455-9277",
                "02-3700-3900", "02-318-7051", "02-410-1354",
                "02-3704-3114", "02-360-8590", "02-762-8261"};
        final String homePage[] = {"http://www.museum.go.kr",
                "http://hanokmaeul.seoul.go.kr",
                "http://www.sac.or.kr",
                "http://www.cheonggyecheon.or.kr",
                "http://www.63.co.kr",
                "http://www.nseoultower.com",
                "http://www.royalpalace.go.kr",
                "http://www.visitseoul.net/kr/article/article.do?_method=view&art_id=49160&lang=kr&m=0004003002009&p=03",
                "http://www.88olympic.or.kr",
                "http://www.nfm.go.kr",
                "http://www.sscmc.or.kr/culture2",
                "http://www.cdg.go.kr"};
        MarkerOptions markerOpt;
        if(locationLatLng[0]==null) {
            Toast.makeText(getApplicationContext(),"내 위치 정보를 아직 받지 못했습니다.",Toast.LENGTH_SHORT).show();
        }else {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tourPos, 15));
        }
        markerOpt = new MarkerOptions();
        markerOpt.position(tourPos);

        if (chk == false) {
            markerOpt.title(address[pos]);
            markerOpt.snippet(tel[pos]);
        } else {
            markerOpt.title("현재 내 위치");
            markerOpt.snippet("위도 : " + myLatLng[0] + "경도 : " + myLatLng[1]);
        }
        markerOpt.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
        mMap.addMarker(markerOpt).showInfoWindow();
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //해당 관광지의 홈페이지 연결
                if (chk == false) {
                    Uri uri = Uri.parse(homePage[pos]);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
                return false;
            }
        });
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                //해당 관광지에 전화연결
                if (chk == false) {
                    Uri uri = Uri.parse("tel:" + tel[pos]);
                    Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Location location=providerApi.getLastLocation(apiClient);
        if(location !=null) {
            myLatLng[0]=location.getLatitude();
            myLatLng[1]=location.getLongitude();
        }else {
            //위치 정보를 아직 받아오지 못했습니다.
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

   /* private void setMyLocation() {
            myLocation=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
            //locationProvider=myLocation.getBestProvider(new Criteria(), true);
            //myLocation.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        myLocation.isProviderEnabled(LocationManager.GPS_PROVIDER);
            listener=new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    myLatLng[0]=location.getLatitude();
                    myLatLng[1]=location.getLongitude();
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                    switch (status) {
                        case LocationProvider.OUT_OF_SERVICE:
                            //통신 서비스 구역을 벗어남
                            break;
                        case LocationProvider.TEMPORARILY_UNAVAILABLE:
                            //일시적 불능 상태
                            break;
                        case LocationProvider.AVAILABLE:
                            //사용 가능
                            break;
                    }
                }

                @Override
                public void onProviderEnabled(String provider) {
                    //통신 서비스 사용가능
                }

                @Override
                public void onProviderDisabled(String provider) {
                    //통신 서비스 사용불가
                }
            };
    }*/
}

package com.arturo.google_maps_app;



import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends FragmentActivity {
	private GoogleMap mapa;
	private int vista = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mapa = ((SupportMapFragment) getSupportFragmentManager()
				   .findFragmentById(R.id.map)).getMap();
        
        //Para el click corto sobre el mapa
        mapa.setOnMapClickListener(new OnMapClickListener() {
			public void onMapClick(LatLng point) {
				Projection proj = mapa.getProjection();//se obtiene la proyeccion del mapa
				Point coord = proj.toScreenLocation(point);//se guardan las coordenadas
				//Toast con información del punto pulsado
				Toast.makeText(
						MainActivity.this, 
						"Click\n" + 
						"Lat: " + point.latitude + "\n" +
						"Lng: " + point.longitude + "\n" +
						"X: " + coord.x + " - Y: " + coord.y,
						Toast.LENGTH_SHORT).show();
			}
		});
        
        //Para el click continuado/largo
        mapa.setOnMapLongClickListener(new OnMapLongClickListener() {
			public void onMapLongClick(LatLng point) {
				Projection proj = mapa.getProjection();
				Point coord = proj.toScreenLocation(point);
				
				Toast.makeText(
						MainActivity.this, 
						"Click Largo\n" + 
						"Lat: " + point.latitude + "\n" +
						"Lng: " + point.longitude + "\n" +
						"X: " + coord.x + " - Y: " + coord.y,
						Toast.LENGTH_SHORT).show();
			}
		});
        
        //Para los movimientos del mapa
        mapa.setOnCameraChangeListener(new OnCameraChangeListener() {
			public void onCameraChange(CameraPosition position) {
				Toast.makeText(
						MainActivity.this, 
						"Cambio Cámara\n" + 
						"Lat: " + position.target.latitude + "\n" +
						"Lng: " + position.target.longitude + "\n" +
						"Zoom: " + position.zoom + "\n" +
						"Orientación: " + position.bearing + "\n" +
						"Ángulo: " + position.tilt,
						Toast.LENGTH_SHORT).show();
			}
		});
        
        //Para la pulsación sobre un marcador
        mapa.setOnMarkerClickListener(new OnMarkerClickListener() {
			public boolean onMarkerClick(Marker marker) {
				Toast.makeText(
						MainActivity.this, 
						"Marcador pulsado:\n" + 
						marker.getTitle(),
						Toast.LENGTH_SHORT).show();
				return false;
			}
		});
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
    	switch(item.getItemId())
		{
			case R.id.menu_vista:
				alternarVista();
				break;
			case R.id.menu_mover:
				//Centramos el mapa en Espa�a
				CameraUpdate camUpd1 = 
					CameraUpdateFactory.newLatLng(new LatLng(40.41, -3.69));
				mapa.moveCamera(camUpd1);
				break;
			case R.id.menu_animar:
				//Centramos el mapa en Espa�a y con nivel de zoom 5
				CameraUpdate camUpd2 = 
					CameraUpdateFactory.newLatLngZoom(new LatLng(40.41, -3.69), 5F);
				mapa.animateCamera(camUpd2);
				break;
			case R.id.menu_3d:
				LatLng madrid = new LatLng(40.417325, -3.683081);
				CameraPosition camPos = new CameraPosition.Builder()
					    .target(madrid)   //Centramos el mapa en Madrid
					    .zoom(19)         //Establecemos el zoom en 19
					    .bearing(45)      //Establecemos la orientaci�n con el noreste arriba
					    .tilt(70)         //Bajamos el punto de vista de la c�mara 70 grados
					    .build();
				
				CameraUpdate camUpd3 = 
						CameraUpdateFactory.newCameraPosition(camPos);
				
				mapa.animateCamera(camUpd3);
				break;
			case R.id.menu_posicion:
				CameraPosition camPos2 = mapa.getCameraPosition();
				LatLng pos = camPos2.target;
				Toast.makeText(MainActivity.this, 
						"Lat: " + pos.latitude + " - Lng: " + pos.longitude, 
						Toast.LENGTH_LONG).show();
				break;
			case R.id.menu_marcadores:
				mostrarMarcador(44.51, 33.98);
				break;
			case R.id.menu_lineas:
				mostrarLineas();
				break;
		}
		
		return super.onOptionsItemSelected(item);
	
    }
    
    private void alternarVista()
	{
		vista = (vista + 1) % 4;
		
		switch(vista)
		{
			case 0:
				mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				break;
			case 1:
				mapa.setMapType(GoogleMap.MAP_TYPE_HYBRID);
				break;
			case 2:
				mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
				break;
			case 3:
				mapa.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
				break;
		}
	}
    
    private void mostrarMarcador(double lat, double lng)
	{
		mapa.addMarker(new MarkerOptions()
        .position(new LatLng(lat, lng))//Posición para el marcador
        .title("Aquí está Sebastopol"));//Texto del marcador
		
	}
    
    private void mostrarLineas()
	{
		//Triángulo con líneas
		
//		PolylineOptions lineas = new PolylineOptions()
//	        .add(new LatLng(46.40, 34.40))
//	        .add(new LatLng(44.0, 33.0))
//	        .add(new LatLng(43.80, 36.80))
//	        .add(new LatLng(46.40, 34.40));
//
//		lineas.width(3);
//		lineas.color(Color.MAGENTA);
//
//		mapa.addPolyline(lineas);
		
		//Triángulo con polígono
		
		PolygonOptions triangulo = new PolygonOptions()
		            .add(new LatLng(46.40, 34.40))
		  	        .add(new LatLng(44.0, 33.0))
			        .add(new LatLng(43.80, 36.80))
			        .add(new LatLng(46.40, 34.40));
		
		triangulo.strokeWidth(3);
		triangulo.strokeColor(Color.MAGENTA);
		
		mapa.addPolygon(triangulo);
	}
}

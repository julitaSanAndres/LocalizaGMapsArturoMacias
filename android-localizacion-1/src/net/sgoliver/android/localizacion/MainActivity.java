package net.sgoliver.android.localizacion;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private Button btnActualizar;
	private Button btnDesactivar;
	private TextView lblLatitud; 
	private TextView lblLongitud;
	private TextView lblExactitud;
	private TextView lblEstado;
	
	private LocationManager locManager;
	private LocationListener locListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		btnActualizar = (Button)findViewById(R.id.BtnActualizar);
        btnDesactivar = (Button)findViewById(R.id.BtnDesactivar);
        lblLatitud = (TextView)findViewById(R.id.LblPosLatitud);
        lblLongitud = (TextView)findViewById(R.id.LblPosLongitud);
        lblExactitud = (TextView)findViewById(R.id.LblPosExactitud);
        lblEstado = (TextView)findViewById(R.id.LblEstado);
        
        
        
        btnActualizar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				comenzarLocalizacion();
			}
		});
        
        btnDesactivar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
		    	locManager.removeUpdates(locListener);
			}
		});
	}
	
    private void comenzarLocalizacion()
    {
    	//Obtenemos una referencia al LocationManager
    	locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    	
    	//Comprobación inicial. Si el GPS está desactivado lanza un toast
        if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
        	Toast.makeText(this, "GPS desactivado", Toast.LENGTH_SHORT).show();
        }
    	
    	//Obtenemos la �ltima posici�n conocida
    	Location loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    	
    	//Mostramos la �ltima posici�n conocida
    	mostrarPosicion(loc);
    	
    	//Nos registramos para recibir actualizaciones de la posición
    	locListener = new LocationListener() {
	    	public void onLocationChanged(Location location) {
	    		mostrarPosicion(location);
	    	}
	    	public void onProviderDisabled(String provider){
	    		lblEstado.setText("Provider OFF");
	    	}
	    	public void onProviderEnabled(String provider){
	    		lblEstado.setText("Provider ON ");
	    	}
	    	public void onStatusChanged(String provider, int status, Bundle extras){
	    		Log.i("", "Provider Status: " + status);
	    		lblEstado.setText("Provider Status: " + status);
	    	}
    	};
    	
    	locManager.requestLocationUpdates(
    			LocationManager.GPS_PROVIDER, 30000, 0, locListener); 	
    }
     
    private void mostrarPosicion(Location loc) {
    	if(loc != null)
    	{
    		lblLatitud.setText("Latitud: " + String.valueOf(loc.getLatitude()));
    		lblLongitud.setText("Longitud: " + String.valueOf(loc.getLongitude()));
    		lblExactitud.setText("Exactitud: " + String.valueOf(loc.getAccuracy()));
    		Log.i("", String.valueOf(loc.getLatitude() + " - " + String.valueOf(loc.getLongitude())));
    	}
    	else
    	{
    		lblLatitud.setText("Latitud: (sin_datos)");
    		lblLongitud.setText("Longitud: (sin_datos)");
    		lblExactitud.setText("Exactitud: (sin_datos)");
    	}
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

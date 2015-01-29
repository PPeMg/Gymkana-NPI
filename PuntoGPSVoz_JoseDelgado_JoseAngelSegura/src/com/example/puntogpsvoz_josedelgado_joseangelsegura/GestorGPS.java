/*
 *	Copyright 2014 José Delgado Dolset and Jose Angel Segura Muros.
 *
 *	This file is part of the fourth practise in the subject of
 *	"Nuevos Paradigmas de la Interacción" in the Computer Engineering's
 *	Degree from University of Granada.
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.example.puntogpsvoz_josedelgado_joseangelsegura;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;

public class GestorGPS extends Service implements LocationListener{
	
	private final Context mContext;
	
	// Controla que la posición sea obtenible.
    boolean posicionObtenible = false;
    
    // Variables relativas a la posición geográfica
    Location posicion;
    double latitud;
    double longitud;
	
    // Estado del GPS
    private boolean okGPS = false;
 
    // Distancia mínima entre actualizaciones
    private static final long DISTANCIA_ACTUALIZACION = 10;
 
    // Retardo mínimo en milisegundos
    private static final long RETARDO_ACTUALIZACION = 60 * 1000;
 
    // Estado de la Red
    private boolean redActiva = false;
    
    // EL Gestor de Posicion será un objeto de la clase LocationManager
    protected LocationManager gestorPosicion;
 
    public GestorGPS(Context context) {
        this.mContext = context;
        getPosicion();
    }
    
    public Location getPosicion() {
        try {
            gestorPosicion = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);
 
            // Obtenemos el estado de la red:
            redActiva = gestorPosicion
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
 
            // Obtenemos el estado del GPS:
            okGPS = gestorPosicion
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            
            if (!okGPS && !redActiva) {
                // No se recibe información de ningún tipo:
            	
            } else {
                this.posicionObtenible = true;
                
                // Obtenemos la posición a partir de la red siempre que esta 
                // esté activa.
                if (redActiva) {
                    gestorPosicion.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            RETARDO_ACTUALIZACION,
                            DISTANCIA_ACTUALIZACION, this);
                    Log.d("Network", "Network");
                    
                    if (gestorPosicion != null) {
                        posicion = gestorPosicion
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (posicion != null) {
                            latitud = posicion.getLatitude();
                            longitud = posicion.getLongitude();
                        }
                    }
                }
                
                // Usamos el GPS para obtener la posición si
                // el sensor GPS está disponible y no lo hemos 
                // obtenido mediante la red.
                if (okGPS) {
                    if (posicion == null) {
                        gestorPosicion.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                RETARDO_ACTUALIZACION,
                                DISTANCIA_ACTUALIZACION, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        
                        if (gestorPosicion != null) {
                            posicion = gestorPosicion
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (posicion != null) {
                                latitud = posicion.getLatitude();
                                longitud = posicion.getLongitude();
                            }
                        }
                    }
                }
            }
 
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        return posicion;
    }
    
    /**
     * Metodo para obtener la latitud.
     * */
    public double getLatitud(){
        if(posicion != null){
            latitud = posicion.getLatitude();
        }
         
        // return latitude
        return latitud;
    }
     
    /**
     * Metodo para obtener la longitud.
     * */
    public double getLongitud(){
        if(posicion != null){
            longitud = posicion.getLongitude();
        }
         
        return longitud;
    }
    
    /**
     * Metodo que devuelve true solo si la posición es obtenible
     * */
    public boolean esPosicionObtenible() {
        return this.posicionObtenible;
    }
     
    /**
     * Metodo para mostrar un aviso cuando el GPS esta desactivado:
     * */
    public void lanzarAviso(){
        AlertDialog.Builder aviso = new AlertDialog.Builder(mContext);
      
        // Establecer titulo del aviso
        aviso.setTitle(R.string.titulo_dialogo);
  
        // Establecer mensaje del aviso
        aviso.setMessage(R.string.mensaje_dialogo);
  
        // Handler para cuando se pulsa el botón de respuesta afirmativa.
        // Se lanza una nueva Intent hacia la pantalla de opciones correspondiente:
        aviso.setPositiveButton(R.string.activar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });
  
        // Handler para cuando se pulsa el botón de respuesta negativa.
        // Se ejecuta el metodo cancel. Cancela la activacion del GPS.
        aviso.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            }
        });
  
        // Una vez creado el aviso, lo mostramos.
        aviso.show();
    }
	
    
    /** 
     * El resto de métodos son creados por el IDE.
     **/
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}
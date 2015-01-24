/*
 *  Copyright (C) 2014, 2015 - Antonio Doncel Campos <adoncel@correo.ugr.es>, Hugo Mario Lupi�n Fern�ndez <hugolupionfernandez@correo.ugr.es>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * Clase encargada de leer el c�digo QR y mostrarlo en una caja de texto
 * @author: Antonio Doncel Campos, Hugo Mario Lupi�n Fern�ndez
 * @version: 28/12/14-0
 */

package com.josan.segmur.puntogestosqr_josesegura_josedolset;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.metaio.sdk.ARViewActivity;
import com.metaio.sdk.MetaioDebug;
import com.metaio.sdk.jni.IGeometry;
import com.metaio.sdk.jni.IMetaioSDK;
import com.metaio.sdk.jni.IMetaioSDKCallback;
import com.metaio.sdk.jni.IRadar;
import com.metaio.sdk.jni.LLACoordinate;
import com.metaio.sdk.jni.TrackingValues;
import com.metaio.sdk.jni.TrackingValuesVector;
import com.metaio.tools.io.AssetsManager;



public class QReader extends ARViewActivity{
	/**
     * Texto donde se visualizar� la informaci�n del QR
     */
    private TextView mText;
    private double latitud_num;
    private double longitud_num;
	private AlertDialog mAlert;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mText = new TextView(this);
        mGUIView = mText;
    }

    @Override
    protected int getGUILayout()
    {
        return 0;
    }

    /**
     * Muestra el texto en la pantalla
     * @param data String a ser visualizados
     */
    private void displayText(final String data)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run() {

                mText.setText(data);
            }
        });
    }

    @Override
    protected IMetaioSDKCallback getMetaioSDKCallbackHandler()
    {
        return new MetaioSDKCallbackHandler();
    }

    /**
     * Carga el tipo de c�digo a leer
     */
    @Override
    protected void loadContents()
    {
        // set QR code reading configuration
        final boolean result = metaioSDK.setTrackingConfiguration("QRCODE");
        MetaioDebug.log("Tracking data loaded: " + result);
    }

    /**
     * Clase encargada de reconocer los l�mites del c�digo
     * y extraer la informaci�n que contiene
     */
    final class MetaioSDKCallbackHandler extends IMetaioSDKCallback
    {
        @Override
        public void onTrackingEvent(TrackingValuesVector trackingValues)
        {
            if (trackingValues.size() > 0)
            {
                final TrackingValues v = trackingValues.get(0);

                if (v.isTrackingState())
                {
                    final String[] tokens = v.getAdditionalValues().split("::");
                    if (tokens.length > 1)
                    {
                        String direccion = tokens[1];
                        String latitud = "", longitud = "";
                        int cont = 0;
                        boolean num = false;
                        for(int i = 0; i < direccion.length(); i++){
                        	if(num){
                        		if(direccion.charAt(i) == '_'){
                            		cont++;
                            		num = false;
                            	}
                        		else{
                        			if(cont == 1)
                        				latitud += direccion.charAt(i);
                        			else
                        				longitud += direccion.charAt(i);
                        		}
                        	}
                        	else if(direccion.charAt(i) == '_'){
                        		cont++;
                        		num = true;
                        	}
                        }

                        try {
				    		latitud_num = Double.parseDouble(latitud);
				    		longitud_num = Double.parseDouble(longitud);
				    		displayText("Latitud: "+latitud_num+" Longitud: "+longitud_num);
						} catch (Exception e) {}

                        runOnUiThread(new Runnable() {
	                        @Override
	                        public void run() {
	                            if (mAlert == null) {
	                                mAlert = new AlertDialog.Builder(QReader.this)
	                                        .setTitle("Scanned QR-Code")
	                                        .setMessage(tokens[1])
	                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog,int id) {
                                                    //
                                                    //
                                                    Intent returnBtn = new Intent(getApplicationContext(), MainActivity.class);

                                                    startActivity(returnBtn);
                                                    QReader.this.finish();
                                                }
                                            })
	                                        .create();
	                            }
	                            if (!mAlert.isShowing()) {
	                                mAlert.setMessage(tokens[1]);
	                                mAlert.show();
	                            }
	                        }
	                    });
                    }
                }
            }
        }
    }

	@Override
	protected void onGeometryTouched(final IGeometry geometry) {
	}
}

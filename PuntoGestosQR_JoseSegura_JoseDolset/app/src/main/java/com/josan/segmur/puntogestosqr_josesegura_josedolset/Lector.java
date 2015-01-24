/*
 *  Copyright (C) 2014, 2015 - Jose Angel Segura Muros <shaljas@correo.ugr.es>, Jose Delgado Dolset <jdeldo@gmail.com>
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
 * Actividad que lee un codigo QR dado
 * @author: Jose Angel Segura Muros, Jose Delgado Dolset
 * @version: 1.0
 * @date: 25/01/2015
 *
 * Todo el codigo de lectura de QRs asi como la libreria metaioSDK pertenecen a
 * http://www.metaio.com/sdk/
 */
package com.josan.segmur.puntogestosqr_josesegura_josedolset;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.metaio.sdk.ARViewActivity;
import com.metaio.sdk.MetaioDebug;
import com.metaio.sdk.jni.IGeometry;
import com.metaio.sdk.jni.IMetaioSDKCallback;
import com.metaio.sdk.jni.TrackingValues;
import com.metaio.sdk.jni.TrackingValuesVector;

/**
 * Clase para la actividad Lector, se encarga de leer los codigos QR
 */
public class Lector extends ARViewActivity{

	private AlertDialog mAlert;

    /**
     * Funcion de creacion, se limita a llamar al constructor de su padre
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    /**
     * Funcion necesaria para ARViewActivity
     * @return
     */
    @Override
    protected int getGUILayout()
    {
        return 0;
    }

    /**
     * Funcion que implementa el callback tras la lectura de un codigo
     * @return
     */
    @Override
    protected IMetaioSDKCallback getMetaioSDKCallbackHandler()
    {
        return new MetaioSDKCallbackHandler();
    }

    /**
     * Funcion para la carga de contenidos, es necesaria para ARViewActivity
     */
    @Override
    protected void loadContents()
    {
        // set QR code reading configuration
        final boolean result = metaioSDK.setTrackingConfiguration("QRCODE");
        MetaioDebug.log("Tracking data loaded: " + result);
    }

    /**
     * Clase para determinar los codigos y descifrarlos
     */
    final class MetaioSDKCallbackHandler extends IMetaioSDKCallback
    {
        /**
         * Recibe el feedback de la camara, en caso de haber obtenido informacion de un
         * codigo la muestra mediante un AlertDialog
         * @param trackingValues
         */
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
                        runOnUiThread(new Runnable() {
	                        @Override
	                        public void run() {
	                            if (mAlert == null) {
	                                mAlert = new AlertDialog.Builder(Lector.this)
	                                        .setTitle("Codigo escaneado:")
	                                        .setMessage(tokens[1])
	                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog,int id) {

                                                    Intent returnMain = new Intent(getApplicationContext(), MainActivity.class);
                                                    startActivity(returnMain);
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

    /**
     * Funcion necesaria para IMetaioSDKCallback
     * @param geometry Geometry that is touched
     */
	@Override
	protected void onGeometryTouched(final IGeometry geometry) {
	}
}

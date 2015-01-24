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
 * Clase principal para la ejecuci�n del lector de c�digos QR
 * @author: Antonio Doncel Campos, Hugo Mario Lupi�n Fern�ndez
 * @version: 28/12/14-0
 */

package com.josan.segmur.puntogestosqr_josesegura_josedolset;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

//implements GestureDetector.OnGestureListener,
//GestureDetector.OnDoubleTapListener

public class MainActivity extends Activity {
	
	/**
     * Boton que lanzara la segunda actividad
     */
	public Button button_qreader;
	
	//private static final String DEBUG_TAG = "Gestures";
	//private GestureDetectorCompat mDetector;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_qreader = (Button) findViewById(R.id.qrreader_button);
        button_qreader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	lanzar_QRReader(v);
            }
        });
    }

	/**
     * M�todo para ejecturar otra actividad
     */
	public void lanzar_QRReader(View view) {
        Intent i = new Intent(this, LockPattern.class );
        startActivity(i);
	}
}

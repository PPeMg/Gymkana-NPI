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
 * Actividad principal para la aplicación que acepta un patron de movimiento y lee un codigo QR
 * @author: Jose Angel Segura Muros, Jose Delgado Dolset
 * @version: 1.0
 * @date: 25/01/2015
 */

package com.josan.segmur.puntogestosqr_josesegura_josedolset;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Clase principal de la actividad
 */
public class MainActivity extends Activity {
	
	/**
     * Boton que inicia la aplicacion
     */
	public Button launch_Button;

    /**
     * Metodo de la creacion de la actividad, inicializa los componentes de la misma
     * @param savedInstanceState
     */
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        launch_Button = (Button) findViewById(R.id.qrreader_button);
        launch_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	ejecutaDP(v);
            }
        });
    }

	/**
     * Metodo para la ejecucion de DetectorPatrones
     */
	public void ejecutaDP(View view) {
        Intent i = new Intent(this, DetectorPatrones.class );
        startActivity(i);
	}
}

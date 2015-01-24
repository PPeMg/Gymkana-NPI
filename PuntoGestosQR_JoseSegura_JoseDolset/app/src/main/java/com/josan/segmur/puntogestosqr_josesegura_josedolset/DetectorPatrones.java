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
 * Todo el codigo de deteccion de patrones, junto con todas las clases que tienen [[Lock]] en su nombre pertecen a
 * http://www.metaio.com/sdk/
 */

package com.josan.segmur.puntogestosqr_josesegura_josedolset;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Clase para la actividad encargada de leer los patrones de movimiento
 */
public class DetectorPatrones extends Activity{
	private static final int REQUEST_CODE_SET_LOCK_PATTERN = 10001;
	private static final int REQUEST_CODE_VERIFY_LOCK_PATTERN = 10002;

	protected PatternView mView = null;

    /**
     * Si no existe un patron guardado en el movil lanza la aplicacion de reconocimiento de patrones
     * en caso contrario lanza la de creacion de patrones
     * @param savedInstanceState
     */
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//
		mView = new PatternView(this);
		//
		String savedData = LockPatternUtils.loadFromPreferences(this);
		if (savedData == null) {
			Intent intent = new Intent(this, SetLockPatternActivity.class);
			startActivityForResult(intent, REQUEST_CODE_SET_LOCK_PATTERN);
		} else {
			Intent intent = new Intent(this, VerifyLockPatternActivity.class);
			startActivityForResult(intent, REQUEST_CODE_VERIFY_LOCK_PATTERN);
		}
	}

    /**
     * Funcion que recibe el resultado de la finalizacion de las actividades lanzables
     * en el onCreate. Lanza los intents necesarios a cada situacion.
     * @param requestCode
     * @param resultCode
     * @param data
     */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_CODE_SET_LOCK_PATTERN:
			if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Patron guardado", Toast.LENGTH_SHORT).show();
                Intent returnMain = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(returnMain);

			} else {
                Toast.makeText(getApplicationContext(), "Patron no guardado", Toast.LENGTH_SHORT).show();
                Intent returnMain = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(returnMain);
			}
			break;

		case REQUEST_CODE_VERIFY_LOCK_PATTERN:
			if (resultCode == Activity.RESULT_OK) {
				Intent i = new Intent(this, Lector.class );
		        startActivity(i);
			} else {
                Toast.makeText(getApplicationContext(), "Patron incorrecto", Toast.LENGTH_SHORT).show();
                Intent returnMain = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(returnMain);
			}
			break;
		}
	}
}

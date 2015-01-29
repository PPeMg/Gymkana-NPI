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

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	// Default values for the language model and maximum number of recognition
	// results. They are shown in the GUI when the app starts, and they are used
	// when the
	// user selection is not valid
	private final static int DEFAULT_NUMBER_RESULTS = 10;
	private final static String DEFAULT_LANG_MODEL = RecognizerIntent.LANGUAGE_MODEL_FREE_FORM;

	private int numberRecoResults = DEFAULT_NUMBER_RESULTS;
	private String languageModel = DEFAULT_LANG_MODEL;

	private static final String LOGTAG = "ASRBEGIN";
	private static int LATITUD_CODE = 123;
	private static int LONGITUD_CODE = 321;

	// Conjunto de Botones a usar:
	private Button GPS_Button;

	// Gestor de GPS:
	private GestorGPS gestor_GPS;

	// Coordenadas de destino:
	private double longitud_dst;
	private double latitud_dst;

	// Coordenadas en STRING
	private String longitud_text;
	private String latitud_text;

	/**
	 * Los siguientes métodos están sacados de la aplicación ASRWithIntent de
	 * Zoraida Callejas y Michael McTear.
	 * 
	 * @author Zoraida Callejas
	 * @author Michael McTear
	 * @see https
	 *      ://github.com/zoraidacallejas/sandra/tree/master/Apps/ASRWithIntent
	 * @version 1.7, 01/22/14
	 */

	/**
	 * Initializes the speech recognizer and starts listening to the user input
	 */
	private void listen(int codigo) {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

		// Specify language model
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, languageModel);

		// Specify how many results to receive
		intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, numberRecoResults);

		// Start listening
		startActivityForResult(intent, codigo);
	}

	/**
	 * El resto de métodos son de programación propia, utilizando la
	 * documentación existente.
	 */

	public void reconocerLongitud(View v) {
		// Llamamos al reconocedor con el código de la longitud
		listen(LONGITUD_CODE);
	}

	public void reconocerLatitud(View v) {
		// Llamamos al reconocedor con el código de la latitud
		listen(LATITUD_CODE);
	}

	public void lanzarGPS(View arg0) {
		double longitud_act, latitud_act;

		// Inicializamos el gestor del GPS
		this.gestor_GPS = new GestorGPS(MainActivity.this);
		
		// Comprobamos que esté activado:
		if (this.gestor_GPS.esPosicionObtenible()) {

			// Obtenemos la posición actual:
			latitud_act = this.gestor_GPS.getLatitud();
			longitud_act = this.gestor_GPS.getLongitud();

			// Si la posición de destino está inicializada, es decir, si es
			// distinta de 0:
			if (this.latitud_dst != 0 && this.longitud_dst != 0) {
				// Establecemos la página correspondiente de Google Maps:
				String direccionURL = "http://maps.google.com/maps?saddr="
						+ latitud_act + "," + longitud_act + "&daddr="
						+ this.latitud_dst + "," + this.longitud_dst;

				// Creamos la intención para lanzar Google Maps:
				Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
						Uri.parse(direccionURL));
				intent.setClassName("com.google.android.apps.maps",
						"com.google.android.maps.MapsActivity");
				startActivity(intent);
			}
			Toast.makeText(
					getApplicationContext(),
					"Te encuentras en la posición: \nLat: " + latitud_act
							+ "\nLong: " + longitud_act, Toast.LENGTH_LONG)
					.show();
		} else {
			// Si el GPS no está activado, pedimos al usuario que lo active:
			this.gestor_GPS.lanzarAviso();
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Establecemos los parámetros que usaremos para el reconocimiento de
		// voz.
		this.numberRecoResults = DEFAULT_NUMBER_RESULTS;
		this.languageModel = RecognizerIntent.LANGUAGE_MODEL_FREE_FORM;

		// Inicializamos las coordenadas:
		this.latitud_dst = 0.0;
		this.longitud_dst = 0.0;
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
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public String limpiarCadena(String cadena) {
		cadena = cadena.replaceAll("coma", ",");
		cadena = cadena.replaceAll("punto", ".");
		cadena = cadena.replaceAll("menos", "-");
		cadena = cadena.replaceAll(" ", "");

		return cadena;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (data != null) {

				// Obtiene la lista de mejores coincidencias:
				ArrayList<String> listaMejoresCoincidencias = data
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

				if (requestCode == LATITUD_CODE) {

					this.latitud_text = limpiarCadena(listaMejoresCoincidencias
							.get(0));

					TextView latitud_txt = (TextView) findViewById(R.id.val_Lat);
					latitud_txt.setText(this.latitud_text);

					try {
						this.latitud_dst = Double
								.parseDouble(this.latitud_text);
					} catch (Exception e) {
						latitud_txt.setText("Error al obtener Latitud.");
					}
				}
				if (requestCode == LONGITUD_CODE) {

					this.longitud_text = limpiarCadena(listaMejoresCoincidencias
							.get(0));

					TextView longitud_txt = (TextView) findViewById(R.id.val_Lon);
					longitud_txt.setText(this.longitud_text);

					try {
						this.longitud_dst = Double
								.parseDouble(this.longitud_text);
					} catch (Exception e) {
						longitud_txt.setText("Error al obtener Longitud.");
					}
				}
			} else {
				// Reports error in recognition error in log
				Log.e(LOGTAG, "Recognition was not successful");
			}
		}
	}
}

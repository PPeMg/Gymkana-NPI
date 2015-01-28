package NPI.P4.puntofotobrujula_josedelgado_joseangelsegura;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import android.support.v7.app.ActionBarActivity;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;

/**
 * Clase Principal de la Aplicación. Se ha utilizado el ejemplo de brújula
 * desarrollado por David Gásquez en la práctica 3 para aprender su
 * funcionamiento:
 * 
 * @author José Delgado Dolset
 * @author José Ángel Segura Moya
 * @see https://github.com/DavidGasquez/windrose
 * 
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class Brujula extends ActionBarActivity implements SensorEventListener {

	// Variables de orientación de la brújula. Representan
	// el ángulo con respecto al norte (que sería 0 grados).
	// Necesitamos dos para controlar la animación de giro
	// de la brújula.
	private float orientacionActual = 0.0f;
	private float orientacionAnterior = 0.0f;

	// La imagen de la aguja de la brújula:
	private ImageView aguja;

	// El gestor que utilizaremos para escuchar los datos de la brújula:
	private SensorManager brujula;

	// El punto cardinal que estamos buscando actualmente:
	private PuntoCardinal objetivo;

	// Objeto utilizado para comprobar si el sensor apunta la dirección
	// adecuada:
	private ComprobadorDireccion comparador;

	// Este booleano nos controlará si tenemos algún objetivo:
	private boolean enBusqueda;

	// Esta constante se utiliza como un código, de forma que solicitemos al
	// handlerç
	// guardar la foto.
	private static final int GUARDAR_FOTO = 1;

	// Este vector de booleanos controla el objetivo activado actualmente. Cada
	// componente corresponde a un punto cardinal empenzando por el norte y en
	// el sentido de las agujas del reloj.:
	private boolean[] direcciones_activadas;

	// Estos son los botones correspondientes a cada punto cardinal:
	ImageButton N_Button;
	ImageButton NE_Button;
	ImageButton E_Button;
	ImageButton SE_Button;
	ImageButton S_Button;
	ImageButton SO_Button;
	ImageButton O_Button;
	ImageButton NO_Button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_brujula);

		this.aguja = (ImageView) findViewById(R.id.Aguja);
		this.enBusqueda = false;

		direcciones_activadas = new boolean[8];

		for (int i = 0; i < 8; i++)
			direcciones_activadas[i] = false;

		// Inicializamos los botones:
		this.N_Button = (ImageButton) findViewById(R.id.N_Button);
		this.NE_Button = (ImageButton) findViewById(R.id.NE_Button);
		this.E_Button = (ImageButton) findViewById(R.id.E_Button);
		this.SE_Button = (ImageButton) findViewById(R.id.SE_Button);
		this.S_Button = (ImageButton) findViewById(R.id.S_Button);
		this.SO_Button = (ImageButton) findViewById(R.id.SO_Button);
		this.O_Button = (ImageButton) findViewById(R.id.O_Button);
		this.NO_Button = (ImageButton) findViewById(R.id.NO_Button);

		// Inicializamos el coomparador:
		this.comparador = new ComprobadorDireccion();
		this.comparador.setTolerancia(0.05f);

		// Inicializamos el sensorManager:
		this.brujula = (SensorManager) getSystemService(SENSOR_SERVICE);
	}

	/**
	 * Manejador para el evento de pulsar en el botón N: Selecciona el Norte
	 * como la nueva dirección buscada:
	 * */
	public void onClickN_Button(View arg0) {
		if (!this.enBusqueda)
			this.enBusqueda = true;

		this.objetivo = PuntoCardinal.Norte;
		this.comparador.setDireccion(this.objetivo);

		this.N_Button.setImageResource(R.drawable.n_pushed);

		if (!this.direcciones_activadas[0]) {
			for (int i = 1; i < 8; i++) {
				if (this.direcciones_activadas[i]) {
					this.direcciones_activadas[i] = false;

					restablecerIcono(i);
				}
			}

			this.direcciones_activadas[0] = true;
		}

	}

	/**
	 * Manejador para el evento de pulsar en el botón NE: Selecciona el Noreste
	 * como la nueva dirección buscada:
	 * */
	public void onClickNE_Button(View arg0) {
		if (!this.enBusqueda)
			this.enBusqueda = true;

		this.objetivo = PuntoCardinal.Noreste;
		this.comparador.setDireccion(this.objetivo);

		this.NE_Button.setImageResource(R.drawable.ne_pushed);

		if (!this.direcciones_activadas[1]) {
			for (int i = 0; i < 8; i++) {
				if (this.direcciones_activadas[i] && i != 1) {
					this.direcciones_activadas[i] = false;

					restablecerIcono(i);
				}

				this.direcciones_activadas[1] = true;
			}
		}

	}

	/**
	 * Manejador para el evento de pulsar en el botón E: Selecciona el Este como
	 * la nueva dirección buscada:
	 * */
	public void onClickE_Button(View arg0) {
		if (!this.enBusqueda)
			this.enBusqueda = true;

		this.objetivo = PuntoCardinal.Este;
		this.comparador.setDireccion(this.objetivo);

		this.E_Button.setImageResource(R.drawable.e_pushed);

		if (!this.direcciones_activadas[2]) {
			for (int i = 0; i < 8; i++) {
				if (this.direcciones_activadas[i] && i != 2) {
					this.direcciones_activadas[i] = false;

					restablecerIcono(i);
				}

				this.direcciones_activadas[2] = true;
			}
		}

	}

	/**
	 * Manejador para el evento de pulsar en el botón SE: Selecciona el Sudeste
	 * como la nueva dirección buscada:
	 * */
	public void onClickSE_Button(View arg0) {
		if (!this.enBusqueda)
			this.enBusqueda = true;

		this.objetivo = PuntoCardinal.Sudeste;
		this.comparador.setDireccion(this.objetivo);

		this.SE_Button.setImageResource(R.drawable.se_pushed);

		if (!this.direcciones_activadas[3]) {
			for (int i = 0; i < 8; i++) {
				if (this.direcciones_activadas[i] && i != 3) {
					this.direcciones_activadas[i] = false;

					restablecerIcono(i);
				}

				this.direcciones_activadas[3] = true;
			}
		}

	}

	/**
	 * Manejador para el evento de pulsar en el botón S: Selecciona el Sur como
	 * la nueva dirección buscada:
	 * */
	public void onClickS_Button(View arg0) {
		if (!this.enBusqueda)
			this.enBusqueda = true;

		this.objetivo = PuntoCardinal.Sur;
		this.comparador.setDireccion(this.objetivo);

		this.S_Button.setImageResource(R.drawable.s_pushed);

		if (!this.direcciones_activadas[4]) {
			for (int i = 0; i < 8; i++) {
				if (this.direcciones_activadas[i] && i != 4) {
					this.direcciones_activadas[i] = false;

					restablecerIcono(i);
				}

				this.direcciones_activadas[4] = true;
			}
		}

	}

	/**
	 * Manejador para el evento de pulsar en el botón SO: Selecciona el Suroeste
	 * como la nueva dirección buscada:
	 * */
	public void onClickSO_Button(View arg0) {
		if (!this.enBusqueda)
			this.enBusqueda = true;

		this.objetivo = PuntoCardinal.Suroeste;
		this.comparador.setDireccion(this.objetivo);

		this.SO_Button.setImageResource(R.drawable.so_pushed);

		if (!this.direcciones_activadas[5]) {
			for (int i = 0; i < 8; i++) {
				if (this.direcciones_activadas[i] && i != 5) {
					this.direcciones_activadas[i] = false;

					restablecerIcono(i);
				}

				this.direcciones_activadas[5] = true;
			}
		}

	}

	/**
	 * Manejador para el evento de pulsar en el botón O: Selecciona el Oeste
	 * como la nueva dirección buscada:
	 * */
	public void onClickO_Button(View arg0) {
		if (!this.enBusqueda)
			this.enBusqueda = true;

		this.objetivo = PuntoCardinal.Oeste;
		this.comparador.setDireccion(this.objetivo);

		this.O_Button.setImageResource(R.drawable.o_pushed);

		if (!this.direcciones_activadas[6]) {
			for (int i = 0; i < 8; i++) {
				if (this.direcciones_activadas[i] && i != 6) {
					this.direcciones_activadas[i] = false;

					restablecerIcono(i);
				}

				this.direcciones_activadas[6] = true;
			}
		}

	}

	/**
	 * Manejador para el evento de pulsar en el botón N: Selecciona el Noroeste
	 * como la nueva dirección buscada:
	 * */
	public void onClickNO_Button(View arg0) {
		if (!this.enBusqueda)
			this.enBusqueda = true;

		this.objetivo = PuntoCardinal.Noroeste;
		this.comparador.setDireccion(this.objetivo);

		this.NO_Button.setImageResource(R.drawable.no_pushed);

		if (!this.direcciones_activadas[7]) {
			for (int i = 0; i < 7; i++) {
				if (this.direcciones_activadas[i]) {
					this.direcciones_activadas[i] = false;

					restablecerIcono(i);
				}

				this.direcciones_activadas[7] = true;
			}
		}

	}

	// Este método restaura la imagen del icono sin pulsar.
	public void restablecerIcono(int valor) {

		switch (valor) {
		case 0:
			this.N_Button.setImageResource(R.drawable.n);
			break;

		case 1:
			this.NE_Button.setImageResource(R.drawable.ne);
			break;

		case 2:
			this.E_Button.setImageResource(R.drawable.e);
			break;

		case 3:
			this.SE_Button.setImageResource(R.drawable.se);
			break;

		case 4:
			this.S_Button.setImageResource(R.drawable.s);
			break;

		case 5:
			this.SO_Button.setImageResource(R.drawable.so);
			break;

		case 6:
			this.O_Button.setImageResource(R.drawable.o);
			break;

		case 7:
			this.NO_Button.setImageResource(R.drawable.no);
			break;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.brujula, menu);
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

	/**
	 * Método utilizado para activar el sensor cuando la aplicación continua su
	 * funcionamiento.
	 */
	@Override
	protected void onResume() {
		super.onResume();

		// Continue listening the orientation sensor
		this.brujula.registerListener(this,
				this.brujula.getDefaultSensor(Sensor.TYPE_ORIENTATION),
				SensorManager.SENSOR_DELAY_GAME);
	}

	/**
	 * Método utilizado para desactivar el sensor cuando la aplicación se
	 * detiene:
	 */
	@Override
	protected void onPause() {
		super.onPause();

		// Stop listening the sensor
		this.brujula.unregisterListener(this);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {

		// Obtenemos el ángulo calculado por el sensor:
		this.orientacionActual = (float) event.values[0];

		// Comprobamos si tenemos objetivo:
		if (this.enBusqueda) {

			// En caso de que sí, comprobamos si estamos apuntando al objetivo:
			if (comparador.direccionCorrecta(this.objetivo,
					this.orientacionActual)) {

				// En caso correcto, lanzamos la cámara, mediante un intent:
				Intent camaraIntent = new Intent(
						android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

				// Si nadie está usando la cámara, podemos realizar al foto:
				if (camaraIntent.resolveActivity(getPackageManager()) != null) {
					try {
						startActivityForResult(camaraIntent, GUARDAR_FOTO);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}
		}

		// Giramos la aguja de la brújula cuanto sea necesario. Para ello,
		// creamos la siguiente animación:
		RotateAnimation giro;
		giro = new RotateAnimation(this.orientacionAnterior,
				this.orientacionActual, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);

		giro.setDuration(300);
		giro.setFillAfter(true);
		this.aguja.startAnimation(giro);

		// Por último, actualizamos el valor anterior de la brújula:
		this.orientacionAnterior = this.orientacionActual;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
	}

}

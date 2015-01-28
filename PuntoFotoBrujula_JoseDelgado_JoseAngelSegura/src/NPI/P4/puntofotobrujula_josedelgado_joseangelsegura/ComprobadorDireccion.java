package NPI.P4.puntofotobrujula_josedelgado_joseangelsegura;

public class ComprobadorDireccion {

	// Punto cardinal actual:
	public PuntoCardinal direccion;

	// Tolerancia, para ajustar la precisión:
	private float tolerancia;

	public ComprobadorDireccion() {
		this.direccion = PuntoCardinal.Norte;
		this.tolerancia = 0.05f;
	}

	public ComprobadorDireccion(PuntoCardinal dir) {
		this.direccion = dir;
		this.tolerancia = 0.05f;
	}

	public ComprobadorDireccion(PuntoCardinal dir, float tol) {
		this.direccion = dir;
		this.tolerancia = tol;
	}

	public void setTolerancia(float tol) {
		this.tolerancia = tol;
	}

	public void setDireccion(PuntoCardinal dir) {
		this.direccion = dir;
	}

	public boolean direccionCorrecta(PuntoCardinal punto, float angulo) {
		boolean retorno = false;

		float correcto = (float) punto.angulo() * 1.0f;
		float inf = (float) angulo * (1.0f - this.tolerancia);
		float sup = (float) angulo * (1.0f + this.tolerancia);

		if (punto != PuntoCardinal.Norte) {
			if (correcto >= inf && correcto <= sup) {
				retorno = true;
			}
		} else {
			int aux_correcto = (int) Math.round(correcto) % 360;
			int aux_angulo = (int) Math.round(angulo) % 360;
			
			if (aux_correcto == aux_angulo)
				retorno = true;
		}

		return retorno;

	}

}

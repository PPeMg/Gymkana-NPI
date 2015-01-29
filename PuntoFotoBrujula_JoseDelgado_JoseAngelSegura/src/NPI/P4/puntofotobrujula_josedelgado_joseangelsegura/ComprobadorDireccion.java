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

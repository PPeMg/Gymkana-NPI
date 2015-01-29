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

public enum PuntoCardinal {
	Norte (0),
	Noreste (45),
	Este (90),
	Sudeste (135),
	Sur (180),
	Suroeste (225),
	Oeste (270),
	Noroeste (315);
	
	// Ángulo correspondiente al punto cardinal:
	private final float angulo;
	
	private PuntoCardinal(float ang) {
		this.angulo = ang;
	}
	
	public float angulo(){
		return this.angulo;
	}
}

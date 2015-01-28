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

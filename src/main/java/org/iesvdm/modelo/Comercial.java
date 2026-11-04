package org.iesvdm.modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comercial {
	private int id;
	private String nombre;
	private String apellido1;
	private String apellido2;
	private Float comisión;

	public String getNombreCompleto() {
		if (apellido2 != null && !apellido2.trim().isEmpty()) {
			return nombre + " " + apellido1 + " " + apellido2;
		}
		return (nombre != null ? nombre : "") + (apellido1 != null ? " " + apellido1 : "");
	}
}
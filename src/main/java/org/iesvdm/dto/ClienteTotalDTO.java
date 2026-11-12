package org.iesvdm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClienteTotalDTO {
	private int id;
	private String nombre;
	private String apellido1;
	private String apellido2;
	private String ciudad;
	private Integer categoría;
	private Double totalPedidos;

	public String getNombreCompleto() {
		if (apellido2 != null && !apellido2.trim().isEmpty()) {
			return nombre + " " + apellido1 + " " + apellido2;
		}
		return (nombre != null ? nombre : "") + (apellido1 != null ? " " + apellido1 : "");
	}

	public Double getTotal() {
		return totalPedidos;
	}
}

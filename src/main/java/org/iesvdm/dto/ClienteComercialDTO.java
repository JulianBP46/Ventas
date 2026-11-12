package org.iesvdm.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClienteComercialDTO {
	private int clienteId;
	private int comercialId;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate fecha;
	private Integer prioridad;

	private String comercialNombre;
	private String comercialApellido1;
	private String comercialApellido2;
	private Float comercialComision;
	private String comercialNombreCompleto;

	public String getComercialNombreCompleto() {
		if (comercialNombreCompleto != null && !comercialNombreCompleto.isBlank()) {
			return comercialNombreCompleto;
		}
		if (comercialApellido2 != null && !comercialApellido2.trim().isEmpty()) {
			return comercialNombre + " " + comercialApellido1 + " " + comercialApellido2;
		}
		return (comercialNombre != null ? comercialNombre : "") + (comercialApellido1 != null ? " " + comercialApellido1 : "");
	}

	public String getNombreCompleto() {
		return getComercialNombreCompleto();
	}
}

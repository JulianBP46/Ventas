package org.iesvdm.modelo;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoDetalle {
	private int id;
	private Double total;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate fecha;
	private Integer clienteId;
	private Integer comercialId;
	private String clienteNombreCompleto;
	private String comercialNombreCompleto;

	public String getClienteNombre() {
		return clienteNombreCompleto;
	}

	public String getComercialNombre() {
		return comercialNombreCompleto;
	}
}

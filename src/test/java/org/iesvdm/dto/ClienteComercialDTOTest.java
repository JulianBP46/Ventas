package org.iesvdm.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class ClienteComercialDTOTest {

	@Test
	public void testDTOPropertiesAndFullName() {
		LocalDate fecha = LocalDate.of(2024, 1, 15);
		ClienteComercialDTO dto = ClienteComercialDTO.builder()
				.clienteId(1)
				.comercialId(2)
				.fecha(fecha)
				.prioridad(1)
				.comercialNombre("Daniel")
				.comercialApellido1("Sáez")
				.comercialApellido2("Vega")
				.comercialComision(0.15f)
				.comercialNombreCompleto("Daniel Sáez Vega")
				.build();

		assertEquals(1, dto.getClienteId());
		assertEquals(2, dto.getComercialId());
		assertEquals(fecha, dto.getFecha());
		assertEquals(1, dto.getPrioridad());
		assertEquals("Daniel Sáez Vega", dto.getComercialNombreCompleto());
		assertEquals("Daniel Sáez Vega", dto.getNombreCompleto());
		assertEquals(0.15f, dto.getComercialComision());
	}

	@Test
	public void testCalculatedFullNameSinApellido2() {
		ClienteComercialDTO dto = ClienteComercialDTO.builder()
				.clienteId(1)
				.comercialId(2)
				.comercialNombre("Marta")
				.comercialApellido1("Herrera")
				.comercialApellido2(null)
				.build();

		assertEquals("Marta Herrera", dto.getComercialNombreCompleto());
	}
}

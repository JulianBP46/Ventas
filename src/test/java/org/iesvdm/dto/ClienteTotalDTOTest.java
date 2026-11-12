package org.iesvdm.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ClienteTotalDTOTest {

	@Test
	public void testNombreCompletoConApellido2() {
		ClienteTotalDTO dto = new ClienteTotalDTO(1, "Adela", "Salas", "Díaz", "Granada", 200, 8870.86);

		assertEquals("Adela Salas Díaz", dto.getNombreCompleto());
		assertEquals(8870.86, dto.getTotalPedidos());
		assertEquals(8870.86, dto.getTotal());
	}

	@Test
	public void testNombreCompletoSinApellido2() {
		ClienteTotalDTO dto = new ClienteTotalDTO(2, "Pilar", "Ruiz", null, "Sevilla", 300, 2400.60);

		assertEquals("Pilar Ruiz", dto.getNombreCompleto());
		assertEquals(2400.60, dto.getTotalPedidos());
	}
}

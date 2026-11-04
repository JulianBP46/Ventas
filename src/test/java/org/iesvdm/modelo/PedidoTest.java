package org.iesvdm.modelo;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class PedidoTest {

	@Test
	public void testPedidoCreationAndGettersSetters() {
		LocalDate fecha = LocalDate.of(2023, 5, 20);
		Pedido pedido = new Pedido(1, 150.50, fecha, 2, 3);

		assertEquals(1, pedido.getId());
		assertEquals(150.50, pedido.getTotal());
		assertEquals(fecha, pedido.getFecha());
		assertEquals(2, pedido.getClienteId());
		assertEquals(3, pedido.getComercialId());

		pedido.setTotal(200.0);
		assertEquals(200.0, pedido.getTotal());
	}

	@Test
	public void testPedidoDetalle() {
		LocalDate fecha = LocalDate.of(2023, 5, 20);
		PedidoDetalle detalle = new PedidoDetalle(1, 150.50, fecha, 2, 3, "Juan Pérez", "Marta Gómez");

		assertEquals(1, detalle.getId());
		assertEquals(150.50, detalle.getTotal());
		assertEquals(fecha, detalle.getFecha());
		assertEquals(2, detalle.getClienteId());
		assertEquals(3, detalle.getComercialId());
		assertEquals("Juan Pérez", detalle.getClienteNombreCompleto());
		assertEquals("Marta Gómez", detalle.getComercialNombreCompleto());
		assertEquals("Juan Pérez", detalle.getClienteNombre());
		assertEquals("Marta Gómez", detalle.getComercialNombre());
	}

	@Test
	public void testClienteNombreCompletoSinApellido2() {
		Cliente cliente = new Cliente(1, "Adrián", "Suárez", null, "Jaén", 300);
		assertEquals("Adrián Suárez", cliente.getNombreCompleto());
	}

	@Test
	public void testClienteNombreCompletoConApellido2() {
		Cliente cliente = new Cliente(1, "Aarón", "Rivero", "Gómez", "Almería", 100);
		assertEquals("Aarón Rivero Gómez", cliente.getNombreCompleto());
	}

	@Test
	public void testComercialNombreCompletoSinApellido2() {
		Comercial comercial = new Comercial(1, "Daniel", "Sáez", null, 0.15f);
		assertEquals("Daniel Sáez", comercial.getNombreCompleto());
	}

	@Test
	public void testComercialNombreCompletoConApellido2() {
		Comercial comercial = new Comercial(1, "Daniel", "Sáez", "Vega", 0.15f);
		assertEquals("Daniel Sáez Vega", comercial.getNombreCompleto());
	}
}


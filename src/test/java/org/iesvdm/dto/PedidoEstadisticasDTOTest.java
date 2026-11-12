package org.iesvdm.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.iesvdm.modelo.PedidoDetalle;
import org.junit.jupiter.api.Test;

public class PedidoEstadisticasDTOTest {

	@Test
	public void testEstadisticasConPedidos() {
		PedidoDetalle p1 = new PedidoDetalle(1, 100.0, LocalDate.now(), 1, 1, "Cliente 1", "Comercial 1");
		PedidoDetalle p2 = new PedidoDetalle(2, 300.0, LocalDate.now(), 2, 1, "Cliente 2", "Comercial 1");
		PedidoDetalle p3 = new PedidoDetalle(3, 200.0, LocalDate.now(), 3, 1, "Cliente 3", "Comercial 1");

		PedidoEstadisticasDTO stats = PedidoEstadisticasDTO.fromPedidos(List.of(p1, p2, p3));

		assertEquals(600.0, stats.getTotal());
		assertEquals(200.0, stats.getMedia());
		assertEquals(300.0, stats.getMax());
		assertEquals(100.0, stats.getMin());
		assertEquals(3, stats.getTotalPedidos());
	}

	@Test
	public void testEstadisticasConListaVacia() {
		PedidoEstadisticasDTO stats = PedidoEstadisticasDTO.fromPedidos(Collections.emptyList());

		assertEquals(0.0, stats.getTotal());
		assertEquals(0.0, stats.getMedia());
		assertNull(stats.getMax());
		assertNull(stats.getMin());
		assertEquals(0, stats.getTotalPedidos());
	}

	@Test
	public void testEstadisticasConListaNull() {
		PedidoEstadisticasDTO stats = PedidoEstadisticasDTO.fromPedidos(null);

		assertEquals(0.0, stats.getTotal());
		assertEquals(0.0, stats.getMedia());
		assertNull(stats.getMax());
		assertNull(stats.getMin());
		assertEquals(0, stats.getTotalPedidos());
	}

	@Test
	public void testEstadisticasConUnSoloPedido() {
		PedidoDetalle p1 = new PedidoDetalle(1, 250.50, LocalDate.now(), 1, 1, "Cliente 1", "Comercial 1");

		PedidoEstadisticasDTO stats = PedidoEstadisticasDTO.fromPedidos(List.of(p1));

		assertEquals(250.50, stats.getTotal());
		assertEquals(250.50, stats.getMedia());
		assertEquals(250.50, stats.getMax());
		assertEquals(250.50, stats.getMin());
		assertEquals(1, stats.getTotalPedidos());
	}
}

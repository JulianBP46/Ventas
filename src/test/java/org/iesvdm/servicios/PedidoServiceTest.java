package org.iesvdm.servicios;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.iesvdm.dao.PedidoDAO;
import org.iesvdm.modelo.Pedido;
import org.iesvdm.modelo.PedidoDetalle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PedidoServiceTest {

	@Mock
	private PedidoDAO pedidoDAO;

	@InjectMocks
	private PedidoService pedidoService;

	private Pedido pedido;
	private PedidoDetalle pedidoDetalle;

	@BeforeEach
	public void setUp() {
		pedido = new Pedido(1, 250.0, LocalDate.of(2023, 1, 10), 1, 2);
		pedidoDetalle = new PedidoDetalle(1, 250.0, LocalDate.of(2023, 1, 10), 1, 2, "Aarón Rivero Gómez", "Daniel Sáez");
	}

	@Test
	public void testListAll() {
		when(pedidoDAO.getAll()).thenReturn(List.of(pedido));
		List<Pedido> result = pedidoService.listAll();
		assertEquals(1, result.size());
		assertEquals(250.0, result.get(0).getTotal());
		verify(pedidoDAO, times(1)).getAll();
	}

	@Test
	public void testListAllDetalle() {
		when(pedidoDAO.getAllDetalle()).thenReturn(List.of(pedidoDetalle));
		List<PedidoDetalle> result = pedidoService.listAllDetalle();
		assertEquals(1, result.size());
		assertEquals("Aarón Rivero Gómez", result.get(0).getClienteNombreCompleto());
		verify(pedidoDAO, times(1)).getAllDetalle();
	}

	@Test
	public void testFind() {
		when(pedidoDAO.find(1)).thenReturn(Optional.of(pedido));
		Optional<Pedido> result = pedidoService.find(1);
		assertTrue(result.isPresent());
		assertEquals(1, result.get().getId());
		verify(pedidoDAO, times(1)).find(1);
	}

	@Test
	public void testFindDetalle() {
		when(pedidoDAO.findDetalle(1)).thenReturn(Optional.of(pedidoDetalle));
		Optional<PedidoDetalle> result = pedidoService.findDetalle(1);
		assertTrue(result.isPresent());
		assertEquals("Daniel Sáez", result.get().getComercialNombreCompleto());
		verify(pedidoDAO, times(1)).findDetalle(1);
	}

	@Test
	public void testCreate() {
		pedidoService.create(pedido);
		verify(pedidoDAO, times(1)).create(pedido);
	}

	@Test
	public void testUpdate() {
		pedidoService.update(pedido);
		verify(pedidoDAO, times(1)).update(pedido);
	}

	@Test
	public void testDelete() {
		pedidoService.delete(1);
		verify(pedidoDAO, times(1)).delete(1);
	}
}


package org.iesvdm.servicios;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.iesvdm.dao.ComercialDAO;
import org.iesvdm.dao.PedidoDAO;
import org.iesvdm.modelo.Comercial;
import org.iesvdm.modelo.PedidoDetalle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ComercialServiceTest {

	@Mock
	private ComercialDAO comercialDAO;

	@Mock
	private PedidoDAO pedidoDAO;

	@InjectMocks
	private ComercialService comercialService;

	private Comercial comercial;
	private PedidoDetalle pedidoDetalle;

	@BeforeEach
	public void setUp() {
		comercial = new Comercial(1, "Daniel", "Sáez", "Vega", 0.15f);
		pedidoDetalle = new PedidoDetalle(1, 150.0, LocalDate.of(2023, 5, 1), 1, 1, "Aarón Rivero Gómez", "Daniel Sáez Vega");
	}

	@Test
	public void testListAll() {
		when(comercialDAO.getAll()).thenReturn(List.of(comercial));
		List<Comercial> list = comercialService.listAll();
		assertEquals(1, list.size());
		verify(comercialDAO, times(1)).getAll();
	}

	@Test
	public void testFind() {
		when(comercialDAO.find(1)).thenReturn(Optional.of(comercial));
		Optional<Comercial> result = comercialService.find(1);
		assertTrue(result.isPresent());
		assertEquals("Daniel", result.get().getNombre());
		verify(comercialDAO, times(1)).find(1);
	}

	@Test
	public void testListPedidosByComercial() {
		when(pedidoDAO.findByComercialId(1)).thenReturn(List.of(pedidoDetalle));
		List<PedidoDetalle> pedidos = comercialService.listPedidosByComercial(1);
		assertEquals(1, pedidos.size());
		assertEquals(150.0, pedidos.get(0).getTotal());
		verify(pedidoDAO, times(1)).findByComercialId(1);
	}
}

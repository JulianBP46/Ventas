package org.iesvdm.controlador;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.iesvdm.modelo.Cliente;
import org.iesvdm.modelo.Comercial;
import org.iesvdm.modelo.Pedido;
import org.iesvdm.modelo.PedidoDetalle;
import org.iesvdm.servicios.ClienteService;
import org.iesvdm.servicios.ComercialService;
import org.iesvdm.servicios.PedidoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import org.springframework.web.servlet.view.RedirectView;

@ExtendWith(MockitoExtension.class)
public class PedidoControllerTest {

	@Mock
	private PedidoService pedidoService;

	@Mock
	private ClienteService clienteService;

	@Mock
	private ComercialService comercialService;

	@InjectMocks
	private PedidoController pedidoController;

	private Cliente cliente;
	private Comercial comercial;
	private Pedido pedido;
	private PedidoDetalle pedidoDetalle;

	@BeforeEach
	public void setUp() {
		cliente = new Cliente(1, "Aarón", "Rivero", "Gómez", "Almería", 100);
		comercial = new Comercial(1, "Daniel", "Sáez", "Vega", 0.15f);
		pedido = new Pedido(1, 150.0, LocalDate.of(2023, 5, 1), 1, 1);
		pedidoDetalle = new PedidoDetalle(1, 150.0, LocalDate.of(2023, 5, 1), 1, 1, "Aarón Rivero Gómez", "Daniel Sáez Vega");
	}

	@Test
	public void testListar() {
		Model model = new ConcurrentModel();
		when(pedidoService.listAllDetalle()).thenReturn(List.of(pedidoDetalle));

		String view = pedidoController.listar(model);

		assertEquals("pedidos", view);
		assertTrue(model.containsAttribute("pedidos"));
		assertEquals(List.of(pedidoDetalle), model.getAttribute("pedidos"));
	}

	@Test
	public void testDetalleFound() {
		Model model = new ConcurrentModel();
		when(pedidoService.findDetalle(1)).thenReturn(Optional.of(pedidoDetalle));

		String view = pedidoController.detalle(model, 1);

		assertEquals("detalle-pedido", view);
		assertTrue(model.containsAttribute("pedido"));
		assertEquals(pedidoDetalle, model.getAttribute("pedido"));
	}

	@Test
	public void testDetalleNotFound() {
		Model model = new ConcurrentModel();
		when(pedidoService.findDetalle(99)).thenReturn(Optional.empty());

		String view = pedidoController.detalle(model, 99);

		assertEquals("redirect:/pedidos", view);
	}

	@Test
	public void testCrear() {
		Model model = new ConcurrentModel();
		when(clienteService.listAll()).thenReturn(List.of(cliente));
		when(comercialService.listAll()).thenReturn(List.of(comercial));

		String view = pedidoController.crear(model);

		assertEquals("crear-pedido", view);
		assertTrue(model.containsAttribute("pedido"));
		assertTrue(model.containsAttribute("clientes"));
		assertTrue(model.containsAttribute("comerciales"));
	}

	@Test
	public void testSubmitCrearValido() {
		Model model = new ConcurrentModel();
		when(clienteService.find(1)).thenReturn(Optional.of(cliente));
		when(comercialService.find(1)).thenReturn(Optional.of(comercial));

		Object result = pedidoController.submitCrear(pedido, model);

		verify(pedidoService, times(1)).create(pedido);
		assertTrue(result instanceof RedirectView);
		assertEquals("/pedidos", ((RedirectView) result).getUrl());
	}

	@Test
	public void testSubmitCrearClienteInvalido() {
		Model model = new ConcurrentModel();
		Pedido pedidoInvalido = new Pedido(0, 100.0, LocalDate.now(), 999, 1);
		when(clienteService.find(999)).thenReturn(Optional.empty());
		when(comercialService.find(1)).thenReturn(Optional.of(comercial));
		when(clienteService.listAll()).thenReturn(List.of(cliente));
		when(comercialService.listAll()).thenReturn(List.of(comercial));

		Object result = pedidoController.submitCrear(pedidoInvalido, model);

		verify(pedidoService, never()).create(any());
		assertEquals("crear-pedido", result);
		assertTrue(model.containsAttribute("error"));
	}

	@Test
	public void testSubmitCrearConClienteIdNull() {
		Model model = new ConcurrentModel();
		Pedido pedidoNull = new Pedido(0, 100.0, LocalDate.now(), null, 1);
		when(clienteService.listAll()).thenReturn(List.of(cliente));
		when(comercialService.listAll()).thenReturn(List.of(comercial));

		Object result = pedidoController.submitCrear(pedidoNull, model);

		verify(pedidoService, never()).create(any());
		assertEquals("crear-pedido", result);
		assertTrue(model.containsAttribute("error"));
	}

	@Test
	public void testSubmitCrearComercialInvalido() {
		Model model = new ConcurrentModel();
		Pedido pedidoInvalido = new Pedido(0, 100.0, LocalDate.now(), 1, 999);
		when(clienteService.find(1)).thenReturn(Optional.of(cliente));
		when(comercialService.find(999)).thenReturn(Optional.empty());
		when(clienteService.listAll()).thenReturn(List.of(cliente));
		when(comercialService.listAll()).thenReturn(List.of(comercial));

		Object result = pedidoController.submitCrear(pedidoInvalido, model);

		verify(pedidoService, never()).create(any());
		assertEquals("crear-pedido", result);
		assertTrue(model.containsAttribute("error"));
	}

	@Test
	public void testEditarFound() {
		Model model = new ConcurrentModel();
		when(pedidoService.find(1)).thenReturn(Optional.of(pedido));
		when(clienteService.listAll()).thenReturn(List.of(cliente));
		when(comercialService.listAll()).thenReturn(List.of(comercial));

		String view = pedidoController.editar(model, 1);

		assertEquals("editar-pedido", view);
		assertTrue(model.containsAttribute("pedido"));
		assertTrue(model.containsAttribute("clientes"));
		assertTrue(model.containsAttribute("comerciales"));
	}

	@Test
	public void testEditarNotFound() {
		Model model = new ConcurrentModel();
		when(pedidoService.find(99)).thenReturn(Optional.empty());

		String view = pedidoController.editar(model, 99);

		assertEquals("redirect:/pedidos", view);
	}

	@Test
	public void testSubmitEditarValido() {
		Model model = new ConcurrentModel();
		when(clienteService.find(1)).thenReturn(Optional.of(cliente));
		when(comercialService.find(1)).thenReturn(Optional.of(comercial));

		Object result = pedidoController.submitEditar(1, pedido, model);

		verify(pedidoService, times(1)).update(pedido);
		assertTrue(result instanceof RedirectView);
		assertEquals("/pedidos", ((RedirectView) result).getUrl());
	}

	@Test
	public void testSubmitEditarIdInvalido() {
		Model model = new ConcurrentModel();
		Pedido pedidoInvalido = new Pedido(1, 100.0, LocalDate.now(), 999, 1);
		when(clienteService.find(999)).thenReturn(Optional.empty());
		when(comercialService.find(1)).thenReturn(Optional.of(comercial));
		when(clienteService.listAll()).thenReturn(List.of(cliente));
		when(comercialService.listAll()).thenReturn(List.of(comercial));

		Object result = pedidoController.submitEditar(1, pedidoInvalido, model);

		verify(pedidoService, never()).update(any());
		assertEquals("editar-pedido", result);
		assertTrue(model.containsAttribute("error"));
	}

	@Test
	public void testSubmitBorrar() {
		RedirectView result = pedidoController.submitBorrar(1);

		verify(pedidoService, times(1)).delete(1);
		assertEquals("/pedidos", result.getUrl());
	}
}

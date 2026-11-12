package org.iesvdm.controlador;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.iesvdm.modelo.Comercial;
import org.iesvdm.modelo.PedidoDetalle;
import org.iesvdm.servicios.ComercialService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

@ExtendWith(MockitoExtension.class)
public class ComercialControllerTest {

	@Mock
	private ComercialService comercialService;

	@InjectMocks
	private ComercialController comercialController;

	private Comercial comercial;
	private PedidoDetalle pedidoDetalle;

	@BeforeEach
	public void setUp() {
		comercial = new Comercial(1, "Daniel", "Sáez", "Vega", 0.15f);
		pedidoDetalle = new PedidoDetalle(1, 150.0, LocalDate.of(2023, 5, 1), 1, 1, "Aarón Rivero Gómez", "Daniel Sáez Vega");
	}

	@Test
	public void testDetalleFound() {
		Model model = new ConcurrentModel();
		when(comercialService.find(1)).thenReturn(Optional.of(comercial));
		when(comercialService.listPedidosByComercial(1)).thenReturn(List.of(pedidoDetalle));

		String view = comercialController.detalle(model, 1);

		assertEquals("detalle-comercial", view);
		assertEquals(comercial, model.getAttribute("comercial"));
		assertEquals(List.of(pedidoDetalle), model.getAttribute("pedidos"));
	}

	@Test
	public void testDetalleNotFound() {
		Model model = new ConcurrentModel();
		when(comercialService.find(99)).thenReturn(Optional.empty());

		String view = comercialController.detalle(model, 99);

		assertEquals("redirect:/comerciales", view);
	}
}

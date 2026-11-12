package org.iesvdm.controlador;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.iesvdm.dto.ClienteTotalDTO;
import org.iesvdm.dto.PedidoEstadisticasDTO;
import org.iesvdm.modelo.Comercial;
import org.iesvdm.modelo.PedidoDetalle;
import org.iesvdm.servicios.ComercialService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

@ExtendWith(MockitoExtension.class)
public class ComercialControllerDetalleTest {

	@Mock
	private ComercialService comercialService;

	@InjectMocks
	private ComercialController comercialController;

	@Test
	public void testDetalleCargaEstadisticasYClientesOrdenados() {
		int comercialId = 1;
		Comercial comercial = new Comercial(comercialId, "Daniel", "Sáez", "Vega", 0.15f);
		PedidoDetalle p1 = new PedidoDetalle(1, 100.0, LocalDate.now(), 1, comercialId, "Cliente 1", "Daniel Sáez Vega");
		PedidoDetalle p2 = new PedidoDetalle(2, 300.0, LocalDate.now(), 2, comercialId, "Cliente 2", "Daniel Sáez Vega");
		List<PedidoDetalle> pedidos = List.of(p1, p2);
		PedidoEstadisticasDTO stats = PedidoEstadisticasDTO.fromPedidos(pedidos);
		List<ClienteTotalDTO> clientesOrdenados = List.of(
				new ClienteTotalDTO(2, "Cliente", "Dos", null, "Sevilla", 100, 300.0),
				new ClienteTotalDTO(1, "Cliente", "Uno", null, "Granada", 200, 100.0)
		);

		when(comercialService.find(comercialId)).thenReturn(Optional.of(comercial));
		when(comercialService.listPedidosByComercial(comercialId)).thenReturn(pedidos);
		when(comercialService.getEstadisticasPedidos(comercialId)).thenReturn(stats);
		when(comercialService.listClientesByComercialOrderedByTotalDesc(comercialId)).thenReturn(clientesOrdenados);

		Model model = new ConcurrentModel();
		String view = comercialController.detalle(model, comercialId);

		assertEquals("detalle-comercial", view);
		assertEquals(comercial, model.getAttribute("comercial"));
		assertEquals(pedidos, model.getAttribute("pedidos"));
		assertEquals(stats, model.getAttribute("estadisticas"));
		assertEquals(clientesOrdenados, model.getAttribute("clientesOrdenados"));
	}

	@Test
	public void testDetalleComercialNoEncontradoRedirige() {
		when(comercialService.find(999)).thenReturn(Optional.empty());

		Model model = new ConcurrentModel();
		String view = comercialController.detalle(model, 999);

		assertEquals("redirect:/comerciales", view);
	}
}

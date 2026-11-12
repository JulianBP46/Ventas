package org.iesvdm.controlador;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.iesvdm.dto.ClienteComercialDTO;
import org.iesvdm.modelo.Cliente;
import org.iesvdm.modelo.Comercial;
import org.iesvdm.servicios.ClienteService;
import org.iesvdm.servicios.ComercialService;
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
public class ClienteControllerEditarComercialTest {

	@Mock
	private ClienteService clienteService;

	@Mock
	private ComercialService comercialService;

	@InjectMocks
	private ClienteController clienteController;

	private Cliente cliente;
	private Comercial comercial;
	private ClienteComercialDTO vinculado;

	@BeforeEach
	public void setUp() {
		cliente = new Cliente(1, "Aarón", "Rivero", "Gómez", "Almería", 100);
		comercial = new Comercial(2, "Juan", "Gómez", "López", 0.13f);
		vinculado = ClienteComercialDTO.builder()
				.clienteId(1)
				.comercialId(2)
				.fecha(LocalDate.of(2024, 2, 1))
				.prioridad(1)
				.comercialNombre("Juan")
				.comercialApellido1("Gómez")
				.comercialApellido2("López")
				.comercialNombreCompleto("Juan Gómez López")
				.build();
	}

	@Test
	public void testEditarCargaComercialesVinculadosYDisponibles() {
		when(clienteService.find(1)).thenReturn(Optional.of(cliente));
		when(clienteService.listComercialesVinculados(1)).thenReturn(List.of(vinculado));
		when(clienteService.listComercialesDisponibles(1)).thenReturn(List.of(comercial));
		when(comercialService.listAll()).thenReturn(List.of(comercial));

		Model model = new ConcurrentModel();
		String view = clienteController.editar(model, 1);

		assertEquals("editar-cliente", view);
		assertEquals(cliente, model.getAttribute("cliente"));
		assertEquals(List.of(vinculado), model.getAttribute("comercialesVinculados"));
		assertEquals(List.of(comercial), model.getAttribute("comercialesDisponibles"));
	}

	@Test
	public void testSubmitVincularComercial() {
		LocalDate fecha = LocalDate.of(2024, 3, 15);
		Integer prioridad = 2;

		RedirectView redirect = clienteController.submitVincularComercial(1, 2, fecha, prioridad);

		verify(clienteService, times(1)).vincularComercial(1, 2, fecha, prioridad);
		assertEquals("/clientes/editar/1", redirect.getUrl());
	}

	@Test
	public void testSubmitDesvincularComercial() {
		RedirectView redirect = clienteController.submitDesvincularComercial(1, 2);

		verify(clienteService, times(1)).desvincularComercial(1, 2);
		assertEquals("/clientes/editar/1", redirect.getUrl());
	}
}

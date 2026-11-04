package org.iesvdm.controlador;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.iesvdm.servicios.ClienteService;
import org.iesvdm.servicios.ComercialService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
import org.springframework.web.servlet.view.RedirectView;

@ExtendWith(MockitoExtension.class)
public class ClienteControllerForeignKeyTest {

	@Mock
	private ClienteService clienteService;

	@Mock
	private ComercialService comercialService;

	@InjectMocks
	private ClienteController clienteController;

	@InjectMocks
	private ComercialController comercialController;

	@Test
	public void testBorrarClienteConPedidosAsociadosLanzaErrorClaro() {
		RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();
		doThrow(new DataIntegrityViolationException("FK constraint"))
				.when(clienteService).delete(1);

		RedirectView result = clienteController.submitBorrar(1, redirectAttributes);

		assertEquals("/clientes", result.getUrl());
		assertTrue(redirectAttributes.getFlashAttributes().containsKey("error"));
		assertEquals("No se puede eliminar el cliente porque tiene pedidos asociados.",
				redirectAttributes.getFlashAttributes().get("error"));
	}

	@Test
	public void testBorrarComercialConPedidosAsociadosLanzaErrorClaro() {
		RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();
		doThrow(new DataIntegrityViolationException("FK constraint"))
				.when(comercialService).delete(1);

		RedirectView result = comercialController.submitBorrar(1, redirectAttributes);

		assertEquals("/comerciales", result.getUrl());
		assertTrue(redirectAttributes.getFlashAttributes().containsKey("error"));
		assertEquals("No se puede eliminar el comercial porque tiene pedidos asociados.",
				redirectAttributes.getFlashAttributes().get("error"));
	}
}


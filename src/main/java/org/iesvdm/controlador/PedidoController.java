package org.iesvdm.controlador;

import java.util.List;

import org.iesvdm.modelo.Pedido;
import org.iesvdm.modelo.PedidoDetalle;
import org.iesvdm.servicios.ClienteService;
import org.iesvdm.servicios.ComercialService;
import org.iesvdm.servicios.PedidoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class PedidoController {

	private final PedidoService pedidoService;
	private final ClienteService clienteService;
	private final ComercialService comercialService;

	public PedidoController(PedidoService pedidoService, ClienteService clienteService, ComercialService comercialService) {
		this.pedidoService = pedidoService;
		this.clienteService = clienteService;
		this.comercialService = comercialService;
	}

	@GetMapping("/pedidos")
	public String listar(Model model) {
		List<PedidoDetalle> pedidos = pedidoService.listAllDetalle();
		model.addAttribute("pedidos", pedidos);
		return "pedidos";
	}

	@GetMapping("/pedidos/{id}")
	public String detalle(Model model, @PathVariable int id) {
		return pedidoService.findDetalle(id)
				.map(pedido -> {
					model.addAttribute("pedido", pedido);
					return "detalle-pedido";
				})
				.orElse("redirect:/pedidos");
	}

	@GetMapping("/pedidos/crear")
	public String crear(Model model) {
		model.addAttribute("pedido", new Pedido());
		model.addAttribute("clientes", clienteService.listAll());
		model.addAttribute("comerciales", comercialService.listAll());
		return "crear-pedido";
	}

	@PostMapping("/pedidos/crear")
	public Object submitCrear(@ModelAttribute("pedido") Pedido pedido, Model model) {
		boolean clienteExiste = pedido.getClienteId() != null && clienteService.find(pedido.getClienteId()).isPresent();
		boolean comercialExiste = pedido.getComercialId() != null && comercialService.find(pedido.getComercialId()).isPresent();

		if (!clienteExiste || !comercialExiste) {
			model.addAttribute("error", "El cliente o comercial seleccionado no existe.");
			model.addAttribute("clientes", clienteService.listAll());
			model.addAttribute("comerciales", comercialService.listAll());
			return "crear-pedido";
		}

		pedidoService.create(pedido);
		return new RedirectView("/pedidos");
	}

	@GetMapping("/pedidos/editar/{id}")
	public String editar(Model model, @PathVariable int id) {
		return pedidoService.find(id)
				.map(pedido -> {
					model.addAttribute("pedido", pedido);
					model.addAttribute("clientes", clienteService.listAll());
					model.addAttribute("comerciales", comercialService.listAll());
					return "editar-pedido";
				})
				.orElse("redirect:/pedidos");
	}

	@PostMapping("/pedidos/editar/{id}")
	public Object submitEditar(@PathVariable int id, @ModelAttribute("pedido") Pedido pedido, Model model) {
		boolean clienteExiste = pedido.getClienteId() != null && clienteService.find(pedido.getClienteId()).isPresent();
		boolean comercialExiste = pedido.getComercialId() != null && comercialService.find(pedido.getComercialId()).isPresent();

		if (!clienteExiste || !comercialExiste) {
			pedido.setId(id);
			model.addAttribute("error", "El cliente o comercial seleccionado no existe.");
			model.addAttribute("clientes", clienteService.listAll());
			model.addAttribute("comerciales", comercialService.listAll());
			return "editar-pedido";
		}

		pedido.setId(id);
		pedidoService.update(pedido);
		return new RedirectView("/pedidos");
	}

	@PostMapping("/pedidos/borrar/{id}")
	public RedirectView submitBorrar(@PathVariable int id) {
		pedidoService.delete(id);
		return new RedirectView("/pedidos");
	}
}

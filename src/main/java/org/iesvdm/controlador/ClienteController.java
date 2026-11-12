package org.iesvdm.controlador;

import java.time.LocalDate;
import java.util.List;

import org.iesvdm.modelo.Cliente;
import org.iesvdm.servicios.ClienteService;
import org.iesvdm.servicios.ComercialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller 
public class ClienteController {

    @Autowired 
    private ClienteService clienteService;

    @Autowired
    private ComercialService comercialService;

    @GetMapping("/clientes")
    public String listar(Model model) {
        List<Cliente> clientes = clienteService.listAll();
        model.addAttribute("clientes", clientes);
        return "clientes";
    }

    @GetMapping("/clientes/{id}")
    public String detalle(Model model, @PathVariable int id) {
        return clienteService.find(id)
                .map(cliente -> {
                    model.addAttribute("cliente", cliente);
                    model.addAttribute("pedidos", clienteService.listPedidosByCliente(id));
                    return "detalle-cliente";
                })
                .orElse("redirect:/clientes");
    }

    @GetMapping("/clientes/crear")
    public String crear(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "crear-cliente";
    }

    @PostMapping("/clientes/crear")
    public RedirectView submitCrear(@ModelAttribute("cliente") Cliente cliente) {
        clienteService.create(cliente);
        return new RedirectView("/clientes");
    }

    @GetMapping("/clientes/editar/{id}")
    public String editar(Model model, @PathVariable int id) {
        return clienteService.find(id)
                .map(cliente -> {
                    model.addAttribute("cliente", cliente);
                    model.addAttribute("comercialesVinculados", clienteService.listComercialesVinculados(id));
                    model.addAttribute("comercialesDisponibles", clienteService.listComercialesDisponibles(id));
                    model.addAttribute("comerciales", comercialService.listAll());
                    return "editar-cliente";
                })
                .orElse("redirect:/clientes");
    }

    @PostMapping("/clientes/editar/{id}")
    public RedirectView submitEditar(@PathVariable int id, @ModelAttribute("cliente") Cliente cliente) {
        cliente.setId(id);
        clienteService.update(cliente);
        return new RedirectView("/clientes");
    }

    @PostMapping({"/clientes/editar/{id}/comercial", "/clientes/{id}/comercial", "/clientes/editar/{id}/asociar"})
    public RedirectView submitVincularComercial(
            @PathVariable int id,
            @RequestParam int comercialId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fecha,
            @RequestParam(required = false) Integer prioridad) {

        clienteService.vincularComercial(id, comercialId, fecha, prioridad);
        return new RedirectView("/clientes/editar/" + id);
    }

    @PostMapping({"/clientes/editar/{clienteId}/comercial/{comercialId}/borrar", "/clientes/{clienteId}/comercial/{comercialId}/borrar", "/clientes/editar/{clienteId}/desvincular/{comercialId}"})
    public RedirectView submitDesvincularComercial(
            @PathVariable int clienteId,
            @PathVariable int comercialId) {

        clienteService.desvincularComercial(clienteId, comercialId);
        return new RedirectView("/clientes/editar/" + clienteId);
    }

    @PostMapping("/clientes/borrar/{id}")
    public RedirectView submitBorrar(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            clienteService.delete(id);
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error", "No se puede eliminar el cliente porque tiene pedidos asociados.");
        }
        return new RedirectView("/clientes");
    }
}

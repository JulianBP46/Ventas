package org.iesvdm.servicios;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.iesvdm.dao.ClienteDAO;
import org.iesvdm.dao.PedidoDAO;
import org.iesvdm.dto.ClienteComercialDTO;
import org.iesvdm.modelo.Cliente;
import org.iesvdm.modelo.Comercial;
import org.iesvdm.modelo.PedidoDetalle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service 
public class ClienteService {

    @Autowired
    private ClienteDAO clienteDAO;

    @Autowired
    private PedidoDAO pedidoDAO;

    public List<Cliente> listAll() {
        return clienteDAO.getAll();
    }

    public Optional<Cliente> find(int id) {
        return clienteDAO.find(id);
    }

    public void create(Cliente cliente) {
        clienteDAO.create(cliente);
    }

    public void update(Cliente cliente) {
        clienteDAO.update(cliente);
    }

    public void delete(int id) {
        clienteDAO.delete(id);
    }

    public List<PedidoDetalle> listPedidosByCliente(int clienteId) {
        return pedidoDAO.findByClienteId(clienteId);
    }

    public void vincularComercial(int clienteId, int comercialId, LocalDate fecha, Integer prioridad) {
        clienteDAO.vincularComercial(clienteId, comercialId, fecha, prioridad);
    }

    public void desvincularComercial(int clienteId, int comercialId) {
        clienteDAO.desvincularComercial(clienteId, comercialId);
    }

    public List<ClienteComercialDTO> listComercialesVinculados(int clienteId) {
        return clienteDAO.getComercialesVinculados(clienteId);
    }

    public List<Comercial> listComercialesDisponibles(int clienteId) {
        return clienteDAO.getComercialesDisponibles(clienteId);
    }
}

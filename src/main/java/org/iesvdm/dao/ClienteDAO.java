package org.iesvdm.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.iesvdm.dto.ClienteComercialDTO;
import org.iesvdm.modelo.Cliente;
import org.iesvdm.modelo.Comercial;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteDAO {
	public void create(Cliente cliente);
	public List<Cliente> getAll();
	public Optional<Cliente> find(int id);
	public void update(Cliente cliente);
	public void delete(int id);

	public void vincularComercial(int clienteId, int comercialId, LocalDate fecha, Integer prioridad);
	public void desvincularComercial(int clienteId, int comercialId);
	public List<ClienteComercialDTO> getComercialesVinculados(int clienteId);
	public List<Comercial> getComercialesDisponibles(int clienteId);
}

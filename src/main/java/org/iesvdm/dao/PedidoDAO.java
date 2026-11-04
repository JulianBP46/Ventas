package org.iesvdm.dao;

import java.util.List;
import java.util.Optional;
import org.iesvdm.modelo.Pedido;
import org.iesvdm.modelo.PedidoDetalle;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoDAO {
	void create(Pedido pedido);
	List<Pedido> getAll();
	Optional<Pedido> find(int id);
	void update(Pedido pedido);
	void delete(int id);

	List<PedidoDetalle> getAllDetalle();
	Optional<PedidoDetalle> findDetalle(int id);
}


package org.iesvdm.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.List;
import java.util.Optional;

import org.iesvdm.modelo.Pedido;
import org.iesvdm.modelo.PedidoDetalle;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class PedidoDAOImpl implements PedidoDAO {

	private final JdbcTemplate jdbcTemplate;

	public PedidoDAOImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public synchronized void create(Pedido pedido) {
		String sqlInsert = """
				INSERT INTO pedido (total, fecha, id_cliente, id_comercial)
				VALUES (?, ?, ?, ?)
				""";

		KeyHolder keyHolder = new GeneratedKeyHolder();
		int rows = jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sqlInsert, new String[] { "id" });
			int idx = 1;
			ps.setObject(idx++, pedido.getTotal(), Types.DOUBLE);
			if (pedido.getFecha() != null) {
				ps.setDate(idx++, Date.valueOf(pedido.getFecha()));
			} else {
				ps.setNull(idx++, Types.DATE);
			}
			ps.setInt(idx++, pedido.getClienteId());
			ps.setInt(idx++, pedido.getComercialId());
			return ps;
		}, keyHolder);

		if (keyHolder.getKey() != null) {
			pedido.setId(keyHolder.getKey().intValue());
		}
		log.info("Insertados {} registros de Pedido.", rows);
	}

	@Override
	public List<Pedido> getAll() {
		String sql = "SELECT id, total, fecha, id_cliente, id_comercial FROM pedido";
		List<Pedido> pedidos = jdbcTemplate.query(
				sql,
				(rs, rowNum) -> new Pedido(
						rs.getInt("id"),
						rs.getObject("total", Double.class),
						rs.getDate("fecha") != null ? rs.getDate("fecha").toLocalDate() : null,
						rs.getInt("id_cliente"),
						rs.getInt("id_comercial")
				)
		);
		log.info("Devueltos {} registros de Pedido.", pedidos.size());
		return pedidos;
	}

	@Override
	public Optional<Pedido> find(int id) {
		String sql = "SELECT id, total, fecha, id_cliente, id_comercial FROM pedido WHERE id = ?";
		List<Pedido> pedidos = jdbcTemplate.query(
				sql,
				(rs, rowNum) -> new Pedido(
						rs.getInt("id"),
						rs.getObject("total", Double.class),
						rs.getDate("fecha") != null ? rs.getDate("fecha").toLocalDate() : null,
						rs.getInt("id_cliente"),
						rs.getInt("id_comercial")
				),
				id
		);

		if (pedidos.isEmpty()) {
			log.info("Pedido con id {} no encontrado.", id);
		}
		return pedidos.stream().findFirst();
	}

	@Override
	public void update(Pedido pedido) {
		String sqlUpdate = """
				UPDATE pedido
				SET total = ?, fecha = ?, id_cliente = ?, id_comercial = ?
				WHERE id = ?
				""";
		int rows = jdbcTemplate.update(
				sqlUpdate,
				pedido.getTotal(),
				pedido.getFecha() != null ? Date.valueOf(pedido.getFecha()) : null,
				pedido.getClienteId(),
				pedido.getComercialId(),
				pedido.getId()
		);
		log.info("Update de Pedido con {} registros actualizados.", rows);
	}

	@Override
	public void delete(int id) {
		int rows = jdbcTemplate.update("DELETE FROM pedido WHERE id = ?", id);
		log.info("Delete de Pedido con {} registros eliminados.", rows);
	}

	private static final String SELECT_PEDIDOS_CON_NOMBRES = """
			SELECT p.id, p.total, p.fecha, p.id_cliente, p.id_comercial,
			       CONCAT_WS(' ', c.nombre, c.apellido1, c.apellido2) AS cliente_nombre,
			       CONCAT_WS(' ', com.nombre, com.apellido1, com.apellido2) AS comercial_nombre
			FROM pedido p
			LEFT JOIN cliente c ON p.id_cliente = c.id
			LEFT JOIN comercial com ON p.id_comercial = com.id
			""";

	private static final RowMapper<PedidoDetalle> PEDIDO_DETALLE_ROW_MAPPER = (rs, rowNum) -> new PedidoDetalle(
			rs.getInt("id"),
			rs.getObject("total", Double.class),
			rs.getDate("fecha") != null ? rs.getDate("fecha").toLocalDate() : null,
			rs.getInt("id_cliente"),
			rs.getInt("id_comercial"),
			rs.getString("cliente_nombre"),
			rs.getString("comercial_nombre")
	);

	@Override
	public List<PedidoDetalle> getAllDetalle() {
		List<PedidoDetalle> pedidosDetalle = jdbcTemplate.query(
				SELECT_PEDIDOS_CON_NOMBRES + " ORDER BY p.id",
				PEDIDO_DETALLE_ROW_MAPPER
		);
		log.info("Devueltos {} registros de Pedido con detalle.", pedidosDetalle.size());
		return pedidosDetalle;
	}

	@Override
	public Optional<PedidoDetalle> findDetalle(int id) {
		List<PedidoDetalle> pedidosDetalle = jdbcTemplate.query(
				SELECT_PEDIDOS_CON_NOMBRES + " WHERE p.id = ?",
				PEDIDO_DETALLE_ROW_MAPPER,
				id
		);

		if (pedidosDetalle.isEmpty()) {
			log.info("PedidoDetalle con id {} no encontrado.", id);
		}
		return pedidosDetalle.stream().findFirst();
	}

	@Override
	public List<PedidoDetalle> findByComercialId(int comercialId) {
		return jdbcTemplate.query(
				SELECT_PEDIDOS_CON_NOMBRES + " WHERE p.id_comercial = ?",
				PEDIDO_DETALLE_ROW_MAPPER,
				comercialId);
	}

	@Override
	public List<PedidoDetalle> findByClienteId(int clienteId) {
		return jdbcTemplate.query(
				SELECT_PEDIDOS_CON_NOMBRES + " WHERE p.id_cliente = ?",
				PEDIDO_DETALLE_ROW_MAPPER,
				clienteId);
	}
}


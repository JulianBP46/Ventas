package org.iesvdm.dao;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.iesvdm.modelo.Pedido;
import org.iesvdm.modelo.PedidoDetalle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

@ExtendWith(MockitoExtension.class)
public class PedidoDAOTest {

	@Mock
	private JdbcTemplate jdbcTemplate;

	@InjectMocks
	private PedidoDAOImpl pedidoDAO;

	private Pedido pedido;

	@BeforeEach
	public void setUp() {
		pedido = new Pedido(1, 250.75, LocalDate.of(2023, 6, 15), 3, 4);
	}

	@Test
	public void testGetAll() {
		when(jdbcTemplate.query(anyString(), any(RowMapper.class)))
				.thenReturn(List.of(pedido));

		List<Pedido> pedidos = pedidoDAO.getAll();

		assertEquals(1, pedidos.size());
		assertEquals(250.75, pedidos.get(0).getTotal());
		verify(jdbcTemplate, times(1)).query(anyString(), any(RowMapper.class));
	}

	@Test
	public void testFind() {
		when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq(1)))
				.thenReturn(List.of(pedido));

		Optional<Pedido> result = pedidoDAO.find(1);

		assertTrue(result.isPresent());
		assertEquals(1, result.get().getId());
	}

	@Test
	public void testUpdate() {
		when(jdbcTemplate.update(anyString(), any(), any(), any(), any(), any()))
				.thenReturn(1);

		pedidoDAO.update(pedido);

		verify(jdbcTemplate, times(1)).update(
				anyString(),
				eq(250.75),
				eq(Date.valueOf(LocalDate.of(2023, 6, 15))),
				eq(3),
				eq(4),
				eq(1)
		);
	}

	@Test
	public void testDelete() {
		when(jdbcTemplate.update(anyString(), eq(1))).thenReturn(1);

		pedidoDAO.delete(1);

		verify(jdbcTemplate, times(1)).update(anyString(), eq(1));
	}

	@Test
	public void testGetAllDetalle() {
		PedidoDetalle detalle = new PedidoDetalle(1, 250.75, LocalDate.of(2023, 6, 15), 3, 4, "Adolfo Rubio Flores", "Marta Herrera Gil");
		when(jdbcTemplate.query(anyString(), any(RowMapper.class)))
				.thenReturn(List.of(detalle));

		List<PedidoDetalle> list = pedidoDAO.getAllDetalle();

		assertEquals(1, list.size());
		assertEquals("Adolfo Rubio Flores", list.get(0).getClienteNombreCompleto());
		assertEquals("Marta Herrera Gil", list.get(0).getComercialNombreCompleto());
	}

	@Test
	public void testFindDetalle() {
		PedidoDetalle detalle = new PedidoDetalle(1, 250.75, LocalDate.of(2023, 6, 15), 3, 4, "Adolfo Rubio Flores", "Marta Herrera Gil");
		when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq(1)))
				.thenReturn(List.of(detalle));

		Optional<PedidoDetalle> result = pedidoDAO.findDetalle(1);

		assertTrue(result.isPresent());
		assertEquals("Adolfo Rubio Flores", result.get().getClienteNombreCompleto());
	}

	@Test
	public void testFindByComercialId() {
		PedidoDetalle detalle = new PedidoDetalle(1, 250.75, LocalDate.of(2023, 6, 15), 3, 4, "Adolfo Rubio Flores", "Marta Herrera Gil");
		when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq(4)))
				.thenReturn(List.of(detalle));

		List<PedidoDetalle> list = pedidoDAO.findByComercialId(4);

		assertEquals(1, list.size());
		assertEquals("Adolfo Rubio Flores", list.get(0).getClienteNombreCompleto());
		assertEquals("Marta Herrera Gil", list.get(0).getComercialNombreCompleto());
		verify(jdbcTemplate, times(1)).query(anyString(), any(RowMapper.class), eq(4));
	}

	@Test
	public void testFindByClienteId() {
		PedidoDetalle detalle = new PedidoDetalle(1, 250.75, LocalDate.of(2023, 6, 15), 3, 4, "Adolfo Rubio Flores", "Marta Herrera Gil");
		when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq(3)))
				.thenReturn(List.of(detalle));

		List<PedidoDetalle> list = pedidoDAO.findByClienteId(3);

		assertEquals(1, list.size());
		assertEquals("Adolfo Rubio Flores", list.get(0).getClienteNombreCompleto());
		verify(jdbcTemplate, times(1)).query(anyString(), any(RowMapper.class), eq(3));
	}

	@Test
	public void testFindClientesByComercialOrderByTotalDesc() {
		org.iesvdm.dto.ClienteTotalDTO dto = new org.iesvdm.dto.ClienteTotalDTO(1, "Adela", "Salas", "Díaz", "Granada", 200, 5000.0);
		when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq(1)))
				.thenReturn(List.of(dto));

		List<org.iesvdm.dto.ClienteTotalDTO> list = pedidoDAO.findClientesByComercialOrderByTotalDesc(1);

		assertEquals(1, list.size());
		assertEquals("Adela Salas Díaz", list.get(0).getNombreCompleto());
		assertEquals(5000.0, list.get(0).getTotalPedidos());
		verify(jdbcTemplate, times(1)).query(anyString(), any(RowMapper.class), eq(1));
	}
}


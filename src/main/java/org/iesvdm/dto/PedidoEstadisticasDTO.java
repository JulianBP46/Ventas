package org.iesvdm.dto;

import java.util.DoubleSummaryStatistics;
import java.util.List;

import org.iesvdm.modelo.PedidoDetalle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PedidoEstadisticasDTO {
	private Double total;
	private Double media;
	private Double max;
	private Double min;
	private long totalPedidos;

	public static PedidoEstadisticasDTO fromPedidos(List<PedidoDetalle> pedidos) {
		if (pedidos == null || pedidos.isEmpty()) {
			return new PedidoEstadisticasDTO(0.0, 0.0, null, null, 0);
		}

		DoubleSummaryStatistics stats = pedidos.stream()
				.filter(p -> p != null && p.getTotal() != null)
				.mapToDouble(PedidoDetalle::getTotal)
				.summaryStatistics();

		if (stats.getCount() == 0) {
			return new PedidoEstadisticasDTO(0.0, 0.0, null, null, 0);
		}

		return new PedidoEstadisticasDTO(
				stats.getSum(),
				stats.getAverage(),
				stats.getMax(),
				stats.getMin(),
				stats.getCount()
		);
	}
}

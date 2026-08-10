package com.api.sosremedio.dto.response;

import java.math.BigDecimal;

public record PriceStatisticsResponse(
        BigDecimal currentPrice,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        BigDecimal averagePrice,
        BigDecimal variationValue,
        BigDecimal variationPercent,
        int sampleCount
) {
}

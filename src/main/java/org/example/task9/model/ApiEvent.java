package org.example.task9.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record ApiEvent(Long id,
                       String title,
                       String price,
                       @JsonProperty("is_free") boolean isFree) {

    private static final Pattern PRICE_PATTERN = Pattern.compile("\\d+(?:\\s?\\d+)*");

    public boolean isFitsBudget(BigDecimal budget) {
        if (isFree) return true;

        if (price == null || price.isEmpty()) {
            return false;
        }

        Integer extractedPrice = extractPrice(price);

        return extractedPrice != null && budget.compareTo(BigDecimal.valueOf(extractedPrice)) >= 0;
    }

    private Integer extractPrice(String priceString) {
        Matcher matcher = PRICE_PATTERN.matcher(priceString);
        if (matcher.find()) {
            try {
                String priceStr = matcher.group().replaceAll("\\s", "");
                return Integer.parseInt(priceStr);
            } catch (NumberFormatException e) {
                // Ignore invalid numbers
            }
        }
        return null;
    }
}
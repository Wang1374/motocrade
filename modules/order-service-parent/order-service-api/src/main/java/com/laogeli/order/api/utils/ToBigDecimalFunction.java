package com.laogeli.order.api.utils;

import java.math.BigDecimal;

@FunctionalInterface
public interface ToBigDecimalFunction<T> {

    BigDecimal applyAsBigDecimal(T value);

}

package com.fashion.backend.service.impl;

import com.fashion.backend.repository.CommerceRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CommerceServiceImplTest {
    @Test
    void checkoutCalculatesAmountsAndPersistsOrderItems() {
        CommerceRepository repository=mock(CommerceRepository.class);
        CommerceServiceImpl service=new CommerceServiceImpl(repository);
        Map<String,Object> variant=new HashMap<>();
        variant.put("id",12);
        variant.put("product_name","Tee");
        variant.put("size","M");
        variant.put("color","Black");
        variant.put("stock",5);
        variant.put("price",new BigDecimal("100000.00"));
        variant.put("sale_price",new BigDecimal("80000.00"));
        when(repository.variantForCheckout(12)).thenReturn(variant);
        when(repository.createOrder(anyMap(),anyString(),any(),any(),any(),any())).thenReturn(45);
        when(repository.decreaseStock(12,2)).thenReturn(true);

        Map<String,Object> request=new HashMap<>();
        request.put("receiverName","Test");
        request.put("receiverPhone","0900000000");
        request.put("receiverAddress","Hanoi");
        request.put("userId",99);
        request.put("items",List.of(Map.of("variantId",12,"quantity",2)));
        Map<String,Object> response=service.checkout(request);

        assertEquals(new BigDecimal("160000.00"),response.get("total"));
        assertEquals(new BigDecimal("30000"),response.get("shippingFee"));
        assertEquals(new BigDecimal("190000.00"),response.get("finalAmount"));
        verify(repository).createOrder(argThat(order -> order.get("userId") == null),
            anyString(),any(),any(),any(),any());
        verify(repository).createOrderItem(45,variant,new BigDecimal("80000.00"),2);
        verify(repository).decreaseStock(12,2);
    }
}

package com.example.theclearvision.feature.order.mapper;

import com.example.theclearvision.feature.order.domain.OrderEntity;
import com.example.theclearvision.feature.order.domain.OrderItem;
import com.example.theclearvision.feature.order.dto.OrderDTO;
import com.example.theclearvision.feature.order.dto.OrderItemDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "payment.status", target = "paymentStatus")
    @Mapping(source = "user.firstName", target = "customerName")
    @Mapping(source = "user.email", target = "customerEmail")
    OrderDTO toDTO(OrderEntity order);

    @Mapping(source = "productVariant.id", target = "productVariantId")
    @Mapping(source = "productVariant.product.title", target = "productName")
    @Mapping(source = "productVariant.variantName", target = "variantName")
    OrderItemDTO toItemDTO(OrderItem item);

    List<OrderDTO> toDTOList(List<OrderEntity> orders);
}

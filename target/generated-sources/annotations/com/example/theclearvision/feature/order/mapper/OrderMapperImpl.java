package com.example.theclearvision.feature.order.mapper;

import com.example.theclearvision.domain.Product;
import com.example.theclearvision.domain.ProductVariant;
import com.example.theclearvision.domain.User;
import com.example.theclearvision.feature.order.domain.OrderEntity;
import com.example.theclearvision.feature.order.domain.OrderItem;
import com.example.theclearvision.feature.order.dto.OrderDTO;
import com.example.theclearvision.feature.order.dto.OrderItemDTO;
import com.example.theclearvision.feature.payment.domain.Payment;
import com.example.theclearvision.feature.payment.domain.PaymentStatus;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-28T11:35:17+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public OrderDTO toDTO(OrderEntity order) {
        if ( order == null ) {
            return null;
        }

        OrderDTO orderDTO = new OrderDTO();

        orderDTO.setPaymentStatus( orderPaymentStatus( order ) );
        orderDTO.setCustomerName( orderUserFirstName( order ) );
        orderDTO.setCustomerEmail( orderUserEmail( order ) );
        orderDTO.setId( order.getId() );
        orderDTO.setOrderNumber( order.getOrderNumber() );
        orderDTO.setStatus( order.getStatus() );
        orderDTO.setTotal( order.getTotal() );
        orderDTO.setCreatedAt( order.getCreatedAt() );
        orderDTO.setItems( orderItemListToOrderItemDTOList( order.getItems() ) );

        return orderDTO;
    }

    @Override
    public OrderItemDTO toItemDTO(OrderItem item) {
        if ( item == null ) {
            return null;
        }

        OrderItemDTO orderItemDTO = new OrderItemDTO();

        orderItemDTO.setProductVariantId( itemProductVariantId( item ) );
        orderItemDTO.setProductName( itemProductVariantProductTitle( item ) );
        orderItemDTO.setVariantName( itemProductVariantVariantName( item ) );
        orderItemDTO.setQuantity( item.getQuantity() );
        orderItemDTO.setUnitPrice( item.getUnitPrice() );
        orderItemDTO.setLineTotal( item.getLineTotal() );

        return orderItemDTO;
    }

    @Override
    public List<OrderDTO> toDTOList(List<OrderEntity> orders) {
        if ( orders == null ) {
            return null;
        }

        List<OrderDTO> list = new ArrayList<OrderDTO>( orders.size() );
        for ( OrderEntity orderEntity : orders ) {
            list.add( toDTO( orderEntity ) );
        }

        return list;
    }

    private PaymentStatus orderPaymentStatus(OrderEntity orderEntity) {
        if ( orderEntity == null ) {
            return null;
        }
        Payment payment = orderEntity.getPayment();
        if ( payment == null ) {
            return null;
        }
        PaymentStatus status = payment.getStatus();
        if ( status == null ) {
            return null;
        }
        return status;
    }

    private String orderUserFirstName(OrderEntity orderEntity) {
        if ( orderEntity == null ) {
            return null;
        }
        User user = orderEntity.getUser();
        if ( user == null ) {
            return null;
        }
        String firstName = user.getFirstName();
        if ( firstName == null ) {
            return null;
        }
        return firstName;
    }

    private String orderUserEmail(OrderEntity orderEntity) {
        if ( orderEntity == null ) {
            return null;
        }
        User user = orderEntity.getUser();
        if ( user == null ) {
            return null;
        }
        String email = user.getEmail();
        if ( email == null ) {
            return null;
        }
        return email;
    }

    protected List<OrderItemDTO> orderItemListToOrderItemDTOList(List<OrderItem> list) {
        if ( list == null ) {
            return null;
        }

        List<OrderItemDTO> list1 = new ArrayList<OrderItemDTO>( list.size() );
        for ( OrderItem orderItem : list ) {
            list1.add( toItemDTO( orderItem ) );
        }

        return list1;
    }

    private String itemProductVariantId(OrderItem orderItem) {
        if ( orderItem == null ) {
            return null;
        }
        ProductVariant productVariant = orderItem.getProductVariant();
        if ( productVariant == null ) {
            return null;
        }
        String id = productVariant.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String itemProductVariantProductTitle(OrderItem orderItem) {
        if ( orderItem == null ) {
            return null;
        }
        ProductVariant productVariant = orderItem.getProductVariant();
        if ( productVariant == null ) {
            return null;
        }
        Product product = productVariant.getProduct();
        if ( product == null ) {
            return null;
        }
        String title = product.getTitle();
        if ( title == null ) {
            return null;
        }
        return title;
    }

    private String itemProductVariantVariantName(OrderItem orderItem) {
        if ( orderItem == null ) {
            return null;
        }
        ProductVariant productVariant = orderItem.getProductVariant();
        if ( productVariant == null ) {
            return null;
        }
        String variantName = productVariant.getVariantName();
        if ( variantName == null ) {
            return null;
        }
        return variantName;
    }
}

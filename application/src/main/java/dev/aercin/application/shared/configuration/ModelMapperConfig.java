package dev.aercin.application.shared.configuration;

import dev.aercin.application.features.order_place.external_models.BasketItem;
import dev.aercin.application.shared.integration.events.produce.Message;
import dev.aercin.application.shared.integration.events.produce.OrderItem;
import dev.aercin.domain.entities.Order;
import dev.aercin.domain.entities.OrderProduct;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {

        ModelMapper modelMapper = new ModelMapper();

        // STRICT eşleme stratejisi ile yalnızca tam eşleşen property'ler dikkate alınır.
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        TypeMap<OrderProduct, OrderItem> tmOrderProduct2OrderPlacedEventItem = modelMapper.createTypeMap(OrderProduct.class, OrderItem.class);
        tmOrderProduct2OrderPlacedEventItem.addMappings(mapper -> {
            mapper.map(OrderProduct::getId, OrderItem::setProductId);
        });

        TypeMap<Order, Message> tmOrder2OrderPlacedEvent = modelMapper.createTypeMap(Order.class, Message.class);
        tmOrder2OrderPlacedEvent.addMappings(mapper -> {
            mapper.map(Order::getId, Message::setOrderId);
            mapper.map(Order::getProducts, Message::setItems);
        });

        TypeMap<BasketItem, OrderProduct> tmBasketItem2OrderItem = modelMapper.createTypeMap(BasketItem.class, OrderProduct.class);
        tmBasketItem2OrderItem.addMappings(mapper -> {
            mapper.map(BasketItem::getProductId, OrderProduct::setId);
        });

        return modelMapper;
    }
}

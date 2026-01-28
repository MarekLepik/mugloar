package com.lepik.mugloar.mappers;

import com.lepik.mugloar.client.dto.ShopResponse;
import com.lepik.mugloar.model.Item;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ShopMapper {
    public Item toDomain(ShopResponse shopResponse) {
        return new Item(
                shopResponse.getId(),
                shopResponse.getName(),
                shopResponse.getCost()
        );
    }

    public List<Item> toDomainList(ShopResponse[] shopResponse) {
        return Arrays.stream(shopResponse)
                .map(this::toDomain)
                .toList();
    }
}

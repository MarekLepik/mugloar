package com.lepik.mugloar.mappers;

import com.lepik.mugloar.client.dto.PurchaseResponse;
import com.lepik.mugloar.model.Purchase;
import org.springframework.stereotype.Component;

@Component
public class PurchaseMapper {
    public Purchase toDomain(PurchaseResponse purchaseResponse) {
        if (purchaseResponse == null) {
            return null;
        }

        return new Purchase(
                purchaseResponse.isShoppingSuccess(),
                purchaseResponse.getGold(),
                purchaseResponse.getLives(),
                purchaseResponse.getLevel(),
                purchaseResponse.getTurn()
        );
    }
}

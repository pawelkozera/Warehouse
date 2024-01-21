package org.CommunicationModule.Commodity;

import java.io.Serializable;

public record CommodityReception (int id, int commodityId, int receptionId, int quantity) implements Serializable {
    public CommodityReception(int commodityId, int quantity) {
        this(
                0, commodityId, 0, quantity
        );
    }
}
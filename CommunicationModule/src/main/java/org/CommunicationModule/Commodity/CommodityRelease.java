package org.CommunicationModule.Commodity;

import java.io.Serializable;

public record CommodityRelease (int commodityId, String releaseId, int quantity) implements Serializable {
    public CommodityRelease(int commodityId, int quantity) {
        this(
                 commodityId, "", quantity
        );
    }
}

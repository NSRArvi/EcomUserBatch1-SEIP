package com.tanvir.training.ecomuserbatch1.callbacks;

import com.tanvir.training.ecomuserbatch1.models.CartModel;

public interface AddRemoveCartItemListener {
    void onCartItemAdd(CartModel cartModel, int position);
    void onCartItemRemove(String productId, int position);
}

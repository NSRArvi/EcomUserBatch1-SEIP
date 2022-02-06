package com.tanvir.training.ecomuserbatch1.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.tanvir.training.ecomuserbatch1.callbacks.AddRemoveCartItemListener;
import com.tanvir.training.ecomuserbatch1.callbacks.OnProductItemClickListener;
import com.tanvir.training.ecomuserbatch1.databinding.ProductRowItemBinding;
import com.tanvir.training.ecomuserbatch1.models.CartModel;
import com.tanvir.training.ecomuserbatch1.models.ProductModel;
import com.tanvir.training.ecomuserbatch1.models.UserProductModel;

public class ProductAdapter extends ListAdapter<UserProductModel, ProductAdapter.ProductViewHolder> {
    private OnProductItemClickListener listener;
    private AddRemoveCartItemListener cartItemListener;
    public ProductAdapter(OnProductItemClickListener listener, AddRemoveCartItemListener cartItemListener) {
        super(new ProductDiff());
        this.listener = listener;
        this.cartItemListener = cartItemListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final ProductRowItemBinding binding = ProductRowItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        final UserProductModel userProductModel = getItem(position);
        holder.bind(userProductModel);
        if (getItem(position).isInCart()) {
            holder.binding.addToCartBtn.setVisibility(View.GONE);
            holder.binding.removeFromCartBtn.setVisibility(View.VISIBLE);
        }else {
            holder.binding.addToCartBtn.setVisibility(View.VISIBLE);
            holder.binding.removeFromCartBtn.setVisibility(View.GONE);
        }

        holder.binding.addToCartBtn.setOnClickListener(v -> {
            userProductModel.setInCart(true);
            //notifyItemChanged(position, userProductModel);
            final CartModel cartModel = new CartModel(
                    userProductModel.getProductId(),
                    userProductModel.getProductName(),
                    userProductModel.getPrice(),
                    1
            );
            cartItemListener.onCartItemAdd(cartModel, position);
        });
        holder.binding.removeFromCartBtn.setOnClickListener(v -> {
            userProductModel.setInCart(false);
            //notifyItemChanged(position, userProductModel);
            cartItemListener.onCartItemRemove(userProductModel.getProductId(), position);
        });

    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        private ProductRowItemBinding binding;
        public ProductViewHolder(ProductRowItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.productRowCardView.setOnClickListener(v -> {
                listener.onProductItemClicked(
                        getItem(getAdapterPosition()).getProductId());
            });
        }
        public void bind(UserProductModel productModel) {
            binding.setProduct(productModel);
        }
    }

    static class ProductDiff extends DiffUtil.ItemCallback<UserProductModel>{
        @Override
        public boolean areItemsTheSame(@NonNull UserProductModel oldItem, @NonNull UserProductModel newItem) {
            return oldItem.getProductId().equals(newItem.getProductId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull UserProductModel oldItem, @NonNull UserProductModel newItem) {
            return oldItem.getProductId().equals(newItem.getProductId());
        }
    }
}

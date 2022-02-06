package com.tanvir.training.ecomuserbatch1;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tanvir.training.ecomuserbatch1.adapters.ProductAdapter;
import com.tanvir.training.ecomuserbatch1.callbacks.AddRemoveCartItemListener;
import com.tanvir.training.ecomuserbatch1.callbacks.OnActionCompleteListener;
import com.tanvir.training.ecomuserbatch1.callbacks.OnProductItemClickListener;
import com.tanvir.training.ecomuserbatch1.databinding.FragmentProductListBinding;
import com.tanvir.training.ecomuserbatch1.models.CartModel;
import com.tanvir.training.ecomuserbatch1.viewmodels.LoginViewModel;
import com.tanvir.training.ecomuserbatch1.viewmodels.ProductViewModel;

public class ProductListFragment extends Fragment {
    private LoginViewModel loginViewModel;
    private ProductViewModel productViewModel;
    private FragmentProductListBinding binding;
    private ProductAdapter adapter;
    public ProductListFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProductListBinding.inflate(inflater);
        loginViewModel = new ViewModelProvider(requireActivity())
                .get(LoginViewModel.class);
        loginViewModel.getStateLiveData()
                .observe(getViewLifecycleOwner(), authState -> {
                    if (authState == LoginViewModel.AuthState.UNAUTHENTICATED) {
                        Navigation.findNavController(container)
                                .navigate(R.id.action_productListFragment_to_loginFragment);
                    }
                });
        productViewModel = new ViewModelProvider(requireActivity())
                .get(ProductViewModel.class);
        adapter = new ProductAdapter(productId -> {
            // TODO: 2/3/2022 go to details page with this id
        }, cartItemListener);
        binding.productRV.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        binding.productRV.setAdapter(adapter);
        productViewModel.productListLiveData.observe(getViewLifecycleOwner(),
                productList -> {
                    if (!productList.isEmpty()) {
                        productViewModel.getAllCartItems(loginViewModel.getUser().getUid());
                    }
                    //adapter.submitList(productList);
                });

        productViewModel.userProductListLiveData.observe(
                getViewLifecycleOwner(), userProductModels -> {
                    adapter.submitList(userProductModels);
                }
        );

        return binding.getRoot();
    }

    private AddRemoveCartItemListener cartItemListener = new AddRemoveCartItemListener() {
        @Override
        public void onCartItemAdd(CartModel cartModel, int position) {
            productViewModel.addToCart(cartModel, loginViewModel.getUser().getUid(), new OnActionCompleteListener() {
                @Override
                public void onSuccess() {
                    adapter.notifyItemChanged(position);
                }

                @Override
                public void onFailure() {

                }
            });
        }

        @Override
        public void onCartItemRemove(String productId, int position) {
            productViewModel.removeFromCart(
                    loginViewModel.getUser().getUid(),
                    productId, new OnActionCompleteListener() {
                        @Override
                        public void onSuccess() {
                            adapter.notifyItemChanged(position);
                        }

                        @Override
                        public void onFailure() {

                        }
                    }
            );
        }
    };
}
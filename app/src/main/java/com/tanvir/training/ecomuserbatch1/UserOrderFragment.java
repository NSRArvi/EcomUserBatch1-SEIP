package com.tanvir.training.ecomuserbatch1;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tanvir.training.ecomuserbatch1.adapters.OrderAdapter;
import com.tanvir.training.ecomuserbatch1.databinding.FragmentUserOrderBinding;
import com.tanvir.training.ecomuserbatch1.viewmodels.LoginViewModel;
import com.tanvir.training.ecomuserbatch1.viewmodels.OrderViewModel;

public class UserOrderFragment extends Fragment {
    private OrderViewModel orderViewModel;
    private LoginViewModel loginViewModel;
    private FragmentUserOrderBinding binding;
    public UserOrderFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserOrderBinding.inflate(inflater);
        loginViewModel = new ViewModelProvider(requireActivity())
                .get(LoginViewModel.class);
        orderViewModel = new ViewModelProvider(requireActivity())
                .get(OrderViewModel.class);
        orderViewModel.getAllOrders(loginViewModel.getUser().getUid());
        final OrderAdapter adapter = new OrderAdapter();
        binding.orderListRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.orderListRV.setAdapter(adapter);
        orderViewModel.getOrderListLiveData().observe(getViewLifecycleOwner(),
                orderModels -> {
                    adapter.submitList(orderModels);
                });

        return binding.getRoot();
    }
}
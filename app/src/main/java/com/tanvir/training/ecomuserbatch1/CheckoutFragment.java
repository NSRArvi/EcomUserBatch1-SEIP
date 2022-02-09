package com.tanvir.training.ecomuserbatch1;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.Toolbar;

import com.tanvir.training.ecomuserbatch1.callbacks.OnActionCompleteListener;
import com.tanvir.training.ecomuserbatch1.databinding.FragmentCheckoutBinding;
import com.tanvir.training.ecomuserbatch1.utils.Constants;
import com.tanvir.training.ecomuserbatch1.viewmodels.LoginViewModel;
import com.tanvir.training.ecomuserbatch1.viewmodels.ProductViewModel;

public class CheckoutFragment extends Fragment {
    private ProductViewModel productViewModel;
    private LoginViewModel loginViewModel;
    private FragmentCheckoutBinding binding;
    public CheckoutFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        productViewModel = new ViewModelProvider(requireActivity())
                .get(ProductViewModel.class);
        loginViewModel = new ViewModelProvider(requireActivity())
                .get(LoginViewModel.class);
        loginViewModel.getUserData();
        binding = FragmentCheckoutBinding.inflate(inflater);
        binding.paymentRG.setOnCheckedChangeListener((group, checkedId) -> {
            final RadioButton rb = container.findViewById(checkedId);
            productViewModel.paymentMethod = rb.getText().toString();
        });
        loginViewModel.getEcomUserMutableLiveData().observe(getViewLifecycleOwner(), ecomUser -> {
            if (ecomUser.getDeliveryAddress() != null) {
                binding.deliveryAddressET.setText(ecomUser.getDeliveryAddress());
            }
        });
        binding.nextBtn.setOnClickListener(v -> {
            final String address = binding.deliveryAddressET.getText().toString();
            if (address.isEmpty()) {
                binding.deliveryAddressET.setError("Provide a valid delivery address");
                return;
            }

            loginViewModel.updateDeliveryAddress(address, new OnActionCompleteListener() {
                @Override
                public void onSuccess() {
                    //Toast.makeText(getActivity(), "address saved", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(v)
                            .navigate(R.id.action_checkoutFragment_to_confirmationFragment);
                }

                @Override
                public void onFailure() {
                    Toast.makeText(getActivity(), "could not save address", Toast.LENGTH_SHORT).show();
                }
            });
        });
        return binding.getRoot();
    }
}
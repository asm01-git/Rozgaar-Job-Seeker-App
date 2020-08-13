package com.asm.rozgaar.Fragments.RegisterFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.asm.rozgaar.R;


public class UserTypeFragment extends Fragment implements View.OnClickListener{
    Button seekerRegister,providerRegister;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_user_type,container,false);
        seekerRegister=v.findViewById(R.id.register_seeker);
        providerRegister=v.findViewById(R.id.register_provider);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("Job Seeker/Provider");
        seekerRegister.setOnClickListener(this);
        providerRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.register_seeker:
                String phone=getArguments().getString("phone");//Getting data from Register Activity
                //Now pass this data to the details fragment
                SeekerDetailsFragment sdf=new SeekerDetailsFragment(phone);

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack("SeekerDetailsFragment")
                        .replace(R.id.root_register_activity,sdf)
                        .commit();
                break;
            case R.id.register_provider:
                ProviderDetailsFragment pdf=new ProviderDetailsFragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack("ProviderDetailsFragment")
                        .replace(R.id.root_register_activity,pdf)
                        .commit();

        }
    }


}

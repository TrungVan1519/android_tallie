package com.example.tallie.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tallie.LoginActivity;
import com.example.tallie.R;
import com.example.tallie.adapters.BookAdapter;
import com.example.tallie.models.Book;
import com.example.tallie.models.Cart;
import com.example.tallie.utils.DBHandler;
import com.example.tallie.utils.Formatter;
import com.example.tallie.utils.SharedPreferencesHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InfomationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    List<Book> orderBooks, favBooks, seenBooks;
    BookAdapter orderAdapter, favAdapter, seenAdapter;

    TextView txtUsername;
    Button btnLogout, btnSetting;
    FloatingActionButton btnPayCart, btnClearCart, btnFavorite, btnSeen;
    RecyclerView rcvMyOrders, rcvMyCollection;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        txtUsername = view.findViewById(R.id.txtUsername);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnSetting = view.findViewById(R.id.btnSetting);
        btnPayCart = view.findViewById(R.id.btnPayCart);
        btnClearCart = view.findViewById(R.id.btnClearCart);
        btnFavorite = view.findViewById(R.id.btnFavorite);
        btnSeen = view.findViewById(R.id.btnSeen);
        rcvMyOrders = view.findViewById(R.id.rcvMyOrders);
        rcvMyCollection = view.findViewById(R.id.rcvMyCollection);

        // TODO: set up TextView
        txtUsername.setText(SharedPreferencesHandler.loadAppData(getActivity()).get(0));

        // TODO: set up RecyclerView
        setupRecyclerView(R.id.btnFavorite);

        // TODO: handle e
        btnFavorite.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.purple_200)));
        btnLogout.setOnClickListener(this);
        btnPayCart.setOnClickListener(this);
        btnClearCart.setOnClickListener(this);
        btnFavorite.setOnClickListener(this);
        btnSeen.setOnClickListener(this);

        // Inflate the layout for this fragment
        return view;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogout:
                SharedPreferencesHandler.saveAppData(getActivity(), "", "");
                new DBHandler(getActivity()).dropDB();

                Intent i = new Intent(getActivity(), LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                getActivity().finish();

                Toast.makeText(getActivity(), "Log out done", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnSetting:
                Toast.makeText(getActivity(), "User setting is not available", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnPayCart:
                long result = new DBHandler(getActivity())
                        .addRecord(new Cart(Formatter.dateToString(Calendar.getInstance().getTime()), orderBooks.size()));
                if (result > -1) {
                    Toast.makeText(getActivity(), "Pay cart done", Toast.LENGTH_SHORT).show();
                }

                orderBooks.clear();
                orderAdapter.notifyDataSetChanged();
                break;
            case R.id.btnClearCart:
                orderBooks.clear();
                orderAdapter.notifyDataSetChanged();
                break;
            case R.id.btnFavorite:
            case R.id.btnSeen:
                setupRecyclerView(v.getId());
                changeBackgroundTint((FloatingActionButton) v);
                break;
            default:
                break;
        }
    }

    private void setupRecyclerView(int id) {
        orderBooks = new ArrayList<>();
        orderAdapter = new BookAdapter(getActivity(), R.layout.layout_book_row, orderBooks);
        rcvMyOrders.setAdapter(orderAdapter);
        rcvMyOrders.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));

        if (id == R.id.btnFavorite) {
            favBooks = new ArrayList<>();
            favAdapter = new BookAdapter(getActivity(), R.layout.layout_book_row, favBooks);
            rcvMyCollection.setAdapter(favAdapter);
        } else {
            seenBooks = new ArrayList<>();
            seenAdapter = new BookAdapter(getActivity(), R.layout.layout_book_row, seenBooks);
            rcvMyCollection.setAdapter(seenAdapter);
        }
        rcvMyCollection.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));
    }

    private void changeBackgroundTint(FloatingActionButton chosen) {
        for (FloatingActionButton btn : Arrays.asList(btnFavorite, btnSeen)) {
            btn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
        }
        chosen.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.purple_200)));
    }
}
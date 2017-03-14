package com.mubeen.vanesa.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.mubeen.vanesa.Classes.Product;
import com.mubeen.vanesa.R;
import com.mubeen.vanesa.app.AppController;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.support.design.R.styleable.RecyclerView;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ItemFragment extends Fragment {

    ProgressDialog pDialog;

    public static ArrayList<Product> productArrayList = new ArrayList<>();
    RecyclerView.Adapter productsAdapter;
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ItemFragment newInstance(int columnCount) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading products");
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            requestJsonData("http://production.technology-architects.com/vanesa/testapi.php");

            productsAdapter = new MyItemRecyclerViewAdapter(productArrayList, mListener,getActivity());

            recyclerView.setAdapter(productsAdapter);
        }
        return view;
    }

    public void requestJsonData(String url){
        showpDialog();
        productArrayList.clear();

        JsonArrayRequest productsRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                Log.d("response", String.valueOf(jsonArray));
                for(int i =0; i<jsonArray.length();i++){
                    try {
                        JSONObject productOBJ = (JSONObject) jsonArray.get(i);
                        String name = productOBJ.getString("name");
                        int id = Integer.parseInt(productOBJ.getString("id"));
                        double price = Double.parseDouble(productOBJ.getString("price"));
                        String imgurl = productOBJ.getString("image");
                        String desc = productOBJ.getString("description");

                        Product p = new Product(id,name,price,imgurl,desc);
                        productArrayList.add(p);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                productsAdapter.notifyDataSetChanged();
                hidepDialog();
           }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
           Log.d("json error:", String.valueOf(volleyError));
            hidepDialog();
            }

        });

        AppController.getInstance().addToRequestQueue(productsRequest);

    }

    private void hidepDialog() {
        if(pDialog.isShowing()){
            pDialog.dismiss();
        }
    }

    private void showpDialog(){
        if(!pDialog.isShowing()){
            pDialog.show();
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Product item);
    }
}
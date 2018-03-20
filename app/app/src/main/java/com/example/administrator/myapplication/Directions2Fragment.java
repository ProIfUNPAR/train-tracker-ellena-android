package com.example.administrator.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Directions2Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Directions2Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Directions2Fragment extends Fragment   {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public Button but1;
    String[] items;
    ArrayList<String> listItems;
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter2;
    ListView listView;
    EditText editText;
    ListView listView2;
    EditText editText2;
    String data[]={"Canada","Japan","China","USA"};

    private OnFragmentInteractionListener mListener;

    public Directions2Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Directions2Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Directions2Fragment newInstance(String param1, String param2) {
        Directions2Fragment fragment = new Directions2Fragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_directions2,container,false);
        Button directionsButton = (Button) view.findViewById(R.id.directions_button);
        directionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"Directions Button",Toast.LENGTH_SHORT).show();

            }
        });


        listView = (ListView) view.findViewById(R.id.listview1);
        editText = (EditText) view.findViewById(R.id.txtsearch1);
        listView2 = (ListView) view.findViewById(R.id.listview2);
        editText2 = (EditText) view.findViewById(R.id.txtsearch2);

        but1 = (Button) view.findViewById(R.id.directions_button);
        but1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent toy = new Intent(Directions2Fragment.super.getContext(),ResultSearchActivity.class);
                startActivity(toy);

            }

        });
        //initList(view);
        //initList2(view);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }


            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                if(!charSequence.toString().equals("")){
                    initList(view);
                   }
                else{
                    if(charSequence.toString().equals("")){
                        //reset listview
                       // initList(view);

                    } else{
                        //perform search
                        searchItem(charSequence.toString(), adapter);
                    }
                }



            }



            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().equals("")){
                    //reset listview
                    //initList(view);
                } else{
                    //perform search
                    searchItem(editable.toString(), adapter);
                }
            }
        });

        editText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().equals("")){
                initList2(view);
                }
                else{
                if(charSequence.toString().equals("")){
                    //reset listview
                    //initList2(view);

                } else{
                    //perform search
                    searchItem(charSequence.toString(), adapter2);
                }
                }

            }



            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().equals("")){
                    //reset listview
                   //initList2(view);
                } else{
                    //perform search
                    searchItem(editable.toString(), adapter2);
                }
            }
        });


        return  view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {

        }
    }

      //Toast.makeText(context,"null",Toast.LENGTH_SHORT).show();

    //Directions Fragment Attached
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    public void searchItem(String textToSearch, ArrayAdapter x){
        for (String item:items){
            if(!item.contains(textToSearch)){
                listItems.remove(item);
            }
        }
        x.notifyDataSetChanged();
    }

    public void initList(View view){
        items = new String [] {"Canada","China","Japan","USA"};
        listItems = new ArrayList<>(Arrays.asList(items));
        adapter = new ArrayAdapter<String>(view.getContext(), R.layout.list_item,R.id.textView,listItems);
        listView.setAdapter(adapter);

    }


    public void initList2(View view){
        items = new String [] {"Canada","China","Japan","USA"};
        listItems = new ArrayList<>(Arrays.asList(items));
        adapter2 = new ArrayAdapter<String>(view.getContext(), R.layout.list_item,R.id.textView,listItems);
        listView2.setAdapter(adapter2);
    }

    public void init(){
   //  but1 = (Button) getView().findViewById(R.id.directions_button);
   //  but1.setOnClickListener(getView().onCli);

    }
}

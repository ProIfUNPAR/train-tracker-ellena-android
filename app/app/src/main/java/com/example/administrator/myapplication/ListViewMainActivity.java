package com.example.administrator.myapplication;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toolbar;
import java.util.ArrayList;
import java.util.Arrays;



/**
 * Created by Administrator on 21/02/2018.
 */

public class ListViewMainActivity  extends AppCompatActivity{

  //  android.support.v7.widget.Toolbar toolbar;
    ListView listSearch;
    EditText editSearch;
    ArrayAdapter<String> adapter;
    String data[]={"Canada","Japan","China","USA"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_directions2);

        listSearch = (ListView)findViewById(R.id.listview1);
        editSearch = (EditText)findViewById(R.id.txtsearch1);
        adapter = new ArrayAdapter<String>(this,R.layout.list_item,R.id.textView,data);
        listSearch.setAdapter(adapter);

       // toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
     //  setSupportActionBar(toolbar);


    }



    /**
    String[] items;
    ArrayList<String> listItems;
    ArrayAdapter<String> adapter;
    ListView listView;
    EditText editText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_directions2);
        listView = (ListView) findViewById(R.id.listview1);
        editText = (EditText) findViewById(R.id.txtsearch1);
        initList();
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                 if(charSequence.toString().equals("")){
                     //reset listview
                     initList();
                 } else{
                     //perform search
                     searchItem(charSequence.toString());
                 }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void searchItem(String textToSearch){
        for (String item:items){
            if(!item.contains(textToSearch)){
                listItems.remove(item);
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void initList(){
        items = new String [] {"Canada","China","Japan","USA"};
        listItems = new ArrayList<>(Arrays.asList(items));
        adapter = new ArrayAdapter<String>(this, R.layout.list_item,R.id.txtitem,listItems);
        listView.setAdapter(adapter);
    }
     **/
}

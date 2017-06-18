package es.ulpgc.eii.android.project3.practica3_marlonfernandez.fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FilterQueryProvider;
import android.widget.ListView;

import es.ulpgc.eii.android.project3.practica3_marlonfernandez.MainActivity;
import es.ulpgc.eii.android.project3.practica3_marlonfernandez.R;
import es.ulpgc.eii.android.project3.practica3_marlonfernandez.db.AppDatabase;
import es.ulpgc.eii.android.project3.practica3_marlonfernandez.db.AppHelperDB;
import es.ulpgc.eii.android.project3.practica3_marlonfernandez.model.Customer;

public class CustomerListFragment extends Fragment {
    private AppHelperDB customerHelper;
    private ListView listView;

    public CustomerListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_list, container, false);
        listView = (ListView) view.findViewById(R.id.list_customers);

        // Generate ListView from SQLite Database //
        displayListView();

        return view;
    }

    private void displayListView() {
        Context context = getContext();
        AppDatabase helper = new AppDatabase(context);
        customerHelper = new AppHelperDB(helper);
        customerHelper.open();
        Cursor cursor = customerHelper.fetchAllCustomers();

        // The desired columns to be bound
        String[] columns = new String[]{
                AppDatabase.KEY_NAME_CUSTOMER,
                AppDatabase.KEY_ADDRESS_CUSTOMER
        };

        // the XML defined views which the data will be bound to
        int[] to = new int[]{
                R.id.title,
                R.id.subtitle
        };

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        SimpleCursorAdapter dataAdapter;
        if (((MainActivity) getActivity()).isSelectViewShown()) {
            dataAdapter = new SimpleCursorAdapter(context,
                    R.layout.item_select_list, cursor, columns, to, 0);
        } else {
            dataAdapter =
                    new SimpleCursorAdapter(context, R.layout.item_list, cursor, columns, to, 0);
        }

        // Assign adapter to ListView
        dataAdapter.notifyDataSetChanged();
        listView.setAdapter(dataAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) adapter.getItemAtPosition(position);

                // Get the state's capital from this row in the database.
                int columnID = cursor.getColumnIndexOrThrow(AppDatabase.KEY_ID_CUSTOMER);
                int columnName = cursor.getColumnIndexOrThrow(AppDatabase.KEY_NAME_CUSTOMER);
                int columnAddress = cursor.getColumnIndexOrThrow(AppDatabase.KEY_ADDRESS_CUSTOMER);
                int idCustomer = cursor.getInt(columnID);
                String name = cursor.getString(columnName);
                String address = cursor.getString(columnAddress);
                Customer customer = new Customer(idCustomer, name, address);

                if (((MainActivity) getActivity()).isSelectViewShown()) {
                    ((MainActivity) getActivity()).setCustomer(customer);
                } else {
                    ((MainActivity) getActivity()).openCustomerForm(customer);
                }
            }
        });

        dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return customerHelper.fetchCustomerByID(Integer.valueOf(constraint.toString()));
            }
        });
    }

}

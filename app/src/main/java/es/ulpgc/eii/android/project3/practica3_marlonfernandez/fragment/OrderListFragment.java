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
import es.ulpgc.eii.android.project3.practica3_marlonfernandez.model.Order;
import es.ulpgc.eii.android.project3.practica3_marlonfernandez.model.Product;

public class OrderListFragment extends Fragment {
    private AppHelperDB orderHelper;
    private ListView listView;

    public OrderListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);
        listView = (ListView) view.findViewById(R.id.list_products);

        // Generate ListView from SQLite Database //
        displayListView();

        return view;
    }

    private void displayListView() {
        Context context = getContext();
        AppDatabase helper = new AppDatabase(context);
        orderHelper = new AppHelperDB(helper);
        orderHelper.open();

        Cursor cursor = orderHelper.fetchAllOrders();

        // The desired columns to be bound
        String[] columns = new String[]{
                "customer_name",
                "product_name",
                AppDatabase.KEY_CODE_ORDER
        };

        // the XML defined views which the data will be bound to
        int[] to = new int[]{
                R.id.title,
                R.id.subtitle,
                R.id.code
        };

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        SimpleCursorAdapter dataAdapter =
                new SimpleCursorAdapter(context, R.layout.item_list, cursor, columns, to, 0);

        // Assign adapter to ListView
        dataAdapter.notifyDataSetChanged();
        listView.setAdapter(dataAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                // Get the state's capital from this row in the database.
                int columnID = cursor.getColumnIndexOrThrow(AppDatabase.KEY_ID_ORDER);
                int columnCode = cursor.getColumnIndexOrThrow(AppDatabase.KEY_CODE_ORDER);
                int columnDate = cursor.getColumnIndexOrThrow(AppDatabase.KEY_DATE_ORDER);
                int columnIdCustomer = cursor.getColumnIndexOrThrow(AppDatabase.KEY_ID_CUSTOMER_ORDER);
                int columnNameCustomer = cursor.getColumnIndexOrThrow("customer_name");
                int columnAddressCustomer = cursor.getColumnIndexOrThrow(AppDatabase.KEY_ADDRESS_CUSTOMER);
                int columnIdProduct = cursor.getColumnIndexOrThrow(AppDatabase.KEY_ID_PRODUCT_ORDER);
                int columnNameProduct = cursor.getColumnIndexOrThrow("product_name");
                int columnDescriptionProduct = cursor.getColumnIndexOrThrow(AppDatabase.KEY_DESCRIPTION_PRODUCT);
                int columnPriceProduct = cursor.getColumnIndexOrThrow(AppDatabase.KEY_PRICE_PRODUCT);
                int columnQuantity = cursor.getColumnIndexOrThrow(AppDatabase.KEY_QUANTITY_ORDER);

                int idOrder = cursor.getInt(columnID);
                String code = cursor.getString(columnCode);
                String date = cursor.getString(columnDate);
                String[] dateParts = date.split("-");
                String strYear = dateParts[0];
                String strMonth = dateParts[1];
                String strDay = dateParts[2];
                int day = Integer.valueOf(strDay);
                int month = Integer.valueOf(strMonth) - 1;
                int year = Integer.valueOf(strYear);
                int idCustomer = cursor.getInt(columnIdCustomer);
                String nameCustomer = cursor.getString(columnNameCustomer);
                String address = cursor.getString(columnAddressCustomer);
                int idProduct = cursor.getInt(columnIdProduct);
                String nameProduct = cursor.getString(columnNameProduct);
                String description = cursor.getString(columnDescriptionProduct);
                float price = cursor.getFloat(columnPriceProduct);
                int quantity = cursor.getInt(columnQuantity);

                Customer customer = new Customer(idCustomer, nameCustomer, address);
                Product product = new Product(idProduct, nameProduct, description, price);
                //String description = cursor.getString(columnDescription);
                Order order = new Order(idOrder, code, day, month, year, customer, product, quantity);
                ((MainActivity) getActivity()).openOrderForm(order);
            }
        });

        dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return orderHelper.fetchOrderByID(Integer.valueOf(constraint.toString()));
            }
        });
    }

}

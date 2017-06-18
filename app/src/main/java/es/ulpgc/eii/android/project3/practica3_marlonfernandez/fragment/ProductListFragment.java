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
import es.ulpgc.eii.android.project3.practica3_marlonfernandez.model.Product;

public class ProductListFragment extends Fragment {
    private AppHelperDB productHelper;
    private ListView listView;

    public ProductListFragment() {
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
        productHelper = new AppHelperDB(helper);
        productHelper.open();

        Cursor cursor = productHelper.fetchAllProducts();

        // The desired columns to be bound
        String[] columns = new String[]{
                AppDatabase.KEY_NAME_PRODUCT,
                AppDatabase.KEY_DESCRIPTION_PRODUCT,
                AppDatabase.KEY_PRICE_PRODUCT
        };

        // the XML defined views which the data will be bound to
        int[] to = new int[]{
                R.id.title,
                R.id.subtitle
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
                int columnID = cursor.getColumnIndexOrThrow(AppDatabase.KEY_ID_PRODUCT);
                int columnName = cursor.getColumnIndexOrThrow(AppDatabase.KEY_NAME_PRODUCT);
                int columnDescription = cursor.getColumnIndexOrThrow(AppDatabase.KEY_DESCRIPTION_PRODUCT);
                int columnPrice = cursor.getColumnIndexOrThrow(AppDatabase.KEY_PRICE_PRODUCT);
                int idProduct = cursor.getInt(columnID);
                float price = cursor.getFloat(columnPrice);
                String name = cursor.getString(columnName);
                String description = cursor.getString(columnDescription);
                Product product = new Product(idProduct, name, description, price);

                if (((MainActivity) getActivity()).isSelectViewShown()) {
                    ((MainActivity) getActivity()).setProduct(product);
                } else {
                    ((MainActivity) getActivity()).openProductForm(product);
                }
            }
        });

        dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return productHelper.fetchProductByID(Integer.valueOf(constraint.toString()));
            }
        });
    }

}

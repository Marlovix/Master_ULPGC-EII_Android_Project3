package es.ulpgc.eii.android.project3.practica3_marlonfernandez;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import es.ulpgc.eii.android.project3.practica3_marlonfernandez.fragment.CustomerFormFragment;
import es.ulpgc.eii.android.project3.practica3_marlonfernandez.fragment.OrderFormFragment;
import es.ulpgc.eii.android.project3.practica3_marlonfernandez.fragment.ProductFormFragment;
import es.ulpgc.eii.android.project3.practica3_marlonfernandez.model.Customer;
import es.ulpgc.eii.android.project3.practica3_marlonfernandez.model.Order;
import es.ulpgc.eii.android.project3.practica3_marlonfernandez.model.Product;

public class FormActivity extends AppCompatActivity {

    public final static String CUSTOMER = Customer.class.getName();
    public final static String PRODUCT = Product.class.getName();
    public final static String ORDER = Order.class.getName();

    public final static String ID_CUSTOMER = "ID_CUSTOMER";
    public final static String ID_PRODUCT = "ID_PRODUCT";

    private int tabPosition;
    private Customer customer;
    private Product product;
    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        tabPosition = intent.getIntExtra(MainActivity.TAB_POSITION, 0);
        String title = intent.getStringExtra(MainActivity.TAB_NAME);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
            getSupportActionBar().setLogo(R.mipmap.ic_ulpgc);
            toolbar.setNavigationIcon(R.drawable.ic_action_back_dark);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

        if (savedInstanceState == null) {
            toolbar.setVisibility(View.VISIBLE);
            switch (tabPosition) {
                case 0:
                    customer = intent.getParcelableExtra(MainActivity.CUSTOMER);
                    showCustomerForm();
                    break;
                case 1:
                    product = intent.getParcelableExtra(MainActivity.PRODUCT);
                    showProductForm();
                    break;
                case 2:
                    order = intent.getParcelableExtra(MainActivity.ORDER);
                    if (order != null) {
                        customer = order.getCustomer();
                        product = order.getProduct();
                    }
                    showOrderForm();
                    break;
            }
        }
    }

    public void openListView() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.TAB_POSITION, tabPosition);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void showCustomerForm() {
        int content = R.id.content;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle arguments = new Bundle();
        arguments.putParcelable(CUSTOMER, customer);
        CustomerFormFragment customerForm = CustomerFormFragment.newInstance(arguments);
        transaction.replace(content, customerForm, CustomerFormFragment.TAG).commit();
    }

    public void showProductForm() {
        int content = R.id.content;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle arguments = new Bundle();
        arguments.putParcelable(PRODUCT, product);
        ProductFormFragment productForm = ProductFormFragment.newInstance(arguments);
        transaction.replace(content, productForm, ProductFormFragment.TAG).commit();
    }

    public void showOrderForm() {
        int content = R.id.content;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle arguments = new Bundle();
        arguments.putParcelable(CUSTOMER, customer);
        arguments.putParcelable(PRODUCT, product);
        arguments.putParcelable(ORDER, order);
        OrderFormFragment orderForm = OrderFormFragment.newInstance(arguments);
        transaction.replace(content, orderForm, OrderFormFragment.TAG).commit();
    }
}

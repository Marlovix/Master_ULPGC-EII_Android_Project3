package es.ulpgc.eii.android.project3.practica3_marlonfernandez;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import es.ulpgc.eii.android.project3.practica3_marlonfernandez.fragment.CustomerListFragment;
import es.ulpgc.eii.android.project3.practica3_marlonfernandez.fragment.OrderListFragment;
import es.ulpgc.eii.android.project3.practica3_marlonfernandez.fragment.ProductListFragment;
import es.ulpgc.eii.android.project3.practica3_marlonfernandez.model.Customer;
import es.ulpgc.eii.android.project3.practica3_marlonfernandez.model.Order;
import es.ulpgc.eii.android.project3.practica3_marlonfernandez.model.Product;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    final static String TAB_POSITION = TabLayout.class.getName();
    final static String TAB_NAME = String.class.getName();
    final static String CUSTOMER = Customer.class.getName();
    final static String PRODUCT = Product.class.getName();
    final static String ORDER = Order.class.getName();

    private Customer customer;
    private Product product;
    private SectionsPagerAdapter adapter;
    private ViewPager pager;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private int tabPosition;
    private boolean selectViewShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //deleteDatabase(AppDatabase.DATABASE_NAME);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        selectViewShown = false;

        Intent intent = getIntent();
        tabPosition = intent.getIntExtra(MainActivity.TAB_POSITION, -1);

        int idCustomer = intent.getIntExtra(FormActivity.ID_CUSTOMER, -1);
        int idProduct = intent.getIntExtra(FormActivity.ID_PRODUCT, -1);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        adapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        pager = (ViewPager) findViewById(R.id.container);
        pager.setAdapter(adapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);
        if (tabPosition == -1) { // If no sent tabPosition from FormActivity //
            if (idCustomer != -1) {
                tabPosition = 0;
                selectViewShown = true;
                toolbar.setVisibility(View.GONE);
                tabLayout.setVisibility(View.GONE);
                pager.setCurrentItem(tabPosition);
            } else if (idProduct != -1) {
                tabPosition = 1;
                selectViewShown = true;
                toolbar.setVisibility(View.GONE);
                tabLayout.setVisibility(View.GONE);
                pager.setCurrentItem(tabPosition);
            } else {
                tabPosition = tabLayout.getSelectedTabPosition();
            }
        } else {
            pager.setCurrentItem(tabPosition);
        }

        if (getSupportActionBar() != null) {
            CharSequence tabName = adapter.getPageTitle(tabPosition);
            getSupportActionBar().setTitle(tabName);
            getSupportActionBar().setLogo(R.mipmap.ic_ulpgc);
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab != null) {
                    CharSequence nameTab = tab.getText();
                    toolbar.setTitle(nameTab);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        MenuItem navCustomers = menu.findItem(R.id.nav_customers);
        MenuItem navProducts = menu.findItem(R.id.nav_products);
        MenuItem navOrders = menu.findItem(R.id.nav_orders);

        String itemCustomers = navCustomers.getTitle().toString();
        navCustomers.setTitle(itemCustomers.substring(0, 1).toUpperCase() + itemCustomers.substring(1));
        String itemProducts = navProducts.getTitle().toString();
        navProducts.setTitle(itemProducts.substring(0, 1).toUpperCase() + itemProducts.substring(1));
        String itemOrders = navOrders.getTitle().toString();
        navOrders.setTitle(itemOrders.substring(0, 1).toUpperCase() + itemOrders.substring(1));

        navigationView.setNavigationItemSelectedListener(this);
    }

    public Customer getCustomer() {
        return customer;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
        Intent returnIntent = new Intent();
        returnIntent.putExtra(FormActivity.PRODUCT, product);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        Intent returnIntent = new Intent();
        returnIntent.putExtra(FormActivity.CUSTOMER, customer);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    public boolean isSelectViewShown() {
        return selectViewShown;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_new:
                openCustomerForm(null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openCustomerForm(Customer customer) {
        int tabPosition = tabLayout.getSelectedTabPosition();
        CharSequence tabName = adapter.getPageTitle(tabPosition);
        Intent intent = new Intent(this, FormActivity.class);
        intent.putExtra(TAB_POSITION, tabPosition);
        intent.putExtra(TAB_NAME, tabName);
        intent.putExtra(CUSTOMER, customer);
        startActivity(intent);
    }

    public void openProductForm(Product product) {
        int tabPosition = tabLayout.getSelectedTabPosition();
        CharSequence tabName = adapter.getPageTitle(tabPosition);
        Intent intent = new Intent(this, FormActivity.class);
        intent.putExtra(TAB_POSITION, tabPosition);
        intent.putExtra(TAB_NAME, tabName);
        intent.putExtra(PRODUCT, product);
        startActivity(intent);
    }

    public void openOrderForm(Order order) {
        int tabPosition = tabLayout.getSelectedTabPosition();
        CharSequence tabName = adapter.getPageTitle(tabPosition);
        Intent intent = new Intent(this, FormActivity.class);
        intent.putExtra(TAB_POSITION, tabPosition);
        intent.putExtra(TAB_NAME, tabName);
        intent.putExtra(ORDER, order);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_customers:
                tabPosition = 0;
                break;
            case R.id.nav_products:
                tabPosition = 1;
                break;
            case R.id.nav_orders:
                tabPosition = 2;
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        pager.setCurrentItem(tabPosition);
        return true;
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new CustomerListFragment();
                case 1:
                    return new ProductListFragment();
                case 2:
                    return new OrderListFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String tab = getTabResource(position);
            return tab.substring(0, 1).toUpperCase() + tab.substring(1);
        }

        String getTabResource(int tabPosition) {
            String tab;
            switch (tabPosition) {
                case 0:
                    tab = getString(R.string.customers);
                    break;
                case 1:
                    tab = getString(R.string.products);
                    break;
                case 2:
                    tab = getString(R.string.orders);
                    break;
                default:
                    tab = "null";
                    break;
            }
            return tab;
        }
    }

}

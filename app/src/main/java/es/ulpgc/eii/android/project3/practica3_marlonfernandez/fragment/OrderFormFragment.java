package es.ulpgc.eii.android.project3.practica3_marlonfernandez.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import es.ulpgc.eii.android.project3.practica3_marlonfernandez.FormActivity;
import es.ulpgc.eii.android.project3.practica3_marlonfernandez.MainActivity;
import es.ulpgc.eii.android.project3.practica3_marlonfernandez.R;
import es.ulpgc.eii.android.project3.practica3_marlonfernandez.db.AppDatabase;
import es.ulpgc.eii.android.project3.practica3_marlonfernandez.db.AppHelperDB;
import es.ulpgc.eii.android.project3.practica3_marlonfernandez.model.Customer;
import es.ulpgc.eii.android.project3.practica3_marlonfernandez.model.Order;
import es.ulpgc.eii.android.project3.practica3_marlonfernandez.model.Product;

public class OrderFormFragment extends Fragment {
    public static final String TAG = ProductFormFragment.class.getName();
    private static final String ORDER_INSTANCE = Order.class.getName();
    private static final String ORDERS_ALERT_DIALOG = AlertDialog.class.getSimpleName();
    private static final String ORDERS_CODE = "CODE";
    private static final String ORDERS_DAY = "DAY";
    private static final String ORDERS_MONTH = "MONTH";
    private static final String ORDERS_YEAR = "YEAR";
    private static final String ORDERS_CUSTOMER = "CUSTOMER";
    private static final String ORDERS_PRODUCT = "PRODUCT";
    private static final String ORDERS_QUANTITY = "QUANTITY";
    private static final String ORDERS_PRICE = "PRICE";
    public final static int RESULT_CUSTOMER = 0;
    public final static int RESULT_PRODUCT = 1;

    private EditText editTextCode;
    private DatePicker datePicker;
    private EditText editTextCustomer;
    private EditText editTextProduct;
    private EditText editTextQuantity;
    private EditText editTextPrice;
    private String code;
    private int day;
    private int month;
    private int year;
    private Customer customer;
    private Product product;
    private int quantity;
    private float price;
    private Order order;
    private AppHelperDB orderHelper;
    private AppDatabase helper;
    private AlertDialog dialog;
    private String dialogType;

    public OrderFormFragment() {
        // Required empty public constructor
    }

    public static OrderFormFragment newInstance(Bundle arguments) {
        OrderFormFragment fragment = new OrderFormFragment();
        if (arguments != null) {
            fragment.setArguments(arguments);
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            order = getArguments().getParcelable(ORDER_INSTANCE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_form, container, false);
        setHasOptionsMenu(true);

        TextView textViewCode = (TextView) view.findViewById(R.id.codeLabel);
        TextView textViewCustomer = (TextView) view.findViewById(R.id.customerLabel);
        TextView textViewProduct = (TextView) view.findViewById(R.id.productLabel);
        TextView textViewQuantity = (TextView) view.findViewById(R.id.quantityLabel);
        TextView textViewPrice = (TextView) view.findViewById(R.id.priceLabel);

        String labelCode = textViewCode.getText().toString();
        labelCode = labelCode.substring(0, 1).toUpperCase() + labelCode.substring(1);
        String labelCustomer = textViewCustomer.getText().toString();
        labelCustomer = labelCustomer.substring(0, 1).toUpperCase() + labelCustomer.substring(1);
        String labelProduct = textViewProduct.getText().toString();
        labelProduct = labelProduct.substring(0, 1).toUpperCase() + labelProduct.substring(1);
        String labelQuantity = textViewQuantity.getText().toString();
        labelQuantity = labelQuantity.substring(0, 1).toUpperCase() + labelQuantity.substring(1);
        String labelPrice = textViewPrice.getText().toString();
        labelPrice = labelPrice.substring(0, 1).toUpperCase() + labelPrice.substring(1);

        textViewCode.setText(labelCode);
        textViewCustomer.setText(labelCustomer);
        textViewProduct.setText(labelProduct);
        textViewQuantity.setText(labelQuantity);
        textViewPrice.setText(labelPrice);

        dialogType = "";
        editTextCode = (EditText) view.findViewById(R.id.code);
        datePicker = (DatePicker) view.findViewById(R.id.datePicker);
        editTextCustomer = (EditText) view.findViewById(R.id.customer);
        editTextProduct = (EditText) view.findViewById(R.id.product);
        editTextQuantity = (EditText) view.findViewById(R.id.quantity);
        editTextPrice = (EditText) view.findViewById(R.id.price);

        editTextCustomer.setKeyListener(null);
        editTextProduct.setKeyListener(null);
        editTextQuantity.setKeyListener(null);
        editTextPrice.setKeyListener(null);

        if (order != null) {
            editTextCode.setText(order.getCode());
            int day = order.getDay();
            int month = order.getMonth();
            int year = order.getYear();
            datePicker.updateDate(year, month, day);
            customer = order.getCustomer();
            product = order.getProduct();
            editTextCustomer.setText(customer.getName());
            editTextProduct.setText(product.getName());
            editTextQuantity.setText(String.valueOf(order.getQuantity()));
            editTextPrice.setText(String.valueOf(order.getPrice()));
        }

        Button buttonCustomer = (Button) view.findViewById(R.id.buttonDisplayCustomers);
        Button buttonProduct = (Button) view.findViewById(R.id.buttonDisplayProducts);
        Button buttonMinus = (Button) view.findViewById(R.id.buttonMinus);
        Button buttonPlus = (Button) view.findViewById(R.id.buttonPlus);

        buttonCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper = new AppDatabase(getContext());
                orderHelper = new AppHelperDB(helper);
                orderHelper.open();
                int numberOfOrders = orderHelper.getCustomersCount();
                if (numberOfOrders > 0) {
                    displayCustomers();
                } else {
                    Toast.makeText(getContext(),
                            getString(R.string.no_customers), Toast.LENGTH_SHORT).show();
                }
                orderHelper.close();
            }
        });

        buttonProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper = new AppDatabase(getContext());
                orderHelper = new AppHelperDB(helper);
                orderHelper.open();
                int numberOfOrders = orderHelper.getProductsCount();
                if (numberOfOrders > 0) {
                    displayProducts();
                } else {
                    Toast.makeText(getContext(),
                            getString(R.string.no_products), Toast.LENGTH_SHORT).show();
                }
                orderHelper.close();
            }
        });

        buttonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minusQuality();
            }
        });

        buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plusQuality();
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            dialogType = savedInstanceState.getString(ORDERS_ALERT_DIALOG);
            code = savedInstanceState.getString(ORDERS_CODE);
            day = savedInstanceState.getInt(ORDERS_DAY);
            month = savedInstanceState.getInt(ORDERS_MONTH);
            year = savedInstanceState.getInt(ORDERS_YEAR);
            customer = savedInstanceState.getParcelable(ORDERS_CUSTOMER);
            product = savedInstanceState.getParcelable(ORDERS_PRODUCT);
            quantity = savedInstanceState.getInt(ORDERS_QUANTITY);
            price = savedInstanceState.getFloat(ORDERS_PRICE);
            if (dialogType != null && !dialogType.equals("")) {
                switch (dialogType) {
                    case "SUBMIT":
                        submit();
                        break;
                    case "DELETE":
                        delete();
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (dialog != null) dialog.dismiss();
        if (editTextQuantity.getText().toString().equals("")) {
            quantity = 0;
        } else {
            quantity = Integer.valueOf(editTextQuantity.getText().toString());
        }
        if (editTextPrice.getText().toString().equals("")) {
            price = -1f;
        } else {
            price = Float.valueOf(editTextPrice.getText().toString());
        }
        outState.putString(ORDERS_ALERT_DIALOG, dialogType);
        outState.putString(ORDERS_CODE, editTextCode.getText().toString());
        outState.putInt(ORDERS_DAY, datePicker.getDayOfMonth());
        outState.putInt(ORDERS_MONTH, datePicker.getMonth());
        outState.putInt(ORDERS_YEAR, datePicker.getYear());
        outState.putParcelable(ORDERS_CUSTOMER, customer);
        outState.putParcelable(ORDERS_PRODUCT, product);
        outState.putInt(ORDERS_QUANTITY, quantity);
        outState.putFloat(ORDERS_PRICE, price);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_accept, menu);
        if (order != null) {
            inflater.inflate(R.menu.menu_discard, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_accept:
                code = editTextCode.getText().toString();
                day = datePicker.getDayOfMonth();
                month = datePicker.getMonth();
                year = datePicker.getYear();
                if (editTextQuantity.getText().toString().equals("")) {
                    quantity = -1;
                } else {
                    quantity = Integer.valueOf(editTextQuantity.getText().toString());
                }
                if (editTextPrice.getText().toString().equals("")) {
                    price = -1;
                } else {
                    price = Float.valueOf(editTextPrice.getText().toString());
                }
                if (editTextPrice.getText().toString().equals("")) {
                    price = -1;
                } else {
                    price = Float.valueOf(editTextPrice.getText().toString());
                }
                submit();
                return true;
            case R.id.action_discard:
                delete();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void submit() {
        boolean validForm = true;

        if (code.equals("")) {
            editTextCode.setError(getString(R.string.error_empty));
            validForm = false;
        }

        if (customer == null) {
            editTextCustomer.setError(getString(R.string.error_empty));
            validForm = false;
        }

        if (product == null) {
            editTextProduct.setError(getString(R.string.error_empty));
            validForm = false;
        }

        if (quantity == -1) {
            editTextQuantity.setError(getString(R.string.error_empty));
            validForm = false;
        }

        if (quantity == 0) {
            editTextQuantity.setError(getString(R.string.error_quantity));
            validForm = false;
        }

        if (price == -1) {
            editTextPrice.setError(getString(R.string.error_empty));
            validForm = false;
        }

        if (validForm) {
            Context context = getContext();
            helper = new AppDatabase(context);
            orderHelper = new AppHelperDB(helper);

            String title = getString(R.string.orders);
            title = title.substring(0, 1).toUpperCase() + title.substring(1);
            String positive = getString(android.R.string.yes);
            String negative = getString(android.R.string.cancel);
            String message;
            if (order == null) {
                message = getString(R.string.create_order_question);
            } else {
                int idOrder = order.getIdOrder();
                message = String.format(
                        getString(R.string.update_order_question), idOrder);
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            showSubmitDialog(builder, title, positive, negative, message);
        }
    }

    public void delete() {
        int idOrder = order.getIdOrder();
        Context context = getContext();
        helper = new AppDatabase(context);
        orderHelper = new AppHelperDB(helper);

        String title = getString(R.string.orders);
        title = title.substring(0, 1).toUpperCase() + title.substring(1);
        String positive = getString(android.R.string.yes);
        String negative = getString(android.R.string.cancel);
        String message = String.format(getString(R.string.delete_order_question), idOrder);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        showDeleteDialog(builder, idOrder, title, message, positive, negative);
    }

    private void showSubmitDialog(AlertDialog.Builder builder,
                                  final String title, String positive, String negative, String message) {
        dialogType = "SUBMIT";
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                String result;
                int idOrder;

                orderHelper.open();
                if (order == null) {
                    order = new Order(code, day, month, year, customer, product, quantity);
                    orderHelper.createOrder(order);
                    result = getString(R.string.success_order_created);
                } else {
                    order.setCode(code);
                    order.setDay(day);
                    order.setMonth(month);
                    order.setYear(year);
                    order.setCustomer(customer);
                    order.setProduct(product);
                    order.setQuantity(quantity);
                    orderHelper.updateOrder(order);
                    idOrder = order.getIdOrder();
                    result = String.format(
                            getString(R.string.success_order_updated), idOrder);
                }
                orderHelper.close();

                Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
                ((FormActivity) getActivity()).openListView();
            }
        });
        builder.setNegativeButton(negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) dialog.dismiss();
                dialogType = "";
            }
        });
        builder.setCancelable(false); // Avoid close the alert with back button of the device //
        dialog = builder.show();
    }

    private void showDeleteDialog(AlertDialog.Builder builder, int id,
                                  String title, String message, String positive, String negative) {
        dialogType = "DELETE";
        final int idOrder = id;
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                orderHelper.open();
                orderHelper.deleteOrder(idOrder);
                orderHelper.close();

                String result = String.format(getString(R.string.success_order_deleted), idOrder);

                Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
                ((FormActivity) getActivity()).openListView();
            }
        });
        builder.setNegativeButton(negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) dialog.dismiss();
                dialogType = "";
            }
        });
        builder.setCancelable(false); // Avoid close the alert with back button of the device //
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_CUSTOMER && resultCode == Activity.RESULT_OK) {
            customer = data.getParcelableExtra(FormActivity.CUSTOMER);
            if (customer != null) {
                editTextCustomer.setError(null);
                editTextCustomer.setText(customer.getName());
            }

        } else if (requestCode == RESULT_PRODUCT && resultCode == Activity.RESULT_OK) {
            product = data.getParcelableExtra(FormActivity.PRODUCT);
            if (product != null) {
                editTextProduct.setError(null);
                editTextProduct.setText(product.getName());
                updatePrice();
            }
        }
    }

    private void updatePrice() {
        editTextQuantity.setError(null);
        editTextPrice.setError(null);
        if (editTextQuantity.getText().toString().equals("")) {
            editTextPrice.setText("0");
        } else {
            if (product != null) {
                price = product.getPrice();
            } else {
                price = 0;
            }
            float result = price * quantity;
            editTextPrice.setText(String.valueOf(result));
        }
    }

    public void displayCustomers() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.putExtra(ORDER_INSTANCE, order);
        if (customer != null) {
            intent.putExtra(FormActivity.ID_CUSTOMER, customer.getIdCustomer());
        } else {
            intent.putExtra(FormActivity.ID_CUSTOMER, 0);
        }
        startActivityForResult(intent, RESULT_CUSTOMER);
    }


    public void displayProducts() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.putExtra(ORDER_INSTANCE, order);
        if (product != null) {
            intent.putExtra(FormActivity.ID_PRODUCT, product.getIdProduct());
        } else {
            intent.putExtra(FormActivity.ID_PRODUCT, 0);
        }
        startActivityForResult(intent, RESULT_PRODUCT);
    }

    public void minusQuality() {
        String strQuantity = editTextQuantity.getText().toString();
        if (strQuantity.equals("") || strQuantity.equals("0")) {
            editTextQuantity.setText("0");
        } else {
            quantity = Integer.valueOf(strQuantity);
            quantity--;
            editTextQuantity.setText(String.valueOf(quantity));
            updatePrice();
        }
    }

    public void plusQuality() {
        String strQuantity = editTextQuantity.getText().toString();
        if (strQuantity.equals("")) {
            quantity = 0;
        } else {
            quantity = Integer.valueOf(strQuantity);
        }
        quantity++;
        editTextQuantity.setText(String.valueOf(quantity));
        updatePrice();
    }

}
package es.ulpgc.eii.android.project3.practica3_marlonfernandez.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import es.ulpgc.eii.android.project3.practica3_marlonfernandez.model.Customer;
import es.ulpgc.eii.android.project3.practica3_marlonfernandez.model.Order;
import es.ulpgc.eii.android.project3.practica3_marlonfernandez.model.Product;

public class AppHelperDB {

    private AppDatabase helper;
    private SQLiteDatabase database;

    private String[] columnsCustomer = new String[]{
            AppDatabase.KEY_ID_CUSTOMER,
            AppDatabase.KEY_NAME_CUSTOMER,
            AppDatabase.KEY_ADDRESS_CUSTOMER
    };

    private String[] columnsProduct = new String[]{
            AppDatabase.KEY_ID_PRODUCT,
            AppDatabase.KEY_NAME_PRODUCT,
            AppDatabase.KEY_DESCRIPTION_PRODUCT,
            AppDatabase.KEY_PRICE_PRODUCT
    };

    private String[] columnsOrder = new String[]{
            AppDatabase.KEY_ID_ORDER,
            AppDatabase.KEY_ID_CUSTOMER_ORDER,
            AppDatabase.KEY_ID_PRODUCT_ORDER,
            AppDatabase.KEY_CODE_ORDER,
            AppDatabase.KEY_DATE_ORDER,
            AppDatabase.KEY_QUANTITY_ORDER
    };

    public AppHelperDB(AppDatabase helper) {
        this.helper = helper;
    }

    public AppHelperDB open() throws SQLException {
        database = helper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (helper != null) {
            helper.close();
        }
    }

    public long createCustomer(Customer customer) {
        ContentValues contentDB = customerMapper(customer);
        return database.insert(AppDatabase.KEY_CUSTOMER_TABLE, null, contentDB);
    }

    public long createProduct(Product product) {
        ContentValues contentDB = productMapper(product);
        return database.insert(AppDatabase.KEY_PRODUCT_TABLE, null, contentDB);
    }

    public long createOrder(Order order) {
        ContentValues contentDB = orderMapper(order);
        return database.insert(AppDatabase.KEY_ORDER_TABLE, null, contentDB);
    }

    public long deleteCustomer(int idCustomer) {
        String where = AppDatabase.KEY_ID_CUSTOMER + "=" + idCustomer;
        return database.delete(AppDatabase.KEY_CUSTOMER_TABLE, where, null);
    }

    public long deleteProduct(int idProduct) {
        String where = AppDatabase.KEY_ID_PRODUCT + "=" + idProduct;
        return database.delete(AppDatabase.KEY_PRODUCT_TABLE, where, null);
    }

    public long deleteOrder(int idOrder) {
        String where = AppDatabase.KEY_ID_ORDER + "=" + idOrder;
        return database.delete(AppDatabase.KEY_ORDER_TABLE, where, null);
    }

    public Cursor fetchCustomerByID(int idCustomer) throws SQLException {
        Cursor cursor;
        if (idCustomer == 0)
            cursor = database.query(AppDatabase.KEY_PRODUCT_TABLE, columnsProduct,
                    null, null, null, null, null);
        else
            cursor = database.query(true, AppDatabase.KEY_PRODUCT_TABLE, columnsProduct,
                    AppDatabase.KEY_ID_CUSTOMER + " = " + idCustomer, null, null, null, null, null);

        if (cursor != null) cursor.moveToFirst();

        return cursor;
    }

    public Cursor fetchProductByID(int idProduct) throws SQLException {
        Cursor cursor;
        if (idProduct == 0)
            cursor = database.query(AppDatabase.KEY_PRODUCT_TABLE, columnsProduct,
                    null, null, null, null, null);
        else
            cursor = database.query(true, AppDatabase.KEY_PRODUCT_TABLE, columnsProduct,
                    AppDatabase.KEY_ID_PRODUCT + " = " + idProduct, null, null, null, null, null);

        if (cursor != null) cursor.moveToFirst();

        return cursor;
    }

    public Cursor fetchOrderByID(int idOrder) throws SQLException {
        Cursor cursor;
        if (idOrder == 0)
            cursor = database.query(AppDatabase.KEY_ORDER_TABLE, columnsOrder,
                    null, null, null, null, null);
        else
            cursor = database.query(true, AppDatabase.KEY_ORDER_TABLE, columnsOrder,
                    AppDatabase.KEY_ID_ORDER + " = " + idOrder, null, null, null, null, null);

        if (cursor != null) cursor.moveToFirst();

        return cursor;
    }

    public Cursor fetchAllCustomers() {
        String orderBy = AppDatabase.KEY_NAME_PRODUCT + " ASC";
        Cursor cursor = database.query(AppDatabase.KEY_CUSTOMER_TABLE, columnsCustomer,
                null, null, null, null, orderBy);
        if (cursor != null) cursor.moveToFirst();

        return cursor;
    }

    public Cursor fetchAllProducts() {
        String orderBy = AppDatabase.KEY_NAME_PRODUCT + " ASC";
        Cursor cursor = database.query(AppDatabase.KEY_PRODUCT_TABLE, columnsProduct,
                null, null, null, null, orderBy);
        if (cursor != null) cursor.moveToFirst();

        return cursor;
    }

    public Cursor fetchAllOrders() {
        String selectColumns = "o." + AppDatabase.KEY_ID_ORDER + ","
                + AppDatabase.KEY_CODE_ORDER + ","
                + AppDatabase.KEY_DATE_ORDER + ","
                + AppDatabase.KEY_ID_CUSTOMER_ORDER + ","
                + "c." + AppDatabase.KEY_NAME_CUSTOMER + " AS customer_name,"
                + AppDatabase.KEY_ADDRESS_CUSTOMER + ","
                + AppDatabase.KEY_ID_PRODUCT_ORDER + ","
                + "p." + AppDatabase.KEY_NAME_PRODUCT + " AS product_name,"
                + AppDatabase.KEY_DESCRIPTION_PRODUCT + ","
                + AppDatabase.KEY_PRICE_PRODUCT + ","
                + AppDatabase.KEY_QUANTITY_ORDER;
        String selectSQL = "SELECT " + selectColumns + " FROM " + AppDatabase.KEY_ORDER_TABLE + " AS o"
                + " JOIN " + AppDatabase.KEY_CUSTOMER_TABLE + " AS c"
                + " ON o." + AppDatabase.KEY_ID_CUSTOMER_ORDER + "=" + "c." + AppDatabase.KEY_ID_CUSTOMER
                + " JOIN " + AppDatabase.KEY_PRODUCT_TABLE + " AS p"
                + " ON o." + AppDatabase.KEY_ID_PRODUCT_ORDER + "=" + "p." + AppDatabase.KEY_ID_PRODUCT
                + " ORDER BY"
                + " c." + AppDatabase.KEY_NAME_CUSTOMER + ","
                + " p." + AppDatabase.KEY_NAME_PRODUCT + ","
                + " o." + AppDatabase.KEY_CODE_ORDER + " ASC";
        Cursor cursor = database.rawQuery(selectSQL, null);
        if (cursor != null) cursor.moveToFirst();

        return cursor;
    }

    public int getCustomersCount() {
        String countQuery = "SELECT * FROM " + AppDatabase.KEY_CUSTOMER_TABLE;
        Cursor cursor = database.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public int getProductsCount() {
        String countQuery = "SELECT * FROM " + AppDatabase.KEY_PRODUCT_TABLE;
        Cursor cursor = database.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public boolean customerInOrder(int idCustomer) {
        String sql = "SELECT COUNT(*) FROM " + AppDatabase.KEY_ORDER_TABLE
                + " WHERE " + AppDatabase.KEY_ID_CUSTOMER_ORDER + "=" + idCustomer;
        Cursor cursor = database.rawQuery(sql, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count != 0;
    }

    public boolean productInOrder(int idProduct) {
        String countQuery = "SELECT * FROM "
                + AppDatabase.KEY_ORDER_TABLE +
                " WHERE " + AppDatabase.KEY_ID_PRODUCT_ORDER + "=" + idProduct;
        Cursor cursor = database.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt != 0;
    }

    private ContentValues customerMapper(Customer customer) {
        String name = customer.getName();
        String address = customer.getAddress();
        ContentValues initialValues = new ContentValues();
        initialValues.put(AppDatabase.KEY_NAME_CUSTOMER, name);
        initialValues.put(AppDatabase.KEY_ADDRESS_CUSTOMER, address);
        return initialValues;
    }

    private ContentValues productMapper(Product product) {
        String name = product.getName();
        String description = product.getDescription();
        float price = product.getPrice();
        ContentValues initialValues = new ContentValues();
        initialValues.put(AppDatabase.KEY_NAME_PRODUCT, name);
        initialValues.put(AppDatabase.KEY_DESCRIPTION_PRODUCT, description);
        initialValues.put(AppDatabase.KEY_PRICE_PRODUCT, price);
        return initialValues;
    }

    private ContentValues orderMapper(Order order) {
        String code = order.getCode();
        int day = order.getDay();
        int month = order.getMonth() + 1;
        int year = order.getYear();
        String date = mapperDateDB(day, month, year);
        Customer customer = order.getCustomer();
        Product product = order.getProduct();
        int quantity = order.getQuantity();
        ContentValues initialValues = new ContentValues();
        initialValues.put(AppDatabase.KEY_CODE_ORDER, code);
        initialValues.put(AppDatabase.KEY_DATE_ORDER, date);
        initialValues.put(AppDatabase.KEY_ID_CUSTOMER_ORDER, customer.getIdCustomer());
        initialValues.put(AppDatabase.KEY_ID_PRODUCT_ORDER, product.getIdProduct());
        initialValues.put(AppDatabase.KEY_QUANTITY_ORDER, quantity);
        return initialValues;
    }

    private String mapperDateDB(int day, int month, int year) {
        String dayDB = String.valueOf(day);
        String monthDB = String.valueOf(month);
        String yearDB = String.valueOf(year);

        int dayLength = String.valueOf(day).length();
        int monthLength = String.valueOf(month).length();
        if (dayLength == 1) {
            dayDB = "0" + dayDB;
        }
        if (monthLength == 1) {
            monthDB = "0" + monthDB;
        }
        return yearDB + "-" + monthDB + "-" + dayDB;
    }

    public long updateCustomer(Customer customer) {
        int id = customer.getIdCustomer();
        ContentValues contentDB = customerMapper(customer);
        return database.update(AppDatabase.KEY_CUSTOMER_TABLE, contentDB, "_id=" + id, null);
    }

    public long updateProduct(Product product) {
        int id = product.getIdProduct();
        ContentValues contentDB = productMapper(product);
        return database.update(AppDatabase.KEY_PRODUCT_TABLE, contentDB, "_id=" + id, null);
    }

    public long updateOrder(Order order) {
        int id = order.getIdOrder();
        ContentValues contentDB = orderMapper(order);
        return database.update(AppDatabase.KEY_ORDER_TABLE, contentDB, "_id=" + id, null);
    }

}
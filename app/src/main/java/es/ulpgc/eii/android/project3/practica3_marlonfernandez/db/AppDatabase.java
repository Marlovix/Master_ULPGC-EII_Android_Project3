package es.ulpgc.eii.android.project3.practica3_marlonfernandez.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "android_ulpgc";

    public static final String KEY_ID_CUSTOMER = "_id";
    public static final String KEY_ADDRESS_CUSTOMER = "address";
    public static final String KEY_NAME_CUSTOMER = "name";

    public static final String KEY_ID_PRODUCT = "_id";
    public static final String KEY_DESCRIPTION_PRODUCT = "description";
    public static final String KEY_NAME_PRODUCT = "name";
    public static final String KEY_PRICE_PRODUCT = "price";

    public static final String KEY_ID_ORDER = "_id";
    public static final String KEY_CODE_ORDER = "code";
    public static final String KEY_DATE_ORDER = "date";
    public static final String KEY_ID_CUSTOMER_ORDER = "id_customer";
    public static final String KEY_ID_PRODUCT_ORDER = "id_product";
    public static final String KEY_QUANTITY_ORDER = "quantity";

    static final String KEY_CUSTOMER_TABLE = "customers";
    static final String KEY_PRODUCT_TABLE = "products";
    static final String KEY_ORDER_TABLE = "orders";

    private static final String CREATE_CUSTOMER_TABLE = "CREATE TABLE " + KEY_CUSTOMER_TABLE + " ("
            + KEY_ID_CUSTOMER + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_ADDRESS_CUSTOMER + " VARCHAR(100) NOT NULL, "
            + KEY_NAME_CUSTOMER + " VARCHAR(30) NOT NULL);";

    private static final String CREATE_PRODUCT_TABLE = "CREATE TABLE " + KEY_PRODUCT_TABLE + " ("
            + KEY_ID_PRODUCT + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_DESCRIPTION_PRODUCT + " VARCHAR(255) NOT NULL, "
            + KEY_NAME_PRODUCT + " VARCHAR(30) NOT NULL, "
            + KEY_PRICE_PRODUCT + " DECIMAL(8,2) NOT NULL);";

    private static final String CREATE_ORDER_TABLE = "CREATE TABLE " + KEY_ORDER_TABLE + " ("
            + KEY_ID_ORDER + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_CODE_ORDER + " VARCHAR(10) NOT NULL, "
            + KEY_DATE_ORDER + " DATE NOT NULL, "
            + KEY_ID_CUSTOMER_ORDER + " INTEGER NOT NULL, "
            + KEY_ID_PRODUCT_ORDER + " INTEGER NOT NULL, "
            + KEY_QUANTITY_ORDER + " INTEGER NOT NULL, "
            + "FOREIGN KEY(" + KEY_ID_CUSTOMER + ") REFERENCES " + KEY_CUSTOMER_TABLE + " (_id), "
            + "FOREIGN KEY(" + KEY_ID_PRODUCT + ") REFERENCES " + KEY_PRODUCT_TABLE + " (_id));";

    public AppDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CUSTOMER_TABLE);
        db.execSQL(CREATE_PRODUCT_TABLE);
        db.execSQL(CREATE_ORDER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + KEY_CUSTOMER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + KEY_PRODUCT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + KEY_ORDER_TABLE);
        onCreate(db);
    }
}

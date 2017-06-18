package es.ulpgc.eii.android.project3.practica3_marlonfernandez.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Order implements Parcelable {
    private int idOrder;
    private String code;
    private int day;
    private int month;
    private int year;
    private Customer customer;
    private Product product;
    private int quantity;

    public Order(int idOrder, String code, int day, int month, int year,
                 Customer customer, Product product, int quantity) {
        this.idOrder = idOrder;
        this.code = code;
        this.day = day;
        this.month = month;
        this.year = year;
        this.customer = customer;
        this.product = product;
        this.quantity = quantity;
    }

    public Order(String code, int day, int month, int year,
                 Customer customer, Product product, int quantity) {
        this.idOrder = 0;
        this.code = code;
        this.day = day;
        this.month = month;
        this.year = year;
        this.customer = customer;
        this.product = product;
        this.quantity = quantity;
    }

    public int getIdOrder() {
        return idOrder;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        if (product == null) {
            return -1;
        } else {
            float price = product.getPrice();
            return price * quantity;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.idOrder);
        dest.writeString(this.code);
        dest.writeInt(this.day);
        dest.writeInt(this.month);
        dest.writeInt(this.year);
        dest.writeParcelable(this.customer, flags);
        dest.writeParcelable(this.product, flags);
        dest.writeInt(this.quantity);
    }

    private Order(Parcel in) {
        this.idOrder = in.readInt();
        this.code = in.readString();
        this.day = in.readInt();
        this.month = in.readInt();
        this.year = in.readInt();
        this.customer = in.readParcelable(Customer.class.getClassLoader());
        this.product = in.readParcelable(Product.class.getClassLoader());
        this.quantity = in.readInt();
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel source) {
            return new Order(source);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };
}

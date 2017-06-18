package es.ulpgc.eii.android.project3.practica3_marlonfernandez.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Customer implements Parcelable {
    private int idCustomer;
    private String name;
    private String address;

    public Customer(int idCustomer, String name, String address) {
        this.idCustomer = idCustomer;
        this.name = name;
        this.address = address;
    }

    public Customer(String name, String address) {
        this.idCustomer = 0;
        this.name = name;
        this.address = address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.idCustomer);
        dest.writeString(this.name);
        dest.writeString(this.address);
    }

    protected Customer(Parcel in) {
        this.idCustomer = in.readInt();
        this.name = in.readString();
        this.address = in.readString();
    }

    public static final Creator<Customer> CREATOR = new Creator<Customer>() {
        @Override
        public Customer createFromParcel(Parcel source) {
            return new Customer(source);
        }

        @Override
        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };

    public int getIdCustomer() {
        return idCustomer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

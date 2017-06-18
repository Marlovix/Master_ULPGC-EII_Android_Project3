package es.ulpgc.eii.android.project3.practica3_marlonfernandez.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
    private int idProduct;
    private String name;
    private String description;
    private float price;


    public Product(int idProduct, String name, String description, float price) {
        this.idProduct = idProduct;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public Product(String name, String description, float price) {
        this.idProduct = 0;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public float getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.idProduct);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeFloat(this.price);
    }

    protected Product(Parcel in) {
        this.idProduct = in.readInt();
        this.name = in.readString();
        this.description = in.readString();
        this.price = in.readFloat();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}

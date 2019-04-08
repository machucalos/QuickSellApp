package com.machucalos.quicksell;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

public class CustomItem implements Parcelable {
    private String name;
    private String serial_id;

    public CustomItem(String name, String serial_id, String size, double amount) {
        this.name = name;
        this.serial_id = serial_id;
        this.size = size;
        this.amount = amount;
    }

    private String size;
    private double amount;


    protected CustomItem(Parcel in) {
        name = in.readString();
        serial_id = in.readString();
        size = in.readString();
        amount = in.readDouble();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSerial_id() {
        return serial_id;
    }

    public void setSerial_id(String serial_id) {
        this.serial_id = serial_id;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public static final Creator<CustomItem> CREATOR = new Creator<CustomItem>() {
        @Override
        public CustomItem createFromParcel(Parcel in) {
            return new CustomItem(in);
        }

        @Override
        public CustomItem[] newArray(int size) {
            return new CustomItem[size];
        }
    };



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(serial_id);
        dest.writeString(size);
        dest.writeDouble(amount);

    }
}

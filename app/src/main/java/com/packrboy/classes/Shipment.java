package com.packrboy.classes;

import android.os.Parcel;
import android.os.Parcelable;

import com.packrboy.logging.L;

/**
 * Created by arindam.paaltao on 29-Jul-15.
 */
public class Shipment implements Parcelable{
    int itemId;

    protected Shipment(Parcel in) {
        itemId = in.readInt();
        itemTypeId = in.readInt();
        itemType = in.readString();
        itemQuantity = in.readString();
        requestType = in.readString();
        deliveryType = in.readString();
        transitStatus = in.readString();
        postalCode = in.readString();
        streetNo = in.readString();
        route = in.readString();
        city = in.readString();
        state = in.readString();
        country = in.readString();
        itemDescription = in.readString();
        createdTime = in.readString();
        updatedTime = in.readString();
        imageURL = in.readString();
    }

    public static final Parcelable.Creator<Shipment> CREATOR = new Parcelable.Creator<Shipment>() {
        @Override
        public Shipment createFromParcel(Parcel in) {
            return new Shipment(in);
        }

        @Override
        public Shipment[] newArray(int size) {
            return new Shipment[size];
        }
    };

    public Shipment(){

    }


    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public int getItemTypeId() {
        return itemTypeId;
    }

    public void setItemTypeId(int itemTypeId) {
        this.itemTypeId = itemTypeId;
    }

    public String getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(String itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getStreetNo() {
        return streetNo;
    }

    public void setStreetNo(String streetNo) {
        this.streetNo = streetNo;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Float getItemValue() {
        return itemValue;
    }

    public void setItemValue(Float itemValue) {
        this.itemValue = itemValue;
    }

    public Float getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Float itemPrice) {
        this.itemPrice = itemPrice;
    }

    int itemTypeId;
    String itemType,itemQuantity;
    String requestType,deliveryType;

    public String getTransitStatus() {
        return transitStatus;
    }

    public void setTransitStatus(String transitStatus) {
        this.transitStatus = transitStatus;
    }

    String transitStatus;

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }
    String postalCode;
    String streetNo;
    String route;
    String city;
    String state;
    String country;
    String itemDescription;
    String createdTime;
    String updatedTime;

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    String imageURL;
    Double latitude,longitude;
    Float itemValue,itemPrice;

    @Override
    public int describeContents() {
        L.m("Describe contents shipments");
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        L.m("Write to parcel shipment");
    }
}

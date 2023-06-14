package com.example.cookmap;

import android.os.Parcel;
import android.os.Parcelable;

public class MapData implements Parcelable {
    private int mapId;
    private double latitude;
    private double longitude;
    private String name;
    private String image;

    public MapData(int mapId, double latitude, double longitude, String name, String image) {
        this.mapId = mapId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.image = image;
    }

    public MapData(int mapId, double latitude, double longitude) {
        this.mapId = mapId;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    protected MapData(Parcel in){
        mapId = in.readInt();
        latitude = in.readDouble();
        longitude = in.readDouble();
        name = in.readString();
        image = in.readString();
    }


    public static final Creator<MapData> CREATOR = new Creator<MapData>() {
        @Override
        public MapData createFromParcel(Parcel in) {
            return new MapData(in);
        }

        @Override
        public MapData[] newArray(int size) {
            return new MapData[size];
        }
    };

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getMapId() {
        return mapId;
    }
    public void setMapId(int mapId) {
        this.mapId = mapId;
    }
    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mapId);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeString(name);
    }
}

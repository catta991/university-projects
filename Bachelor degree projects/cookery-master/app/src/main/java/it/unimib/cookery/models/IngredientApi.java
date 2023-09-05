package it.unimib.cookery.models;

import android.os.Parcel;
import android.os.Parcelable;


import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


import java.util.ArrayList;
import java.util.Objects;



@Entity(tableName = "ingredient_api")
public class IngredientApi implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int idIngredient;


    private int id;
    private String name;
    @Ignore
    private double amount;
    @Ignore
    private String unit;
    @Ignore
    private ArrayList<String> possibleUnits;

    public IngredientApi(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Ignore
    public IngredientApi(int id, String name, double amount, String unit, ArrayList<String> possibleUnits) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.unit = unit;
        this.possibleUnits = possibleUnits;
    }
    @Ignore
    public IngredientApi(int id, String name, double amount, String unit) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.unit = unit;
    }

    @Ignore
    public IngredientApi() {
    }

    @Ignore
    protected IngredientApi(Parcel in) {
        id = in.readInt();
        name = in.readString();
        amount = in.readDouble();
        unit = in.readString();
    }


    public static final Parcelable.Creator<IngredientApi> CREATOR = new Parcelable.Creator<IngredientApi>() {
        @Override
        public IngredientApi createFromParcel(Parcel in) {
            return new IngredientApi(in);
        }

        @Override
        public IngredientApi[] newArray(int size) {
            return new IngredientApi[size];
        }
    };


    public ArrayList<String> getPossibleUnits() {
        return possibleUnits;
    }

    public void setPossibleUnits(ArrayList<String> possibleUnits) {
        this.possibleUnits = possibleUnits;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }


    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getIdIngredient() {
        return idIngredient;
    }

    public void setIdIngredient(int idIngredientPantry) {
        this.idIngredient = idIngredientPantry;
    }

    @Override
    public String toString() {
        return "IngredientApi{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                ", unit='" + unit + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IngredientApi)) return false;
        IngredientApi that = (IngredientApi) o;
        return id == that.id && Objects.equals(name, that.name);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeDouble(amount);
        dest.writeString(unit);
    }

}


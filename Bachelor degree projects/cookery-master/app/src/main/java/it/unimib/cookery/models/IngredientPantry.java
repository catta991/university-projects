package it.unimib.cookery.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "ingredient_pantry")
public class IngredientPantry extends IngredientApi {


    private int data;
    private String measureUnit;
    public long pantryId;
    private double quantity;

    @Ignore
    public IngredientPantry(int id, String name) {
        super(id, name);
    }


    public IngredientPantry(int id, String name, double quantity, int data, long pantryId, String measureUnit) {
        super(id, name);
        this.data = data;
        this.quantity = quantity;
        this.pantryId = pantryId;
        this.measureUnit = measureUnit;
    }


    public String getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(String measureUneit) {
        this.measureUnit = measureUneit;
    }


    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public long getPantryId() {
        return pantryId;
    }

    public void setPantryId(long pantryId) {
        this.pantryId = pantryId;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }




}
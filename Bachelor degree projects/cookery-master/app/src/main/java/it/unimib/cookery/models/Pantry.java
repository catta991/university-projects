package it.unimib.cookery.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "pantry")
public class Pantry {
     @PrimaryKey(autoGenerate = true)
     private int idPantry;

     @ColumnInfo(name = "name_pantry")
     private String namePantry;

     public Pantry(String namePantry) {
          this.namePantry = namePantry;
     }

     public int getIdPantry() {
          return idPantry;
     }

     public void setIdPantry(int idPantry) {
          idPantry = idPantry;
     }

     public String getNamePantry() {
          return namePantry;
     }

     public void setNamePantry(String namePantry) {
          this.namePantry = namePantry;
     }



}

package com.example.praveensubramaniyam.whatsfordinner;

import android.provider.BaseColumns;

public final class RecipesReaderContract {

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    public static final String TABLE_NAME = "recipes";
    public static final String COLUMN_NAME_RECIPESID = "recipesId";
    public static final String COLUMN_NAME_RECIPESNAME = "recipesName";
    public static final String COLUMN_NAME_RECIPESDESC = "recipesDesc";
    public static final String COLUMN_NAME_UNITSFORMEALS = "unitsForMeals";
    public static final String COLUMN_NAME_IMAGEURL = "url";
    public static final String CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + RecipesReaderContract.TABLE_NAME + " (" +
                    RecipesReaderContract.COLUMN_NAME_RECIPESID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    RecipesReaderContract.COLUMN_NAME_RECIPESNAME + TEXT_TYPE + COMMA_SEP +
                    RecipesReaderContract.COLUMN_NAME_RECIPESDESC + TEXT_TYPE + COMMA_SEP +
                    RecipesReaderContract.COLUMN_NAME_UNITSFORMEALS+ " INTEGER, "+
                    RecipesReaderContract.COLUMN_NAME_IMAGEURL +" TEXT )";

}
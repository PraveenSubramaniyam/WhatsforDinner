package com.example.praveensubramaniyam.whatsfordinner;

import android.provider.BaseColumns;

public final class IngredientsReaderContract {

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    public static final String TABLE_NAME = "ingredients";
    public static final String COLUMN_NAME_INGREDIENTSID = "ingredientsId";
    public static final String COLUMN_NAME_INGREDIENTSNAME = "ingredientsName";
    public static final String COLUMN_NAME_INGREDIENTSUNIT = "ingredientsUnit";
    public static final String CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + IngredientsReaderContract.TABLE_NAME + " (" +
                    IngredientsReaderContract.COLUMN_NAME_INGREDIENTSID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    IngredientsReaderContract.COLUMN_NAME_INGREDIENTSNAME + TEXT_TYPE + COMMA_SEP +
                    IngredientsReaderContract.COLUMN_NAME_INGREDIENTSUNIT + TEXT_TYPE + " )";
}
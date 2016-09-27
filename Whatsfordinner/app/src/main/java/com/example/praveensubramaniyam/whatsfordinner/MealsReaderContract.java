package com.example.praveensubramaniyam.whatsfordinner;

import android.provider.BaseColumns;

public final class MealsReaderContract {

        public static final String TABLE_NAME = "Meals";
        public static final String COLUMN_NAME_MEALSID = "MealsId";
        public static final String COLUMN_NAME_DATE = "MealDate";
        public static final String COLUMN_NAME_MEAL = "Meal";
        public static final String MEALS_BREAKFAST ="breakFast";
        public static final String MEALS_LUNCH ="lunch";
        public static final String MEALS_DINNER ="dinner";
        public static final String MEALS_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " +
                    COLUMN_NAME_MEALSID+ " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    COLUMN_NAME_DATE + " TEXT, " +
                    COLUMN_NAME_MEAL + " TEXT" + " )";

}
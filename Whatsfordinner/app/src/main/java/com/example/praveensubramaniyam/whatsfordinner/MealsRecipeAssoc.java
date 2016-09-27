package com.example.praveensubramaniyam.whatsfordinner;

public final class MealsRecipeAssoc {

        public static final String TABLE_NAME = "MealsRecipeAssoc";
        public static final String COLUMN_NAME_MEALSRECIPEASSOCID = "MealsRecipeAssocId";
        public static final String COLUMN_NAME_MEALSID = "MealsId";
        public static final String COLUMN_NAME_RECIPEID = "RecipeId";
        public static final String MEALSRECIPEASSOC_CREATE_ENTRIES =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " +
                        COLUMN_NAME_MEALSRECIPEASSOCID+ " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                        COLUMN_NAME_MEALSID + " INTEGER, " +
                        COLUMN_NAME_RECIPEID + " INTEGER" + " )";

}
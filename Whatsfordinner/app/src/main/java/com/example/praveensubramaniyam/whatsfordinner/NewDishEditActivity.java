package com.example.praveensubramaniyam.whatsfordinner;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class NewDishEditActivity extends AppCompatActivity {

   private static final String INGREDIENTSRECIPESASSOC_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + IngreRecipesAssocReaderContract.TABLE_NAME + " (" +
                    IngreRecipesAssocReaderContract.COLUMN_NAME_INGRERECIPESASSOCID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    IngreRecipesAssocReaderContract.COLUMN_NAME_RECIPESID + " INTEGER ," +
                    IngreRecipesAssocReaderContract.COLUMN_NAME_INGREDIENTSID + " INTEGER ," +
                    IngreRecipesAssocReaderContract.COLUMN_NAME_NOOFUNITS + " INTEGER )";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        String recipe = "";
        SQLiteDatabase myDB = null;
        List<String> ingrName = new ArrayList<String>();
        List<String> ingrUnits = new ArrayList<>();
        List<Integer> noOfUnits = new ArrayList<>();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            recipe = extras.getString("recipeName");
            //The key argument here must match that used in the other activity
        }
        setContentView(R.layout.newdisheditlayout);
        TextView recipeName = (TextView) findViewById(R.id.recipeEditName);
        recipeName.setText(recipe);

        try{
            myDB = this.openOrCreateDatabase("whatsfordinner.db", MODE_PRIVATE, null);

            String query = "select "+ IngredientsReaderContract.COLUMN_NAME_INGREDIENTSNAME + " ,"+
                    IngredientsReaderContract.COLUMN_NAME_INGREDIENTSUNIT+" , "+IngreRecipesAssocReaderContract.COLUMN_NAME_NOOFUNITS+" from "
                    +IngredientsReaderContract.TABLE_NAME + " i, "+RecipesReaderContract.TABLE_NAME+ " r, "+IngreRecipesAssocReaderContract.TABLE_NAME+
                    " ir where ir."+IngreRecipesAssocReaderContract.COLUMN_NAME_RECIPESID+ " = r."+RecipesReaderContract.COLUMN_NAME_RECIPESID+ " and "+
                    " ir."+IngreRecipesAssocReaderContract.COLUMN_NAME_INGREDIENTSID+ " = i."+IngredientsReaderContract.COLUMN_NAME_INGREDIENTSID+ " and r."+
                    RecipesReaderContract.COLUMN_NAME_RECIPESNAME+ " = '"+recipe+"'";

            System.out.println("query is: "+query);

            Cursor c = myDB.rawQuery(query, null);
            c.moveToFirst();
            while (c.isAfterLast() == false) {
                ingrName.add(c.getString(0));
                ingrUnits.add(c.getString(1));
                noOfUnits.add(c.getInt(2));
                System.out.println("adding");
                c.moveToNext();
            }
            c.close();


            String delQuery = "delete from "+IngreRecipesAssocReaderContract.TABLE_NAME + " where "+IngreRecipesAssocReaderContract.COLUMN_NAME_RECIPESID +" = "+
                    "(select "+RecipesReaderContract.COLUMN_NAME_RECIPESID + " from "+ RecipesReaderContract.TABLE_NAME + " where "+RecipesReaderContract.COLUMN_NAME_RECIPESNAME+" = '"+
                    recipe +"')";

            myDB.execSQL(delQuery);
            System.out.println("Del query: "+delQuery);

            String del2Query = "delete from "+RecipesReaderContract.TABLE_NAME + " where "+RecipesReaderContract.COLUMN_NAME_RECIPESNAME+" = '"+
            recipe +"'";
            myDB.execSQL(del2Query);
            System.out.println("Del2query: "+del2Query);
        }
        catch (Exception e)
        {
            System.out.println("Exception: "+e);
        }
        finally {
            if (myDB != null)
                myDB.close();
        }

        String ingrNameStr = ingrName.remove(0);
        String ingrUnitsStr = ingrUnits.remove(0);
        int noOfUnitsInt = noOfUnits.remove(0);

        EditText ingrNameEditText = (EditText) findViewById(R.id.ingredientsEditName);
        EditText unitsEditText = (EditText) findViewById(R.id.editUnit);
        EditText noOfUnitsEditText = (EditText) findViewById(R.id.editCount);

        ingrNameEditText.setText(ingrNameStr);
        unitsEditText.setText(ingrUnitsStr);
        //noOfUnitsEditText.setText(noOfUnitsInt);


        while(ingrName.size()!=0)
        {
            System.out.println("firt");
            LinearLayout container;
            container = (LinearLayout)findViewById(R.id.editContainer);

            LayoutInflater layoutInflater =
                    (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View addView = layoutInflater.inflate(R.layout.ingredientsrow, null);
            ((EditText)(addView.findViewById(R.id.ingredientsName))).setText(ingrName.remove(0));
            ((EditText)(addView.findViewById(R.id.unit))).setText(ingrUnits.remove(0));
           // ((EditText)(addView.findViewById(R.id.count))).setText(noOfUnits.remove(0));

            Button buttonRemove = (Button)addView.findViewById(R.id.removeButton);
            buttonRemove.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    ((LinearLayout)addView.getParent()).removeView(addView);
                }});

            container.addView(addView);
        }
    }


    public void changeImage(View view)
    {
        ImageView imageView =  (ImageView) findViewById(R.id.recipeDescImage);

    }

    public void addIngredients(View view)
    {
        LinearLayout container;
        container = (LinearLayout)findViewById(R.id.editContainer);

        LayoutInflater layoutInflater =
                        (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View addView = layoutInflater.inflate(R.layout.ingredientsrow, null);

        Button buttonRemove = (Button)addView.findViewById(R.id.removeButton);
        buttonRemove.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                ((LinearLayout)addView.getParent()).removeView(addView);
            }});

        container.addView(addView);
    }


    public void updateRecipe(View view)
    {

        SQLiteDatabase myDB = null;
        TextView recipeNameBox   = (TextView)findViewById(R.id.recipeEditName);
        String recipeName = recipeNameBox.getText().toString();
        String ingredientsName = ((EditText)findViewById(R.id.ingredientsEditName)).getText().toString();
        String ingredientsUnit = ((EditText)findViewById(R.id.editUnit)).getText().toString();
        String ingredientsCountStr = ((EditText)findViewById(R.id.editCount)).getText().toString();
        int ingredientsCount =0;
        try {
            ingredientsCount  = Integer.parseInt(ingredientsCountStr);
        }
        catch(Exception e)
        {
            System.out.println("Got exception "+e);
            System.out.println("Count Str:"+ingredientsCountStr+"e");
        }


        LinearLayout parentLinearLayout = (LinearLayout)findViewById(R.id.editContainer);
        long ingredientId = 0;
        ContentValues ingredientsValues = null;

        try {
            myDB = this.openOrCreateDatabase("whatsfordinner.db", MODE_PRIVATE, null);
            myDB.execSQL(RecipesReaderContract.CREATE_ENTRIES);
            myDB.execSQL(IngredientsReaderContract.CREATE_ENTRIES);
            myDB.execSQL(INGREDIENTSRECIPESASSOC_CREATE_ENTRIES);

            ContentValues recipeValues = new ContentValues();
            recipeValues.put(RecipesReaderContract.COLUMN_NAME_RECIPESNAME, recipeName);
            recipeValues.put(RecipesReaderContract.COLUMN_NAME_RECIPESDESC, recipeName);
            recipeValues.put(RecipesReaderContract.COLUMN_NAME_UNITSFORMEALS, 0);

            long recipeId = myDB.insert(RecipesReaderContract.TABLE_NAME,null,recipeValues);

            String query = "select "+ IngredientsReaderContract.COLUMN_NAME_INGREDIENTSID + " from " +IngredientsReaderContract.TABLE_NAME+
            " where "+IngredientsReaderContract.COLUMN_NAME_INGREDIENTSNAME+" = '"+ingredientsName + "' ";
            System.out.println("query is: "+query);
            Cursor c = myDB.rawQuery(query, null);
            if (c.moveToFirst()) {
                ingredientId = c.getInt(0);
                System.out.println("got Id : "+ingredientId);
            }

            c.close();
            System.out.println("got Id : "+ingredientId);

            if (ingredientId == 0)
            {
                ingredientsValues = new ContentValues();
                ingredientsValues.put(IngredientsReaderContract.COLUMN_NAME_INGREDIENTSNAME, ingredientsName);
                ingredientsValues.put(IngredientsReaderContract.COLUMN_NAME_INGREDIENTSUNIT, ingredientsUnit);

                ingredientId = myDB.insert(IngredientsReaderContract.TABLE_NAME, null, ingredientsValues);
            }

            ContentValues ingredientsRecipesAssocValues = new ContentValues();
            ingredientsRecipesAssocValues.put(IngreRecipesAssocReaderContract.COLUMN_NAME_RECIPESID, recipeId);
            ingredientsRecipesAssocValues.put(IngreRecipesAssocReaderContract.COLUMN_NAME_INGREDIENTSID, ingredientId);
            ingredientsRecipesAssocValues.put(IngreRecipesAssocReaderContract.COLUMN_NAME_NOOFUNITS, ingredientsCount);

            myDB.insert(IngreRecipesAssocReaderContract.TABLE_NAME,null,ingredientsRecipesAssocValues);


            for (int i = 0; i < parentLinearLayout.getChildCount(); i++) {
                ingredientId = 0;
                View v = parentLinearLayout.getChildAt(i);

                if (v instanceof LinearLayout) {
                    LinearLayout v2 = (LinearLayout)((LinearLayout) v).getChildAt(0);
                    ingredientsName = ((EditText) v2.getChildAt(0)).getText().toString();
                    ingredientsUnit = ((EditText) v2.getChildAt(1)).getText().toString();
                    ingredientsCountStr = ((EditText) v2.getChildAt(2)).getText().toString();
                    ingredientsCount = Integer.parseInt(ingredientsCountStr);

                    query = "select "+ IngredientsReaderContract.COLUMN_NAME_INGREDIENTSID + " from " +IngredientsReaderContract.TABLE_NAME+
                            " where "+IngredientsReaderContract.COLUMN_NAME_INGREDIENTSNAME+" = '"+ingredientsName + "' ";
                    c = myDB.rawQuery(query, null);
                    if (c.moveToFirst()) {
                        ingredientId = c.getInt(0);
                    }

                    c.close();

                    if (ingredientId == 0)
                    {
                        ingredientsValues = new ContentValues();
                        ingredientsValues.put(IngredientsReaderContract.COLUMN_NAME_INGREDIENTSNAME, ingredientsName);
                        ingredientsValues.put(IngredientsReaderContract.COLUMN_NAME_INGREDIENTSUNIT, ingredientsUnit);

                        ingredientId = myDB.insert(IngredientsReaderContract.TABLE_NAME, null, ingredientsValues);
                    }

                    ingredientsRecipesAssocValues = new ContentValues();
                    ingredientsRecipesAssocValues.put(IngreRecipesAssocReaderContract.COLUMN_NAME_RECIPESID, recipeId);
                    ingredientsRecipesAssocValues.put(IngreRecipesAssocReaderContract.COLUMN_NAME_INGREDIENTSID, ingredientId);
                    ingredientsRecipesAssocValues.put(IngreRecipesAssocReaderContract.COLUMN_NAME_NOOFUNITS, ingredientsCount);

                    myDB.insert(IngreRecipesAssocReaderContract.TABLE_NAME,null,ingredientsRecipesAssocValues);

                    System.out.println("Ingredients Name: "+ingredientsName+" Unit: "+ingredientsUnit+" count:"+ingredientsCount);
                }
            }
        }
        catch(Exception e) {
            Log.e("Error", "Error", e);
        } finally {
            if (myDB != null) {
                System.out.println("Successfully closed database");
                myDB.close();
            }
        }
        Intent intent= new Intent(NewDishEditActivity.this,FragmentLayout.class);
        startActivity(intent);

        System.out.println("onclick called");
    }
}

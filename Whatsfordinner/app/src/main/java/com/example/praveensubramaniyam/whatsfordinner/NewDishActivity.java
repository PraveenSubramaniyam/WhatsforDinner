package com.example.praveensubramaniyam.whatsfordinner;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.io.File;
import java.net.URI;
import  	java.net.URL;

import java.io.InputStream;


public class NewDishActivity extends AppCompatActivity {

   private static final String INGREDIENTSRECIPESASSOC_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + IngreRecipesAssocReaderContract.TABLE_NAME + " (" +
                    IngreRecipesAssocReaderContract.COLUMN_NAME_INGRERECIPESASSOCID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    IngreRecipesAssocReaderContract.COLUMN_NAME_RECIPESID + " INTEGER ," +
                    IngreRecipesAssocReaderContract.COLUMN_NAME_INGREDIENTSID + " INTEGER ," +
                    IngreRecipesAssocReaderContract.COLUMN_NAME_NOOFUNITS + " INTEGER )";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newdishlayout);
        final EditText recipeName = (EditText) findViewById(R.id.recipeName) ;
        recipeName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    checkrecipeName(recipeName.getText().toString());
                    System.out.println("Checking this ");
                }
            }
        });


    }

    public void checkrecipeName(String recipeName)
    {
        SQLiteDatabase myDB = null;

        try {
            myDB = this.getApplication().openOrCreateDatabase("whatsfordinner.db", MODE_PRIVATE, null);
            myDB.execSQL(RecipesReaderContract.CREATE_ENTRIES);
            myDB.execSQL(IngredientsReaderContract.CREATE_ENTRIES);
            myDB.execSQL(INGREDIENTSRECIPESASSOC_CREATE_ENTRIES);


            String query = "select " + RecipesReaderContract.COLUMN_NAME_RECIPESID + " from " + RecipesReaderContract.TABLE_NAME +
                    " where " + RecipesReaderContract.COLUMN_NAME_RECIPESNAME + " = '" + recipeName + "' ";
            Cursor c = myDB.rawQuery(query, null);
            if (c.moveToFirst()) {
                Toast.makeText(this, "Recipe with the name already exists", Toast.LENGTH_SHORT)
                        .show();
                c.close();
                myDB.close();
                return;
            }
            c.close();

        }
        catch (Exception e)
        {
            System.out.println("Exception "+e);
        }
        finally {
            if(myDB != null)
            myDB.close();
        }


    }


    public void changeImage(View view)
    {
        ImageView imageView =  (ImageView) findViewById(R.id.recipeDescImage);
        int SELECT_PHOTO = 100;
        System.out.println("Image change called");

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case 100:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();


                    ImageView imageView =  (ImageView) findViewById(R.id.recipeDescImage);

                    imageView.setImageURI(selectedImage);
                    imageView.setTag(getPathFromURI(selectedImage));

                }
        }
    }

    public void addIngredients(View view)
    {
        LinearLayout container;
        container = (LinearLayout)findViewById(R.id.container);

        if (container.getChildCount() == 9) {
            Toast.makeText(this, "Can't add more than 10 ingredients", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
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

    /*myDB.execSQL("INSERT INTO "
                    + RecipesReaderContract.Recipes.TABLE_NAME
                    + " ("+RecipesReaderContract.Recipes.COLUMN_NAME_RECIPESNAME+", "+RecipesReaderContract.Recipes.COLUMN_NAME_RECIPESDESC
                    + ") VALUES ('"+recipeName+"',"+recipeName+");");

            myDB.execSQL("INSERT INTO "
                    + IngredientsReaderContract.Ingredients.TABLE_NAME
                    + " ("+IngredientsReaderContract.Ingredients.COLUMN_NAME_INGREDIENTSNAME+", "+IngredientsReaderContract.Ingredients.COLUMN_NAME_INGREDIENTSUNIT
                    + ") VALUES ('"+ingredientsName+"',"+ingredientsUnit+");"); */

    public void createRecipe(View view)
    {
        SQLiteDatabase myDB = null;
        EditText recipeNameBox   = (EditText)findViewById(R.id.recipeName);
        String recipeName = recipeNameBox.getText().toString();
        String ingredientsName = ((EditText)findViewById(R.id.ingredientsName)).getText().toString();
        String ingredientsUnit = ((EditText)findViewById(R.id.unit)).getText().toString();
        String ingredientsCountStr = ((EditText)findViewById(R.id.count)).getText().toString();
        String imageUri = (String)((ImageView)findViewById(R.id.recipeDescImage)).getTag();
        int ingredientsCount =0;
        try {
            ingredientsCount  = Integer.parseInt(ingredientsCountStr);
        }
        catch(Exception e)
        {
            System.out.println("Got exception "+e);
            System.out.println("Count Str:"+ingredientsCountStr+"e");
            Toast.makeText(this, "Enter number for count", Toast.LENGTH_SHORT)
                    .show();
            return;
        }


        LinearLayout parentLinearLayout = (LinearLayout)findViewById(R.id.container);
        long ingredientId = 0;
        int recipeIdVald = 0;
        ContentValues ingredientsValues = null;

        try {
            myDB = this.openOrCreateDatabase("whatsfordinner.db", MODE_PRIVATE, null);
            myDB.execSQL(RecipesReaderContract.CREATE_ENTRIES);
            myDB.execSQL(IngredientsReaderContract.CREATE_ENTRIES);
            myDB.execSQL(INGREDIENTSRECIPESASSOC_CREATE_ENTRIES);


            String query = "select "+ RecipesReaderContract.COLUMN_NAME_RECIPESID + " from " +RecipesReaderContract.TABLE_NAME+
                    " where "+RecipesReaderContract.COLUMN_NAME_RECIPESNAME+" = '"+recipeName + "' ";
            Cursor c = myDB.rawQuery(query, null);
            if (c.moveToFirst()) {
                recipeIdVald = c.getInt(0);
            }
            c.close();
            if (recipeIdVald != 0) {
                Toast.makeText(this, "Recipe with the name already exists", Toast.LENGTH_SHORT)
                        .show();
                myDB.close();
                return;
            }


            ContentValues recipeValues = new ContentValues();
            recipeValues.put(RecipesReaderContract.COLUMN_NAME_RECIPESNAME, recipeName);
            recipeValues.put(RecipesReaderContract.COLUMN_NAME_RECIPESDESC, recipeName);
            recipeValues.put(RecipesReaderContract.COLUMN_NAME_UNITSFORMEALS, 0);
            recipeValues.put(RecipesReaderContract.COLUMN_NAME_IMAGEURL,imageUri);

            long recipeId = myDB.insert(RecipesReaderContract.TABLE_NAME,null,recipeValues);

            query = "select "+ IngredientsReaderContract.COLUMN_NAME_INGREDIENTSID + " from " +IngredientsReaderContract.TABLE_NAME+
            " where "+IngredientsReaderContract.COLUMN_NAME_INGREDIENTSNAME+" = '"+ingredientsName + "' ";
            System.out.println("query is: "+query);
             c = myDB.rawQuery(query, null);
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
        Intent intent= new Intent(NewDishActivity.this,FragmentLayout.class);
        startActivity(intent);
    }
}

package com.example.praveensubramaniyam.whatsfordinner;

/**
 * Created by Praveen Subramaniyam on 9/24/2016.
 */

import android.app.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.util.Log;
import android.util.SparseBooleanArray;

import android.view.View;

import android.widget.ArrayAdapter;

import android.widget.Button;

import android.widget.ListView;

import android.widget.Toast;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MealsScreenSelectRecipes extends Activity {

    ListView myList;
    Button getChoice;

    Map<String,ArrayList<Integer>> recipeList = new HashMap<String, ArrayList<Integer>>();


    private void addValues(String key, int value) {
        ArrayList tempList = null;
        if (recipeList.containsKey(key)) {
            tempList = recipeList.get(key);
            if(tempList == null)
                tempList = new ArrayList();
            tempList.add(value);
        } else {
            tempList = new ArrayList();
            tempList.add(value);
        }
        recipeList.put(key,tempList);
    }
    /** Called when the activity is first created. */

    @Override

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.meals_screen_select_recipes);
        myList = (ListView)findViewById(R.id.list);
        getChoice = (Button)findViewById(R.id.getchoice);

        SQLiteDatabase myDB = null;
        List<String> recipeName = new ArrayList<String>();

        try {
            myDB = SQLiteDatabase.openDatabase("/data/data/com.example.praveensubramaniyam.whatsfordinner/databases/whatsfordinner.db", null, 0);
           // myDB = SQLiteDatabase.openOrCreateDatabase("whatsfordinner.db", null, null);
            String query ="select "+RecipesReaderContract.COLUMN_NAME_RECIPESID+", "+RecipesReaderContract.COLUMN_NAME_RECIPESNAME+" , "+
                    RecipesReaderContract.COLUMN_NAME_UNITSFORMEALS+
                    " from "+ RecipesReaderContract.TABLE_NAME +" where "+
                    RecipesReaderContract.COLUMN_NAME_UNITSFORMEALS+" != 0";





            Cursor c = myDB.rawQuery(query,null);

            int Column0 = c.getColumnIndex(RecipesReaderContract.COLUMN_NAME_RECIPESID);
            int Column1 = c.getColumnIndex(RecipesReaderContract.COLUMN_NAME_RECIPESNAME);
            int Column2 = c.getColumnIndex(RecipesReaderContract.COLUMN_NAME_UNITSFORMEALS);
            c.moveToFirst();
            if (c != null) {
                // Loop through all Results
                do {
                    int count = c.getInt(Column2);
                    while(count >0) {
                        addValues(c.getString(Column1),c.getInt(Column0));
                        count --;
                    }
                } while (c.moveToNext());
            }
        }
        catch (Exception e)
        {
            Log.e("exception",e.toString());
        }
        finally {
            myDB.close();
        }

        for (String key : recipeList.keySet()) {
            int count = recipeList.get(key).size();
            while ( count > 0) {
                recipeName.addAll(recipeList.keySet());
                count --;
            }
        }
        ArrayAdapter<String> adapter
                = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice,recipeName);

        myList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        myList.setAdapter(adapter);

        getChoice.setOnClickListener(new Button.OnClickListener(){

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            ArrayList<String> selected = new ArrayList<String>();
            ArrayList<Integer> selectedId = new ArrayList<Integer>();
            int cntChoice = myList.getCount();
            SparseBooleanArray sparseBooleanArray = myList.getCheckedItemPositions();

            for(int i = 0; i < cntChoice; i++){
                if(sparseBooleanArray.get(i)) {
                    String recipeName = myList.getItemAtPosition(i).toString();
                    selected.add(recipeName);
                    selectedId.add(recipeList.get(recipeName).remove(0));

                    //selected += myList.getItemAtPosition(i).toString() + "\n";
                }
            }
            Intent intentMessage=new Intent();
            Bundle extras = new Bundle();
            extras.putStringArrayList("recipeName",selected);
            extras.putIntegerArrayList("recipeId",selectedId);

            // put the message to return as result in Intent
            intentMessage.putExtras(extras);

            // Set The Result in Intent
            setResult(2,intentMessage);
            // finish The activity
            finish();

        }});
    }

}
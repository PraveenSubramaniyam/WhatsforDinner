package com.example.praveensubramaniyam.whatsfordinner;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * Created by Praveen Subramaniyam on 9/21/2016.
 */
public class MealsScreenMainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mealscreenmain);

        SQLiteDatabase myDB = null;
        try {
            myDB = this.openOrCreateDatabase("whatsfordinner.db", MODE_PRIVATE, null);
            myDB.execSQL(MealsReaderContract.MEALS_CREATE_ENTRIES);
            myDB.execSQL(MealsRecipeAssoc.MEALSRECIPEASSOC_CREATE_ENTRIES);
        }
        catch(Exception e) {
            Log.e("Error", "Error", e);
        } finally {
            if (myDB != null) {
                System.out.println("Successfully closed database");
                myDB.close();
            }
        }


        MealsScreenBottomView details = (MealsScreenBottomView) getFragmentManager().findFragmentById(R.id.details);
        // Make new fragment to show this selection.
        SimpleDateFormat sdf = new SimpleDateFormat("MM dd yyyy");
        Date d = new Date();
        String currentDate = sdf.format(d);
        details = MealsScreenBottomView.newInstance(currentDate);
        // Execute a transaction, replacing any existing fragment with this one inside the frame.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.mealsScreenBottomViewLayout, details);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    public void updateToPreviousDate(View view)
    {
        ((MealsCalendarMenuFragment)getFragmentManager().findFragmentById(R.id.titles)).updateToPreviousDate();
        String date = ((MealsCalendarMenuFragment)getFragmentManager().findFragmentById(R.id.titles)).getTextViewDateString();
        MealsScreenBottomView details = (MealsScreenBottomView) getFragmentManager().findFragmentById(R.id.details);
        // Make new fragment to show this selection.
        details = MealsScreenBottomView.newInstance(date);
        // Execute a transaction, replacing any existing fragment with this one inside the frame.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.mealsScreenBottomViewLayout, details);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();

    }

    public void updateToNextDate(View view)
    {
        ((MealsCalendarMenuFragment)getFragmentManager().findFragmentById(R.id.titles)).updateToNextDate();
        String date = ((MealsCalendarMenuFragment)getFragmentManager().findFragmentById(R.id.titles)).getTextViewDateString();
        MealsScreenBottomView details = (MealsScreenBottomView) getFragmentManager().findFragmentById(R.id.details);
        // Make new fragment to show this selection.
        details = MealsScreenBottomView.newInstance(date);
        // Execute a transaction, replacing any existing fragment with this one inside the frame.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.mealsScreenBottomViewLayout, details);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    public void selectBreakFastRecipes(View view)
    {
        Intent intentGetMessage=new Intent(this,MealsScreenSelectRecipes.class);
        startActivityForResult(intentGetMessage, 1);// Activity is started with requestCode 2
    }

    public void selectLunchRecipes(View view)
    {
        Intent intentGetMessage=new Intent(this,MealsScreenSelectRecipes.class);
        startActivityForResult(intentGetMessage, 2);// Activity is started with requestCode 2
    }

    public void selectDinnerRecipes(View view)
    {
        Intent intentGetMessage=new Intent(this,MealsScreenSelectRecipes.class);
        startActivityForResult(intentGetMessage, 3);// Activity is started with requestCode 2
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        String date = ((MealsCalendarMenuFragment)getFragmentManager().findFragmentById(R.id.titles)).getTextViewDateString();

        // check if the request code is same as what is passed  here it is 2
        if(requestCode == 1 || requestCode == 2 || requestCode == 3) {
            // fetch the message String
            if(data == null)
                return;

            List<Integer> recipeIdList = data.getIntegerArrayListExtra("recipeId");
            List<String> recipeNameList = data.getStringArrayListExtra("recipeName");

            // Set the message string in textView
            System.out.println("Got message: " + recipeIdList);

            if (recipeIdList.isEmpty())
                return;
            SQLiteDatabase myDB = null;

            try {
                myDB = this.openOrCreateDatabase("whatsfordinner.db", MODE_PRIVATE, null);

                ContentValues MealsValues = new ContentValues();
                MealsValues.put(MealsReaderContract.COLUMN_NAME_DATE, date);

                switch(requestCode)
                {
                    case 1:
                        MealsValues.put(MealsReaderContract.COLUMN_NAME_MEAL, MealsReaderContract.MEALS_BREAKFAST);
                        break;
                    case 2:
                        MealsValues.put(MealsReaderContract.COLUMN_NAME_MEAL, MealsReaderContract.MEALS_LUNCH);
                        break;
                    case 3:
                        MealsValues.put(MealsReaderContract.COLUMN_NAME_MEAL, MealsReaderContract.MEALS_DINNER);
                        break;
                }

                long mealId = myDB.insert(MealsReaderContract.TABLE_NAME, null, MealsValues);
                //List<String> recipeIdList = Arrays.asList(message.split(","));

                for (Integer recipeId : recipeIdList) {
                    ContentValues mealsRecipeValues = new ContentValues();
                    mealsRecipeValues.put(MealsRecipeAssoc.COLUMN_NAME_MEALSID, mealId);
                    mealsRecipeValues.put(MealsRecipeAssoc.COLUMN_NAME_RECIPEID, recipeId);
                    myDB.insert(MealsRecipeAssoc.TABLE_NAME, null, mealsRecipeValues);

                    String query ="update "+RecipesReaderContract.TABLE_NAME+" set "+RecipesReaderContract.COLUMN_NAME_UNITSFORMEALS + " = "+
                            RecipesReaderContract.COLUMN_NAME_UNITSFORMEALS + " - 1 where "+RecipesReaderContract.COLUMN_NAME_RECIPESID + " = "+
                            recipeId;

                    myDB.execSQL(query);
                }
            } catch (Exception e) {
                Log.e("exception", e.toString());
            } finally {
                myDB.close();
            }

            MealsScreenBottomView details = (MealsScreenBottomView) getFragmentManager().findFragmentById(R.id.details);
            // Make new fragment to show this selection.
            details = MealsScreenBottomView.newInstance(date);
            // Execute a transaction, replacing any existing fragment with this one inside the frame.
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.mealsScreenBottomViewLayout, details);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
        }
    }

}

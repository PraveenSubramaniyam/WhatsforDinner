package com.example.praveensubramaniyam.whatsfordinner;


import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MealsScreenBottomView extends Fragment
{
  /**
   * Create a new instance of DetailsFragment, initialized to
   * show the text at 'index'.
  */
  public static MealsScreenBottomView newInstance(String date)
  {
    MealsScreenBottomView f = new MealsScreenBottomView();
    // Supply index input as an argument.
    Bundle args = new Bundle();
    args.putString("date", date);
    f.setArguments(args);
    return f;

  }

  public String getDate()
  {
    return getArguments().getString("date");
  }
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
  {
      View v = null;
      String date = getDate();
      String breakFastRecipes = "";
      String lunchRecipes = "";
      String dinnerRecipes = "";

      v =  inflater.inflate(R.layout.mealsscreenbottomview, container, false);
      SQLiteDatabase myDB = null;

      try {
          myDB = SQLiteDatabase.openDatabase("/data/data/com.example.praveensubramaniyam.whatsfordinner/databases/whatsfordinner.db", null, 0);
         // myDB = SQLiteDatabase.openOrCreateDatabase("whatsfordinner.db", null, null);

          String query ="select "+RecipesReaderContract.COLUMN_NAME_RECIPESNAME+", "+MealsReaderContract.COLUMN_NAME_MEAL+
                  " from "+ RecipesReaderContract.TABLE_NAME + " r, "+ MealsReaderContract.TABLE_NAME +" m, "+ MealsRecipeAssoc.TABLE_NAME+ " mr where r."+
                  RecipesReaderContract.COLUMN_NAME_RECIPESID+" = mr."+ MealsRecipeAssoc.COLUMN_NAME_RECIPEID+" and m."+MealsReaderContract.COLUMN_NAME_MEALSID+
                  " = mr."+MealsRecipeAssoc.COLUMN_NAME_MEALSID+ " and "+MealsReaderContract.COLUMN_NAME_DATE +" = '"+date+"'" ;

          System.out.println("Query to execute: "+query);
          Cursor res =  myDB.rawQuery(query, null );
          res.moveToFirst();


          while (res.isAfterLast() == false) {
              String mealType = res.getString(res.getColumnIndex(MealsReaderContract.COLUMN_NAME_MEAL));
              if (mealType.equals(MealsReaderContract.MEALS_BREAKFAST))
              {
                  breakFastRecipes = breakFastRecipes +res.getString(res.getColumnIndex(RecipesReaderContract.COLUMN_NAME_RECIPESNAME))+"\n";
              }
              else if (mealType.equals(MealsReaderContract.MEALS_LUNCH))
              {
                    lunchRecipes = lunchRecipes + res.getString(res.getColumnIndex(RecipesReaderContract.COLUMN_NAME_RECIPESNAME)) +"\n";
              }
              else if (mealType.equals(MealsReaderContract.MEALS_DINNER))
              {
                  dinnerRecipes = dinnerRecipes + res.getString(res.getColumnIndex(RecipesReaderContract.COLUMN_NAME_RECIPESNAME)) +"\n";
              }
              res.moveToNext();
          }
      }
      catch (Exception e)
      {
          Log.e("exception",e.toString());
      }
      finally {
          myDB.close();
      }

      TextView breakFastView = (TextView) v.findViewById(R.id.breakFastTextView);
      TextView lunchView = (TextView) v.findViewById(R.id.lunchTextView);
      TextView dinnerView = (TextView) v.findViewById(R.id.dinnerTextView);

      if(!breakFastRecipes.isEmpty())
          breakFastView.setText(breakFastRecipes);

      if(!lunchRecipes.isEmpty())
          lunchView.setText(lunchRecipes);

      if(!dinnerRecipes.isEmpty())
          dinnerView.setText(dinnerRecipes);

      return v;
  }

}
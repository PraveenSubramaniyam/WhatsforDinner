package com.example.praveensubramaniyam.whatsfordinner;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.util.prefs.Preferences;

public class MainActivity extends AppCompatActivity {
    PopupWindow popupMessage;
    LinearLayout layoutOfPopup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void openMealsActivity(View v)
    {
        Intent intent= new Intent(MainActivity.this,MealsScreenMainActivity.class);
        startActivity(intent);
    }

    public void openRecipeActivity(View v){
        Intent intent= new Intent(MainActivity.this,FragmentLayout.class);
        startActivity(intent);
    }

    public void openGroceriesActivity(View v){
        Intent intent= new Intent(MainActivity.this,SimpleActivity.class);
        startActivity(intent);
    }

    public void openNewDishActivity(View v){

        Intent intent= new Intent(MainActivity.this,NewDishActivity.class);
        startActivity(intent);
    }

    public void showVersion(View v)
    {
        LinearLayout btnOpenPopup = (LinearLayout) findViewById(R.id.ll1);
        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(MainActivity.this, btnOpenPopup);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(MainActivity.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        popup.show();//showing popup menu
    }




}

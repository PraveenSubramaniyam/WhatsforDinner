/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2015 baoyongzhang <baoyz94@gmail.com>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.example.praveensubramaniyam.whatsfordinner;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * SwipeMenuListView
 * Created by baoyz on 15/6/29.
 */
public class SimpleActivity extends Activity {

    private List<String> mAppList;
    private AppAdapter mAdapter;
    private SwipeMenuListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        mAppList = new ArrayList<String>();

        SQLiteDatabase myDB = null;

        try {
            myDB = SQLiteDatabase.openDatabase("/data/data/com.example.praveensubramaniyam.whatsfordinner/databases/whatsfordinner.db", null, 0);
           // myDB = SQLiteDatabase.openOrCreateDatabase("whatsfordinner.db", null, null);
            String query ="select i."+IngredientsReaderContract.COLUMN_NAME_INGREDIENTSID+" , "+IngredientsReaderContract.COLUMN_NAME_INGREDIENTSNAME+", "+
                    IngredientsReaderContract.COLUMN_NAME_INGREDIENTSUNIT+" , "+
                    "sum( "+IngreRecipesAssocReaderContract.COLUMN_NAME_NOOFUNITS+" ) from "+IngredientsReaderContract.TABLE_NAME +
                    " i ,"+IngreRecipesAssocReaderContract.TABLE_NAME + " ir, "+MealsReaderContract.TABLE_NAME+" m, "+MealsRecipeAssoc.TABLE_NAME+" mr where"+
                    " mr."+MealsRecipeAssoc.COLUMN_NAME_MEALSID + " = m."+ MealsReaderContract.COLUMN_NAME_MEALSID+" and mr."+ MealsRecipeAssoc.COLUMN_NAME_RECIPEID+ " = ir."+
                    IngreRecipesAssocReaderContract.COLUMN_NAME_RECIPESID+" and ir."+ IngreRecipesAssocReaderContract.COLUMN_NAME_INGREDIENTSID+" = "+
                    " i."+ IngredientsReaderContract.COLUMN_NAME_INGREDIENTSID+" group by( i."+IngredientsReaderContract.COLUMN_NAME_INGREDIENTSID+ " )";

            System.out.println("Query to execute: "+query);
            Cursor res =  myDB.rawQuery(query, null );
            res.moveToFirst();

            while (res.isAfterLast() == false) {
                String IngredientsName = res.getString(res.getColumnIndex(IngredientsReaderContract.COLUMN_NAME_INGREDIENTSNAME));
                int count = res.getInt(res.getColumnIndex("sum( "+IngreRecipesAssocReaderContract.COLUMN_NAME_NOOFUNITS +" )"));
                String units = res.getString(res.getColumnIndex(IngredientsReaderContract.COLUMN_NAME_INGREDIENTSUNIT));
                //System.out.println("Name: "+IngredientsName+" count: "+count);
                mAppList.add(IngredientsName + " ("+count+" "+units+ ")");
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


        mListView = (SwipeMenuListView) findViewById(R.id.listView);

        mAdapter = new AppAdapter();
        mListView.setAdapter(mAdapter);

        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(dp2px(90));
                // set item title
                openItem.setTitle("+");
                // set item title fontsize
                openItem.setTitleSize(40);
                // set item title font color
                openItem.setTitleColor(Color.BLACK);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                //deleteItem.setIcon(R.drawable.minus);
                deleteItem.setTitle("-");
                deleteItem.setTitleSize(50);
                deleteItem.setTitleColor(Color.BLACK);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        mListView.setMenuCreator(creator);

        // step 2. listener item click event
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                String item = mAppList.get(position);
                switch (index) {
                    case 0:
                        // open
                        //open(item);
                        String itemVal = mAppList.remove(position);
                        String[] parts = itemVal.split("\\(");
                        String[] parts1 = parts[1].split(" ");
                        int count = Integer.parseInt(parts1[0]);
                        count += 1;
                        mAppList.add(position,parts[0]+"("+count+" "+parts1[1]);
                        mAdapter.notifyDataSetChanged();
                        break;

                    case 1:
                        // delete
//					delete(item);

                        itemVal = mAppList.remove(position);
                        parts = itemVal.split("\\(");
                        parts1 = parts[1].split(" ");
                        count = Integer.parseInt(parts1[0]);
                        count -= 1;

                        mAppList.add(position,parts[0]+"("+count+" "+parts1[1]);
                        mAdapter.notifyDataSetChanged();
                        break;
                }
                return false;
            }
        });

        // set SwipeListener
        mListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });

        // set MenuStateChangeListener
        mListView.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
            @Override
            public void onMenuOpen(int position) {
            }

            @Override
            public void onMenuClose(int position) {
            }
        });

        // other setting
//		listView.setCloseInterpolator(new BounceInterpolator());

        // test item long click
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                Toast.makeText(getApplicationContext(), position + " long click", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

    }

    private void delete(ApplicationInfo item) {
        // delete app
        try {
            Intent intent = new Intent(Intent.ACTION_DELETE);
            intent.setData(Uri.fromParts("package", item.packageName, null));
            startActivity(intent);
        } catch (Exception e) {
        }
    }

    private void open(ApplicationInfo item) {
        // open app
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(item.packageName);
        List<ResolveInfo> resolveInfoList = getPackageManager()
                .queryIntentActivities(resolveIntent, 0);
        if (resolveInfoList != null && resolveInfoList.size() > 0) {
            ResolveInfo resolveInfo = resolveInfoList.get(0);
            String activityPackageName = resolveInfo.activityInfo.packageName;
            String className = resolveInfo.activityInfo.name;

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName componentName = new ComponentName(
                    activityPackageName, className);

            intent.setComponent(componentName);
            startActivity(intent);
        }
    }

    class AppAdapter extends BaseSwipListAdapter {

        @Override
        public int getCount() {
            return mAppList.size();
        }

        @Override
        public String getItem(int position) {
            System.out.println("item name :"+mAppList.get(position));
            return mAppList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(),
                        R.layout.item_list_app, null);
                new ViewHolder(convertView);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            String item = getItem(position);
            holder.tv_name.setText(item);
            holder.iv_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(SimpleActivity.this, "iv_icon_click", Toast.LENGTH_SHORT).show();
                }
            });
            holder.tv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(SimpleActivity.this,"iv_icon_click",Toast.LENGTH_SHORT).show();
                }
            });
            return convertView;
        }

        class ViewHolder {
            ImageView iv_icon;
            TextView tv_name;

            public ViewHolder(View view) {
                iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
                tv_name = (TextView) view.findViewById(R.id.tv_name);
                view.setTag(this);
            }
        }

        @Override
        public boolean getSwipEnableByPosition(int position) {
            //if(position % 2 == 0){
              //  return false;
            //}
            return true;
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_left) {
            mListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
            return true;
        }
        if (id == R.id.action_right) {
            mListView.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

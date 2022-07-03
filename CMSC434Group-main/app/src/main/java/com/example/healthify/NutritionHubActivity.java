package com.example.healthify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthify.Food;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.Integer.parseInt;

public class NutritionHubActivity extends AppCompatActivity {

    private ImageButton homeButton;
    private ImageButton logFoodButton;
    private ImageButton databaseButton;
    private ListView foodList;

    private Context context;

    private ArrayList<String> foodArrayList;// = new ArrayList<>();
    private ArrayAdapter<String> foodListAdapter;
    private ArrayList<Food> foodObjectList;
    private TextView calorieCounter;


//    StableArrayAdapter adapter = new StableArrayAdapter(this, android.R.layout.simple_list_item_1, foodArrayList);

    String foodName = "";
    String date = "";
    String calories = "";
    String mealTime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_hub);
       // Bundle bundle = getIntent().getExtras();
        homeButton = (ImageButton) findViewById(R.id.homeButton);
        logFoodButton = (ImageButton) findViewById(R.id.logFoodButton);
        databaseButton = (ImageButton) findViewById(R.id.databaseButton);
        foodList = (ListView) findViewById(R.id.foodList);
        calorieCounter = (TextView) findViewById(R.id.caloriesConsumedLabel);

       // System.out.println(cals);

        foodArrayList = new ArrayList<String>();
        foodObjectList = new ArrayList<Food>();
        context = getApplicationContext();


      //  if(bundle != null) {
            //foodArrayList = bundle.getStringArrayList("foodArrayList");


            //foodArrayList = new ArrayList<String>();
          //  final StableArrayAdapter adapter = new StableArrayAdapter(this, android.R.layout.simple_list_item_1, foodArrayList);

            foodListAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, foodArrayList) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent){
                    View view = super.getView(position, convertView, parent);
                    TextView tv = (TextView) view.findViewById(android.R.id.text1);
                    tv.setTextColor(Color.WHITE);
                    return view;
                }
             };

            //ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.la);

          //  foodList.setAdapter((adapter));
            foodList.setAdapter(foodListAdapter);
            initializeFoodList();

            Bundle extras = getIntent().getExtras();


            // Bundle bundle = getIntent().getExtras();

            if (extras != null) {
                foodName = extras.getString("foodName");
                date = extras.getString("date");
                calories = extras.getString("calories");
                mealTime = extras.getString("mealTime");

                Food newFood = new Food(foodName, mealTime, date, calories);
                addFood(newFood);
            }

            foodListAdapter.notifyDataSetChanged();
            //Toast.makeText(getApplicationContext(), (foodName +  " " + date + " " + mealTime + " " + calories), Toast.LENGTH_SHORT).show();
            //System.out.println(foodName +  " " + date + " " + mealTime + " " + calories);

        //important
//            System.out.println(foodArrayList);
//
//            foodList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    final String item = (String) parent.getItemAtPosition(position);
//                    view.animate().setDuration(2000).alpha(0).withEndAction(new Runnable() {
//                        @Override
//                        public void run() {
//                            foodArrayList.remove(item);
//                            adapter.notifyDataSetChanged();
//                            foodList.setAlpha(1);
//                        }
//
//                    });
//                }
//            });
//
//        //}
        initializeOnClickListeners();

    }

    private void initializeOnClickListeners() {

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), MainActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });

        logFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), NutritionTrackerAddFoodActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });

        databaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), NutritionDatabaseActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });

        foodList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Food currentFood = foodObjectList.get(position);

                Intent myIntent = new Intent(view.getContext(), ViewExerciseActivity.class);

                myIntent.putExtra("name", currentFood.getFoodName());
                myIntent.putExtra("mealTime", currentFood.getMealTime());
                myIntent.putExtra("date", currentFood.getDate());
                myIntent.putExtra("calories", currentFood.getCalories());


                startActivityForResult(myIntent, 0);
            }
        });
    }


//    private class StableArrayAdapter extends ArrayAdapter<String> {
//
//        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();
//
//        public StableArrayAdapter(Context context, int textViewResourceId,
//                                  List<String> objects) {
//            super(context, textViewResourceId, objects);
//
//            for (int i = 0; i < objects.size(); i++) {
//                mIdMap.put(objects.get(i), i);
//            }
//        }
//
//        @Override
//        public long getItemId(int position) {
//            if(mIdMap.size() > 0) {
//                String item = getItem(position);
//                return mIdMap.get(item);
//            } else {
//                return (long) 0;
//            }
//
//        }
//
//        @Override
//        public boolean hasStableIds() {
//            return true;
//        }
//
//    }


    public void initializeFoodList() {

        try {
            String fileName = "storage.json";
            FileInputStream fis = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            JSONArray users = new JSONArray();
            String line;

            if ((line = bufferedReader.readLine()) != null) {
                users = new JSONArray(line);
            }

            for (int i = 0; i < users.length(); i++) {
                JSONObject currUser = new JSONObject(users.getString(i));
                String username = currUser.getString("username");

                if (username.equals(Session.username)) {
                    JSONArray foods = new JSONArray(currUser.getString("foods"));

                    for (int j = 0; j < foods.length(); j++) {
                        JSONObject currFood = new JSONObject(foods.getString(j));
                        Food food = new Food (currFood.getString("foodName"), currFood.getString("mealTime"),
                                currFood.getString("date"), currFood.getString("calories"));
                        foodObjectList.add(food);
                        foodArrayList.add(food.toString());
                    }

                }
            }

            fis.close();

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addFood(Food newFood) {
        try {
            String fileName = "storage.json";
            FileInputStream fis = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            JSONArray users = new JSONArray();
            JSONArray updatedUsers = new JSONArray();
            String line;

            if ((line = bufferedReader.readLine()) != null) {
                users = new JSONArray(line);
            }

            for (int i = 0; i < users.length(); i++) {
                JSONObject currUser = new JSONObject(users.getString(i));
                String username = currUser.getString("username");

                if (username.equals(Session.username)) {
                    JSONArray foods = new JSONArray(currUser.getString("foods"));

                    foods.put(newFood.toJSONString());

                    foodObjectList.add(newFood);
                    foodArrayList.add(newFood.toString());

                    int cals = parseInt((String) calorieCounter.getText());
                    int newCals = parseInt(newFood.getCalories());

                    cals += newCals;
                    calorieCounter.setText(String.valueOf(cals));


                    currUser.put("foods", foods);

                    updatedUsers.put(currUser.toString());
                } else {
                    updatedUsers.put(currUser.toString());
                }
            }

            fis.close();

            FileOutputStream fos = context.openFileOutput(fileName,Context.MODE_PRIVATE);

            fos.write(updatedUsers.toString().getBytes());

            fos.close();

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
package com.example.a29751.finalproject;
/**
 * Date: 2017-10-31
 * Finished by Du Liangliang
 * This is about Food Nutrition information tracker
 */
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class FoodNutrition extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_nutrition);

        TextView fName = (TextView)findViewById(R.id.foodName);

    }
}

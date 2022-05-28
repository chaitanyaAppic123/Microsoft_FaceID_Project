package com.microsoft.facerecognition;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewDataActivity extends AppCompatActivity {

    TextView tvClear, tvNoData;
    ImageView ivBack;
    RecyclerView rvStudData;
    List<String> studentList;

    int OUTPUT_SIZE=192; //Output size of model
    StudentDataAdapter studentDataAdapter;

    private HashMap<String, SimilarityClassifier.Recognition> registered = new HashMap<>(); //saved Faces

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registered=readFromSP(); //Load saved faces from memory when app starts
        setContentView(R.layout.activity_database);
        tvClear = findViewById(R.id.tv_clear_data);
        ivBack = findViewById(R.id.iv_arrow);
        rvStudData = findViewById(R.id.rv_student_data);
        tvNoData = findViewById(R.id.tv_no_data);
        studentList = new ArrayList<>();
        displaynameListview();

        tvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearnameList();
            }
        });

    }


    private void displaynameListview()
    {
        if(registered.isEmpty()) {
            rvStudData.setVisibility(View.GONE);
            tvNoData.setVisibility(View.VISIBLE);
        }else{
            rvStudData.setVisibility(View.VISIBLE);
            tvNoData.setVisibility(View.GONE);
            }


        // add a checkbox list
        String[] names= new String[registered.size()];
        boolean[] checkedItems = new boolean[registered.size()];
        int i=0;
        for (Map.Entry<String, SimilarityClassifier.Recognition> entry : registered.entrySet())
        {
            names[i]=entry.getKey();
            checkedItems[i]=false;
            i=i+1;
        }

        studentList = new ArrayList<String>(Arrays.asList(names));

        studentDataAdapter = new StudentDataAdapter(ViewDataActivity.this,studentList);
        rvStudData.setAdapter(studentDataAdapter);
    }


    private  void clearnameList()
    {
        AlertDialog.Builder builder =new AlertDialog.Builder(ViewDataActivity.this);
        builder.setTitle("Do you want to delete all Recognitions?");
        builder.setPositiveButton("Delete All", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                registered.clear();
                insertToSP(registered,1);
                Toast.makeText(ViewDataActivity.this, "Recognitions Cleared", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ViewDataActivity.this, FirstActivity.class);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("Cancel",null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    //Save Faces to Shared Preferences.Conversion of Recognition objects to json string
    private void insertToSP(HashMap<String, SimilarityClassifier.Recognition> jsonMap, int mode) {
        if(mode==1)  //mode: 0:save all, 1:clear all, 2:update all
            jsonMap.clear();
        else if (mode==0)
            jsonMap.putAll(readFromSP());
        String jsonString = new Gson().toJson(jsonMap);
        SharedPreferences sharedPreferences = getSharedPreferences("HashMap", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("map", jsonString);
        //System.out.println("Input josn"+jsonString.toString());
        editor.apply();
        Toast.makeText(ViewDataActivity.this, "Recognitions Saved", Toast.LENGTH_SHORT).show();
    }

    //Load Faces from Shared Preferences.Json String to Recognition object
    private HashMap<String, SimilarityClassifier.Recognition> readFromSP(){
        SharedPreferences sharedPreferences = getSharedPreferences("HashMap", MODE_PRIVATE);
        String defValue = new Gson().toJson(new HashMap<String, SimilarityClassifier.Recognition>());
        String json=sharedPreferences.getString("map",defValue);
        TypeToken<HashMap<String,SimilarityClassifier.Recognition>> token = new TypeToken<HashMap<String,SimilarityClassifier.Recognition>>() {};
        HashMap<String,SimilarityClassifier.Recognition> retrievedMap=new Gson().fromJson(json,token.getType());

        //During type conversion and save/load procedure,format changes(eg float converted to double).
        //So embeddings need to be extracted from it in required format(eg.double to float).
        for (Map.Entry<String, SimilarityClassifier.Recognition> entry : retrievedMap.entrySet())
        {
            float[][] output=new float[1][OUTPUT_SIZE];
            ArrayList arrayList= (ArrayList) entry.getValue().getExtra();
            arrayList = (ArrayList) arrayList.get(0);
            for (int counter = 0; counter < arrayList.size(); counter++) {
                output[0][counter]= ((Double) arrayList.get(counter)).floatValue();
            }
            entry.getValue().setExtra(output);

            //System.out.println("Entry output "+entry.getKey()+" "+entry.getValue().getExtra() );

        }
        return retrievedMap;
    }
}

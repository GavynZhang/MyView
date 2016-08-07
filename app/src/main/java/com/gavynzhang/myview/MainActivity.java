package com.gavynzhang.myview;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gavynzhang.myview.model.Pie;

public class MainActivity extends AppCompatActivity{

    private Pie pies[] = new Pie[6];
    private PieView mPieView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPieView = (PieView)findViewById(R.id.pie_view);

        mPieView.setPies(pies);

        getData();


    }

    private void getData(){

        for (int i = 0; i < 6 ; i++){
            Pie pie = new Pie();
            pie.setColor(getResources().getColor(R.color.md_amber_400));
            pie.setDeepColor(getResources().getColor(R.color.md_light_green_A100));
            pie.setDepartNum(10);
            pie.setNumber(100+i*30);
            pies[i] = pie;
        }

    }

}

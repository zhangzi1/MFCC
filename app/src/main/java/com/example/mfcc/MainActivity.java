package com.example.mfcc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private int n=64;  // 点数
    private Complex[] x = new Complex[n];
    private Complex[] y_c = new Complex[n];
    private double[] y_mel = new double[21];
    private double[] y_ceps = new double[21];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化
        for (int i = 0; i < n; i++)
        {
            x[i] = new Complex(Math.cos(2*3.14159/n*i), 0);
        }

        // FFT变换
        y_c = MFCC.FFT(x);
        // Mel滤波器
        y_mel=MFCC.MelFilter(y_c);
        // 转倒谱
        y_ceps=MFCC.Cepstrum(y_mel);
    }
}

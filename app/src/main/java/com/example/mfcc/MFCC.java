package com.example.mfcc;

import android.util.Log;

public class MFCC
{
    public static Complex[] fft(Complex[] x)
    {
        int n = x.length;
        // base case
        if (n == 1) return new Complex[] { x[0] };
        // radix 2 Cooley-Tukey fft
        if (n % 2 != 0) {
            throw new IllegalArgumentException("n is not a power of 2");
        }
        // fft of even terms
        Complex[] even = new Complex[n/2];
        for (int k = 0; k < n/2; k++) {
            even[k] = x[2*k];
        }
        Complex[] q = fft(even);
        // fft of odd terms
        Complex[] odd  = even;  // reuse the array
        for (int k = 0; k < n/2; k++) {
            odd[k] = x[2*k + 1];
        }
        Complex[] r = fft(odd);
        // combine
        Complex[] y = new Complex[n];
        for (int k = 0; k < n/2; k++) {
            double kth = -2 * k * Math.PI / n;
            Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
            y[k]       = q[k].plus(wk.times(r[k]));
            y[k + n/2] = q[k].minus(wk.times(r[k]));
        }
        return y;
    }

    public static Complex[] FFT(Complex[] x)
    {
        Log.d("fft","---------------------------------");
        for (int i = 0; i < x.length; i++)
        {
            if(i<10)
                Log.d("Seq","n="+Integer.toString(i)+"   "+Double.toString(x[i].abs()));
            else
                Log.d("Seq","n="+Integer.toString(i)+"  "+Double.toString(x[i].abs()));
        }

        Complex[] y = new Complex[x.length];
        y=fft(x);

        Log.d("fft","---------------------------------");
        for (int i = 0; i <= x.length/2; i++)
        {
            if(i<10)
                Log.d("FFT","k="+Integer.toString(i)+"   "+Double.toString(y[i].abs()));
            else
                Log.d("FFT","k="+Integer.toString(i)+"  "+Double.toString(y[i].abs()));
        }
        return y;
    }

    /*
    Mel-filters amount: 21
    Mel-filter resolution: 100mel
    */
    public static double[] MelFilter(Complex[] x)
    {
        double[] mark=new double[21];
        for(int i=100;i<2200;i+=100)
            mark[i/100-1]=700*((Math.pow(10,(i/2595.0)))-1);

        double[] melfilter=new double[21];
        for(int i=0;i<21;i++)
            melfilter[i]=0;

        int n=x.length/2;
        for(int i=0;i<n;i++)  // 点循环
        {
            double f=i*8000/2/n;
            if(0<=f&&f<mark[0])
                melfilter[0]+=(f-0)/(mark[0]-0)*x[i].abs();
            for(int j=0;j<20;j++)
            {
                if(mark[j]<=f&&f<mark[j+1])
                {
                    melfilter[j]+=(mark[j+1]-f)/(mark[j+1]-mark[j])*x[i].abs();
                    melfilter[j+1]+=(f-mark[j])/(mark[j+1]-mark[j])*x[i].abs();
                }
            }
            if(mark[20]<=f&&f<4230)
                melfilter[20]+=(4230-f)/(4230-mark[20])*x[i].abs();
        }

        Log.d("Mel","--------------------------");
        for(int i=0;i<21;i++)
        {
            if(i<10)
                Log.d("Mel","k="+Integer.toString(i)+"   "+Double.toString(melfilter[i]));
            else
                Log.d("Mel","k="+Integer.toString(i)+"  "+Double.toString(melfilter[i]));
        }
        return melfilter;
    }

    public static double[] Cepstrum(double[] melfilter)
    {
        double[] y=new double[21];
        for(int i=0;i<21;i++)
        {
            y[i]=Math.log(melfilter[i])/Math.log(10);
        }

        double[] cepstrum=new double[21];

        for(int k=0;k<21;k++)
        {
            cepstrum[k]=0;
            for(int n=0;n<21;n++)
            {
                cepstrum[k]+=y[n]*Math.cos(3.14159/21*(n+0.5)*k);
            }
        }

        Log.d("Ceps","--------------------------");
        for(int i=0;i<21;i++)
        {
            if(i<10)
                Log.d("Ceps"," k="+Integer.toString(i)+"   "+Double.toString(cepstrum[i]));
            else
                Log.d("Ceps"," k="+Integer.toString(i)+"  "+Double.toString(cepstrum[i]));
        }
        return cepstrum;

    }
}

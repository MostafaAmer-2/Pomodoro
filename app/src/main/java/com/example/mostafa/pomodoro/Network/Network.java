package com.example.mostafa.pomodoro.Network;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Network {

    private RequestQueue requestQueue;

    public Network (Context ctx){
        requestQueue = Volley.newRequestQueue(ctx); // 'this' is the Context
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

}

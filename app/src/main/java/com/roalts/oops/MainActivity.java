package com.roalts.oops;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;


public class MainActivity extends ActionBarActivity {

    TextView ottpCode;
    EditText mobile, amount;
    FloatingActionButton fabSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ottpCode = (TextView) findViewById(R.id.ottpCode);
        fabSend = (FloatingActionButton) findViewById(R.id.fabSend);

        final int code = new Random().nextInt(9999);
        ottpCode.setText(String.valueOf(code));

        mobile = (EditText) findViewById(R.id.mobileNo);
        amount = (EditText) findViewById(R.id.amount);

        fabSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TokenSenderActivity.class);
                intent.putExtra("code",code);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void startSenderActivity(View view) {
        Intent intent = new Intent(this, TokenSenderActivity.class);
        startActivity(intent);
    }

    public void startReceiverActivity(View view) {
        Intent intent = new Intent(this, TokenReceiverActivity.class);
        startActivity(intent);
    }
}


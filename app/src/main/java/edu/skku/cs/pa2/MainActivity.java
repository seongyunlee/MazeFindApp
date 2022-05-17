package edu.skku.cs.pa2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;

import edu.skku.cs.pa2.databinding.ActivityMainBinding;
import edu.skku.cs.pa2.util.ConnectionUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    ConnectionUtil connectionUtil;
    ActivityMainBinding activityMainBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        activityMainBinding.executePendingBindings();
        connectionUtil = new ConnectionUtil();
    }
    public void onSignInButtonClicked(View v){
        signIn();
    }
    private void skipSignin(){
        Intent intent = new Intent(MainActivity.this,ListActivity.class);
        intent.putExtra("username","seongmin");
        startActivity(intent);
    }
    private void signIn(){

        String username = activityMainBinding.etUsername.getText().toString();
        connectionUtil.signIn(username, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("sign_in_fail",e.toString());
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,"Server Error",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                Log.d("sign_in_success",response.toString());
                String status= response.body().string();
                if(status.contains("true")){
                    Intent intent = new Intent(MainActivity.this,ListActivity.class);
                    intent.putExtra("username",username);
                    startActivity(intent);
                }
                else{
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this,"Wrong User Name",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }
}
package com.example.voicematch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private String voice_string;
    private Button signin;
    private Button signup;
    private ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = getSharedPreferences("VoiceMatchPref", Context.MODE_PRIVATE);
        int user_id = sharedPref.getInt("id", -1);

        if(user_id != -1) {
            User user = User.getUser(user_id);
            if(user == null) {
                Toast.makeText(this, "Data base error. Please try again later.", Toast.LENGTH_SHORT).show();
            }
            else if(user.getUser_id() == -1) {
                Toast.makeText(this, "Network error. Please try again later.", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(this, Welcome.class);
                intent.putExtra("user", user);
                startActivity(intent);
                finish();
            }
        }

        signin = (Button) findViewById(R.id.btn_sign_in);
        signup = (Button) findViewById(R.id.btn_sign_up);
        bar = (ProgressBar) findViewById(R.id.bar);
        bar.setVisibility(View.GONE);
        signin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    signin.setText("Recording");
                    Record.startRecording();
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    signin.setText("Sign In");
                    Record.stopRecording();
                    voice_string = Record.getVoiceString();
                    signIn();
                    return true;
                }
                return false;
            }
        });
    }

    private void signIn() {
        signin.setVisibility(View.GONE);
        signup.setVisibility(View.GONE);
        bar.setVisibility(View.VISIBLE);
        User user = User.signIn(voice_string);
        if(user == null) {
            Toast.makeText(this, "Sorry, we can not find your voice in the data base.", Toast.LENGTH_SHORT).show();
            signin.setVisibility(View.VISIBLE);
            signup.setVisibility(View.VISIBLE);
            bar.setVisibility(View.GONE);
        }
        else if(user.getUser_id() == -1) {
            Toast.makeText(this, "Network error. Please try again later.", Toast.LENGTH_SHORT).show();
            signin.setVisibility(View.VISIBLE);
            signup.setVisibility(View.VISIBLE);
            bar.setVisibility(View.GONE);
        }
        else {
            Toast.makeText(this, "Sign up success.", Toast.LENGTH_SHORT).show();
            SharedPreferences sharedPref = getSharedPreferences("VoiceMatchPref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("id", user.getUser_id());
            editor.commit();
            Intent intent = new Intent(this, Welcome.class);
            intent.putExtra("user", user);
            startActivity(intent);
            finish();
        }
    }

    public void signUp(View view) {
        Intent intent = new Intent(this, UserProfile.class);
        intent.putExtra("type", "create");
        startActivity(intent);
    }
}

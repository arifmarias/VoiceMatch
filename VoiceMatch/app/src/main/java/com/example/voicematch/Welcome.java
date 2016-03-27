package com.example.voicematch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Welcome extends AppCompatActivity {
    private User user;
    private ImageView avatar;
    private TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        avatar = (ImageView) findViewById(R.id.img_welcome_avatar);
        name = (TextView) findViewById(R.id.txt_welcome_name);

        user = (User) getIntent().getParcelableExtra("user");

        byte[] avatarimage = user.getUser_avatar();
        if (avatarimage != null) {
            Bitmap bm = BitmapFactory.decodeByteArray(avatarimage, 0, avatarimage.length);
            avatar.setImageBitmap(bm);
        }
        else {
            avatar.setImageResource(R.drawable.default_avatar);
        }
        name.setText(user.getUser_name());

        this.setTitle("Welcome, " + user.getUser_name() + "!");
    }

    public void updateProfile(View view) {
        Intent intent = new Intent(this, UserProfile.class);
        intent.putExtra("user", user);
        intent.putExtra("type", "update");
        startActivity(intent);
    }

    public void voiceMatch(View view) {
        Intent intent = new Intent(this, VoiceMatch.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    public void logOut(View view) {
        SharedPreferences sharedPref = getSharedPreferences("VoiceMatchPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.commit();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}

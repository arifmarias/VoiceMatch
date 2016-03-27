package com.example.voicematch;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class VoiceMatch extends AppCompatActivity {
    private LinearLayout matching;
    private LinearLayout matched;
    private ImageView avatar;
    private TextView name;
    private TextView similarity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_match);
        this.setTitle("Match Your Voice");

        matching = (LinearLayout) findViewById(R.id.layout_matching);
        matched = (LinearLayout) findViewById(R.id.layout_matched);
        matched.setVisibility(View.GONE);

        avatar = (ImageView) findViewById(R.id.img_matched_avatar);
        name = (TextView) findViewById(R.id.txt_matched_name);
        similarity = (TextView) findViewById(R.id.txt_matched_similarity);

        User user = (User) getIntent().getParcelableExtra("user");
        User matched_user = User.matchUser(user);

        if(matched_user == null) {
            Toast.makeText(this, "Sorry, there is no match of your voice in the data base.", Toast.LENGTH_SHORT).show();
            finish();
        }
        else if(matched_user.getUser_id() == -1) {
            Toast.makeText(this, "Network error. Please try again later.", Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            matching.setVisibility(View.GONE);
            matched.setVisibility(View.VISIBLE);
            byte[] avatarimage = matched_user.getUser_avatar();
            if (avatarimage != null) {
                Bitmap bm = BitmapFactory.decodeByteArray(avatarimage, 0, avatarimage.length);
                avatar.setImageBitmap(bm);
            }
            else {
                avatar.setImageResource(R.drawable.default_avatar);
            }
            name.setText(matched_user.getUser_name());
            similarity.setText(matched_user.getUser_voice());
        }
    }

    public void jumpBack(View view) {
        finish();
    }
}

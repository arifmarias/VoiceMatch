package com.example.voicematch;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class UserProfile extends AppCompatActivity {
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String type;
    private User user;
    private Button record;
    private Button confirm;
    private ImageView avatar;
    private TextView name;
    private byte[] avatarimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        record = (Button) findViewById(R.id.btn_record);
        confirm = (Button) findViewById(R.id.btn_profile_confirm);
        avatar = (ImageView) findViewById(R.id.img_profile_avatar);
        name = (TextView) findViewById(R.id.txt_profile_name);

        type = getIntent().getStringExtra("type");
        if(type.equals("create")) {
            this.setTitle("Sign Up");
            confirm.setText("Sign Up");

            user = new User(-1, null, null, null);
            avatar.setImageResource(R.drawable.default_avatar);
        }
        else if(type.equals("update")){
            this.setTitle("Update Profile");
            confirm.setText("Update");

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
        }

        record.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    record.setText("Recording");
                    Record.startRecording();
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    record.setText("Record Voice");
                    Record.stopRecording();
                    user.setUser_voice(Record.getVoiceString());
                    return true;
                }
                return false;
            }
        });
    }

    public void changeAvatar(View view) {
        final CharSequence[] items = { "Take photo", "Choose from gallary", "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfile.this);
        builder.setTitle("Add Avatar");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                }
                else if (items[item].equals("Choose from gallary")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap bm = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
        avatarimage = stream.toByteArray();
        avatar.setImageBitmap(bm);
        user.setUser_avatar(avatarimage);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        try {
            Uri selectedImage = data.getData();
            InputStream imageStream = getContentResolver().openInputStream(selectedImage);
            Bitmap bm = BitmapFactory.decodeStream(imageStream);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
            avatarimage = stream.toByteArray();
            avatar.setImageBitmap(bm);
            user.setUser_avatar(avatarimage);
        } catch (FileNotFoundException e) {
            Toast.makeText(this, "File error. Please try again later.", Toast.LENGTH_SHORT).show();
        }
    }

    public void jumpBack(View view) {
        finish();
    }

    public void confirmProfile(View view) {
        user.setUser_name(name.getText().toString());
        if(user.getUser_name() == null || user.getUser_name().equals("")) {
            Toast.makeText(this, "User name can not be empty.", Toast.LENGTH_SHORT).show();
        }
        else if(user.getUser_voice() == null || user.getUser_voice().equals("")) {
            Toast.makeText(this, "Please add your voice.", Toast.LENGTH_SHORT).show();
        }
        else {
            if(type.equals("create")) {
                int user_id = User.addUser(user);
                if(user_id == -1) {
                    Toast.makeText(this, "Network error. Please try again later.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this, "Sign up success.", Toast.LENGTH_SHORT).show();
                    SharedPreferences sharedPref = getSharedPreferences("VoiceMatchPref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt("user_id", user_id);
                    editor.commit();
                    user.setUser_id(user_id);
                    Intent intent = new Intent(this, Welcome.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                    finish();
                }
            }
            else if(type.equals("update")) {
                int status = User.updateUser(user);
                if(status == -1) {
                    Toast.makeText(this, "Network error. Please try again later.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this, "Update success.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, Welcome.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                    finish();
                }
            }
        }
    }
}

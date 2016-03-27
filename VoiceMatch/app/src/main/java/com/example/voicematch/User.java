package com.example.voicematch;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zijin on 26/03/16.
 */
public class User  implements Parcelable{
    private int user_id;
    private String user_name;
    private byte[] user_avatar;
    private String user_voice;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public byte[] getUser_avatar() {
        return user_avatar;
    }

    public void setUser_avatar(byte[] user_avatar) {
        this.user_avatar = user_avatar;
    }

    public String getUser_voice() {
        return user_voice;
    }

    public void setUser_voice(String user_voice) {
        this.user_voice = user_voice;
    }

    public User() {
    }

    public User(int user_id, String user_name, byte[] user_avatar, String user_voice) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_avatar = user_avatar;
        this.user_voice = user_voice;
    }

    public User(Parcel in){
        this.user_id = in.readInt();
        this.user_name = in.readString();
        this.user_voice = in.readString();
        int avatarlength = in.readInt();
        if(avatarlength == 0) {
            this.user_avatar = null;
        }
        else {
            this.user_avatar = new byte[avatarlength];
            in.readByteArray(this.user_avatar);
        }
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.user_id);
        dest.writeString(this.user_name);
        dest.writeString(this.user_voice);
        if(this.user_avatar == null) {
            dest.writeInt(0);
        }
        else {
            dest.writeInt(this.user_avatar.length);
            dest.writeByteArray(this.user_avatar);
        }
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    /*
    return -1 if network error
    else return the inserted user's id
     */
    public static int addUser(User user) {
        return 0;
    }

    /*
    return -1 if network error
     */
    public static int updateUser(User user) {
        return 0;
    }

    /*
    return null if no match
    return user_id = -1 if network error
     */
    public static User getUser(int user_id) {
        return new User(1, "John Lee", null, "");
    }

    /*
    return null if no match
    return user.user_id = -1 if network error
     */
    public static User signIn(String voice_string) {
        return new User(99, "John Lee", null, "");
    }

    /*
    return null if no match
    return user.user_id = -1 if network error

    if matched successfully, set user_voice = similarity
     */
    public static User matchUser(User user) {
        return new User(2, "James Bill", null, "89%");
    }
}

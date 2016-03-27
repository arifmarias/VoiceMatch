package com.example.voicematch;

import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.IOException;

/**
 * Created by zijin on 26/03/16.
 */
public class Record {
    public static final String LOG_TAG = "AudioRecordTest";
    public static String mFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "VoiceMatch.mp4";
    public static MediaRecorder mRecorder = null;

    public static void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    public static void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    /*
    get the voice string from the mp4 file
     */
    public static String getVoiceString() {
        return "asdfadsf";
    }
}

package com.aserbao.androidcustomcamera.blocks.MediaExtractor;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.aserbao.androidcustomcamera.R;
import com.aserbao.androidcustomcamera.base.interfaces.IDetailCallBackListener;
import com.aserbao.androidcustomcamera.blocks.MediaExtractor.combineTwoVideo.CombineTwoVideos;
import com.aserbao.androidcustomcamera.blocks.MediaExtractor.primary.DecoderAudio;
import com.aserbao.androidcustomcamera.blocks.MediaExtractor.primary.EncoderAudioAAC;
import com.aserbao.androidcustomcamera.blocks.MediaExtractor.primary.TransAacHandlerPure;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.media.MediaFormat.MIMETYPE_AUDIO_AAC;
import static android.media.MediaFormat.MIMETYPE_AUDIO_MPEG;

public class MediaExtractorActivity extends AppCompatActivity implements IDetailCallBackListener {
    private static final String TAG = "MediaExtractorActivity";
    @BindView(R.id.record_and_encoder_mp3)
    Button mRecordAndEncoderMp3;
    @BindView(R.id.record_mp3_stop)
    Button mRecordMp3Stop;
    private DecoderAudio decoderAAC;
    private EncoderAudioAAC encoderAudioAAC;
    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_extractor);
        ButterKnife.bind(this);
    }

    String path = Environment.getExternalStorageDirectory().getAbsolutePath();

    @OnClick({R.id.extractor_video_and_audio, R.id.exchange_video_and_audio, R.id.decoder_aac_and_player, R.id.decoder_mp3_and_player,
            R.id.record_and_encoder_mp3, R.id.record_mp3_stop,R.id.mp3_translate_aac_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.extractor_video_and_audio:
                break;
            case R.id.exchange_video_and_audio:
                CombineTwoVideos.combineTwoVideos(path + "/cai.mp4", 0, path + "/lan.mp4", new File(path + "/aserbao.mp3"), this);
                break;
            case R.id.decoder_aac_and_player:
                String audioPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/aac.aac";
                decoderAAC = new DecoderAudio();
                decoderAAC.start(audioPath, MIMETYPE_AUDIO_AAC);
                break;
            case R.id.decoder_mp3_and_player:
                String audioMp3Path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/five.mp3";
                decoderAAC = new DecoderAudio();
                decoderAAC.start(audioMp3Path, MIMETYPE_AUDIO_MPEG);
                break;
            case R.id.record_and_encoder_mp3:
                mRecordMp3Stop.setVisibility(View.VISIBLE);
                mRecordAndEncoderMp3.setVisibility(View.GONE);
                String encoderAACPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/encoder_aac.aac";
                if (encoderAudioAAC == null) {
                    encoderAudioAAC = new EncoderAudioAAC();
                }
                encoderAudioAAC.start(encoderAACPath);
                break;
            case R.id.record_mp3_stop:
                mRecordAndEncoderMp3.setVisibility(View.VISIBLE);
                mRecordMp3Stop.setVisibility(View.GONE);
                if (encoderAudioAAC != null) {
                    encoderAudioAAC.stop();
                    encoderAudioAAC = null;
                }
                break;
            case R.id.mp3_translate_aac_btn:
                TransAacHandlerPure aacHandlerPure = new TransAacHandlerPure(path + "/bell.mp3", path + "/codec.aac");
                aacHandlerPure.setListener(new TransAacHandlerPure.OnProgressListener() {
                    @Override
                    public void onStart() {
                        startTime = System.currentTimeMillis();
                        Log.e(TAG, "onStart: " + startTime);
                    }

                    @Override
                    public void onProgress(int max, int progress) {
                        Log.e(TAG, "onProgress: " );
                    }

                    @Override
                    public void onSuccess() {
                        float v =  (System.currentTimeMillis() - startTime) / (float)1000;
                        Log.d(TAG, "onSuccess() called 一共耗时 ： " + v + "s");// 10s的mp3转aac差不多2.5s
                    }

                    @Override
                    public void onFail() {
                        Log.d(TAG, "onFail() called");
                    }
                });
                aacHandlerPure.start();
                break;
        }
    }

    @Override
    public void success() {
        Toast.makeText(MediaExtractorActivity.this, "成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void failed(Exception e) {
        Toast.makeText(MediaExtractorActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
    }
}
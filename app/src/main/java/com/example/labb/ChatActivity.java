package com.example.labb;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.labb.databinding.ActivityChatBinding;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;

public class ChatActivity extends AppCompatActivity {
    EditText code;
    Button join;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        code=findViewById(R.id.roomCode);
        join=findViewById(R.id.join);

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (code.getText().toString().isEmpty()){
                    Toast.makeText(ChatActivity.this, "Introduce un Codigo",Toast.LENGTH_SHORT).show();
                }else {
                    try {
                        JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                                .setServerURL(new URL("https://meet.jit.si"))
                                .setRoom(code.getText().toString())
                                .setAudioMuted(false)
                                .setVideoMuted(false)
                                .setAudioOnly(false)
                                .setConfigOverride("requireDisplayName", false)
                                .build();
                        JitsiMeetActivity.launch(ChatActivity.this, options);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });



    }
}
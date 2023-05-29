package com.example.labb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.labb.databinding.ActivityChatBinding
import org.jitsi.meet.sdk.JitsiMeet
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import java.net.MalformedURLException
import java.net.URL

class LlamadaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_chat)

        super.onCreate(savedInstanceState)
        val serverUrl: URL
        try {
            serverUrl = URL("https://meet.jit.si")
            val options = JitsiMeetConferenceOptions.Builder()
                .setServerURL(serverUrl)
                .build();
            JitsiMeet.setDefaultConferenceOptions(options)
        }
        catch (e: MalformedURLException)
        {
            Toast.makeText(applicationContext, "Ocurrio un error", Toast.LENGTH_SHORT).show()
        }

        binding.join.setOnClickListener{
            val options = JitsiMeetConferenceOptions.Builder()
                .setRoom(binding.roomCode.text.toString())
                .build();
            JitsiMeetActivity.launch(this, options)
        }
    }
}
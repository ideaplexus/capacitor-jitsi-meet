package com.capacitor.jitsi.plugin;

//import java.net.MalformedURLException;
//import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
//import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.react.bridge.UiThreadUtil;
//import com.facebook.react.modules.core.PermissionListener;

import org.jitsi.meet.sdk.JitsiMeetView;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetViewListener;

public class JitsiActivity extends JitsiMeetActivity {
    private JitsiMeetView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = new JitsiMeetView(this);
        Log.d("Listener", "entering");
        view.setListener(new JitsiMeetViewListener() {
            private void on(String name, Map<String, Object> data) {
                UiThreadUtil.assertOnUiThread();

                // Log with the tag "ReactNative" in order to have the log
                // visible in react-native log-android as well.
                Log.d(
                        "Listener",
                        JitsiMeetViewListener.class.getSimpleName() + " "
                                + name + " "
                                + data);
                Intent intent = new Intent(name);
                intent.putExtra("eventName", name);
                sendBroadcast(intent);
            }

            @Override
            public void onConferenceFailed(Map<String, Object> data) {
                view.dispose();
                view = null;
                finish();
                on("onConferenceFailed", data);
            }

            @Override
            public void onConferenceJoined(Map<String, Object> data) {
                on("onConferenceJoined", data);
            }

            @Override
            public void onConferenceLeft(Map<String, Object> data) {
                view.dispose();
                view = null;
                finish();
                on("onConferenceLeft", data);
            }

            @Override
            public void onConferenceWillJoin(Map<String, Object> data) {
                on("onConferenceWillJoin", data);
            }

            @Override
            public void onConferenceWillLeave(Map<String, Object> data) {
                on("onConferenceWillLeave", data);
            }

            @Override
            public void onLoadConfigError(Map<String, Object> data) {
                on("onLoadConfigError", data);
            }
        });

        view.setWelcomePageEnabled(false);
        String url = getIntent().getStringExtra("url");
        String roomName = getIntent().getStringExtra("roomName");
        String fullurl = url + '/' + roomName;
        Integer channelLastN = Integer.parseInt(getIntent().getStringExtra("channelLastN"));
        Boolean startWithAudioMuted = getIntent().getBooleanExtra("startWithAudioMuted", false);
        Boolean startWithVideoMuted = getIntent().getBooleanExtra("startWithVideoMuted", false);
        Log.d("DEBUG", fullurl);

        Bundle config = new Bundle();
        config.putBoolean("startWithAudioMuted", startWithAudioMuted);
        config.putBoolean("startWithVideoMuted", startWithVideoMuted);
        config.putString("googleApiApplicationClientID", "896030655830-pveqh7f6cj8af3p10qh2rhokoqnsapcj.apps.googleusercontent.com");
        config.putInt("channelLastN", channelLastN);
        config.putString("callDisplayName", " ");
        Bundle urlObject = new Bundle();
        urlObject.putBundle("config", config);
        urlObject.putString("url", fullurl);
        view.loadURLObject(urlObject);
        setContentView(view);
    }

    /**
     * The query to perform through {@link AddPeopleController} when the
     * {@code InviteButton} is tapped in order to exercise the public API of the
     * feature invite. If {@code null}, the {@code InviteButton} will not be
     * rendered.
     */
    private static final String ADD_PEOPLE_CONTROLLER_QUERY = null;
}


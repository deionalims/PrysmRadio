package com.prysmradio.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.facebook.widget.FacebookDialog;
import com.google.gson.Gson;
import com.prysmradio.PrysmApplication;
import com.prysmradio.R;
import com.prysmradio.bus.events.BusManager;
import com.prysmradio.bus.events.UpdateCoverEvent;
import com.prysmradio.bus.events.UpdatePlayerEvent;
import com.prysmradio.bus.events.UpdatePodcastTitleEvent;
import com.prysmradio.fragments.BottomPlayerFragment;
import com.prysmradio.utils.Constants;
import com.prysmradio.utils.CurrentStreamInfo;
import com.prysmradio.utils.Utils;
import com.squareup.otto.Subscribe;

import java.util.List;

/**
 * Created by fxoxe_000 on 03/05/2014.
 */
public class PrysmActivity extends BaseActivity {

    protected BottomPlayerFragment bottomPlayerFragment;

    @Subscribe
    public void onUpdatePlayerEventReceived(UpdatePlayerEvent event){
        if (event.isBuffering()){
            ((PrysmApplication) getApplicationContext()).setServiceIsRunning(true);
            setProgressBarIndeterminateVisibility(true);
        } else if (event.isPlaying()){
            ((PrysmApplication) getApplicationContext()).setServiceIsRunning(true);
            setProgressBarIndeterminateVisibility(false);
        } else {
            setProgressBarIndeterminateVisibility(false);
            ((PrysmApplication) getApplicationContext()).setServiceIsRunning(false);
            BusManager.getInstance().getBus().post(new UpdateCoverEvent(null));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        BusManager.getInstance().getBus().register(this);
        bottomPlayerFragment.setPlayPauseImageView(((PrysmApplication) getApplicationContext()).isServiceIsRunning());
    }

    @Override
    public void onPause() {
        super.onPause();
        BusManager.getInstance().getBus().unregister(this);
    }

    public BottomPlayerFragment getBottomPlayerFragment() {
        return bottomPlayerFragment;
    }

    public void startStopAudioService() {
        if (!((PrysmApplication) getApplicationContext()).isServiceIsRunning()){
            ((PrysmApplication) getApplicationContext()).setServiceIsRunning(true);

            if (CurrentStreamInfo.getInstance().getCurrentRadio() != null){
                Intent intent = new Intent(Constants.START_RADIO_SERVICE_ACTION);
                intent.putExtra(Constants.AUDIO_URL_EXTRA, CurrentStreamInfo.getInstance().getCurrentRadio().getAACStreamURL());
                startService(new Intent(intent));
            } else if (CurrentStreamInfo.getInstance().getPodcastEpisode() != null){
                Intent intent = new Intent(Constants.START_PODCAST_SERVICE_ACTION);
                intent.putExtra(Constants.AUDIO_URL_EXTRA, CurrentStreamInfo.getInstance().getPodcastEpisode().getAudioUrl());
                BusManager.getInstance().getBus().post(new UpdatePodcastTitleEvent(CurrentStreamInfo.getInstance().getPodcastEpisode().getTitle(), CurrentStreamInfo.getInstance().getPodcastEpisode().getSubtitle()));
                startService(new Intent(intent));
            }
        } else {
            ((PrysmApplication) getApplicationContext()).setServiceIsRunning(false);
            if (CurrentStreamInfo.getInstance().getCurrentRadio() != null){
                startService(new Intent(Constants.STOP_RADIO_SERVICE_ACTION));
                saveCurrentRadio();
            } else if (CurrentStreamInfo.getInstance().getPodcastEpisode() != null){
                startService(new Intent(Constants.STOP_PODCAST_SERVICE_ACTION));
                saveCurrentPodcastEdisode();
            }
        }
    }

    private void saveCurrentRadio(){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = preferences.edit();

        edit.remove(Constants.PODCAST_EPISODE_PREF);
        Gson gson = new Gson();
        String json = gson.toJson(CurrentStreamInfo.getInstance().getCurrentRadio());
        edit.putString(Constants.RADIO_PREF, json);

        edit.commit();
    }

    private void saveCurrentPodcastEdisode(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = preferences.edit();

        edit.remove(Constants.RADIO_PREF);
        Gson gson = new Gson();
        String json = gson.toJson(CurrentStreamInfo.getInstance().getPodcastEpisode());
        edit.putString(Constants.PODCAST_EPISODE_PREF, json);

        edit.commit();
    }

    protected void shareFacebookPlayingRadio(){
            FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(this)
                    .setLink(getString(R.string.facebook_share_link))
                    .setApplicationName("Prysm Radio France")
                    .setDescription(getString(R.string.facebook_share_description))
                    .setName(String.format(getString(R.string.facebook_share_name), CurrentStreamInfo.getInstance().getCurrentRadio().getArtist(), CurrentStreamInfo.getInstance().getCurrentRadio().getTitle()))
                    .setPicture(CurrentStreamInfo.getInstance().getCurrentRadio().getSongCover())
                    .build();
            uiHelper.trackPendingDialogCall(shareDialog.present());
    }

    protected void shareFacebookNonPlayingRadio(){
        FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(this)
                .setLink(getString(R.string.facebook_share_link))
                .setApplicationName("Prysm Radio France")
                .setDescription(getString(R.string.facebook_share_description))
                .build();
        uiHelper.trackPendingDialogCall(shareDialog.present());
    }

    protected void shareTwitterListeningRadio(){

            String tweetUrl =
                    String.format("https://twitter.com/intent/tweet?text=%s&url=%s",
                            Utils.urlEncode(getString(R.string.twitter_share_text)), Utils.urlEncode(getString(R.string.twitter_share_url)));
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(tweetUrl));

            List<ResolveInfo> matches = getPackageManager().queryIntentActivities(intent, 0);
            for (ResolveInfo info : matches) {
                if (info.activityInfo.packageName.toLowerCase().startsWith("com.twitter")) {
                    intent.setPackage(info.activityInfo.packageName);
                }
            }

            startActivity(intent);
    }
}

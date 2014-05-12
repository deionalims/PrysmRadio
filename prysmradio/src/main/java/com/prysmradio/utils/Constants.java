package com.prysmradio.utils;


public class Constants {

    // Playing Notification ID
    public static final int NOTIFICATION_ID = 0xC0DE;

    // Media Service Actions
    public static final String START_RADIO_SERVICE_ACTION = "com.prysmradio.radio.service_start";
    public static final String STOP_RADIO_SERVICE_ACTION = "com.prysmradio.radio.service_stop";

    public static final String START_PODCAST_SERVICE_ACTION = "com.prysmradio.podcast.service_start";
    public static final String STOP_PODCAST_SERVICE_ACTION = "com.prysmradio.podcast.service_stop";
    public static final String SEEK_PODCAST_SERVICE_ACTION = "com.prysmradio.podcast.service_seek";

    // REQUEST CODES
    public static final int METADATA_REQUEST_CODE = 1;

    // EXTRAS
    public static final String AUDIO_URL_EXTRA = "audio_url_extra";
    public static final String PODCAST_TITLE_EXTRA = "podcast_title_extra";
    public static final String EPISODE_TITLE_EXTRA = "episode_title_extra";
    public static final String EPISODE_EXTRA = "episode_extra";
    public static final String LOADING_EXTRA = "loading_extra";
    public static final String SEEK_TO_EXTRA = "seek_to_extra";
    public static final String STREAM_ARTIST_EXTRA = "stream_artist_extra";
    public static final String STREAM_TITLE_EXTRA = "stream_title_extra";
    public static final String RADIO_EXTRA = "radio_extra";
    public static final String NEWS_EXTRA = "news_extra";

    // PREFERENCES
    public static final String STREAM_INFO_PREFS = "stream_info_prefs";
    public static final String RADIO_PREF = "radio_pref";
    public static final String PODCAST_EPISODE_PREF = "podcase_episode_pref";
}

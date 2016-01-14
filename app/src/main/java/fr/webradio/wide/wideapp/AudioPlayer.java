package fr.webradio.wide.wideapp;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

/**
 * Created by dubois on 30/11/15.
 */
public class AudioPlayer implements AudioManager.OnAudioFocusChangeListener, MediaPlayer.OnPreparedListener {
    private MediaPlayer mediaPlayer;
    private static AudioPlayer audioPlayer; //On souhaite un seul audioPlayer pour l'application, quoi qu'il arrive

    private Uri lastUri;
    private MediaPlayer.OnCompletionListener onCompletionlistener;
    private Context context;

    public void setOnCompletionlistener(MediaPlayer.OnCompletionListener onCompletionlistener) {
        this.onCompletionlistener = onCompletionlistener;

        if (mediaPlayer != null) mediaPlayer.setOnCompletionListener(onCompletionlistener);
    }

    public static AudioPlayer get(Context context){
        if (audioPlayer == null) {
            audioPlayer = new AudioPlayer(context);
        }

        // On demande à gérer les évènements liés au son
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int result = audioManager.requestAudioFocus(audioPlayer, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);

        if (result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            release();
            // could not get audio focus.
        }

        return audioPlayer;
    }

    public static void release(){
        if (audioPlayer.mediaPlayer == null) return; // déjà libéré

        audioPlayer.mediaPlayer.release(); // libère le mediaplayer
        audioPlayer.mediaPlayer = null;  // Précise que le mediaplayer a été libéré
    }

    private AudioPlayer(Context context) {
        this.context = context;
        init();
    }

    private void init() {
    /*    if (mediaPlayer != null) return; // On a déjà un mediaplayer, ne rien faire

        this.mediaPlayer = new MediaPlayer();
        this.mediaPlayer.setOnPreparedListener(this);
        this.mediaPlayer.selectTrack(AudioManager.STREAM_MUSIC);*/

    }

    /***
     * Charge un fichier son.
     * @param context Activité à partir de laquelle on lance le son
     * @param uri chemin vers le fichier son
     */
    public void loadUri(Context context, Uri uri) {
        if (uri != null) {
            this.context = context;
            lastUri = uri;
        }
        load();
        if (onCompletionlistener != null)
            this.mediaPlayer.setOnCompletionListener(onCompletionlistener);
    }

    private void load() {
        if (lastUri == null) return;
        //init(); // On s'assure la présence d'un mediaPlayer
        // Si on joue déjà qq chose

        if (mediaPlayer !=null ){
            if ( mediaPlayer.isPlaying()) mediaPlayer.stop(); // On abandonne le morceau en cours
            mediaPlayer.reset();
        }




        mediaPlayer = MediaPlayer.create(context,lastUri);

        // On sélectionne le nouveau morceau
        // mediaPlayer.setDataSource(lastContext, lastUri);
        // Il faudra ensuite appeler prepare (puis play, mais ce sera automatique)
    }

    // Gestion
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                // resume playback
                if (mediaPlayer == null) init(); // Si le mediaPlayer n'est pas configuré, on le configure
                else if (!mediaPlayer.isPlaying()) mediaPlayer.start(); // Sinon on reprend où on en était
                mediaPlayer.setVolume(1.0f, 1.0f); // On remet le volume à 100%
                break;

            case AudioManager.AUDIOFOCUS_LOSS:
                // Lost focus for an unbounded amount of time: stop playback and release media player
                if (mediaPlayer == null) break; // Déjà libéré, ne rien faire
                if (mediaPlayer.isPlaying()) mediaPlayer.stop(); // On arrête la musique en cours
                release();
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                // Lost focus for a short time, but we have to stop
                // playback. We don't release the media player because playback
                // is likely to resume
                if (mediaPlayer.isPlaying()) mediaPlayer.pause();
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                // Lost focus for a short time, but it's ok to keep playing
                // at an attenuated level
                if (mediaPlayer.isPlaying()) mediaPlayer.setVolume(0.1f, 0.1f);
                break;
        }
    }

    /***
     * Commence à jouer le morceau en cours.
     */
    public void play() {
        load(); // On (re)charge le dernier morceau
        mediaPlayer.start(); // On joue systématiquement dès que l'on est prêt.
    //    mediaPlayer.prepareAsync(); // On débute le chargement. onPrepared sera appelé ensuite
    }

    public void stop() {
        mediaPlayer.stop();
    }

    public void pause() {
        mediaPlayer.pause();
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
 //       mediaPlayer.start(); // On joue systématiquement dès que l'on est prêt.
    }


    public void loadId(Context context, int id) {
        audioPlayer.loadUri(context, Uri.parse("android.resource://" + context.getPackageName() + "/" + id));

    }
    public void loadId(Context context, String id) {
        audioPlayer.loadUri(context, Uri.parse("android.resource://" + context.getPackageName() + "/raw/" + id));

    }
}
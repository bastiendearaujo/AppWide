package fr.webradio.wide.wideapp;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.PowerManager;

public class MusicService extends Service implements MediaPlayer.OnCompletionListener {
    private  AudioPlayer audioPlayer;
    private String title;

    public static final String COMMAND = "command";
    public static final String START = "start";
    public static final String STOP = "stop";
    public static final String TITLE = "title";
    public static final String MUSIQUE = "musique";
    private PowerManager.WakeLock wakeLock;
    private PendingIntent pi;
    private int position;

    public  void createNotification(){
// Crée un intenta permettant de lancer l'activité quand on clique sur la notification
        if (pi== null) pi = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(getApplicationContext())
                .setContentTitle("Playing ") //Ces 3 champs
                .setContentText(title)           // sont
                .setSmallIcon(R.mipmap.ic_wide) // obligatoires
                .setContentIntent(pi)
                .setOngoing(true)
                .build();

        startForeground(1, notification);
    }

    public MusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        wakeLock.release();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (wakeLock == null) {
            PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MusicLockTag");
        }
        if (intent == null) return Service.START_STICKY;
        String command = intent.getExtras().getString(COMMAND);
        if (START.equals(command)) { // On joue un morceau si cela a été demandé

            position = intent.getExtras().getInt(MusicService.MUSIQUE);
            audioPlayer = AudioPlayer.get(this);
            selectAudioFile();
            audioPlayer.setOnCompletionlistener(this);
            wakeLock.acquire();
            audioPlayer.play();

        } else if (STOP.equals(command)){ // On libère les ressources si nécessaire
            AudioPlayer.release();
            wakeLock.release();
            stopForeground(true);
        }
        return START_STICKY;
    }

    private void selectAudioFile() {
        String[] titre = getResources().getStringArray(R.array.liste);
        String[] musique = getResources().getStringArray(R.array.musique);
        this.title = titre[position];
        createNotification();
        audioPlayer.loadId(this, musique[position]);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        selectAudioFile();
        audioPlayer.play();
    }
}

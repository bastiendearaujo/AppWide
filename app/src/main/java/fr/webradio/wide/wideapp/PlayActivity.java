package fr.webradio.wide.wideapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PlayActivity extends AppCompatActivity  {

    private Button btnPlay;
    private Button btnStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        Typeface myTypeface = Typeface.createFromAsset(this.getAssets(), "fonts/raleway.ttf");
        // Charge la police dans myTypeface

        TextView textView = (TextView) findViewById(R.id.textView4);
        // Récupère le composant auquel appliquer la police

        textView.setTypeface(myTypeface); // Change la police du composant
        final int position = getIntent().getExtras().getInt(MusicService.TITLE);
        String[] titre = getResources().getStringArray(R.array.liste);
        textView.setText(titre[position]);

        btnPlay = (Button) findViewById(R.id.buttonPlay);
        btnPlay.setEnabled(true);
        btnStop = (Button) findViewById(R.id.buttonStop);
        btnStop.setEnabled(true);

        Button buttonStart = (Button) findViewById(R.id.buttonPlay);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent music = new Intent(PlayActivity.this, MusicService.class);
                music.putExtra(MusicService.COMMAND, MusicService.START);
                music.putExtra(MusicService.MUSIQUE, position);
                startService(music);
            }
        });

        Button buttonStop = (Button) findViewById(R.id.buttonStop);
        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent music = new Intent(PlayActivity.this, MusicService.class);
                music.putExtra(MusicService.COMMAND, MusicService.STOP);
                startService(music);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_play, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

package gamer.sass;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by gamer on 8/26/2017.
 */

public class StartActivity extends AppCompatActivity {

    private FirebaseAuth Dbauth;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_main);

        Dbauth = FirebaseAuth.getInstance();
        final FirebaseUser user = Dbauth.getCurrentUser();

        progressBar= (ProgressBar) findViewById(R.id.progress_start);

        progressBar.setVisibility(View.VISIBLE);
        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if(user!= null){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(StartActivity.this, LoginMain.class));
                                progressBar.setVisibility(View.INVISIBLE);
                                finish();
                            }
                        });

                    }
                    else if (user == null)
                    { runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(StartActivity.this, LoginMain.class));
                            progressBar.setVisibility(View.INVISIBLE);
                            finish();
                        }
                    });
                    }
                }
            };
        };
        timer.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
    }
}
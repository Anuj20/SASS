package gamer.sass;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by gamer on 8/26/2017.
 */

public class User_Account extends AppCompatActivity {

    private FirebaseAuth Dbauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__account);

        Dbauth = FirebaseAuth.getInstance();
        FirebaseUser user = Dbauth.getCurrentUser();




    }
}

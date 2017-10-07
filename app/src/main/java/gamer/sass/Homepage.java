package gamer.sass;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.google.firebase.analytics.FirebaseAnalytics.Event.APP_OPEN;

public class Homepage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recycler;
    RecyclerView.LayoutManager layoutManager;
    private ProductAdapter pda;

    GoogleApiClient googleApiClient;

    TextView Name, Email;
    ImageView ProfilePic;

    private FirebaseAnalytics mFirebaseAnalytics;

    private FirebaseAuth Dbauth;
    public FirebaseDatabase database;
    public DatabaseReference databaseReference;
    FirebaseStorage FBstorage;
    StorageReference storageReference;

    private ProgressBar progress_adapter;

    private ChildEventListener mChildEventListener;

    List<ProductSingleton> productlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        FirebaseCrash.report(new Exception("Start of Homepage"));

        Dbauth = FirebaseAuth.getInstance();
        FirebaseUser user = Dbauth.getCurrentUser();

        if(user!= null){
            progress_adapter= (ProgressBar) findViewById(R.id.progress_adapter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView =  navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle params = new Bundle();
        params.putString( "app_open", APP_OPEN);
        mFirebaseAnalytics.logEvent("app_openevent", params);

        database= FirebaseDatabase.getInstance();
            FBstorage= FirebaseStorage.getInstance();
        databaseReference =database.getReference().child("Product_Detail");
        storageReference= FBstorage.getReference().child("Product_pics");

            String user_name= user.getDisplayName();
        String user_email= user.getEmail();
        String user_pic= user.getPhotoUrl().toString();

        Name= headerView. findViewById(R.id.userame);
        Email= headerView.findViewById(R.id.emailaddress);
        ProfilePic= headerView. findViewById(R.id.prof_pic);

        Name.setText(user_name);
        Email.setText(user_email);
        Glide.with(this).load(user_pic).into(ProfilePic);

            progress_adapter.setVisibility(View.VISIBLE);

            productlist = new ArrayList<>();
        recycler= (RecyclerView) findViewById(R.id.recy_prod);
        layoutManager= new StaggeredGridLayoutManager(2, 1);
        recycler.setLayoutManager(layoutManager);

        pda= new ProductAdapter(Homepage.this, productlist);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(pda);
        //recycler.setElevation(15);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        googleApiClient= new GoogleApiClient.Builder(getApplicationContext()).enableAutoManage(this,
                new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(getApplicationContext(), "connection failed!", Toast.LENGTH_LONG).show();
                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Homepage.this, NewProduct.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        attachDatabaseReadListener();
        }

        else if(user== null){
            Toast.makeText(Homepage.this, "There was some problem logging you in", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Homepage.this, LoginMain.class));
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        attachDatabaseReadListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        databaseReference.keepSynced(true);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            detachDatabaseReadListener();
            System.exit(0);
        }
    }

    private void attachDatabaseReadListener() {
        progress_adapter.setVisibility(View.VISIBLE);
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    ProductSingleton item = dataSnapshot.getValue(ProductSingleton.class);
                    productlist.add(item);
                    pda.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot,String s) {
                    productlist.removeAll(productlist);
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ProductSingleton item= snapshot.getValue(ProductSingleton.class);
                        productlist.add(item);
                    }
                    pda.notifyDataSetChanged();
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            databaseReference.addChildEventListener(mChildEventListener);
        }
        else {
            databaseReference.keepSynced(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.homepage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String buttonName = "buttonName";
        int id = item.getItemId();
        Bundle params = new Bundle();
        params.putInt("buttonName", id);


        if (id == R.id.action_rate) {
            buttonName = "settings";
            try {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("https://play.google.com/store/apps/details?id=gamer.sass"));
                startActivity(viewIntent);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Unable to Connect Try Again...",
                        Toast.LENGTH_LONG).show();
                e.printStackTrace();
                return true;
            }
        }
            if (id == R.id.action_feedback) {
                buttonName = "feedback";
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{"sassappfeedback@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "Name\n" + "college&Branch");
                i.putExtra(Intent.EXTRA_TEXT, "Feedback");
                try {
                    startActivity(Intent.createChooser(i, "Send..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                    ex.printStackTrace();
                }
                return true;
            }

            if (id == R.id.action_search) {
                buttonName = "search";
                Toast.makeText(Homepage.this, "This function will be activated in coming updates...", Toast.LENGTH_SHORT).show();
                return true;
            }

            mFirebaseAnalytics.logEvent(buttonName, params);
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_about) {
            // Handle the about section
            startActivity(new Intent(this, AboutMe.class));
        } else if (id == R.id.nav_account) {
            Toast.makeText(Homepage.this, "Coming soon...", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_writercorner) {
            startActivity(new Intent(this, WriterCorner.class));

        } else if (id == R.id.nav_Myads) {
            startActivity(new Intent(Homepage.this, UserAds.class));

        } else if (id == R.id.nav_share) {

            Toast.makeText(Homepage.this, "Share the app from playstore", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_Logout) {
            FirebaseAuth.getInstance().signOut();
            Auth.GoogleSignInApi.signOut(googleApiClient);
            LoginManager.getInstance().logOut();
            detachDatabaseReadListener();
            startActivity(new Intent(Homepage.this, LoginMain.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            databaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }
}

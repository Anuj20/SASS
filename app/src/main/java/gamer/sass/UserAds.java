package gamer.sass;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class UserAds extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ChildEventListener mChildEventListener;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager lma;
    private MyAdsAdapter myadsAdapter;
    private List<ProductSingleton> productlist;
    private FirebaseDatabase database;

    private FirebaseAuth Dbauth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_ads);

        Dbauth = FirebaseAuth.getInstance();
        FirebaseUser user = Dbauth.getCurrentUser();

        String Email = user.getEmail();

        database= FirebaseDatabase.getInstance();
        databaseReference =database.getReference().child("Product_Detail");

        final Query reference = databaseReference.orderByChild("email").equalTo(Email);

        productlist = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.userads);
        lma = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(lma);

        productlist = new ArrayList<>();
        myadsAdapter = new MyAdsAdapter(UserAds.this, productlist);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(myadsAdapter);

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ProductSingleton item = dataSnapshot.getValue(ProductSingleton.class);
                productlist.add(item);
                myadsAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "data displayed", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
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
        });
    }
}

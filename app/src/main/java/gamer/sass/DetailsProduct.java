package gamer.sass;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

/**
 * Created by gamer on 8/26/2017.
 */
public class DetailsProduct extends AppCompatActivity {

    private TextView name, price, contact, description;
    private ImageView image, fullscreenimage;
    CollapsingToolbarLayout toolbarLayout;
    ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_product);


        actionBar = getSupportActionBar();
        String ProductName = getIntent().getStringExtra("prod_name");

        name = (TextView) findViewById(R.id.disp_prodseller);
        price = (TextView) findViewById(R.id.disp_prodprice);
        contact = (TextView) findViewById(R.id.disp_prodcontact);
        description = (TextView) findViewById(R.id.disp_proddesc);
        image = (ImageView) findViewById(R.id.disp_prodimage);
        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        fullscreenimage = (ImageView) findViewById(R.id.full_screen_container);
        fullscreenimage.setBackgroundColor(000000);

        toolbarLayout.setTitle(ProductName);

        final String imagesString= getIntent().getStringExtra("image");
        actionBar.setTitle(getIntent().getStringExtra("category"));
        name.setText("Seller: " + getIntent().getStringExtra("sellername"));
        contact.setText("Contact: " + getIntent().getStringExtra("phone"));
        price.setText(getIntent().getStringExtra("price") + "₹");
        description.setText(getIntent().getStringExtra("description"));
        Glide.with(this).load(imagesString).into(image);

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_DIAL);
                i.setType("phone");
                // i.putExtra(Intent.EXTRA_CC);
                i.putExtra(Intent.EXTRA_PHONE_NUMBER, "Phone");
                try {
                    startActivity(Intent.createChooser(i, "Send..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(DetailsProduct.this, "There are no phone clients installed.", Toast.LENGTH_SHORT).show();
                    ex.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), "Contact Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(DetailsProduct.this).load(imagesString).into(fullscreenimage);
                actionBar.hide();
                fullscreenimage.animate();
                fullscreenimage.getAnimation();
                fullscreenimage.setVisibility(View.VISIBLE);
            }
        });

        fullscreenimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullscreenimage.setVisibility(View.GONE);
                fullscreenimage.animate();
                fullscreenimage.getAnimation();
                actionBar.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (fullscreenimage.getVisibility() == View.VISIBLE) {
            fullscreenimage.setVisibility(View.GONE);
            fullscreenimage.animate();
            actionBar.show();
        } else {
            super.onBackPressed();
        }
    }
}


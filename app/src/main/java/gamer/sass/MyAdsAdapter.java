package gamer.sass;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gamer on 8/26/2017.
 */

public class MyAdsAdapter extends RecyclerView.Adapter<MyadsViewHolder> {

    private List<ProductSingleton> list;
    private Context context;


    public MyAdsAdapter (Context context, List<ProductSingleton> listview){

        this.list = listview;
        this.context= context;

    }

    @Override
    public MyadsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutview = LayoutInflater.from(parent.getContext()).inflate(R.layout.useradslayout, parent, false);
        //  layoutview.setElevation(200);

        MyadsViewHolder pvh =new MyadsViewHolder(layoutview, context, (ArrayList<ProductSingleton>) list);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final MyadsViewHolder holder, final int position) {
        holder.productprice.setText("Price: "+ list.get(position).getPrice());
        holder.productname.setText("Name: "+ list.get(position).getProd_name());
        Glide.with(holder.image.getContext()).load(list.get(position).getPhoto()).into(holder.image);
        if (list.get(position).getPhoto()==null){
            Glide.with(holder.image.getContext()).load(R.drawable.hardwork).into(holder.image);
        }
        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, holder.overflow);
                //inflating menu from xml resource
                popup.inflate(R.menu.options_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.delete:
                                String prodid= list.get(position).getId();
                                DatabaseReference deleteProd = FirebaseDatabase.getInstance().getReference("Product_Detail").child(prodid);
                                deleteProd.removeValue();
                                context.startActivity(new Intent(context, Homepage.class));
                                Toast.makeText(context, "Product Deleted", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.update:
                                Toast.makeText(context, "This feature is in Testing phase", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return false;
                    }
                });
                popup.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }
}

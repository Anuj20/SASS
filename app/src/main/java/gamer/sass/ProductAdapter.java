package gamer.sass;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gamer on 8/26/2017.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder> {

    private List<ProductSingleton> list;
    private Context context;

    public ProductAdapter (Context context, List<ProductSingleton> listview){

        this.list = listview;
        this.context= context;

    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutview = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlayout, parent, false);
        //  layoutview.setElevation(200);

        ProductViewHolder pvh = new ProductViewHolder(layoutview, context, (ArrayList<ProductSingleton>) list);
        return pvh;
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {

        holder.productprice.setText("Price: "+ list.get(position).getPrice());
        holder.productname.setText("Name: "+ list.get(position).getProd_name());
        Glide.with(holder.image.getContext()).load(list.get(position).getPhoto()).into(holder.image);
        if (list.get(position).getPhoto()==null){
            Glide.with(holder.image.getContext()).load(R.drawable.sass).into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }
}

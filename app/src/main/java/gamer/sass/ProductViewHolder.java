package gamer.sass;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by gamer on 8/26/2017.
 */

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView  productprice, productname;
    ImageView image;
    private Context context;
    private ArrayList<ProductSingleton> product_details = new ArrayList<>();




    public ProductViewHolder(View itemView, Context context, ArrayList<ProductSingleton> product_details){
        super(itemView);

        this.context=context;
        this.product_details= product_details;
        itemView.setOnClickListener(this);
        productprice=  itemView.findViewById(R.id.Prod_price);
        productname=  itemView.findViewById(R.id.Prod_name);
        image= itemView.findViewById(R.id.prod_image);
    }

    @Override
    public void onClick(final View v) {

        int position = getAdapterPosition();

        ProductSingleton productitems= this.product_details.get(position);

        Intent in= new Intent(context, DetailsProduct.class);
        in.putExtra("prod_name", productitems.getProd_name());
        in.putExtra("sellername",productitems.getName());
        in.putExtra("phone",productitems.getPhone());
        in.putExtra("price",productitems.getPrice());
        in.putExtra("description",productitems.getDescription());
        in.putExtra("image", productitems.getPhoto());
        in.putExtra("category", productitems.getCategory());

        context.startActivity(in);}
}


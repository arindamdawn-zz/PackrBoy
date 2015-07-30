package com.packrboy.adapters;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.koushikdutta.ion.Ion;
import com.packrboy.R;
import com.packrboy.classes.Shipment;
import com.packrboy.network.VolleySingleton;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static com.packrboy.extras.urlEndPoints.KEY_UAT_BASE_URL;

/**
 * Created by arindam.paaltao on 29-Jul-15.
 */
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ShipmentHolder> {

    private Context context;
    private Activity activity;
    private LayoutInflater inflater;
    private View view;
    private ClickListener clickListener;
    private VolleySingleton singleton;
    private ImageLoader imageLoader;
    private ImageView itemImage;
    private ArrayList<Shipment> shipmentArrayList = new ArrayList<>();

    public TaskAdapter(Context context,Activity activity) {
        this.context = context;
        singleton = VolleySingleton.getsInstance();
        imageLoader = singleton.getImageLoader();
        this.activity = activity;
    }

    public void setClickListener(ClickListener clickListener){
        this.clickListener = clickListener;

    }


    @Override
    public TaskAdapter.ShipmentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.available_task_row, parent, false);

        ShipmentHolder holder = new ShipmentHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final TaskAdapter.ShipmentHolder holder, int position) {
        Shipment current = shipmentArrayList.get(position);
        holder.requestType.setText(current.getRequestType());
        holder.itemType.setText(current.getItemType());
        holder.itemQuantity.setText(current.getItemQuantity());
        holder.requestAddress.setText(current.getStreetNo()+","+current.getRoute()+","+current.getCity()+"-"+current.getPostalCode()+","+current.getState());

        String imageURL = current.getImageURL();
        if(imageURL != null){
            Ion.with(itemImage)
                    .placeholder(R.drawable.ic_cast_dark)
                    .error(R.drawable.ic_cast_disabled_light)
                    .load(KEY_UAT_BASE_URL+imageURL);
        }
    }

    public void setShipmentArrayList(ArrayList<Shipment> shipmentArrayList){
        this.shipmentArrayList = shipmentArrayList;
        notifyDataSetChanged();
        notifyItemRangeChanged(0, shipmentArrayList.size());
    }

    @Override
    public int getItemCount() {
        return shipmentArrayList.size();
    }

    class ShipmentHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView requestType,requestAddress,itemType,itemQuantity,customerName;

        public ShipmentHolder(View itemView) {
            super(itemView);

            itemImage = (ImageView)itemView.findViewById(R.id.itemImage);
            requestType = (TextView)itemView.findViewById(R.id.requestType);
            requestAddress = (TextView)itemView.findViewById(R.id.address);
            itemType = (TextView)itemView.findViewById(R.id.itemType);
            itemQuantity = (TextView)itemView.findViewById(R.id.itemQuantity);
            customerName = (TextView)itemView.findViewById(R.id.customerName);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if(clickListener != null){
                clickListener.itemClicked(v, getLayoutPosition());
            }
        }
    }

    public interface ClickListener{
        void itemClicked(View view, int position);

    }
}

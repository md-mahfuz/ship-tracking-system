package just.cse.mahfuz.shiptrackingsystem.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import just.cse.mahfuz.shiptrackingsystem.Model.Users;
import just.cse.mahfuz.shiptrackingsystem.R;

public class ShipListRecyclerAdapter extends RecyclerView.Adapter<ShipListRecyclerAdapter.myViewHolder> implements Filterable {
    Context context;
    List<Users> usersModel;
    List<Users> filteredUserModel;
    String sImage, sShipName, sShipID, sCountry, sOwnerName, sOwnerEmail, sOwnerPhone;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String uid;
    ProgressDialog progressDialog;

    String type;


    public ShipListRecyclerAdapter(Context c, List<Users> usrs, String type) {
        context = c;
        usersModel = usrs;
        filteredUserModel = usrs;
        this.type = type;

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getUid();
        progressDialog = new ProgressDialog(context);

    }

    @NonNull
    @Override
    public ShipListRecyclerAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.contact_row, viewGroup, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ShipListRecyclerAdapter.myViewHolder myViewHolder, int i) {


        sImage = filteredUserModel.get(i).getsImage();
        sShipID = filteredUserModel.get(i).getsShipID();
        sShipName = filteredUserModel.get(i).getsShipName();
        sCountry = filteredUserModel.get(i).getsCountry();
        sOwnerName = filteredUserModel.get(i).getsOwnerName();
        sOwnerEmail = filteredUserModel.get(i).getsOwnerEmail();
        sOwnerPhone = filteredUserModel.get(i).getsOwnerPhone();


        if (!"".equals(sImage) && sImage != null) {
            Glide.with(context)
                    .load(sImage)
                    //.override(80, 80)
                    //.thumbnail(0.1f)
                    .into(myViewHolder.image);
        }

        myViewHolder.shipID.setText("ID: "+sShipID);
        myViewHolder.shipName.setText("Ship Name: "+sShipName);
        myViewHolder.ownerName.setText("Owner's Name: "+sOwnerName);
        myViewHolder.ownerEmail.setText("Owner's Email: "+sOwnerEmail);
        myViewHolder.ownerPhone.setText("Owner's Phone: "+sOwnerPhone);

        myViewHolder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_DIAL);
                String p = "tel:+88" + myViewHolder.ownerPhone.getText().toString().trim();
                i.setData(Uri.parse(p));
                context.startActivity(i);
            }
        });

//        if ("contacts".equals(type)) {
//
//        }
//
//        else if ("track".equals(type)) {
//
//            myViewHolder.call.setVisibility(View.INVISIBLE);
//
//            myViewHolder.constraintLayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    progressDialog.setMessage("Loading...");
//                    progressDialog.show();
//                    progressDialog.setCancelable(true);
//
//                    Intent intent = new Intent(context, MapsActivity.class);
//
//                    //converting bitmap to bytearray
//                    Bitmap bitmap = ((BitmapDrawable) myViewHolder.image.getDrawable()).getBitmap();
//                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
//                    byte[] b = baos.toByteArray();
//
//
//                    intent.putExtra("image", b);
//
//                    intent.putExtra("startDate", myViewHolder.startDate.getText().toString());
//                    intent.putExtra("endDate", myViewHolder.endDate.getText().toString());
//                    //intent.putExtra("country",myViewHolder.startDate.getText().toString());
//                    intent.putExtra("destination", myViewHolder.destination.getText().toString());
//                    intent.putExtra("deadWeight", myViewHolder.deadWeight.getText().toString());
//                    intent.putExtra("draught", myViewHolder.draught.getText().toString());
//
//                    context.startActivity(intent);
//
//
//                }
//            });
//
//        }
//

    }

    @Override
    public int getItemCount() {

        return filteredUserModel.size();
    }


    class myViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        ImageView image;
        TextView shipID, shipName, ownerName, ownerEmail, ownerPhone;
        Button call;


        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.constraintLayout);
            image = itemView.findViewById(R.id.image);
            shipID = itemView.findViewById(R.id.shipID);
            shipName = itemView.findViewById(R.id.shipName);
            ownerName = itemView.findViewById(R.id.ownerName);
            ownerEmail = itemView.findViewById(R.id.ownerEmail);
            ownerPhone = itemView.findViewById(R.id.ownerPhone);

            call = itemView.findViewById(R.id.call);


        }
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filteredUserModel = usersModel;
                } else {
                    List<Users> filteredList = new ArrayList<>();
                    for (Users row : usersModel) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getsShipName().toLowerCase().contains(charString.toLowerCase())
                                || row.getsOwnerName().toLowerCase().contains(charString.toLowerCase())
                                || row.getsOwnerEmail().toLowerCase().contains(charString.toLowerCase())
                                || row.getsShipID().contains(charSequence)
                                || row.getsOwnerPhone().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }
                    filteredUserModel = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredUserModel;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredUserModel = (ArrayList<Users>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

//    public interface ShipListRecyclerAdapterListener {
//        void onShipSelected(Users contact);
//    }


}




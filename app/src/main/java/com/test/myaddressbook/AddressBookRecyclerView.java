package com.test.myaddressbook;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.test.myaddressbook.models.EmployeeTableModel;

import java.io.InputStream;
import java.util.ArrayList;

public class AddressBookRecyclerView extends RecyclerView.Adapter<AddressBookRecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<EmployeeTableModel> employees = new ArrayList<>();

    public AddressBookRecyclerView(Context context, ArrayList<EmployeeTableModel> employees) {
        this.context = context;
        this.employees = employees;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_address_book_recycler_view, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.employeeName.setText(employees.get(position).getName());
        holder.employeeCity.setText(employees.get(position).getCity());

        // Set image from URL
        new DownloadImage((ImageView) holder.employeeImage).execute(employees.get(position).getPicture());

        // Implement call action
        holder.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent call = new Intent(Intent.ACTION_DIAL);
                call.setData(Uri.parse("tel:" + employees.get(position).getPhone()));
                context.startActivity(call);
            }
        });

        // Implement send email
        holder.emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{employees.get(position).getEmail()});
                email.setType("text/plain");
                try {
                    context.startActivity(Intent.createChooser(email, "Send mail"));
                } catch (android.content.ActivityNotFoundException exception) {
                    Log.d("Error", exception.toString());
                    Toast.makeText(view.getContext(), "Failed to send mail.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    // Download image
    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        ImageView image;

        public DownloadImage(ImageView image) {
            this.image = image;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap bm = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                bm = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bm;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            image.setImageBitmap(bitmap);
        }
    }

    @Override
    public int getItemCount() {
        return employees.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView employeeName, employeeCity;
        Button callButton, emailButton;
        ImageView employeeImage;
        LinearLayout linearLayout;

        public ViewHolder(View view) {
            super(view);
            employeeImage = view.findViewById(R.id.employeeImage);
            employeeName = view.findViewById(R.id.employeeName);
            employeeCity = view.findViewById(R.id.employeeCity);
            callButton = view.findViewById(R.id.callButton);
            emailButton = view.findViewById(R.id.emailButton);
            linearLayout = view.findViewById(R.id.linearLayout);
        }
    }
}
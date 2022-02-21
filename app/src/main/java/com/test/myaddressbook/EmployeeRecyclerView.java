package com.test.myaddressbook;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.test.myaddressbook.models.Employee;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EmployeeRecyclerView extends RecyclerView.Adapter<EmployeeRecyclerView.ViewHolder> {
    private Context context;
    ArrayList<Employee> employees = new ArrayList<>();

    public EmployeeRecyclerView(Context context, ArrayList<Employee> employees) {
        this.context = context;
        this.employees = employees;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_employee_recycler_view, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(EmployeeRecyclerView.ViewHolder holder, int position) {
        holder.employeeName.setText(employees.get(position).getName().getFirst() + " " + employees.get(position).getName().getLast());
        holder.employeeCity.setText("City: " + employees.get(position).getLocation().getCity());
        holder.employeePhone.setText("Phone: " + employees.get(position).getPhone() + " / " + employees.get(position).getCell());

        String strDate = employees.get(position).getRegistered().getDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Date date = null;
        try {
            date = dateFormat.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String dateFormatted = DateFormat.getDateInstance(DateFormat.LONG).format(date);
        String[] dateSplit = dateFormatted.split(" ");
        holder.employeeMemberSince.setText("Member since: " + dateSplit[0] + " " + dateSplit[2]);

        // Set image from URL
        new DownloadImage((ImageView) holder.employeeImage).execute(employees.get(position).getPicture().getLarge());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                MainActivity.callEmployeeDetailViewFragment(employees.get(position));
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

    public void searchEmployee(ArrayList<Employee> searchedEmployee) {
        employees = searchedEmployee;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView employeeName, employeeCity, employeePhone, employeeMemberSince;
        ImageView employeeImage;
        LinearLayout linearLayout;

        public ViewHolder(View view) {
            super(view);
            employeeImage = view.findViewById(R.id.employeeImage);
            employeeName = view.findViewById(R.id.employeeName);
            employeeCity = view.findViewById(R.id.employeeCity);
            employeePhone = view.findViewById(R.id.employeePhone);
            employeeMemberSince = view.findViewById(R.id.employeeMemberSince);
            linearLayout = view.findViewById(R.id.linearLayout);
        }
    }
}
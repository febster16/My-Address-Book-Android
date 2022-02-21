package com.test.myaddressbook;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.test.myaddressbook.models.Employee;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    TextView txtView;
    FrameLayout employeeViewLayout;
    ArrayList<Employee> employees = new ArrayList<>();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.address_book_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.addressBookButton:
                callAddressBookFragment();
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Retrofit JSON Web Service
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://u73olh7vwg.execute-api.ap-northeast-2.amazonaws.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderAPI placeHolderAPI = retrofit.create(JsonPlaceHolderAPI.class);
        Call<JsonObject> listCall = placeHolderAPI.getEmployee();

        listCall.enqueue(new Callback<JsonObject>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(!response.isSuccessful()){
                    Log.d("Error", response.code() + "");
                    return;
                }
//                System.out.println(response.body().getAsJsonArray("employees"));
                Gson gson = new Gson();
                Type type = new TypeToken<List<Employee>>(){}.getType();
                employees = gson.fromJson(response.body().getAsJsonArray("employees").toString(), type);
                callEmployeeViewFragment(employees);


                // Change app ui header
                String nim = response.body().get("nim").getAsString();
                String nama = response.body().get("nama").getAsString();
                changeHeaderTitle(nim, nama);

//                for(Employee employee : employees) {
//                    System.out.println(employee.getEmployeeId());
//                    System.out.println(employee.getEmail());
//                    System.out.println(employee.getName().getFirst() + " " + employee.getName().getLast());
//                    System.out.println(employee.getLocation().getCity());
//                    System.out.println(employee.getPhone() + " / "  + employee.getCell());
//                    System.out.println(employee.getPicture());
//
//                    String strDate = employee.getRegistered().getDate();
//
//                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
//                    Date date = null;
//                    try {
//                        date = dateFormat.parse(strDate);
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                    String dateFormatted = DateFormat.getDateInstance(DateFormat.LONG).format(date);
//                    String[] dateSplit = dateFormatted.split(" ");
//                    System.out.println(dateSplit[0] + " " + dateSplit[2]);
//
//                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });


    }

    public void callEmployeeViewFragment(ArrayList<Employee> employees){
        // Fragment implementation
        employeeViewLayout = findViewById(R.id.employeeViewLayout);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.employeeViewLayout, new EmployeeViewFragment(employees, this));
        ft.setReorderingAllowed(true);
        ft.commit();
    }

    public void callEmployeeDetailViewFragment(Employee employee){
        employeeViewLayout = findViewById(R.id.employeeViewLayout);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.employeeViewLayout, new EmployeeDetailViewFragment(employee, this));
        ft.setReorderingAllowed(true);
        ft.commit();
    }

    public void callAddressBookFragment() {
        employeeViewLayout = findViewById(R.id.employeeViewLayout);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.employeeViewLayout, new AddressBookFragment(this));
        ft.setReorderingAllowed(true);
        ft.commit();
    }

    public void changeHeaderTitle(String nim, String nama){
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {

            actionBar.setTitle(nim + " - " + nama);
        }
    }

}
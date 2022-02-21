package com.test.myaddressbook;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.test.myaddressbook.helpers.DatabaseHelper;
import com.test.myaddressbook.models.Employee;
import com.test.myaddressbook.models.EmployeeTableModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EmployeeDetailViewFragment extends Fragment implements OnMapReadyCallback {

    DatabaseHelper dbHelper;
    TextView employeeName, employeeCity, employeePhone, employeeMemberSince, employeeEmail, employeeExistsTv;
    ImageButton backButton;
    Button addButton;
    Employee employee;
    MainActivity mainActivity;
    GoogleMap googleMap;
    MapView mapView;
    View view;
    ArrayList<EmployeeTableModel> employeesOnAddressBook = new ArrayList<>();

    public EmployeeDetailViewFragment(Employee employee, MainActivity mainActivity) {
        this.employee = employee;
        this.mainActivity = mainActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_employee_detail_view, container, false);

        employeeName = view.findViewById(R.id.employeeName);
        employeeCity = view.findViewById(R.id.employeeCity);
        employeePhone = view.findViewById(R.id.employeePhone);
        employeeMemberSince = view.findViewById(R.id.employeeMemberSince);
        employeeEmail = view.findViewById(R.id.employeeEmail);
        employeeExistsTv = view.findViewById(R.id.employeeExistsTv);

        backButton = view.findViewById(R.id.backButton);
        addButton = view.findViewById(R.id.addButton);

        employeeName.setText(employee.getName().getFirst() + " " + employee.getName().getLast());
        employeeCity.setText("City: " + employee.getLocation().getCity());
        employeePhone.setText("Phone: " + employee.getPhone() + " / " + employee.getCell());

        String strDate = employee.getRegistered().getDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Date date = null;
        try {
            date = dateFormat.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String dateFormatted = DateFormat.getDateInstance(DateFormat.LONG).format(date);
        String[] dateSplit = dateFormatted.split(" ");
        employeeMemberSince.setText("Member since: " + dateSplit[0] + " " + dateSplit[2]);

        employeeEmail.setText("Email: " + employee.getEmail());

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.callEmployeeViewFragment(mainActivity.employees);
            }
        });

        // Disable add to address book if employee is already exists in address book
        dbHelper = new DatabaseHelper(getContext());
        Cursor cursor = dbHelper.getEmployee();
//        if(cursor.getCount() <= 0) {
//            Toast.makeText(getContext(), "No data in my address book.", Toast.LENGTH_SHORT).show();
//            noDataTv.setVisibility(view.VISIBLE);
//        }
        while (cursor.moveToNext()) {
            EmployeeTableModel employee = new EmployeeTableModel(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
            employeesOnAddressBook.add(employee);
        }

        for (EmployeeTableModel employeeTableModel: employeesOnAddressBook) {
            if(employee.getEmployeeId() == employeeTableModel.getEmployeeId()) {
                addButton.setEnabled(false);
                addButton.setBackgroundColor(Color.DKGRAY);
                addButton.setTextColor(Color.WHITE);
                employeeExistsTv.setVisibility(view.VISIBLE);
            }
        }

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = employee.getEmployeeId();
                String name = employee.getName().getFirst() + " " + employee.getName().getLast();
                String city = employee.getLocation().getCity();
                String phone = employee.getPhone();
                String email = employee.getEmail();
                String picture = employee.getPicture().getLarge();
                insertData(id, name, city, phone, email, picture);
            }
        });

        return view;
    }

    private void insertData(int id, String name, String city, String phone, String email, String picture) {
        dbHelper = new DatabaseHelper(getContext());
        boolean isInserted = dbHelper.insertData(id, name, city, phone, email, picture);
        if(isInserted){
            Toast.makeText(getContext(), "Employee successfully added to address book!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Failed to add employee to address book!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = (MapView) view.findViewById(R.id.map);
        if(mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        googleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(employee.getLocation().getCoordinates().getLatitude()),Double.parseDouble(employee.getLocation().getCoordinates().getLongitude()))));
        CameraPosition position = CameraPosition.builder().target(new LatLng(Double.parseDouble(employee.getLocation().getCoordinates().getLatitude()),Double.parseDouble(employee.getLocation().getCoordinates().getLongitude()))).zoom(1).bearing(0).tilt(45).build();

        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(position));
    }
}
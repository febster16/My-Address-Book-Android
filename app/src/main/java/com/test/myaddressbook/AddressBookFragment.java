package com.test.myaddressbook;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.test.myaddressbook.helpers.DatabaseHelper;
import com.test.myaddressbook.models.EmployeeTableModel;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AddressBookFragment extends Fragment {

    DatabaseHelper dbHelper;
    TextView noDataTv;
    ImageButton backButton;
    MainActivity mainActivity;
    ArrayList<EmployeeTableModel> employees = new ArrayList<>();

    public AddressBookFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_address_book, container, false);

        noDataTv = view.findViewById(R.id.noDataTv);
        backButton = view.findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.callEmployeeViewFragment(mainActivity.employees);
            }
        });

        dbHelper = new DatabaseHelper(getContext());
        Cursor cursor = dbHelper.getEmployee();
        if(cursor.getCount() <= 0) {
            Toast.makeText(getContext(), "No data in my address book.", Toast.LENGTH_SHORT).show();
            noDataTv.setVisibility(view.VISIBLE);
        }

        while (cursor.moveToNext()) {
            EmployeeTableModel employee = new EmployeeTableModel(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
            employees.add(employee);
        }

        buildRecyclerView(view);

        return view;
    }

    public void buildRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        AddressBookRecyclerView adapter = new AddressBookRecyclerView(view.getContext(), this.employees);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

    }
}
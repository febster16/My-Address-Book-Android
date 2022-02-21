package com.test.myaddressbook;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.test.myaddressbook.models.Employee;

import java.util.ArrayList;

public class EmployeeViewFragment extends Fragment {

    ArrayList<Employee> employees = new ArrayList<>();
    RecyclerView recyclerView;
    EditText searchEt;
    TextView searchButton;
    EmployeeRecyclerView adapter;
    MainActivity mainActivity;

    public EmployeeViewFragment(ArrayList<Employee> employees, MainActivity mainActivity) {
        this.employees = employees;
        this.mainActivity = mainActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employee_view, container, false);

        buildRecyclerView(view);

        searchEt = view.findViewById(R.id.searchEt);
        searchButton = view.findViewById(R.id.searchButton);
//        searchButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                searchEmployee(searchEt.getText().toString());
//            }
//        });

        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchEmployee(editable.toString());
            }
        });

        return view;
    }

    private void buildRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new EmployeeRecyclerView(view.getContext(), this.employees);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemCLickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        mainActivity.callEmployeeDetailViewFragment(employees.get(position));
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );
    }

    private void searchEmployee(String text) {
        ArrayList<Employee> searchedEmployee = new ArrayList<>();

        for (Employee employee: employees) {
            String employeeName = employee.getName().getFirst().toLowerCase() + " " + employee.getName().getLast().toLowerCase();
            if(employeeName.contains(text.toLowerCase())){
                searchedEmployee.add(employee);
            }
        }

        adapter.searchEmployee(searchedEmployee);
    }
}
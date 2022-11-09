package com.data.employees.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.data.employees.R;
import com.data.employees.model.EmployeeModel;

import java.util.ArrayList;
import java.util.List;

public class EmployeesAdapter extends ArrayAdapter<EmployeeModel> {
    public EmployeesAdapter(Context context, List<EmployeeModel> employeeModelList) {
        super(context, R.layout.item_employee, employeeModelList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        EmployeeModel employeeModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_employee, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvID = (TextView) convertView.findViewById(R.id.tvID);
        // Populate the data into the template view using the data object
        tvName.setText(employeeModel.getEmpName());
        tvID.setText(String.valueOf(employeeModel.getEmpID()));
        // Return the completed view to render on screen
        return convertView;
    }
}
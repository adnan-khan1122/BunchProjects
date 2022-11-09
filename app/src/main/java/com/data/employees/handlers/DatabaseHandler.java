package com.data.employees.handlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.data.employees.model.EmployeeModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "EmployeesManager";
    private static final String TABLE_EMPLOYEES = "Employees";
    private static final String KEY_ID = "EmpID";
    private static final String KEY_NAME = "EmpName";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_EMPLOYEES + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEES);

        // Create tables again
        onCreate(db);
    }

    // code to add the new contact
    public void addEmployee(EmployeeModel employee) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, employee.getEmpName()); // Contact Name

        // Inserting Row
        db.insert(TABLE_EMPLOYEES, null, values);
        db.close(); // Closing database connection
    }

    // code to get the single contact
    EmployeeModel getEmployee(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_EMPLOYEES, new String[] { KEY_ID,
                        KEY_NAME }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        EmployeeModel employee = new EmployeeModel(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1));
        // return contact
        return employee;
    }

    // code to get all contacts in a list view
    public List<EmployeeModel> getAllEmployees() {
        List<EmployeeModel> employeeList = new ArrayList<EmployeeModel>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_EMPLOYEES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                EmployeeModel employee = new EmployeeModel();
                employee.setEmpID(Integer.parseInt(cursor.getString(0)));
                employee.setEmpName(cursor.getString(1));
                // Adding contact to list
                employeeList.add(employee);
            } while (cursor.moveToNext());
        }

        // return contact list
        return employeeList;
    }

}
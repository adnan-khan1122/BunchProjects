package com.data.employees.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.data.employees.handlers.DatabaseHandler;
import com.data.employees.adapters.EmployeesAdapter;
import com.data.employees.R;
import com.data.employees.helper.XlsWriter;
import com.data.employees.model.EmployeeModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final int STORAGE_PERMISSION_REQUEST_CODE = 334;
    SharedPreferences mPreference;
    DatabaseHandler db;
    ProgressBar progressBar;
    ListView listView;
    FloatingActionButton exportToExcel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressBar);
        listView = findViewById(R.id.listView);
        exportToExcel = findViewById(R.id.exportToExcel);

        mPreference = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        db = new DatabaseHandler(this);

        // Inserting Contacts
        if (!mPreference.getBoolean("IS_DB_CREATED", false)) {
            createEmployeesDb();
        }

        try {
            List<EmployeeModel> employeeModelList = db.getAllEmployees();
            EmployeesAdapter adapter = new EmployeesAdapter(this, employeeModelList);
            listView.setAdapter(adapter);
            progressBar.setVisibility(View.GONE);
        } catch (Exception e) {
        }

        exportToExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getStoragePermission())
                    exportDB();
            }
        });

    }

    private void exportDB() {

        FileOutputStream fos;
        File file;

        String selectQuery = "SELECT  * FROM Employees";

        SQLiteDatabase db2 = db.getWritableDatabase();
        Cursor cursor = db2.rawQuery(selectQuery, null);

        File root = getCacheDir();
        File storageDir = new File(root + "/Storage");

        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }

        file = new File(storageDir, "EmployeeTable.xls");

        XlsWriter xlsWriter = new XlsWriter(cursor, file);
        fos = xlsWriter.writeNext();

        if (fos != null) {
            try {
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Toast.makeText(MainActivity.this, "Excel Sheet Generated", Toast.LENGTH_SHORT).show();
        composeEmail("bcsf1410@gmail.com", "Employees Table Exported", file);

    }

    public void composeEmail(String emailAddress, String subject, File file) {


        try {
            Uri fileUri = FileProvider.getUriForFile(this,
                    getPackageName() + ".provider", file);

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("message/rfc822");
//            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_STREAM, fileUri);
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailAddress});
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, "");

            List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                grantUriPermission(packageName, fileUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }

            startActivity(Intent.createChooser(intent, "Choose Email"));
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void createEmployeesDb() {
        try {
            db.addEmployee(new EmployeeModel(1, "Mark"));
            db.addEmployee(new EmployeeModel(2, "Zach"));
            db.addEmployee(new EmployeeModel(3, "Ryan"));
            db.addEmployee(new EmployeeModel(4, "William"));
            db.addEmployee(new EmployeeModel(5, "Hazel"));
            db.addEmployee(new EmployeeModel(6, "Alexander"));
            db.addEmployee(new EmployeeModel(7, "Feyra"));
            db.addEmployee(new EmployeeModel(8, "Omm"));
            db.addEmployee(new EmployeeModel(9, "Tandu"));
            db.addEmployee(new EmployeeModel(10, "Dana"));
            db.addEmployee(new EmployeeModel(11, "Hyara"));
            db.addEmployee(new EmployeeModel(12, "Gabele"));
            db.addEmployee(new EmployeeModel(13, "Nasyra"));
            db.addEmployee(new EmployeeModel(14, "Eodriel"));
            db.addEmployee(new EmployeeModel(15, "Emeryn"));
            db.addEmployee(new EmployeeModel(16, "Avon"));
            db.addEmployee(new EmployeeModel(17, "Leo"));
            db.addEmployee(new EmployeeModel(18, "Jose"));
            db.addEmployee(new EmployeeModel(19, "Mara"));
            db.addEmployee(new EmployeeModel(20, "Russell"));

            mPreference.edit().putBoolean("IS_DB_CREATED", true).commit();
        } catch (Exception e) {
        }
    }


    public boolean getStoragePermission() {
        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        // show an alert digalog
                        Toast.makeText(this, "Goto Setting and give permission!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Request permission
                        ActivityCompat.requestPermissions(
                                MainActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                STORAGE_PERMISSION_REQUEST_CODE
                        );

                    }
                } else {
                    return true;
                }
            } else {
                return true;
            }
        } catch (Exception e) {
        }

        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case STORAGE_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    exportDB();
                } else {
                    Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' statements for other permssions
        }
    }

}
/* ##### BEGIN GPL LICENSE BLOCK #####
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses/>.
 *
 * ##### END GPL LICENSE BLOCK #####
 * __author__ = "Sergi Blanch-Torne"
 * __email__ = "srgblnchtrn@protonmail.ch"
 * __copyright__ = "Copyright 2016 Sergi Blanch-Torne"
 * __license__ = "GPLv3+"
 * __status__ = "development"
 */

package cat.calcurco.ourshoppinglist;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import OurShoppingListObjs.ImportExport;

/**
 * Created by serguei on 07/09/16.
 */

public class ImportExportActivity extends AppCompatActivity {
    final static String TAG = "ImportExportActivity";

    private Button importer;
    private Button exporter;
    private EditText directoryText;
    private EditText filenameText;

    private static final int REQUEST_READ_RIGHTS = 0;
    private static final int REQUEST_WRITE_RIGHTS = 1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.importexport);

        importer = (Button) findViewById(R.id.importer);
        exporter = (Button) findViewById(R.id.exporter);

        directoryText = (EditText) findViewById(R.id.directoryText);
        directoryText.setText(getApplicationContext().getExternalMediaDirs()[0].getAbsolutePath());
        // TODO: click listener for the directorySelector

        filenameText = (EditText) findViewById(R.id.filenameText);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Date now = new Date();
        String fileName = sdf.format(now);  // TODO: Add the extension?
        filenameText.setText(fileName);
        // TODO: click listener for the filenameSelector

        importer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doImport();
            }
        });
        exporter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doExport();
            }
        });
    }

    private void doImport() {
        if ( requestRight(findViewById(R.id.importer), Manifest.permission.READ_EXTERNAL_STORAGE,
                "Read access requested for the feature of import from a CSV file.",
                REQUEST_READ_RIGHTS) ) {
            ImportExport importObj = new ImportExport();
            File directory = new File(directoryText.getText().toString());
            String fileName = filenameText.getText().toString();
            if ( ! fileName.endsWith(".csv")) {
                fileName += ".csv";
            }
            hideSoftKeyboard();
            if ( importObj.importDB2CSV(new File(directory, fileName)) ) {
                Log.i(TAG, "In doImport(): Succeed");
                Snackbar.make(findViewById(R.id.exporter), "Import succeed",
                        Snackbar.LENGTH_INDEFINITE).setAction("OK",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                finish();
                            }
                        }).show();

            } else {
                Log.e(TAG, "In doImport(): Failed");
                Snackbar.make(findViewById(R.id.exporter), "Failed to recover from file",
                        Snackbar.LENGTH_LONG).show();
            }
        } else {
            Snackbar.make(findViewById(R.id.exporter), "No read permission to proceed",
                    Snackbar.LENGTH_LONG).show();
        }

    }

    private void doExport() {
        if ( requestRight(findViewById(R.id.exporter), Manifest.permission.WRITE_EXTERNAL_STORAGE,
                "Write access requested for the feature of export to a CSV file.",
                REQUEST_WRITE_RIGHTS) ) {
            ImportExport exportObj = new ImportExport();
            File directory = new File(directoryText.getText().toString());
            String fileName = filenameText.getText().toString();
            hideSoftKeyboard();
            // proceed with export
            if ( exportObj.exportDB2CSV(directory, fileName) ) {
                Log.i(TAG, "In doExport(): Succeed");
                Snackbar.make(findViewById(R.id.exporter), "Saved file",
                        Snackbar.LENGTH_INDEFINITE).setAction("OK",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                finish();
                            }
                        }).show();
                finish();
            } else {
                Log.e(TAG, "In doExport(): Failed");
                Snackbar.make(findViewById(R.id.exporter), "Failed to store the file",
                        Snackbar.LENGTH_LONG).show();
            }
        } else {
            Snackbar.make(findViewById(R.id.exporter), "No write permission to proceed",
                    Snackbar.LENGTH_LONG).show();
        }

    }

    private boolean requestRight(View who, final String rightCode, String explanation,
                              final int requestCode) {
        int bar = ContextCompat.checkSelfPermission(this.getApplicationContext(), rightCode);
        if ( bar != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "checkSelfPermission(...) didn't granted the permission");
            boolean foo = ActivityCompat.shouldShowRequestPermissionRationale(this, rightCode);
            if (foo) {
                Log.d(TAG, "The permission should be requested to the user");
                Snackbar.make(who, explanation, Snackbar.LENGTH_INDEFINITE).setAction("OK",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ActivityCompat.requestPermissions(ImportExportActivity.this,
                                        new String[]{rightCode}, requestCode);
                            }
                        }).show();
            } else {
                Log.d(TAG, "The permission should be requested to the sytem");
                ActivityCompat.requestPermissions(this, new String[]{rightCode}, requestCode);
            }
            return true;
        }
        return true;
    }

//    //@Override
//    public void OnRequestPermissionsResultCallback(int requestCode, String[] permissions,
//                                                   int[] grantResults){
//        Snackbar.make(findViewById(R.id.exporter),
//                "OnRequestPermissionsResultCallback("+requestCode+", "+permissions+", "+grantResults+")",
//                Snackbar.LENGTH_LONG).show();
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                                   int[] grantResults){
//        Snackbar.make(findViewById(R.id.exporter),
//                "onRequestPermissionsResult("+requestCode+", "+permissions+", "+grantResults+")",
//                Snackbar.LENGTH_LONG).show();
//    }
    private boolean hideSoftKeyboard() {
        View view = this.getCurrentFocus();
        if ( view != null ) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if ( imm.isActive() ) {
                return imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            return true;
        }
        return false;
    }

    private void prepareCallback() {

    }

    public void updateProgressBar(Integer value) {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setProgress(value);
    }
}

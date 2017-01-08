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
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.io.File;

/**
 * Created by serguei on 07/09/16.
 */

public class ImportExportActivity extends AppCompatActivity {
    private Button importer;
    private Button exporter;

    private static final int REQUEST_READ_RIGHTS = 0;
    private static final int REQUEST_WRITE_RIGHTS = 1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.importexport);

        importer = (Button) findViewById(R.id.importer);
        exporter = (Button) findViewById(R.id.exporter);

//        importer.setOnClickListener();
//        exporter.setOnClickListener();
    }

    private  void doImport() {
        requestRight(findViewById(R.id.importer), Manifest.permission.READ_EXTERNAL_STORAGE,
                "Read access requested for the feature of import from a CSV file.",
                REQUEST_READ_RIGHTS);
//        File csvFile = new File();
        // read the file
        // for each row, if exist check discrepancies
    }

    private  void doExport() {
        requestRight(findViewById(R.id.exporter), Manifest.permission.WRITE_EXTERNAL_STORAGE,
                "Write access requested for the feature of export to a CSV file.",
                REQUEST_WRITE_RIGHTS);
        // write a csv file
    }

    private void requestRight(View who, final String rightCode, String explanation,
                              final int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, rightCode)) {
            Snackbar.make(who, explanation, Snackbar.LENGTH_INDEFINITE).setAction("OK",
                    new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityCompat.requestPermissions(ImportExportActivity.this,
                            new String[]{rightCode}, requestCode);
                }
            }).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{rightCode}, requestCode);
        }
    }
}

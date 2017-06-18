package com.adox.matias.formadox;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adox.matias.formadox.model.DatabaseOpenHelper;
import com.adox.matias.formadox.ui.BrushView;
import com.adox.matias.formadox.util.Util;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by matias on 04/09/15.
 */
public class FirmarActivity extends Activity {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private DatabaseOpenHelper db ;
    private FormularioActivity parentView;
    private Button sigBUT;
    private Button borrarBUT;
    private Button firmarBUT;
    private TextView nomYApe;
    private Context context;
    private BrushView brushView=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firmar);

        nomYApe=(TextView) this.findViewById(R.id.nomYApe2);
        nomYApe.setText("Aclaracion: "+ Util.clienteActual.getNombre_completo());



        LinearLayout layoutList = (LinearLayout) this.findViewById(R.id.layout_list);

        if(brushView==null) {
            brushView = new BrushView(this);
        }else{
            ((ViewGroup) brushView.getParent()).removeView(brushView);
        }

        layoutList.addView(brushView);


        borrarBUT = (Button) this.findViewById(R.id.borrar);
        borrarBUT.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        brushView.borr();

                    }
                });

        sigBUT = (Button) this.findViewById(R.id.sigBUT8);
        final FirmarActivity rootViewFinal=this;

        sigBUT.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                View u = rootViewFinal.findViewById(R.id.layout_list);
                u.setDrawingCacheEnabled(true);
                LinearLayout z = (LinearLayout) rootViewFinal.findViewById(R.id.layout_list);
                int totalHeight = z.getChildAt(0).getHeight();
                int totalWidth = z.getChildAt(0).getWidth();
                u.layout(0, 0, totalWidth, totalHeight);
                u.buildDrawingCache(true);
                Bitmap b = Bitmap.createBitmap(u.getDrawingCache());
                u.setDrawingCacheEnabled(false);

                //Save bitmap
                // String extr = Environment.getExternalStorageDirectory().toString() +   File.separator + "Descargadas";
                String fileName = new SimpleDateFormat("yyyyMMddhhmm'_report.jpg'").format(new Date());
                // File myPath = new File(extr, fileName);
                //final File dir = new File(rootViewFinal.getFilesDir() );//esa linea de arriba(extr) o esta
                //dir.mkdirs(); //create folders where write files
                final File myPath = new File(getFilesDir(), fileName);
                FileOutputStream fos = null;
                try {
                    fos = openFileOutput(fileName, Context.MODE_PRIVATE);
                    b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();

                    MediaStore.Images.Media.insertImage(rootViewFinal.getContentResolver(), b, "Screen", "screen");
                }catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                Util.clienteActual.setVinculo_firma(myPath.toString());

                Util.showToast("Se guardo bien su firma",
                        rootViewFinal);


                finish();


            }
        });

    }
}

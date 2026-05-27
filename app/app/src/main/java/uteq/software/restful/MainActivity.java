package uteq.software.restful;

import android.os.Bundle;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private final String url = "https://reqres.in/api/collections/alumnos/records";
    private final String apiKey = "pub_bf56644bb321c3b811fdd148594b6f596de208551a5d8fc67e5e632f08e013a1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        EditText txtLista = findViewById(R.id.editTextTextMultiLineListadoEstudiantes);

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        StringBuilder texto = new StringBuilder();
                        JSONArray data = response.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++)
                            texto.append(agregarAlumnosALista(data.getJSONObject(i), i+1));

                        txtLista.setText(texto.toString());

                    } catch (Exception e) {
                        txtLista.setText("Error procesando datos:\n" + e.getMessage());
                    }
                },
                error -> txtLista.setText("Error API:\n" + error.getMessage())
        ){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("x-api-key", apiKey);
                return headers;
            }
        };

        queue.add(request);
    }

    private String agregarAlumnosALista(JSONObject jsonAlumno, int i) throws JSONException {
        StringBuilder strAlumno= new StringBuilder();
        JSONObject datosalumno = jsonAlumno.getJSONObject("data");

        strAlumno.append(i + ".- " + datosalumno.optString("nombres", "") + "\n");
        strAlumno.append("Paralelo: " +datosalumno.optString("paralelo", "") + "\n");
        strAlumno.append("Periodo: " +datosalumno.optString("periodoacademico", "") + "\n");
        strAlumno.append("Email: " +datosalumno.optString("correo", "") + "\n");
        strAlumno.append("Periodo: " +datosalumno.optString("periodoacademico", "") + "\n\n");

        return strAlumno.toString();
    }
}
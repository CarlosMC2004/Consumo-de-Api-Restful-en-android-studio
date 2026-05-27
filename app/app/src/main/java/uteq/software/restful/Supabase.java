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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Supabase extends AppCompatActivity {

    private final String url = "https://pvflnjfopgpddyfpvlfo.supabase.co/rest/v1/alumnos";
    private final String apiKey = "sb_publishable_X5k6JnHEaNy4t7343I_VBQ_0ZGKFygt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_supabase);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText txtLista = findViewById(R.id.editTextTextMultiLineAlumnos);

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        StringBuilder texto = new StringBuilder();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject alumno = response.getJSONObject(i);
                            texto.append(agregarAlumnosALista(alumno, i + 1));
                        }
                        txtLista.setText(texto.toString());
                    } catch (Exception e) {
                        txtLista.setText("Error procesando datos:\n" + e.getMessage());
                    }
                },
                error -> {
                    String msg = "Error API: " + error.getMessage();
                    if (error.networkResponse != null) {
                        msg += "\nCódigo: " + error.networkResponse.statusCode;
                    }
                    txtLista.setText(msg);
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("apikey", apiKey);
                headers.put("Authorization", "Bearer " + apiKey);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        queue.add(request);
    }

    private String agregarAlumnosALista(JSONObject alumno, int i) throws JSONException {
        StringBuilder sb = new StringBuilder();

        sb.append(i).append(".- ")
                .append(alumno.optString("nombres_completos", "Sin nombre")).append("\n");
        sb.append("Cédula: ").append(alumno.optString("cedula", "N/A")).append("\n");
        sb.append("Correo Institucional: ").append(alumno.optString("correo_institucional", "N/A")).append("\n");
        sb.append("Correo Microsoft: ").append(alumno.optString("correo_microsoft", "N/A")).append("\n");
        sb.append("\n");

        return sb.toString();
    }
}
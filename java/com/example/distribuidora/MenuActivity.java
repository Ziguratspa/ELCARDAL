package com.example.distribuidora;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MenuActivity extends AppCompatActivity {

    private static final int LOCATION_REQUEST_CODE = 1001;
    private FusedLocationProviderClient fusedLocationClient;
    private TextView resultText;
    private Button btnCalcular;

    // Coordenadas distribuidora
    private final double distLat = -33.4298;
    private final double distLng = -70.6495;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        resultText = findViewById(R.id.txtResultado);
        btnCalcular = findViewById(R.id.btnCalcular);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        btnCalcular.setOnClickListener(v -> {
                EditText editTextCompra = findViewById(R.id.editTextCompra);
                String compraStr = editTextCompra.getText().toString();
                int valorCompra = 0;
                if (!compraStr.isEmpty()) {
                    try {
                        valorCompra = Integer.parseInt(compraStr);
                    } catch (NumberFormatException e) {
                        Toast.makeText(MenuActivity.this, "Ingrese un valor válido", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    Toast.makeText(MenuActivity.this, "Debe ingresar el valor de la compra", Toast.LENGTH_SHORT).show();
                    return;
                }
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            } else {
                obtenerUbicacionYCalcular();
            }
        });
    }

    private void obtenerUbicacionYCalcular() {
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    double userLat = location.getLatitude();
                    double userLng = location.getLongitude();

                    // Guardar en Firebase
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("usuarios").child(uid);
                    ref.child("lat").setValue(userLat);
                    ref.child("lng").setValue(userLng);

                    // Calcular distancia (Haversine)
                    double distanciaKm = calcularDistancia(userLat, userLng, distLat, distLng);

                    // Calcular costo despacho según reglas de negocio (ejemplo con compra de 40000)
                    int compra = 40000; // <- aquí puedes reemplazar por valor real de la compra
                    double costo = calcularCostoDespacho(compra, distanciaKm);

                    resultText.setText("Distancia: " + String.format("%.2f", distanciaKm) + " km\nCosto despacho: $" + costo);
                } else {
                    Toast.makeText(MenuActivity.this, "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private double calcularDistancia(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371; // Radio de la tierra en km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    private double calcularCostoDespacho(int compra, double distanciaKm) {
        if (compra >= 50000) {
            if (distanciaKm <= 20) {
                return 0; // Gratis
            } else {
                return 300 * distanciaKm; // Cobro extra si pasa de 20 km
            }
        } else if (compra >= 25000) {
            return 150 * distanciaKm;
        } else {
            return 300 * distanciaKm;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                obtenerUbicacionYCalcular();
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

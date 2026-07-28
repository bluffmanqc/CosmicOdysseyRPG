package com.cosmicodyssey.rpg.activities;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cosmicodyssey.rpg.R;
import com.cosmicodyssey.rpg.data.DataManager;
import com.cosmicodyssey.rpg.models.Party;
import com.cosmicodyssey.rpg.models.Planet;
import com.cosmicodyssey.rpg.models.StarSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GalaxyMapActivity extends AppCompatActivity {
    private GalaxyMapView mapView;
    private TextView systemInfoText;
    private Button travelBtn;
    private Button scanBtn;
    private Button backBtn;

    private Party party;
    private DataManager dataManager;
    private List<StarSystem> systems;
    private StarSystem selectedSystem;
    private Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galaxy_map);

        dataManager = new DataManager(this);
        party = dataManager.loadParties().isEmpty() ? null : dataManager.loadParties().get(0);
        if (party == null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        random = new Random();
        systems = generateSystems();

        mapView = findViewById(R.id.mapContainer);
        systemInfoText = findViewById(R.id.systemInfo);
        travelBtn = findViewById(R.id.travelBtn);
        scanBtn = findViewById(R.id.scanBtn);

        mapView.setSystems(systems, party.getCurrentSystem());

        travelBtn.setOnClickListener(v -> travelToSystem());
        scanBtn.setOnClickListener(v -> scanSystem());
        

        mapView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                float x = event.getX();
                float y = event.getY();
                for (StarSystem system : systems) {
                    float dx = x - (float)system.getX();
                    float dy = y - (float)system.getY();
                    if (Math.sqrt(dx * dx + dy * dy) < 30) {
                        selectedSystem = system;
                        updateSystemInfo();
                        mapView.setSelectedSystem(system);
                        return true;
                    }
                }
            }
            return false;
        });
    }

    private List<StarSystem> generateSystems() {
        List<StarSystem> systems = new ArrayList<>();
        String[] names = {"Systeme Sol Prime", "Nebuleuse d'Orion", "Galaxie d'Andromede",
                "Trou Noir de Cygnus", "Nebuleuse de la Rosette", "Galaxie du Triangle",
                "Amas de la Vierge", "Quasar 3C 273", "Nebuleuse de l'Aigle",
                "Galaxie du Cigare"};

        for (int i = 0; i < names.length; i++) {
            StarSystem system = new StarSystem();
            system.setName(names[i]);
            system.setX(100 + random.nextInt(800));
            system.setY(100 + random.nextInt(1200));
            system.setDiscovered(i < 3);
            system.setThreatLevel(random.nextInt(10) + 1);
            system.setThreatLevel(random.nextInt(10) + 1);

            List<Planet> planets = new ArrayList<>();
            int planetCount = 2 + random.nextInt(5);
            for (int j = 0; j < planetCount; j++) {
                Planet planet = new Planet();
                planet.setName("Planete " + (j + 1) + " de " + system.getName());
                planet.setBiome(random.nextBoolean() ? "Terrestre" : "Gazeuse");
                planet.setDiscovered(i < 3);
                planet.setImageUrl(null);
                planets.add(planet);
            }
            system.setPlanets(planets);
            systems.add(system);
        }

        return systems;
    }

    private void updateSystemInfo() {
        if (selectedSystem == null) {
            systemInfoText.setText("Selectionne un systeme");
            return;
        }

        StringBuilder info = new StringBuilder();
        info.append(selectedSystem.getName()).append("\n");
        info.append("Danger: ").append(selectedSystem.getThreatLevel()).append("/10\n");
        info.append("Ressources: ").append(selectedSystem.getThreatLevel()).append("/10\n");
        info.append("Decouvert: ").append(selectedSystem.isDiscovered() ? "Oui" : "Non").append("\n");
        info.append("Planetes: ").append(selectedSystem.getPlanets().size());

        systemInfoText.setText(info.toString());
    }

    private void travelToSystem() {
        if (selectedSystem == null) {
            Toast.makeText(this, "Selectionne un systeme d'abord", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!selectedSystem.isDiscovered()) {
            Toast.makeText(this, "Systeme non decouvert ! Scanne-le d'abord.", Toast.LENGTH_SHORT).show();
            return;
        }
        party.setCurrentSystem(selectedSystem.getName());
        if (!selectedSystem.getPlanets().isEmpty()) {
            party.setCurrentPlanet(selectedSystem.getPlanets().get(0).getName());
        }
        dataManager.saveParty(party);
        Toast.makeText(this, "Voyage vers " + selectedSystem.getName() + " !", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void scanSystem() {
        if (selectedSystem == null) return;
        selectedSystem.setDiscovered(true);
        for (Planet p : selectedSystem.getPlanets()) {
            p.setDiscovered(true);
            p.setImageUrl(null);
        }
        mapView.invalidate();
        updateSystemInfo();
        Toast.makeText(this, "Systeme scanne !", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    class GalaxyMapView extends View {
        private List<StarSystem> systems;
        private String currentSystemName;
        private StarSystem selectedSystem;
        private Paint systemPaint;
        private Paint discoveredPaint;
        private Paint undiscoveredPaint;
        private Paint selectedPaint;
        private Paint textPaint;
        private Paint connectionPaint;

        public GalaxyMapView(android.content.Context context) {
            super(context);
            initPaints();
        }

        public GalaxyMapView(android.content.Context context, android.util.AttributeSet attrs) {
            super(context, attrs);
            initPaints();
        }

        private void initPaints() {
            systemPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            systemPaint.setColor(Color.WHITE);
            systemPaint.setStyle(Paint.Style.FILL);

            discoveredPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            discoveredPaint.setColor(Color.GREEN);
            discoveredPaint.setStyle(Paint.Style.FILL);

            undiscoveredPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            undiscoveredPaint.setColor(Color.GRAY);
            undiscoveredPaint.setStyle(Paint.Style.FILL);

            selectedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            selectedPaint.setColor(Color.YELLOW);
            selectedPaint.setStyle(Paint.Style.STROKE);
            selectedPaint.setStrokeWidth(4);

            textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize(24);

            connectionPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            connectionPaint.setColor(Color.parseColor("#444444"));
            connectionPaint.setStrokeWidth(2);
        }

        public void setSystems(List<StarSystem> systems, String currentSystemName) {
            this.systems = systems;
            this.currentSystemName = currentSystemName;
            invalidate();
        }

        public void setSelectedSystem(StarSystem system) {
            this.selectedSystem = system;
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (systems == null) return;

            for (int i = 0; i < systems.size() - 1; i++) {
                StarSystem s1 = systems.get(i);
                StarSystem s2 = systems.get(i + 1);
                canvas.drawLine((float)s1.getX(), (float)s1.getY(), (float)s2.getX(), (float)s2.getY(), connectionPaint);
            }

            for (StarSystem system : systems) {
                Paint paint = system.isDiscovered() ? discoveredPaint : undiscoveredPaint;
                canvas.drawCircle((float)system.getX(), (float)system.getY(), 15f, paint);

                if (system.getName().equals(currentSystemName)) {
                    canvas.drawCircle((float)system.getX(), (float)system.getY(), 20f, selectedPaint);
                }

                canvas.drawText(system.getName(), (float)system.getX() - 40f, (float)system.getY() - 25f, textPaint);
            }
        }
    }
}

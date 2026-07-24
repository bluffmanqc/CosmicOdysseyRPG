package com.cosmicodyssey.rpg.activities;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.cosmicodyssey.rpg.R;
import com.cosmicodyssey.rpg.ai.GameMasterAI;
import com.cosmicodyssey.rpg.data.DataManager;
import com.cosmicodyssey.rpg.models.Planet;
import com.cosmicodyssey.rpg.models.StarSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GalaxyMapActivity extends AppCompatActivity {
    private FrameLayout mapContainer;
    private TextView systemInfo;
    private GalaxyMapView mapView;
    private DataManager dataManager;
    private GameMasterAI ai;
    private List<StarSystem> systems;
    private StarSystem selectedSystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galaxy_map);

        dataManager = new DataManager(this);
        ai = new GameMasterAI(this);
        mapContainer = findViewById(R.id.mapContainer);
        systemInfo = findViewById(R.id.systemInfo);

        systems = generateGalaxy();
        mapView = new GalaxyMapView(this, systems);
        mapContainer.addView(mapView);

        findViewById(R.id.travelBtn).setOnClickListener(v -> travelToSelected());
        findViewById(R.id.scanBtn).setOnClickListener(v -> scanSystem());
    }

    private List<StarSystem> generateGalaxy() {
        List<StarSystem> list = new ArrayList<>();
        Random random = new Random();
        
        String[] starTypes = {"G-Type", "Red Giant", "Blue Supergiant", "White Dwarf", "Neutron Star", "Black Hole"};
        String[] factions = {"Fédération Galactique", "Empire Vorak", "Corporation Nexus", "Clans Libres", "Inconnue"};

        for (int i = 0; i < 50; i++) {
            StarSystem sys = new StarSystem();
            sys.setName(generateSystemName(random));
            sys.setStarType(starTypes[random.nextInt(starTypes.length)]);
            sys.setX(random.nextDouble() * 2000 - 1000);
            sys.setY(random.nextDouble() * 2000 - 1000);
            sys.setFaction(factions[random.nextInt(factions.length)]);
            sys.setThreatLevel(random.nextInt(10) + 1);
            sys.setDiscovered(i < 5);

            int planetCount = random.nextInt(5) + 1;
            for (int p = 0; p < planetCount; p++) {
                Planet planet = new Planet();
                planet.setName(sys.getName() + " " + (p + 1));
                planet.setSystemId(sys.getId());
                String[] biomes = {"Désert", "Jungle", "Glace", "Volcanique", "Océan", "Forêt", "Ruines"};
                planet.setBiome(biomes[random.nextInt(biomes.length)]);
                planet.setDangerLevel(random.nextInt(10) + 1);
                planet.setDiscovered(sys.isDiscovered());
                sys.getPlanets().add(planet);
            }

            list.add(sys);
        }

        list.get(0).setDiscovered(true);
        list.get(0).setConnected(true);
        return list;
    }

    private String generateSystemName(Random random) {
        String[] prefixes = {"Alpha", "Beta", "Gamma", "Delta", "Omega", "Neo", "Xeno", "Hyper"};
        String[] suffixes = {"Centauri", "Draconis", "Cygni", "Eridani", "Andromedae", "Pegasi", "Orionis"};
        return prefixes[random.nextInt(prefixes.length)] + " " + suffixes[random.nextInt(suffixes.length)];
    }

    private void travelToSelected() {
        if (selectedSystem == null) {
            Toast.makeText(this, "Sélectionne un système", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!selectedSystem.isDiscovered()) {
            Toast.makeText(this, "Système non découvert ! Scanne d'abord.", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, "Voyage vers " + selectedSystem.getName() + "...", Toast.LENGTH_SHORT).show();
    }

    private void scanSystem() {
        if (selectedSystem == null) return;
        selectedSystem.setDiscovered(true);
        for (Planet p : selectedSystem.getPlanets()) {
            p.setDiscovered(true);
            p.setImageUrl(ai.generatePlanetImageUrl(p.getName(), p.getBiome()));
        }
        mapView.invalidate();
        updateSystemInfo();
        Toast.makeText(this, "Système scanné !", Toast.LENGTH_SHORT).show();
    }

    private void updateSystemInfo() {
        if (selectedSystem == null) return;
        StringBuilder info = new StringBuilder();
        info.append("⭐ ").append(selectedSystem.getName()).append("\n");
        info.append("Type: ").append(selectedSystem.getStarType()).append("\n");
        info.append("Faction: ").append(selectedSystem.getFaction()).append("\n");
        info.append("Menace: ").append(selectedSystem.getThreatLevel()).append("/10\n");
        info.append("Planètes: ").append(selectedSystem.getPlanets().size()).append("\n");
        for (Planet p : selectedSystem.getPlanets()) {
            if (p.isDiscovered()) {
                info.append("  🪐 ").append(p.getName()).append(" (").append(p.getBiome()).append(")\n");
            } else {
                info.append("  ❓ Inconnue\n");
            }
        }
        systemInfo.setText(info.toString());
    }

    class GalaxyMapView extends View {
        private List<StarSystem> systems;
        private Paint starPaint;
        private Paint linePaint;
        private Paint textPaint;
        private Paint fogPaint;
        private float offsetX = 0, offsetY = 0;
        private float scale = 1f;
        private float lastTouchX, lastTouchY;

        public GalaxyMapView(Context context, List<StarSystem> systems) {
            super(context);
            this.systems = systems;

            starPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            linePaint.setColor(Color.parseColor("#00ff88"));
            linePaint.setStrokeWidth(2f);
            linePaint.setAlpha(80);

            textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize(24f);

            fogPaint = new Paint();
            fogPaint.setColor(Color.parseColor("#0a0a1a"));
            fogPaint.setAlpha(200);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawColor(Color.parseColor("#050510"));

            float cx = getWidth() / 2f + offsetX;
            float cy = getHeight() / 2f + offsetY;

            // Draw connections
            for (int i = 0; i < systems.size(); i++) {
                for (int j = i + 1; j < systems.size(); j++) {
                    StarSystem s1 = systems.get(i);
                    StarSystem s2 = systems.get(j);
                    double dist = Math.hypot(s1.getX() - s2.getX(), s1.getY() - s2.getY());
                    if (dist < 300 && s1.isDiscovered() && s2.isDiscovered()) {
                        canvas.drawLine(
                                cx + (float)s1.getX() * scale, cy + (float)s1.getY() * scale,
                                cx + (float)s2.getX() * scale, cy + (float)s2.getY() * scale,
                                linePaint);
                    }
                }
            }

            // Draw systems
            for (StarSystem sys : systems) {
                float x = cx + (float)sys.getX() * scale;
                float y = cy + (float)sys.getY() * scale;

                if (!sys.isDiscovered()) {
                    starPaint.setColor(Color.parseColor("#333333"));
                    canvas.drawCircle(x, y, 8 * scale, starPaint);
                    continue;
                }

                // Star glow
                starPaint.setColor(getStarColor(sys.getStarType()));
                starPaint.setAlpha(60);
                canvas.drawCircle(x, y, 25 * scale, starPaint);
                starPaint.setAlpha(255);
                canvas.drawCircle(x, y, 12 * scale, starPaint);

                // Selection highlight
                if (sys == selectedSystem) {
                    starPaint.setColor(Color.WHITE);
                    starPaint.setStyle(Paint.Style.STROKE);
                    starPaint.setStrokeWidth(3f);
                    canvas.drawCircle(x, y, 18 * scale, starPaint);
                    starPaint.setStyle(Paint.Style.FILL);
                }

                // Name
                if (scale > 0.5f) {
                    canvas.drawText(sys.getName(), x + 20 * scale, y, textPaint);
                }
            }
        }

        private int getStarColor(String type) {
            switch (type) {
                case "Red Giant": return Color.parseColor("#ff4444");
                case "Blue Supergiant": return Color.parseColor("#4488ff");
                case "White Dwarf": return Color.parseColor("#ffffff");
                case "Neutron Star": return Color.parseColor("#00ffff");
                case "Black Hole": return Color.parseColor("#8800ff");
                default: return Color.parseColor("#ffdd44");
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float cx = getWidth() / 2f + offsetX;
            float cy = getHeight() / 2f + offsetY;

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastTouchX = event.getX();
                    lastTouchY = event.getY();

                    // Check system selection
                    for (StarSystem sys : systems) {
                        float sx = cx + (float)sys.getX() * scale;
                        float sy = cy + (float)sys.getY() * scale;
                        float dist = (float) Math.hypot(event.getX() - sx, event.getY() - sy);
                        if (dist < 40 * scale) {
                            selectedSystem = sys;
                            updateSystemInfo();
                            invalidate();
                            return true;
                        }
                    }
                    return true;

                case MotionEvent.ACTION_MOVE:
                    offsetX += event.getX() - lastTouchX;
                    offsetY += event.getY() - lastTouchY;
                    lastTouchX = event.getX();
                    lastTouchY = event.getY();
                    invalidate();
                    return true;
            }
            return super.onTouchEvent(event);
        }
    }
}

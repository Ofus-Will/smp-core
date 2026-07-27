package org.ofus.core.feature.hologram;

import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;
import org.ofus.core.util.Texts;

import java.util.ArrayList;
import java.util.List;

public class Hologram {

    private static final double LINE_SPACING = 0.27;

    private final String id;
    private final List<TextDisplay> displays;

    private List<String> lines;
    private Location loc;
    private Entity mount;
    private double mountOffset;

    public Hologram(Location loc, String... lines) {
        this(null, loc, lines);
    }

    public Hologram(String id, Location loc, String... lines) {
        this.id = id;
        this.loc = loc.clone();
        this.lines = List.of();
        this.displays = new ArrayList<>();

        update(lines);
    }

    public void update(String... lines) {
        this.lines = List.of(lines);

        updateDisplayCount();
        updateDisplayText();
        positionDisplays();
    }

    private void updateDisplayText() {
        for (int i = 0; i < lines.size(); i++) {
            displays.get(i).text(Texts.parse(lines.get(i)));
        }
    }

    private void updateDisplayCount() {
        while (displays.size() < lines.size()) {
            displays.add(createTextDisplay(displays.size()));
        }

        while (displays.size() > lines.size()) {
            displays.remove(displays.size() - 1).remove();
        }
    }

    private void positionDisplays() {
        for (int i = 0; i < displays.size(); i++) {
            TextDisplay display = displays.get(i);

            if (mount != null) {
                mountDisplay(display, i);
                continue;
            }

            display.teleport(getLineLocation(i));
        }
    }

    private Location getLineLocation(int index) {
        return loc.clone().subtract(0, index * LINE_SPACING, 0);
    }

    private TextDisplay createTextDisplay(int index) {
        Location location = getLineLocation(index);
        TextDisplay display = (TextDisplay) location.getWorld().spawnEntity(location, EntityType.TEXT_DISPLAY);
        style(display);

        if (mount != null) mountDisplay(display, index);
        return display;
    }

    private void mountDisplay(TextDisplay display, int index) {
        display.setTransformation(new Transformation(
                new Vector3f(0, (float) (mountOffset - index * LINE_SPACING), 0),
                new AxisAngle4f(),
                new Vector3f(1, 1, 1),
                new AxisAngle4f()
        ));

        if (!mount.getPassengers().contains(display)) {
            mount.addPassenger(display);
        }
    }

    private static void style(TextDisplay display) {
        display.setBillboard(Display.Billboard.CENTER);
        display.setSeeThrough(true);
        display.setDefaultBackground(false);
        display.setShadowed(false);
        display.setPersistent(false);
    }

    public void teleport(Location loc) {
        this.loc = loc.clone();
        this.mount = null;
        positionDisplays();
    }

    public void mount(Entity entity, double offset) {
        this.loc = entity.getLocation();
        this.mount = entity;
        this.mountOffset = offset;
        positionDisplays();
    }

    public void setTextOpacity(byte opacity) {
        displays.forEach(display -> display.setTextOpacity(opacity));
    }

    public void setTeleportInterpolation(int durationTicks) {
        displays.forEach(display -> display.setTeleportDuration(durationTicks));
    }

    public void remove() {
        displays.forEach(TextDisplay::remove);
        displays.clear();
    }

    public String getId() {
        return id;
    }

    public HologramData toData() {
        if (id == null) {
            throw new IllegalStateException("Temporary holograms cannot be saved");
        }

        return new HologramData(id, loc, lines);
    }
}
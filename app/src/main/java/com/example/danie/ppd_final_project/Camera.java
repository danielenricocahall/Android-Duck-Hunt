package com.example.danie.ppd_final_project;


import java.util.Vector;

/**
 * Created by Aaron on 12/12/2017.
 */

public class Camera {

    private static Vector2D screenSize;
    private static float yZeroPx;
    private static float slope;

    public static void init(Vector2D _screenSize) {
        screenSize = _screenSize;
        yZeroPx = screenSize.y * 0.6f;
        // The slope would be calculated as (yZeroPx - 0) / (0 - 1) or just -yZeroPx
        slope = -yZeroPx;
    }

    public static Vector2D worldToScreen(Vector2D position) {
        float screenX = worldXToScreenX(position.x);
        float screenY = worldYToScreenY(position.y);
        return new Vector2D(screenX, screenY);
    }

    public static Vector2D screenToWorld(Vector2D position) {
        float worldX = screenXToWorldX(position.x);
        float worldY = screenYToWorldY(position.y);
        return new Vector2D(worldX, worldY);
    }

    public static float screenXToWorldX(float x) {
        return x / screenSize.x;
    }

    public static float screenYToWorldY(float y) {
        return (y - yZeroPx) / slope;
    }

    public static float worldXToScreenX(float x) {
        return x * screenSize.x;
    }

    public static float worldYToScreenY(float y) {
        return slope * y + yZeroPx;
    }
}

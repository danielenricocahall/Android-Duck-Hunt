package com.example.danie.ppd_final_project;


import android.graphics.RectF;

/**
 * Created by Aaron on 12/12/2017.
 */

public class Camera {

    /**
     * @brief Stores the size of the screen
     */
    private static Vector2D screenSize;

    /**
     * @brief y-intercept used to translate between pixel to world coordinates
     * in the y direction
     */
    private static float yZeroPx;

    /**
     * @brief Slope used for translating between pixels to world coordinates
     */
    private static float slope;

    /**
     * @brief Initializes the camera class. Stores the camera size,
     * calculates the slope and the y-intercept
     * @param _screenSize Size of the screen
     */
    public static void init(Vector2D _screenSize) {
        screenSize = _screenSize;
        yZeroPx = screenSize.y * 0.6f;
        // The slope would be calculated as (yZeroPx - 0) / (0 - 1) or just -yZeroPx
        slope = -yZeroPx;
    }

    /**
     * @brief Converts a point from world coordinates to pixel coordinates
     * @param position The point in world coordinates
     * @return The point in pixel coordinates
     */
    public static Vector2D worldToScreen(Vector2D position) {
        float screenX = worldXToScreenX(position.x);
        float screenY = worldYToScreenY(position.y);
        return new Vector2D(screenX, screenY);
    }

    /**
     * @brief Converts a point from pixel coordinates to world coordinates
     * @param position The point in pixel coordinates
     * @return The point in world coordinates
     */
    public static Vector2D screenToWorld(Vector2D position) {
        float worldX = screenXToWorldX(position.x);
        float worldY = screenYToWorldY(position.y);
        return new Vector2D(worldX, worldY);
    }

    public static RectF screenRectToWorldRect(RectF rect) {
        return new RectF(screenXToWorldX(rect.left), screenYToWorldY(rect.top),
                screenXToWorldX(rect.right), screenYToWorldY(rect.bottom));
    }

    public static RectF worldRectToScreenRect(RectF rect) {
        return new RectF(worldXToScreenX(rect.left), worldYToScreenY(rect.top),
                worldXToScreenX(rect.right), worldYToScreenY(rect.bottom));
    }

    /**
     * @brief Converts an x coordinate from pixels to world space
     * @param x The x coordinate in pixels
     * @return The x coordinate in world coordinates
     */
    public static float screenXToWorldX(float x) {
        return x / screenSize.x;
    }

    /**
     * @brief Converts a y coordinate from pixels to world space
     * @param y The y coordinate in pixels
     * @return The x coordinate in world coordinates
     */
    public static float screenYToWorldY(float y) {
        return (y - yZeroPx) / slope;
    }

    /**
     * @brief Converts an x coordinate from world space to pixels
     * @param x The y coordinate in world space
     * @return The y coordinate in pixels
     */
    public static float worldXToScreenX(float x) {
        return x * screenSize.x;
    }

    /**
     * @brief Converts a y coordinate from world space to pixels
     * @param y The y coordinate in world space
     * @return The y coordinate in pixels
     */
    public static float worldYToScreenY(float y) {
        return slope * y + yZeroPx;
    }

    /**
     * @brief Converts a screen x distance to a world x distance
     * @param x Distance in pixels
     * @return Distance in world coordinates
     */
    public static float screenXDistToWorldXDist(float x) {
        return screenXToWorldX(x);
    }

    /**
     * @brief Converts screen y distance to a world y distance
     * @param y Distance in pixels
     * @return Distance in world coordinates
     */
    public static float screenYDistToWorldYDist(float y) {
        return Math.abs(y / slope);
    }

    /**
     * @brief Converts world x distance to a screen x distance
     * @param x Distance in world coordinates
     * @return Distance in pixels
     */
    public static float worldXDistToScreenXDist(float x) {
        return worldXToScreenX(x);
    }

    /**
     * @brief Converts a world y distance to a screen y distance
     * @param y Distance in world coordinates
     * @return Distance in pixels
     */
    public static float worldYDistToScreenYDist(float y) {
        return Math.abs(y * slope);
    }
}

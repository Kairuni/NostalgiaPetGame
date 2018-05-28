package games.wantz.spencer.nostalgiapetgame.gameplay.drawing;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * A class that holds sprite sheets that are divided into fixed sized cells (see drawable/pets_and_icons.png)
 *
 * @author Keegan Wantz wantzkt@uw.edu
 * @version 0.1, 11 May 2018
 */
public class SpriteSheet {
    /**
     * The dimensions of the bitmap.
     */
    private final int mWidth, mHeight;
    /**
     * The width and height of the frames.
     */
    private final int mFrameWidth, mFrameHeight;
    /**
     * A calculated value that stores the number of frames in a row.
     */
    private final int mFramesPerRow;

    /**
     * Gets the scale of the sprite sheet.
     * @return the scale.
     */
    public float getScale() {
        return mScale;
    }

    /**
     * Sets the scale of the sprite sheet.
     * @param scale The scale to set the sheet to.
     */
    public void setScale(float scale) {
        mScale = scale;
    }

    /** The multiplier to scale this sprite sheet. */
    private float mScale;

    /** The bitmap that this spritesheet is based upon. */
    private Bitmap mBitmap;

    /**
     * Creates a new spritesheet with the provided bitmap, frame dimensions, and scale.
     *
     * @param theBmp      The bitmap to base the sheet on.
     * @param frameWidth  The width of any given frame.
     * @param frameHeight The height of any given frame.
     * @param scale       The scale to multiply by when drawing this sheet.
     */
    public SpriteSheet(Bitmap theBmp, int frameWidth, int frameHeight, float scale) {
        mBitmap = theBmp;
        mWidth = theBmp.getWidth();
        mHeight = theBmp.getHeight();
        mFrameWidth = frameWidth;
        mFrameHeight = frameHeight;
        mScale = scale;

        mFramesPerRow = mWidth / mFrameWidth;
    }

    /**
     * Draws a requested frame of this sprite sheet to a canvas.
     * @param canvas The canvas to draw to.
     * @param dX The top left of the destination rect, x value.
     * @param dY The top left of the destination rect, y value.
     * @param frame The frame to draw.
     */
    public void Draw(Canvas canvas, int dX, int dY, int frame) {
        int frameX = frame % mFramesPerRow;
        int frameY = frame / mFramesPerRow;

        int sourceX = frameX * mFrameWidth;
        int sourceY = frameY * mFrameHeight;

        // Calculate where the desired sprite it.
        Rect source = new Rect(sourceX, sourceY, sourceX + mFrameWidth, sourceY + mFrameHeight);
        // Destination
        // Casting back to int for scale reasons.
        Rect destination = new Rect(dX, dY, dX + (int)(mFrameWidth * mScale), dY + (int)(mFrameHeight * mScale));

        // Disable filtering of scaled bitmaps. We want the blocky pixels.
        canvas.drawBitmap(mBitmap, source, destination, new Paint(0));
    }
}
package games.wantz.spencer.nostalgiapetgame.drawing;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class SpriteSheet {
    private int mWidth, mHeight;
    private int mFrameWidth, mFrameHeight;
    private int mFramesPerRow;
    private float mScale;

    private Bitmap mBitmap;

    public SpriteSheet(Bitmap theBmp, int frameWidth, int frameHeight, float scale) {
        mBitmap = theBmp;
        mWidth = theBmp.getWidth();
        mHeight = theBmp.getHeight();
        mFrameWidth = frameWidth;
        mFrameHeight = frameHeight;
        mScale = scale;

        mFramesPerRow = mWidth / mFrameWidth;
    }

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

        // Disable filtering of scaled bitmaps.
        canvas.drawBitmap(mBitmap, source, destination, new Paint(0));
    }
}

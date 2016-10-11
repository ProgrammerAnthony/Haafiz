package support.ui.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Provides static functions to decode bitmaps at the optimal size
 */
public class BitmapUtil {
  private BitmapUtil() {}

  /**
   * Returns Width or Height of the picture, depending on which size is smaller. Doesn't actually
   * decode the picture, so it is pretty efficient to run.
   */
  public static int getSmallerExtentFromBytes(byte[] bytes) {
    final BitmapFactory.Options options = new BitmapFactory.Options();

    // don't actually decode the picture, just return its bounds
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

    // test what the best sample size is
    return Math.min(options.outWidth, options.outHeight);
  }

  /**
   * Finds the optimal sampleSize for loading the picture
   * @param originalSmallerExtent Width or height of the picture, whichever is smaller
   * @param targetExtent Width or height of the target view, whichever is bigger.
   *
   * If either one of the parameters is 0 or smaller, no sampling is applied
   */
  public static int findOptimalSampleSize(int originalSmallerExtent, int targetExtent) {
    // If we don't know sizes, we can't do sampling.
    if (targetExtent < 1) return 1;
    if (originalSmallerExtent < 1) return 1;

    // Test what the best sample size is. To do that, we find the sample size that gives us
    // the best trade-off between resulting image size and memory requirement. We allow
    // the down-sampled image to be 20% smaller than the target size. That way we can get around
    // unfortunate cases where e.g. a 720 picture is requested for 362 and not down-sampled at
    // all. Why 20%? Why not. Prove me wrong.
    int extent = originalSmallerExtent;
    int sampleSize = 1;
    while ((extent >> 1) >= targetExtent * 0.8f) {
      sampleSize <<= 1;
      extent >>= 1;
    }

    return sampleSize;
  }

  /**
   * Decodes the bitmap with the given sample size
   */
  public static Bitmap decodeBitmapFromBytes(byte[] bytes, int sampleSize) {
    final BitmapFactory.Options options;
    if (sampleSize <= 1) {
      options = null;
    } else {
      options = new BitmapFactory.Options();
      options.inSampleSize = sampleSize;
    }
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
  }

  /**
   * Retrieves a copy of the specified drawable resource, rotated by a specified angle.
   *
   * @param resources The current resources.
   * @param resourceId The resource ID of the drawable to rotate.
   * @param angle The angle of rotation.
   * @return Rotated drawable.
   */
  public static Drawable getRotatedDrawable(
      android.content.res.Resources resources, int resourceId, float angle) {

    // Get the original drawable and make a copy which will be rotated.
    Bitmap original = BitmapFactory.decodeResource(resources, resourceId);
    Bitmap rotated = Bitmap.createBitmap(
        original.getWidth(), original.getHeight(), Bitmap.Config.ARGB_8888);

    // Perform the rotation.
    Canvas tempCanvas = new Canvas(rotated);
    tempCanvas.rotate(angle, original.getWidth()/2, original.getHeight()/2);
    tempCanvas.drawBitmap(original, 0, 0, null);

    return new BitmapDrawable(resources,rotated);
  }
}

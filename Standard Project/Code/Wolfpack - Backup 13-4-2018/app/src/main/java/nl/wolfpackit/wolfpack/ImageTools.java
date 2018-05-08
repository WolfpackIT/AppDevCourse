package nl.wolfpackit.wolfpack;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class ImageTools{
    public static String getStringFromImage(Bitmap image){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bytes = stream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
    public static Bitmap getImageFromString(String string){
        byte[] bytes = Base64.decode(string, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
    public static void makeImageCircular(Bitmap image, ImageResultListener irl){
        makeImageCircular(image, 0, 0, irl);
    }
    @SuppressLint("StaticFieldLeak")
    public static void makeImageCircular(Bitmap image, final int borderWidth, final int borderIntColor, final ImageResultListener irl){
        (new AsyncTask<Bitmap, Void, Bitmap>(){
            protected Bitmap doInBackground(Bitmap... image){
                Bitmap newImage = image[0].copy(Bitmap.Config.ARGB_8888, true);
                newImage.setHasAlpha(true);
                Canvas canvas = new Canvas(newImage);

                int width = canvas.getWidth();
                int height = canvas.getHeight();
                int radius = Math.min(width, height) / 2 - borderWidth;
                int borderRadius = radius + borderWidth;
                int radSq = radius * radius;
                int borderRadSq = borderRadius * borderRadius;

                Paint color = new Paint();
                color.setColor(Color.TRANSPARENT);
                color.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));

                Paint borderColor = new Paint();
                borderColor.setColor(borderIntColor);
                borderColor.setAlpha(Color.alpha(borderIntColor));
                borderColor.setStyle(Paint.Style.FILL);
                borderColor.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

                //loop through pixels and turn them transparent when outside of the circle
                for (int x = 0; x < width; x++) {
                    int dX = x - width / 2;
                    for (int y = 0; y < height; y++) {
                        int dY = y - height / 2;
                        int dist = dX * dX + dY * dY;
                        if (dist > borderRadSq) {
                            canvas.drawPoint(x, y, color);
                        } else if (dist > radSq) {
                            canvas.drawPoint(x, y, borderColor);
                        }
                    }
                }
                return newImage;
            }
            protected void onPostExecute(Bitmap result) {
                irl.onImageRecieve(result);
            }
        }).execute(image);
    }
    @SuppressLint("StaticFieldLeak")
    public static void downloadImage(String url, final ImageResultListener irl){
        (new AsyncTask<String, Void, Bitmap>(){
            protected Bitmap doInBackground(String... urls) {
                String urldisplay = urls[0];
                Bitmap mIcon = null;
                try {
                    InputStream in = new java.net.URL(urldisplay).openStream();
                    return BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
                return mIcon;
            }

            protected void onPostExecute(Bitmap result) {
                irl.onImageRecieve(result);
            }
        }).execute(url);
    }
    public interface ImageResultListener{
        void onImageRecieve(Bitmap image);
    }
}

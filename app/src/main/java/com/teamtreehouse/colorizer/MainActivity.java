package com.teamtreehouse.colorizer;

import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    int[] imageResIds = {R.drawable.cuba1, R.drawable.cuba2, R.drawable.cuba3};
    int imageIndex = 0;
    boolean color = true;
    boolean red = true;
    boolean green = true;
    boolean blue = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView)findViewById(R.id.imageView);
        loadImage();
    }

    //glide library will prevent an out of memory error
    private void loadImage() {
        Glide.with(this).load(imageResIds[imageIndex]).into(imageView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.options_menu, menu);

        Drawable nextImageDrawable = menu.findItem(R.id.nextImage).getIcon();
        nextImageDrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);

        //makes the boxes checked.
        menu.findItem(R.id.red).setChecked(red);
        menu.findItem(R.id.green).setChecked(green);
        menu.findItem(R.id.blue).setChecked(blue);

        //colorGroup is to group the colors in the menu together so if the color is changed off the colors are unable to be selected.
        menu.setGroupVisible(R.id.colorGroup, color);

        //MenuItem menuItem = menu.add("NextImage");
        //this will make sure the item is always shown on the action menu.
        //menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        //menuItem.setIcon(R.drawable.ic_add_a_photo_black_24dp);
        //this will make every non transparent pixel be turned to white to give us a white icon
        //this is a color filter.
        //menuItem.getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        //this will help us know which menu item was selected.
        switch (item.getItemId())
        {
            case R.id.nextImage:
                imageIndex++;
                if (imageIndex >= imageResIds.length)
                    imageIndex = 0;
                loadImage();
                break;
            case R.id.color:
                color = !color;
                //this will update the photo color
                updateSaturation();
                invalidateOptionsMenu();
                break;
            case R.id.red:
                red = !red;
                updateColors();
                item.setChecked(red);
                break;
            case R.id.green:
                green = !green;
                updateColors();
                item.setChecked(green);
                break;
            case R.id.blue:
                blue = !blue;
                updateColors();
                item.setChecked(blue);
                break;
            case R.id.reset:
                imageView.clearColorFilter();
                red = green = blue = color = true;
                invalidateOptionsMenu();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //this will toggle the color of the image between black and white depending on the value of the color field.
    private void updateSaturation() {
        ColorMatrix colorMatrix = new ColorMatrix();
        if (color) {
            red = green = blue = true;
            colorMatrix.setSaturation(1);
        } else {
            colorMatrix.setSaturation(0);
        }
        ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
        imageView.setColorFilter(colorFilter);
    }

    //set the colors of the image based on the RGB fields.
    private void updateColors() {
        ColorMatrix colorMatrix = new ColorMatrix();
        float[] matrix = {
                1, 0, 0, 0, 0,
                0, 1, 0, 0, 0,
                0, 0, 1, 0, 0,
                0, 0, 0, 1, 0,
        };
        if (!red) matrix[0] = 0;
        if (!green) matrix[6] = 0;
        if (!blue) matrix[12] = 0;
        colorMatrix.set(matrix);
        ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
        imageView.setColorFilter(colorFilter);
    }
}

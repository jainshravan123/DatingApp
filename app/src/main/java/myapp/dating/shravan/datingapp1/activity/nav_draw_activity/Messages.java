package myapp.dating.shravan.datingapp1.activity.nav_draw_activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import myapp.dating.shravan.datingapp1.R;

public class Messages extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);


        imageView = (ImageView) findViewById(R.id.txt_leave_number);
        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .width(150)  // width in px
                .height(150) // height in px
                .endConfig()
                .buildRoundRect("A", Color.RED, 10);

        //ImageView image = (ImageView) findViewById(R.id.image_view);
        //imageView.setImageDrawable(drawable);

        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        // generate random color
        int color1 = generator.getRandomColor();
        // generate color based on a key (same key returns the same color), useful for list/grid views
        TextDrawable.IBuilder builder = TextDrawable.builder()
                .beginConfig()
                .withBorder(4)
                .endConfig()
                .rect();

        TextDrawable drawable2 = TextDrawable.builder()
                .buildRound("A", color1);
        // reuse the builder specs to create multiple drawables
        TextDrawable ic1 = builder.build("A", color1);
        imageView.setImageDrawable(drawable2);

    }
}

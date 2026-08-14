package com.pingpong.game;

import android.app.*;
import android.os.*;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.text.InputType;
import android.view.*;
import android.widget.*;

public class MainActivity extends Activity {
    final int GREEN = Color.rgb(131, 193, 66);
    final int TEXT = Color.rgb(31, 41, 55);
    final int MUTED = Color.rgb(120, 128, 138);

    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        showLogin();
    }

    TextView text(String s, float size, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(s);
        t.setTextSize(size);
        t.setTextColor(color);
        t.setTypeface(Typeface.create("sans", bold ? Typeface.BOLD : Typeface.NORMAL));
        return t;
    }

    GradientDrawable bg(int color, float radius, int strokeColor, int strokeWidth) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(radius);
        if (strokeWidth > 0) d.setStroke(strokeWidth, strokeColor);
        return d;
    }

    EditText field(String hint, boolean password) {
        EditText e = new EditText(this);
        e.setHint(hint);
        e.setHintTextColor(Color.rgb(155, 160, 168));
        e.setTextColor(TEXT);
        e.setTextSize(14);
        e.setSingleLine(true);
        e.setPadding(16, 0, 16, 0);
        e.setBackground(bg(Color.rgb(248,249,250), 12, Color.rgb(225,228,232), 1));
        if (password)
            e.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        else
            e.setInputType(InputType.TYPE_CLASS_TEXT);
        return e;
    }

    void showLogin() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        root.setPadding(24, 24, 24, 24);
        root.setBackgroundColor(Color.rgb(250,250,249));

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView brand = text("ASYLUM", 24, TEXT, true);
        brand.setGravity(Gravity.CENTER);
        content.addView(brand, new LinearLayout.LayoutParams(-1, 42));

        TextView subtitle = text("Sign in to continue", 13, MUTED, false);
        subtitle.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams subp = new LinearLayout.LayoutParams(-1, 30);
        subp.setMargins(0, 0, 0, 28);
        content.addView(subtitle, subp);

        EditText user = field("Username", false);
        LinearLayout.LayoutParams fp = new LinearLayout.LayoutParams(-1, 52);
        fp.setMargins(0, 0, 0, 12);
        content.addView(user, fp);

        EditText pass = field("Password", true);
        content.addView(pass, new LinearLayout.LayoutParams(-1, 52));

        TextView login = text("Log In", 14, Color.WHITE, true);
        login.setGravity(Gravity.CENTER);
        login.setBackground(bg(GREEN, 12, GREEN, 0));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, 52);
        lp.setMargins(0, 20, 0, 0);
        content.addView(login, lp);

        TextView status = text("", 11, Color.rgb(210,60,60), false);
        status.setGravity(Gravity.CENTER);
        content.addView(status, new LinearLayout.LayoutParams(-1, 32));

        TextView footer = text("Made with ❤️ for Neelam", 11, MUTED, false);
        footer.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams footp = new LinearLayout.LayoutParams(-1, 28);
        footp.setMargins(0, 18, 0, 0);
        content.addView(footer, footp);

        login.setOnClickListener(v -> {
            if (user.getText().toString().equals("Neelam") &&
                pass.getText().toString().equals("Neelam143")) {
                setContentView(new PongView(this));
            } else {
                status.setText("Invalid username or password");
            }
        });

        root.addView(content, new LinearLayout.LayoutParams(
                Math.min((int)(getResources().getDisplayMetrics().widthPixels * .86f), 340),
                -2));

        setContentView(root);
    }
}

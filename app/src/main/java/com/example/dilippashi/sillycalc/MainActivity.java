package com.example.dilippashi.sillycalc;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.EntypoModule;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.fonts.IoniconsModule;
import com.joanzapata.iconify.fonts.MaterialCommunityModule;
import com.joanzapata.iconify.fonts.MaterialModule;
import com.joanzapata.iconify.fonts.MeteoconsModule;
import com.joanzapata.iconify.fonts.SimpleLineIconsModule;
import com.joanzapata.iconify.fonts.TypiconsModule;
import com.joanzapata.iconify.fonts.WeathericonsModule;

import java.lang.reflect.Field;
import java.text.DecimalFormat;


public class MainActivity extends AppCompatActivity implements OnClickListener {

    private static final String DIGITS = "0123456789.";
    LinearLayout functionPad;
    Button backspace;
    Toolbar toolbar;
    DecimalFormat df = new DecimalFormat("@###########");
    // Preference change listener
    SharedPreferences sharedPrefs;
    PrefsChangeListener mPreferenceListener = null;
    private SoundPool spool;
    private int sound = 0;
    private TextView mCalculatorDisplay;
    private Boolean userIsInTheMiddleOfTypingANumber = false;
    private Operations mCalculatorBrain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // For ClickSound Declaration

        // Forcefully Menu showing In Top Right Due to Menu Hardware key
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ignored) {
        }

        super.onCreate(savedInstanceState);
        iconify();
        setContentView(R.layout.activity_main);
        initialize();


        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mPreferenceListener = new PrefsChangeListener();
        sharedPrefs.registerOnSharedPreferenceChangeListener(mPreferenceListener);
        ApplySettings(sharedPrefs);


        // Set ToolBar as ActionBar
        setSupportActionBar(toolbar);

        // Set APP Icon On ToolBar TopLeft
        // getSupportActionBar().setDisplayShowHomeEnabled(true);
        // getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        // getSupportActionBar().setDisplayUseLogoEnabled(true);

        // For Back button font-awesome
        // Typeface font = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        //  backspace = (Button) findViewById(R.id.backspace);
        // backspace.setTypeface(font);

        mCalculatorBrain = new Operations();
        mCalculatorDisplay = (TextView) findViewById(R.id.textView1);
        df.setMinimumFractionDigits(0);
        df.setMinimumIntegerDigits(1);
        df.setMaximumIntegerDigits(13);


    }

    private void initialize() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        backspace = (Button) findViewById(R.id.backspace);
        backspace.setOnClickListener(this);
        functionPad = (LinearLayout) findViewById(R.id.functionPad);
        findViewById(R.id.button0).setOnClickListener(this);
        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button4).setOnClickListener(this);
        findViewById(R.id.button5).setOnClickListener(this);
        findViewById(R.id.button6).setOnClickListener(this);
        findViewById(R.id.button7).setOnClickListener(this);
        findViewById(R.id.button8).setOnClickListener(this);
        findViewById(R.id.button9).setOnClickListener(this);
        findViewById(R.id.buttonAdd).setOnClickListener(this);
        findViewById(R.id.buttonSubtract).setOnClickListener(this);
        findViewById(R.id.buttonMultiply).setOnClickListener(this);
        findViewById(R.id.buttonDivide).setOnClickListener(this);
        findViewById(R.id.buttonToggleSign).setOnClickListener(this);
        findViewById(R.id.buttonDecimalPoint).setOnClickListener(this);
        findViewById(R.id.buttonEquals).setOnClickListener(this);
        findViewById(R.id.buttonClear).setOnClickListener(this);
        findViewById(R.id.buttonClearMemory).setOnClickListener(this);
        findViewById(R.id.buttonAddToMemory).setOnClickListener(this);
        findViewById(R.id.buttonSubtractFromMemory).setOnClickListener(this);
        findViewById(R.id.buttonRecallMemory).setOnClickListener(this);
        findViewById(R.id.backspace).setOnClickListener(this);

        // The following buttons only exist in layout-land (Landscape mode) and require extra attention.
        // The messier option is to place the buttons in the regular layout too and set android:visibility="invisible".
        if (findViewById(R.id.buttonSquareRoot) != null) {
            findViewById(R.id.buttonSquareRoot).setOnClickListener(this);
        }
        if (findViewById(R.id.buttonSquared) != null) {
            findViewById(R.id.buttonSquared).setOnClickListener(this);
        }
        if (findViewById(R.id.buttonInvert) != null) {
            findViewById(R.id.buttonInvert).setOnClickListener(this);
        }
        if (findViewById(R.id.buttonSine) != null) {
            findViewById(R.id.buttonSine).setOnClickListener(this);
        }
        if (findViewById(R.id.buttonCosine) != null) {
            findViewById(R.id.buttonCosine).setOnClickListener(this);
        }
        if (findViewById(R.id.buttonTangent) != null) {
            findViewById(R.id.buttonTangent).setOnClickListener(this);
        }
    }

    private void iconify() {
        Iconify
                .with(new FontAwesomeModule())
                .with(new EntypoModule())
                .with(new TypiconsModule())
                .with(new MaterialModule())
                .with(new MaterialCommunityModule())
                .with(new MeteoconsModule())
                .with(new WeathericonsModule())
                .with(new SimpleLineIconsModule())
                .with(new IoniconsModule());
    }

    @SuppressLint("UseValueOf")
    @Override
    public void onClick(View v) {

        String buttonPressed = ((Button) v).getText().toString();

        if (DIGITS.contains(buttonPressed)) {

            // digit was pressed
            if (userIsInTheMiddleOfTypingANumber) {

                if (buttonPressed.equals(".") && mCalculatorDisplay.getText().toString().contains(".")) {
                    // ERROR PREVENTION
                    // Eliminate entering multiple decimals
                } else {
                    mCalculatorDisplay.append(buttonPressed);
                }
            } else {

                if (buttonPressed.equals(".")) {
                    // ERROR PREVENTION
                    // This will avoid error if only the decimal is hit before an operator, by placing a leading zero
                    // before the decimal
                    mCalculatorDisplay.setText(0 + buttonPressed);
                } else {
                    mCalculatorDisplay.setText(buttonPressed);
                }
                userIsInTheMiddleOfTypingANumber = true;
            }
        } else {
            // operation was pressed
            if (userIsInTheMiddleOfTypingANumber) {

                mCalculatorBrain.setOperand(Double.parseDouble(mCalculatorDisplay.getText().toString()));
                userIsInTheMiddleOfTypingANumber = false;
            }
            mCalculatorBrain.performOperation(buttonPressed);
            if (new Double(mCalculatorBrain.getResult()).equals(0.0)) {
                mCalculatorDisplay.setText("" + 0);
            } else {
                mCalculatorDisplay.setText(df.format(mCalculatorBrain.getResult()));
            }
        }
        backspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String oldstr = mCalculatorDisplay.getText().toString();
                if (Double.isNaN(mCalculatorBrain.mOperand)) {
                    mCalculatorBrain.setOperand(0);
                    mCalculatorDisplay.setText("0");
                } else if (oldstr.length() > 1) {
                    String newstr = oldstr.substring(0, oldstr.length() - 1);
                    mCalculatorDisplay.setText(newstr);
                    double convert = Double.parseDouble(newstr);
                    mCalculatorBrain.setOperand(convert);
                } else if (oldstr.length() <= 1) {
                    mCalculatorBrain.setOperand(0);
                    mCalculatorDisplay.setText("0");

                }
            }
        });
    }

    //Method for Icon On Inflator Menu
    private void setIconInMenu(Menu menu, int menuItemId, int labelId, int iconId) {
        MenuItem item = menu.findItem(menuItemId);
        SpannableStringBuilder builder = new SpannableStringBuilder("       " + getResources().getString(labelId));
        builder.setSpan(new ImageSpan(this, iconId), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        item.setTitle(builder);
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // TODO Auto-generated method stub
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        setIconInMenu(menu, R.id.color_red, R.string.red, R.mipmap.ic_color_lens_white_18dp);
        setIconInMenu(menu, R.id.color_blue, R.string.blue, R.mipmap.ic_color_lens_white_18dp);
        setIconInMenu(menu, R.id.color_yellow, R.string.yellow, R.mipmap.ic_color_lens_white_18dp);
        setIconInMenu(menu, R.id.color_green, R.string.green, R.mipmap.ic_color_lens_white_18dp);
        setIconInMenu(menu, R.id.Settings, R.string.Settings, R.mipmap.ic_settings_white_18dp);
        setIconInMenu(menu, R.id.exit, R.string.Exit, R.mipmap.ic_close_white_16dp);
        // Set an icon in the Action_Bar
        //menu.findItem(R.id.exit).setIcon(new IconDrawable(this, FontAwesomeIcons.fa_close).colorRes(R.color.colorAccent).actionBarSize());
        //  menu.findItem(R.id.Settings).setIcon(new IconDrawable(this, FontAwesomeIcons.fa_cog).colorRes(R.color.colorAccent).actionBarSize());
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case R.id.color_red:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                functionPad.setBackgroundResource(R.drawable.red);
                return true;
            case R.id.color_blue:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                functionPad.setBackgroundResource(R.drawable.blue);
                return true;
            case R.id.color_green:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                functionPad.setBackgroundResource(R.drawable.green);
                return true;
            case R.id.color_yellow:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                functionPad.setBackgroundResource(R.drawable.yellow);
                return true;
            default:
                return super.onOptionsItemSelected(item);
            case R.id.Settings:
                Intent i = new Intent(getApplicationContext(), SetPreferenceActivity.class);
                startActivity(i);
                break;
            case R.id.exit:
                finish();
                break;
        }

        return false;

    }

    public void ApplySettings(SharedPreferences sharedPrefs) {
        // Click Sound preference
        Boolean checkclick = sharedPrefs.getBoolean("checkclick", true);
        if (checkclick) {
            this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
            spool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
            sound = spool.load(this, R.raw.click, 1);
            if (sound != 0)
                spool.play(sound, 1, 1, 0, 0, 1);
        }

        // FullScreen preference
        Boolean fullscreen = sharedPrefs.getBoolean("fullscreen", false);
        if (fullscreen) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        // list preferences
        String colors = sharedPrefs.getString("colors", "white");
        View view = this.getWindow().getDecorView();
        view = findViewById(R.id.functionPad);
        if (colors.equals("Blue")) {
            view.setBackgroundColor(Color.BLUE);
        } else if (colors.equals("Crimson")) {
            view.setBackgroundColor(Color.parseColor("#DC143C"));
        } else if (colors.equals("Purple")) {
            view.setBackgroundColor(Color.parseColor("#800080"));
        } else if (colors.equals("Green")) {
            view.setBackgroundColor(Color.parseColor("#008000"));
        } else if (colors.equals("Coral")) {
            view.setBackgroundColor(Color.parseColor("#FF7F50"));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save variables on screen orientation change
        outState.putDouble("OPERAND", mCalculatorBrain.getResult());
        outState.putDouble("MEMORY", mCalculatorBrain.getMemory());

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore variables on screen orientation change
        mCalculatorBrain.setOperand(savedInstanceState.getDouble("OPERAND"));
        mCalculatorBrain.setMemory(savedInstanceState.getDouble("MEMORY"));
        mCalculatorDisplay.setText(df.format(mCalculatorBrain.getResult()));
    }

    // registerOnSharedPreferenceChangeListene
    class PrefsChangeListener implements SharedPreferences.OnSharedPreferenceChangeListener {

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPrefs, String key) {
            ApplySettings(sharedPrefs);
        }
    }
}
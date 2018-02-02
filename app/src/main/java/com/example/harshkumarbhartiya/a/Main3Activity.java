package com.example.harshkumarbhartiya.a;

import android.app.Activity;
import android.graphics.PointF;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.example.harshkumarbhartiya.a.views.DrawModel;
import com.example.harshkumarbhartiya.a.views.DrawView;

/**
 * Changed by marianne-linhares on 21/04/17.
 * https://raw.githubusercontent.com/miyosuda/TensorFlowAndroidMNIST/master/app/src/main/java/jp/narr/tensorflowmnist/DrawModel.java
 */

public class Main3Activity extends Activity implements View.OnClickListener, View.OnTouchListener {

    // ui related
    private ImageButton classBtn;
    private TextView resText;
    private int radioID;
    private int n;
    // tensorflow input and output
    private static final int INPUT_SIZE = 28;
    private static final String INPUT_NAME = "input";
    private static final String OUTPUT_NAME = "output";

    private static final String MODEL_FILE = "file:///android_asset/expert-graph.pb";
    private static final String LABEL_FILE = "file:///android_asset/labels.txt";

    private Classifier classifier;

    private Executor executor = Executors.newSingleThreadExecutor();

    // views related
    private DrawModel drawModel;
    private DrawView drawView;
    private static final int PIXEL_WIDTH = 28;

    private PointF mTmpPiont = new PointF();

    private float mLastX;
    private float mLastY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        //get drawing view
        drawView = (DrawView)findViewById(R.id.draw);
        drawModel = new DrawModel(PIXEL_WIDTH, PIXEL_WIDTH);
        radioID = getIntent().getExtras().getInt("radioID");

        drawView.setModel(drawModel);
        drawView.setOnTouchListener(this);

        //class button
        classBtn = (ImageButton) findViewById(R.id.btn_class);
        classBtn.setOnClickListener(this);

        // res text
        resText = (TextView)findViewById(R.id.tfRes);
        if(radioID==R.id.radioButton)
            resText.setText("Result: ");
        else
        {
            Random rn=new Random();
            n=rn.nextInt()%10;
            n=n+9;
            n%=10;
            resText.setText("Draw this:"+n);
        }

        // tensorflow
        loadModel();
    }

    private void loadModel() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    classifier = Classifier.create(getApplicationContext().getAssets(),
                            MODEL_FILE,
                            LABEL_FILE,
                            INPUT_SIZE,
                            INPUT_NAME,
                            OUTPUT_NAME);
                } catch (final Exception e) {
                    throw new RuntimeException("Error initializing TensorFlow!", e);
                }
            }
        });
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */

    public native String stringFromJNI();

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }


    @Override
    public void onClick(View view){

        if(view.getId() == R.id.btn_class) {
            drawModel.clear();
            drawView.reset();
            drawView.invalidate();
            if(radioID==R.id.radioButton)
                resText.setText("Result: ");
            else
            {
                Random rn=new Random();
                n=rn.nextInt()%10;
                n=n+9;
                n%=10;
                resText.setText("Draw this:"+n);
            }
        }
    }

    @Override
    protected void onResume() {
        drawView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        drawView.onPause();
        super.onPause();
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction() & MotionEvent.ACTION_MASK;

        if (action == MotionEvent.ACTION_DOWN) {
            processTouchDown(event);
            return true;

        } else if (action == MotionEvent.ACTION_MOVE) {
            processTouchMove(event);
            return true;

        } else if (action == MotionEvent.ACTION_UP) {
            processTouchUp();
            return true;
        }
        return false;
    }

    private void processTouchDown(MotionEvent event) {
        mLastX = event.getX();
        mLastY = event.getY();
        drawView.calcPos(mLastX, mLastY, mTmpPiont);
        float lastConvX = mTmpPiont.x;
        float lastConvY = mTmpPiont.y;
        drawModel.startLine(lastConvX, lastConvY);
    }

    private void processTouchMove(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        drawView.calcPos(x, y, mTmpPiont);
        float newConvX = mTmpPiont.x;
        float newConvY = mTmpPiont.y;
        drawModel.addLineElem(newConvX, newConvY);

        mLastX = x;
        mLastY = y;
        drawView.invalidate();
    }

    private void processTouchUp() {
        drawModel.endLine();

        float pixels[] = drawView.getPixelData();

        final Classification res = classifier.recognize(pixels);
        if(radioID==R.id.radioButton) {
            String result = "Result: ";
            if (res.getLabel() == null) {
                resText.setText(result + "?");
            } else {
                result += res.getLabel();
                resText.setText(result);
                if (res.getLabel().equals("0")) {
                    MediaPlayer ring = MediaPlayer.create(Main3Activity.this, R.raw.zero);
                    ring.start();
                } else if (res.getLabel().equals("1")) {
                    MediaPlayer ring = MediaPlayer.create(Main3Activity.this, R.raw.one);
                    ring.start();
                } else if (res.getLabel().equals("2")) {
                    MediaPlayer ring = MediaPlayer.create(Main3Activity.this, R.raw.two);
                    ring.start();
                } else if (res.getLabel().equals("3")) {
                    MediaPlayer ring = MediaPlayer.create(Main3Activity.this, R.raw.three);
                    ring.start();
                } else if (res.getLabel().equals("4")) {
                    MediaPlayer ring = MediaPlayer.create(Main3Activity.this, R.raw.four);
                    ring.start();
                } else if (res.getLabel().equals("5")) {
                    MediaPlayer ring = MediaPlayer.create(Main3Activity.this, R.raw.five);
                    ring.start();
                } else if (res.getLabel().equals("6")) {
                    MediaPlayer ring = MediaPlayer.create(Main3Activity.this, R.raw.six);
                    ring.start();
                } else if (res.getLabel().equals("7")) {
                    MediaPlayer ring = MediaPlayer.create(Main3Activity.this, R.raw.seven);
                    ring.start();
                } else if (res.getLabel().equals("8")) {
                    MediaPlayer ring = MediaPlayer.create(Main3Activity.this, R.raw.eight);
                    ring.start();
                } else if (res.getLabel().equals("9")) {
                    MediaPlayer ring = MediaPlayer.create(Main3Activity.this, R.raw.nine);
                    ring.start();
                }
            }
        }
        else
        {
            if((n+"").equals(res.getLabel()))
            {
                MediaPlayer ring = MediaPlayer.create(Main3Activity.this, R.raw.clap);
                ring.start();
                drawModel.clear();
                drawView.reset();
                drawView.invalidate();
                if(radioID==R.id.radioButton)
                    resText.setText("Result: ");
                else
                {
                    Random rn=new Random();
                    n=rn.nextInt()%10;
                    n=n+9;
                    n%=10;
                    resText.setText("Draw this:"+n);
                }
            }
        }
    }
}

package com.realdev.betrage.ColorPickerDialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.realdev.betrage.R;

public class ColorPickerDialogBuilder extends AlertDialog.Builder  {
    public CustomHSVSeekBar alphaHSV,
            hue,
            brightness,
            saturation;
    public CustomRGBSeekBar alphaRGB,red,green,blue;
    public LinearLayout layout,view;
    public View previewRGB,previewHSV;
    public Custom2DSeekBar satAndBright;
    public EditText hexHSV,
            hexRGB;
    public CustomRGBEditText alphaBox,
            greenBox,
            blueBox,
            redBox;

    public Button rgbBtn,hsvBtn;

    int aHSV=255,aRGB,dp10,r,g,b,argb;
    float[] hsv=new float[]{0,1,1};

    Context contxt;
    public ColorPickerDialogBuilder(Context context) {
        super(context);

        contxt=context;

        dp10=(int)TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                10,
                contxt.getResources().getDisplayMetrics());

        argb=Color.WHITE;

        layout=generateMainLayout();
        setView(layout);

        LinearLayout rgbLayout=getRGBLayout();
        LinearLayout hsvLayout=getHSVLayout();

        view.addView(hsvLayout);
        rgbBtn.setEnabled(true);
        hsvBtn.setEnabled(false);

        hsvBtn.setOnClickListener(view1 -> {
            view.removeAllViews();
            view.addView(hsvLayout);
            rgbBtn.setEnabled(true);
            hsvBtn.setEnabled(false);
        });
        rgbBtn.setOnClickListener(view1 -> {
            view.removeAllViews();
            view.addView(rgbLayout);
            rgbBtn.setEnabled(false);
            hsvBtn.setEnabled(true);
        });

        setTitle("Choose a color");

    }

    public LinearLayout generateMainLayout(){
        LinearLayout layout=new LinearLayout(contxt);
        layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        layout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout tabs=new LinearLayout(contxt);
        tabs.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1));
        tabs.setOrientation(LinearLayout.HORIZONTAL);

        rgbBtn=new Button(contxt);
        hsvBtn=new Button(contxt);

        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,1);
        rgbBtn.setLayoutParams(params);
        hsvBtn.setLayoutParams(params);
        rgbBtn.setText(R.string.rgb_upper);
        hsvBtn.setText(R.string.hsv_upper);
        tabs.addView(rgbBtn);
        tabs.addView(hsvBtn);

        layout.addView(tabs);
        view=new LinearLayout(contxt);
        view.setOrientation(LinearLayout.VERTICAL);
        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        layout.addView(view);
        return layout;
    }

    public LinearLayout getHSVLayout(){
        LinearLayout hsvLayout=new LinearLayout(contxt);
        hsvLayout.setOrientation(LinearLayout.VERTICAL);
        hsvLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        hue=new CustomHSVSeekBar(contxt,360);
        brightness=new CustomHSVSeekBar(contxt,100);
        saturation=new CustomHSVSeekBar(contxt,100);
        alphaHSV=new CustomHSVSeekBar(contxt,255);

        LinearLayout container =new LinearLayout(contxt);

        container.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,0f));
        container.setOrientation(LinearLayout.HORIZONTAL);

        hexHSV=new EditText(contxt);
        hexHSV.setEms(10);
        hexHSV.setText(R.string.color_red_hex);
        hexHSV.setImeOptions(EditorInfo.IME_ACTION_DONE);
        hexHSV.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        hexHSV.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        previewHSV=new View(contxt);

        previewHSV.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,dp10*5));
        satAndBright=new Custom2DSeekBar(contxt,Color.RED,dp10*20);
        satAndBright.setBackground(Color.RED);

        container.addView(hexHSV);
        container.addView(previewHSV);

        hsvLayout.addView(satAndBright);
        hsvLayout.addView(hue);
        hsvLayout.addView(brightness);
        hsvLayout.addView(saturation);
        hsvLayout.addView(alphaHSV);
        hsvLayout.addView(container);

        alphaHSV.setProgress(255);
        brightness.setProgress(100);
        saturation.setProgress(100);

        update();

        hue.setProgressDrawable(new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{
                        Color.RED,Color.GREEN,Color.BLUE,Color.RED
                }));
        alphaHSV.setProgressDrawable(new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{
                        Color.TRANSPARENT,Color.RED
                }));
        brightness.setProgressDrawable(new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{
                        Color.BLACK,Color.RED
                }));
        saturation.setProgressDrawable(new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{
                        Color.WHITE,Color.RED
                }));

        hexHSV.setOnFocusChangeListener((view1, b) -> hexFun(((EditText)view1).getText().toString()));
        hexHSV.setOnEditorActionListener((textView, i, keyEvent) -> hexFun(textView.getText().toString()));

        hue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                hsv[0]=i;
                update();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        alphaHSV.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                aHSV=i;
                update();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        brightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                hsv[2]=i/100f;
                satAndBright.setCords(satAndBright.x,(1-hsv[2])*satAndBright.height);
                update();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        saturation.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                hsv[1]=i/100f;
                satAndBright.setCords(hsv[1]*satAndBright.width,satAndBright.y);
                update();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        satAndBright.setCordsChangedListener((x, y) -> {
            hsv[1]=x/satAndBright.width;
            hsv[2]=1-y/satAndBright.height;
            update();
        });

        return hsvLayout;
    }

    public LinearLayout getRGBLayout(){
        LinearLayout rgbLayout=new LinearLayout(contxt);
        rgbLayout.setOrientation(LinearLayout.VERTICAL);
        rgbLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        previewRGB=new View(contxt);
        previewRGB.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,dp10*10));

        rgbLayout.addView(previewRGB);

        red=new CustomRGBSeekBar(contxt);
        blue=new CustomRGBSeekBar(contxt);
        green=new CustomRGBSeekBar(contxt);
        alphaRGB=new CustomRGBSeekBar(contxt);
        redBox=new CustomRGBEditText(contxt);
        greenBox=new CustomRGBEditText(contxt);
        blueBox=new CustomRGBEditText(contxt);
        alphaBox=new CustomRGBEditText(contxt);

        rgbLayout.addView(new CustomRGBContainer(contxt,"Red : ",red,redBox));
        rgbLayout.addView(new CustomRGBContainer(contxt,"Green : ",green,greenBox));
        rgbLayout.addView(new CustomRGBContainer(contxt,"Blue : ",blue,blueBox));
        rgbLayout.addView(new CustomRGBContainer(contxt,"Alpha : ",alphaRGB,alphaBox));

        hexRGB=new EditText(contxt);
        hexRGB.setText(R.string.color_white_hex);
        hexRGB.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        hexRGB.setEms(10);
        hexRGB.setHint("Hex value of color");
        hexRGB.setInputType(EditorInfo.TYPE_TEXT_VARIATION_PERSON_NAME);
        hexRGB.setPadding(dp10,0,dp10,0);

        rgbLayout.addView(hexRGB);

        setArgb(argb);

        alphaRGB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean bo) {
                setArgb(Color.argb(i,r,g,b));
                alphaBox.setText(String.valueOf(i));
                hexRGB.setText(getColorHex(argb));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        red.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean bo) {
                setArgb(Color.argb(aRGB,i,g,b));
                redBox.setText(String.valueOf(i));
                hexRGB.setText(getColorHex(argb));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        green.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean bo) {
                setArgb(Color.argb(aRGB,r,i,b));
                greenBox.setText(String.valueOf(i));
                hexRGB.setText(getColorHex(argb));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        blue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean bo) {
                setArgb(Color.argb(aRGB,r,g,i));
                blueBox.setText(String.valueOf(i));
                hexRGB.setText(getColorHex(argb));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        alphaBox.setOnEditorActionListener((textView, i, keyEvent) -> boxFun(textView.getText().toString(),0));
        redBox.setOnEditorActionListener((textView, i, keyEvent) -> boxFun(textView.getText().toString(),1));
        greenBox.setOnEditorActionListener((textView, i, keyEvent) -> boxFun(textView.getText().toString(),2));
        blueBox.setOnEditorActionListener((textView, i, keyEvent) -> boxFun(textView.getText().toString(),3));

        alphaBox.setOnFocusChangeListener((view, b) -> {
            if(!b){
                boxFun(((EditText)view).getText().toString(),0);
            }
        });
        redBox.setOnFocusChangeListener((view, b) -> {
            if(!b){
                boxFun(((EditText)view).getText().toString(),1);
            }
        });
        greenBox.setOnFocusChangeListener((view, b) -> {
            if(!b){
                boxFun(((EditText)view).getText().toString(),2);
            }
        });
        blueBox.setOnFocusChangeListener((view, b) -> {
            if(!b){
                boxFun(((EditText)view).getText().toString(),3);
            }
        });

        hexRGB.setOnEditorActionListener((textView, i, keyEvent) -> {
            if(i== EditorInfo.IME_ACTION_DONE){
                try{
                    String hexa=textView.getText().toString().toUpperCase().replace("#","");

                    hexa="#"+((hexa.length()==6)?"FF":"")+hexa;
                    int color=Color.parseColor(hexa);

                    int i1=0xFF & (color>>24),
                            i2=0xFF & (color>>16),
                            i3=0xFF & (color>>8),
                            i4=0xFF & color;

                    alphaRGB.setProgress(i1);
                    red.setProgress(i2);
                    green.setProgress(i3);
                    blue.setProgress(i4);
                }catch (Exception e){
                    Toast.makeText(contxt, "Enter a valid hex", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
            return false;
        });

        return rgbLayout;
    }

    public void show(ColorPickedListener listener){
        if(listener==null){return;}
        setPositiveButton("Choose", (dialogInterface, i) -> {
            int color=hsvBtn.isEnabled() ? argb : getColorHSV(false);
            listener.OnColorPicked(color,getColorHex(color));
        });
        show();
    }

    public interface ColorPickedListener {
        void OnColorPicked(int color,String hex);
    }

    void update(){
        int pure=getColorHSV(true),processed=getColorHSV(false);
        previewHSV.setBackgroundColor(processed);
        alphaHSV.setProgressDrawable(new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,new int[]{Color.TRANSPARENT,pure}));
        brightness.setProgressDrawable(new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,new int[]{Color.BLACK,pure}));
        saturation.setProgressDrawable(new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,new int[]{Color.WHITE,pure}));
        satAndBright.setBackground(pure);
        hexHSV.setText(getColorHex(processed));
    }

    boolean hexFun(String colorString){
        try{
            String hexa=colorString.toUpperCase().replace("#","");

            hexa="#"+((hexa.length()==6)?"FF":"")+hexa;
            int color=Color.parseColor(hexa);
            Color.colorToHSV(color,hsv);

            alphaHSV.setProgress(Color.alpha(color));
            saturation.setProgress((int)hsv[1]);
            brightness.setProgress((int)hsv[2]);
            hue.setProgress((int)hsv[0]);
            update();
            return true;
        }catch (Exception e){
            Toast.makeText(contxt, "Enter a valid hex", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public int getColorHSV(boolean isPure){
        return (isPure?Color.HSVToColor(new float[]{hsv[0],1,1}):Color.HSVToColor(aHSV,hsv));
    }

    public String getColorHex(int color){
        return ("#"+correctHex(Integer.toHexString(Color.alpha(color)))+correctHex(Integer.toHexString(Color.red(color)))+correctHex(Integer.toHexString(Color.green(color)))+correctHex(Integer.toHexString(Color.blue(color)))).toUpperCase();
    }

    String correctHex(String hex){
        return hex.length()<2?"0"+hex:hex;
    }

    public void setArgb(int color){
        argb=color;

        aRGB=0xFF & (argb>>24);
        r=0xFF & (argb>>16);
        g=0xFF & (argb>>8);
        b=0xFF & argb;

        previewRGB.setBackgroundColor(argb);
    }

    boolean boxFun(String txt,int index){
        try{
            int i=Integer.parseInt(txt);
            if(i>255 || i<0){
                Toast.makeText(contxt, "Value should be in between 0 and 255", Toast.LENGTH_SHORT).show();
            }else{
                SeekBar[] arr=new SeekBar[]{alphaRGB,red,green,blue};
                arr[index].setProgress(i);
            }
            return true;
        }catch (Exception e){
            Toast.makeText(contxt, "Enter a valid value", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public class CustomHSVSeekBar extends androidx.appcompat.widget.AppCompatSeekBar{

        public CustomHSVSeekBar(Context context,int max) {
            super(context);
            setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1f));
            setMax(max);
            setPadding(dp10,dp10,dp10,dp10);
            setProgress(0);
            GradientDrawable thumb=new GradientDrawable();
            thumb.setShape(GradientDrawable.RECTANGLE);
            thumb.setSize(dp10/2,dp10*2);
            thumb.setColor(Color.BLACK);
            setThumb(thumb);
        }
    }

    public class CustomRGBSeekBar extends androidx.appcompat.widget.AppCompatSeekBar{

        public CustomRGBSeekBar(Context context) {
            super(context);
            setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,1));
            setMax(255);
            setPadding(dp10,dp10,dp10,dp10);
            setProgress(255);
        }
    }

    public class CustomRGBEditText extends androidx.appcompat.widget.AppCompatEditText{

        public CustomRGBEditText(Context context) {
            super(context);
            setLayoutParams(new ViewGroup.LayoutParams(dp10*5, ViewGroup.LayoutParams.WRAP_CONTENT));
            setEms(10);
            setText(R.string.number_255);
            setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
            setImeOptions(EditorInfo.IME_ACTION_DONE);
        }
    }

    public class CustomRGBContainer extends LinearLayout{

        public CustomRGBContainer(Context context,String TextViewText,SeekBar seekBar,EditText editText) {
            super(context);
            setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            setGravity(Gravity.CENTER);
            setOrientation(LinearLayout.HORIZONTAL);
            TextView text=new TextView(contxt);
            text.setLayoutParams(new ViewGroup.LayoutParams(dp10*5, ViewGroup.LayoutParams.WRAP_CONTENT));
            text.setText(TextViewText);
            addView(text);
            addView(seekBar);
            addView(editText);
        }
    }

    @SuppressLint("ViewConstructor")
    public static class Custom2DSeekBar extends View{
        LayerDrawable corneredGradient;
        GradientDrawable blackToTransparent;
        Paint whiteStroke=new Paint(),blackStroke=new Paint();
        public float x=0,y=0,radius,width,height;
        Context contxt;

        public Custom2DSeekBar(Context context,int initColor,int initHeight) {
            super(context);
            contxt=context;

            blackToTransparent= new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,new int[]{
                    Color.BLACK,Color.TRANSPARENT
            });
            radius=initHeight/15f;
            setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,initHeight));
            setBackground(initColor);

            whiteStroke.setColor(Color.WHITE);
            whiteStroke.setStrokeWidth(radius/2.5f);
            whiteStroke.setStyle(Paint.Style.STROKE);

            blackStroke.setColor(Color.BLACK);
            blackStroke.setStrokeWidth(radius/2.5f);
            blackStroke.setStyle(Paint.Style.STROKE);

            addOnLayoutChangeListener((view, i, i1, i2, i3, i4, i5, i6, i7) -> {
                width=i2-i;
                height=i3-i1;
            });
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawCircle(x,y,radius*0.8f,whiteStroke);
            canvas.drawCircle(x,y,radius,blackStroke);
        }

        @SuppressLint("ClickableViewAccessibility")
        public void setCordsChangedListener(SeekBar2DCordsChangedListener listener){
            if(listener==null){return;}
            setOnTouchListener((view, motionEvent) -> {
                switch (motionEvent.getActionMasked()){
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_UP:
                        setCords(motionEvent.getX(),motionEvent.getY());
                        listener.changed(x,y);
                        return true;
                }
                return false;
            });
        }

        public void setCords(float X, float Y){
            x=Math.min(width, Math.max(0,X));
            y=Math.min(height, Math.max(0,Y));
            invalidate();
        }

        public void setBackground(int color){
            corneredGradient=new LayerDrawable(new Drawable[]{
                    new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,new int[]{
                            Color.WHITE,color
                    }),blackToTransparent
            });
            setBackground(corneredGradient);
        }

        public interface SeekBar2DCordsChangedListener{
            void changed(float x,float y);
        }
    }
}

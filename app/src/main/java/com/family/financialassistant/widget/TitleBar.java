package com.family.financialassistant.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.family.financialassistant.R;


/**
 * Created by lsg on 2020/10/28
 */
public class TitleBar extends RelativeLayout{

    private Context context;
    private ImageView ivLeft;
    private TextView tvTitle;
    private ImageView ivRight;
    private TextView tvRight;
    private String titleName;
    private boolean backImageVisible;
    private boolean rightImageVisible;
    private boolean rightTextVisible;
    private int rightImageRecourse;

    public TitleBar(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initAttrs(context,attrs);
        initView();
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initAttrs(context,attrs);
        initView();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);
        titleName = typedArray.getString(R.styleable.TitleBar_titleName);
        backImageVisible = typedArray.getBoolean(R.styleable.TitleBar_backImageVisible,true);
        rightImageVisible = typedArray.getBoolean(R.styleable.TitleBar_rightImageVisible,false);
        rightTextVisible = typedArray.getBoolean(R.styleable.TitleBar_rightTextVisible,false);
        rightImageRecourse = typedArray.getInt(R.styleable.TitleBar_rightImageRecourse,0);
    }


    private void initView(){
        View.inflate(context,R.layout.layout_title,this);
        ivLeft = findViewById(R.id.imgLeft);
        ivRight = findViewById(R.id.imgRight);
        tvRight = findViewById(R.id.tvRight);
        tvTitle = findViewById(R.id.tvTitle);
        if(titleName != null){
            tvTitle.setText(titleName);
        }
        if(rightImageRecourse != 0){
            ivRight.setImageResource(rightImageRecourse);
        }
        setBackImageVisible(backImageVisible);
        setRightImageVisible(rightImageVisible);
        setRightTextVisible(rightTextVisible);
    }

    /**
     * 设置右侧文字是否显示
     * @param rightTextVisible
     */
    public void setRightTextVisible(boolean rightTextVisible) {
        if(rightTextVisible){
            //文字与图片不可同时显示
            if(ivRight.getVisibility() == VISIBLE){
                ivRight.setVisibility(GONE);
            }
            tvRight.setVisibility(VISIBLE);
        }else {
            tvRight.setVisibility(GONE);
        }
    }

    /**
     * 设置右侧按钮是否显示
     * @param rightImageVisible
     */
    public void setRightImageVisible(boolean rightImageVisible) {
        if(rightImageVisible){
            //文字与图片不可同时显示
            if(tvRight.getVisibility() == VISIBLE){
                tvRight.setVisibility(GONE);
            }
            ivRight.setVisibility(VISIBLE);
        }else {
            ivRight.setVisibility(GONE);
        }
    }

    /**
     * 设置右侧文字
     * @param text
     */
    public void setRightText(String text){
        if(text != null){
            if(ivRight.getVisibility() == VISIBLE){
                ivRight.setVisibility(GONE);
            }
            tvRight.setVisibility(VISIBLE);
            tvRight.setText(text);
        }
    }

    /**
     * 设置右侧文字背景
     * @param color
     */
    public void setRightTextBackColor(int color){
        if(tvRight != null){
            tvRight.setBackgroundColor(color);
        }
    }

    /**
     * 设置右侧按钮图片
     * @param imageId
     */
    public void setRightImageId(int imageId){
        if(imageId != 0){
            if(tvRight.getVisibility() == VISIBLE){
                tvRight.setVisibility(GONE);
            }
            ivRight.setVisibility(VISIBLE);
            ivRight.setImageResource(imageId);
        }
    }

    /**
     * 设置左侧按钮图片
     * @param imageId
     */
    public void setLeftImageId(int imageId){
        if(imageId != 0){
            ivLeft.setVisibility(VISIBLE);
            ivLeft.setImageResource(imageId);
        }
    }

    /**
     * 设置返回按钮图片
     * @param imageId
     */
    public void setBackImageId(int imageId){
        if(imageId != 0){
            ivLeft.setImageResource(imageId);
        }
    }

    /**
     * 设置标题栏标题
     * @param title
     */
    public void setTitle(String title){
        if(title != null){
            tvTitle.setText(title);
        }else {
            tvTitle.setText("");
        }
    }

    /**
     * 设置返回按钮是否显示
     * @param imageVisible
     */
    public void setBackImageVisible(boolean imageVisible) {
        if(imageVisible){
            ivLeft.setVisibility(VISIBLE);
        }else {
            ivLeft.setVisibility(GONE);
        }
    }

    /**
     * 设置右侧按钮点击事件
     * @param onClickListener
     */
    public void setRightImageClick(OnClickListener onClickListener){
        ivRight.setOnClickListener(onClickListener);
    }

    /**
     * 设置左侧按钮点击事件
     * @param onClickListener
     */
    public void setLeftImageClick(OnClickListener onClickListener){
        ivLeft.setOnClickListener(onClickListener);
    }

    /**
     * 设置右侧文字点击事件
     * @param onClickListener
     */
    public void setRightTextClick(OnClickListener onClickListener){
        tvRight.setOnClickListener(onClickListener);
    }

}

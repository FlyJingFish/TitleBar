# TitleBar
## 标题栏 TitleBar 可实现全局设置，可以省掉在每个布局中添加，只需要在 BaseActivity 中添加即可 

[![](https://jitpack.io/v/FlyJingFish/TitleBar.svg)](https://jitpack.io/#FlyJingFish/TitleBar)
[![GitHub stars](https://img.shields.io/github/stars/FlyJingFish/TitleBar.svg)](https://github.com/FlyJingFish/TitleBar/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/FlyJingFish/TitleBar.svg)](https://github.com/FlyJingFish/TitleBar/network)
[![GitHub issues](https://img.shields.io/github/issues/FlyJingFish/TitleBar.svg)](https://github.com/FlyJingFish/TitleBar/issues)
[![GitHub license](https://img.shields.io/github/license/FlyJingFish/TitleBar.svg)](https://github.com/FlyJingFish/TitleBar/blob/master/LICENSE)


<img src="https://github.com/FlyJingFish/TitleBar/blob/master/screenshot/Screenshot_20230320_161148.jpg" width="360px" height="720px" alt="show" />


## 第一步，根目录build.gradle

```gradle
    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
```
## 第二步，需要引用的build.gradle （最新版本[![](https://jitpack.io/v/FlyJingFish/TitleBar.svg)](https://jitpack.io/#FlyJingFish/TitleBar)）

```gradle
    dependencies {
        implementation 'com.github.FlyJingFish:TitleBar:1.0.1'
    }
```
## 第三步，使用说明

**设置示例**

```java
public class BaseActivity extends AppCompatActivity {
    protected TitleBar titleBar;

    public boolean isShowTitleBar(){
        return true;
    }

    public String getTitleString(){
        return "";
    }

    public boolean titleAboveContent(){
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titleBar = new TitleBar(this);
        titleBar.setShadow(4, Color.parseColor("#406200EE"), TitleBar.ShadowType.GRADIENT);
        titleBar.setTitleGravity(TitleBar.TitleGravity.CENTER);
        titleBar.setOnBackViewClickListener(v -> finish());
        if (isShowTitleBar()){
            titleBar.show();
        }else {
            titleBar.hide();
        }
        titleBar.setTitle(getTitleString());
        titleBar.setAboveContent(titleAboveContent());
        titleBar.attachToWindow();
    }
}
```

**方法说明**

| 方法                                           |       方法说明       |
|----------------------------------------------|:----------------:|
| attachToWindow                               |  加入到页面 Window 层  |
| setShadow                                    |  设置底部 Shadow 样式  |
| hideShadow                                   |    隐藏 Shadow     |
| showShadow                                   |    显示 Shadow     |
| getRightTextView                             |  获取右侧 TextView   |
| getRightImageView                            |  获取右侧 ImageView  |
| getBackView                                  | 获取返回按钮 ImageView |
| getTitleView                                 |  获取标题 TextView   |
| setTitleBarBackgroundWithStatusBar           |   设置标题栏背景包含状态栏   |
| setTitleBarBackgroundResourceWithStatusBar   |   设置标题栏背景包含状态栏   |
| setTitleBarBackgroundColorWithStatusBar      |   设置标题栏背景包含状态栏   |
| setTitleBarBackground                        | 设置标题栏背景 不 包含状态栏  |
| setTitleBarBackgroundResource                | 设置标题栏背景 不 包含状态栏  |
| setTitleBarBackgroundColor                   | 设置标题栏背景包 不 含状态栏  |
| setCustomView                                |    设置自定义View     |
| setCustomRightView                           |   设置右侧自定义View    |
| setCustomLeftView                            |   设置左侧自定义View    |
| setTitle                                     |       设置标题       |
| setTitleColor                                |      设置标题颜色      |
| setTitleGravity                              |   设置标题位置（左中右）    |
| setAboveContent                              | TitleBar 是否在内容上边 |
| setOnBackViewClickListener                   |     设置返回点击监听     |
| setOnBackViewLongClickListener               |     设置返回长按监听     |
| setOnRightViewClickListener                  |     设置右侧点击监听     |
| setOnRightViewLongClickListener              |     设置右侧长按监听     |
| show                                         |   显示 TitleBar    |
| hide                                         |   隐藏 TitleBar    |

**全局设置**

加入以下设置可实现全局设置

```xml
<resources>
    <!-- TitleBar 最小高度 -->
    <dimen name="title_bar_minHeight">50dp</dimen>
    <!-- 返回图片宽度 -->
    <dimen name="title_bar_backView_width">20dp</dimen>
    <!-- 返回图片高度 -->
    <dimen name="title_bar_backView_height">20dp</dimen>
    <!-- 返回图片距离左侧距离 -->
    <dimen name="title_bar_backView_marginStart">12dp</dimen>
    <!-- 返回图片距离右侧距离 -->
    <dimen name="title_bar_backView_marginEnd">12dp</dimen>
    <!-- 右侧ImageView图片宽度 -->
    <dimen name="title_bar_rightImageview_width">20dp</dimen>
    <!-- 右侧ImageView图片高度 -->
    <dimen name="title_bar_rightImageview_height">20dp</dimen>
    <!-- 右侧View图片距离左侧距离 -->
    <dimen name="title_bar_rightView_marginStart">15dp</dimen>
    <!-- 右侧View图片距离右侧距离 -->
    <dimen name="title_bar_rightView_marginEnd">15dp</dimen>
    <!-- 标题大小 -->
    <dimen name="title_bar_title_textSize">16sp</dimen>
    <!-- 标题颜色 -->
    <color name="title_bar_title_textColor">#000000</color>
    <!-- 右侧TextView 字体大小 -->
    <dimen name="title_bar_rightTextView_textSize">16sp</dimen>
    <!-- 右侧TextView 字体颜色 -->
    <color name="title_bar_rightTextView_textColor">#000000</color>
</resources>
```

# 最后推荐我写的另一个库，轻松实现在应用内点击小图查看大图的动画放大效果

- [OpenImage](https://github.com/FlyJingFish/OpenImage) 

- [主页查看更多开源库](https://github.com/FlyJingFish)




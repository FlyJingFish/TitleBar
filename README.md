# TitleBar

[![](https://jitpack.io/v/FlyJingFish/TitleBar.svg)](https://jitpack.io/#FlyJingFish/TitleBar)
[![GitHub stars](https://img.shields.io/github/stars/FlyJingFish/TitleBar.svg)](https://github.com/FlyJingFish/TitleBar/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/FlyJingFish/TitleBar.svg)](https://github.com/FlyJingFish/TitleBar/network)
[![GitHub issues](https://img.shields.io/github/issues/FlyJingFish/TitleBar.svg)](https://github.com/FlyJingFish/TitleBar/issues)
[![GitHub license](https://img.shields.io/github/license/FlyJingFish/TitleBar.svg)](https://github.com/FlyJingFish/TitleBar/blob/master/LICENSE)

## 标题栏 TitleBar 可实现全局设置，可以省掉在每个布局中添加，只需要在 BaseActivity 中添加即可
## 同样支持在布局中使用，可以在布局中定义各项参数并支持 <预览> 甚至都可以在每一个布局中定义标题，从没有这么智能方便

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
        implementation 'com.github.FlyJingFish:TitleBar:1.1.4'
    }
```
## 第三步，使用说明

### 一、基础使用方法及属性说明

**1、全局设置**

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
    <!-- shadow 颜色 -->
    <color name="title_bar_shadow_default_color">#4d000000</color>
    <!-- shadow 高度 -->
    <dimen name="title_bar_shadow_default_height">1dp</dimen>
</resources>
```

**2、方法说明**

| 方法                                         |                             方法说明                              |
|--------------------------------------------|:-------------------------------------------------------------:|
| attachToWindow                             |             加入到页面 Window 层（如果你直接写在页面布局上，不要调用这一句）              |
| setAboveContent                            | TitleBar 是否在内容上边（true则TitleBar和布局成上下结构，false则TitleBar覆盖在布局上方） |
| getTitleView                               |                         获取标题 TextView                         |
| setTitle                                   |                             设置标题                              |
| setTitleColor                              |                            设置标题颜色                             |
| setTitleGravity                            |                          设置标题位置（左中右）                          |
| setShadow                                  |                        设置底部 Shadow 样式                         |
| setDisplayShadow                           |                          是否显示 Shadow                          |
| setCustomView                              |                           设置自定义View                           |
| getBackView                                |                       获取返回按钮 ImageView                        |
| setCustomLeftView                          |                          设置左侧自定义View                          |
| setDisplayLeftView                         |                    设置是否显示左侧View（通常是指返回按钮）                     |
| getRightTextView                           |                         获取右侧 TextView                         |
| getRightImageView                          |                        获取右侧 ImageView                         |
| setCustomRightView                         |                          设置右侧自定义View                          |
| setDisplayRightView                        |                         设置是否显示右侧View                          |
| setTitleBarBackgroundWithStatusBar         |                         设置标题栏背景包含状态栏                          |
| setTitleBarBackgroundResourceWithStatusBar |                         设置标题栏背景包含状态栏                          |
| setTitleBarBackgroundColorWithStatusBar    |                         设置标题栏背景包含状态栏                          |
| setTitleBarBackground                      |                        设置标题栏背景 不 包含状态栏                        |
| setTitleBarBackgroundResource              |                        设置标题栏背景 不 包含状态栏                        |
| setTitleBarBackgroundColor                 |                        设置标题栏背景包 不 含状态栏                        |
| setOnBackViewClickListener                 |                           设置返回点击监听                            |
| setOnBackViewLongClickListener             |                           设置返回长按监听                            |
| setOnRightViewClickListener                |                           设置右侧点击监听                            |
| setOnRightViewLongClickListener            |                           设置右侧长按监听                            |
| show                                       |                          显示 TitleBar                          |
| hide                                       |                          隐藏 TitleBar                          |
| getTitleBarLayoutParams                    |        如果需要用到 getLayoutParams 方法，建议改用这个，详细说明可看代码中的方法说明        |
| setTitleBarLayoutParams                    |        如果需要用到 setLayoutParams 方法，建议改用这个，详细说明可看代码中的方法说明        |

**3、布局中属性一览**

| attr                            |     format      | description  |
|---------------------------------|:---------------:|:------------:|
| title_bar_title                 |     string      |      标题      |
| title_bar_back_src              |    reference    |   返回按钮资源图    |
| title_bar_right_type            |      enum       |  右侧是文本还是图片   |
| title_bar_right_src             |    reference    |   右侧按钮资源图    |
| title_bar_right_text            |     string      |     右侧文本     |
| title_bar_title_gravity         |      enum       |     标题位置     |
| title_bar_shadow_type           |      enum       |   shadow类型   |
| title_bar_shadow                | reference/color | shadow颜色或资源图 |
| title_bar_shadow_height         |    dimension    |   shadow高度   |
| title_bar_right_textView_style  |    reference    | 右侧文本样式style  |
| title_bar_right_imageview_style |    reference    | 右侧图片样式style  |
| title_bar_back_view_style       |    reference    | 返回图片样式style  |
| title_bar_title_style           |    reference    |  标题样式style   |


### 二、 使用示例

**1、直接在基础Activity 中使用**

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
        titleBar.attachToWindow();//这句只用在TitleBar未加入到页面上时使用
    }
}
```


**2、在布局中使用**

定义右侧文本样式

```xml
 <style name="right_text_style" >
    <item name="android:layout_width">wrap_content</item>
    <item name="android:layout_height">wrap_content</item>
    <item name="android:layout_marginStart">4dp</item>
    <item name="android:layout_marginEnd">4dp</item>
    <item name="android:layout_gravity">center_horizontal|top</item>
    <item name="android:textSize">20sp</item>
    <item name="android:textColor">@color/teal_200</item>
    <item name="android:textStyle">bold|italic</item>
</style>
```

定义右侧图片样式

```xml
<style name="right_image_style">
    <item name="android:layout_width">60dp</item>
    <item name="android:layout_height">30dp</item>
    <item name="android:layout_marginEnd">30dp</item>
    <item name="android:src">@drawable/ic_pay_suc_vip</item>
    <item name="android:scaleType">centerCrop</item>
</style>
```

[更多style配置详看这里](https://github.com/FlyJingFish/TitleBar/blob/master/app/src/main/res/values/style.xml)

布局中使用

```xml
<com.flyjingfish.titlebarlib.TitleBar
    android:id="@+id/title_bar"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:title_bar_right_textView_style="@style/right_text_style"
    app:title_bar_title_style="@style/title_style"
    app:title_bar_right_imageview_style="@style/right_image_style"
    app:title_bar_back_view_style="@style/back_style"
    app:title_bar_title="4444"
    app:title_bar_back_src="@mipmap/ic_launcher_round"
    app:title_bar_right_type="image"
    app:title_bar_right_text="3333"
    app:title_bar_title_gravity="center"
    app:title_bar_shadow_height="4dp"
    app:title_bar_shadow="@color/purple_700"
    app:title_bar_shadow_type="gradient"
    android:background="@color/teal_700"/>
```


# 最后推荐我写的另一个库，轻松实现在应用内点击小图查看大图的动画放大效果

- [OpenImage](https://github.com/FlyJingFish/OpenImage) 

- [主页查看更多开源库](https://github.com/FlyJingFish)




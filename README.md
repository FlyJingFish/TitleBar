# TitleBar
## 镂空TextView，支持渐变色粗边，支持设置背景

[![](https://jitpack.io/v/FlyJingFish/TitleBar.svg)](https://jitpack.io/#FlyJingFish/TitleBar)
[![GitHub stars](https://img.shields.io/github/stars/FlyJingFish/TitleBar.svg)](https://github.com/FlyJingFish/TitleBar/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/FlyJingFish/TitleBar.svg)](https://github.com/FlyJingFish/TitleBar/network)
[![GitHub issues](https://img.shields.io/github/issues/FlyJingFish/TitleBar.svg)](https://github.com/FlyJingFish/TitleBar/issues)
[![GitHub license](https://img.shields.io/github/license/FlyJingFish/TitleBar.svg)](https://github.com/FlyJingFish/TitleBar/blob/master/LICENSE)


<img src="https://github.com/FlyJingFish/TitleBar/blob/master/screenshot/Screenshot_20230320_160819.jpg" width="360px" height="720px" alt="show" />


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
        implementation 'com.github.FlyJingFish:TitleBar:1.0.5'
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
        titleBar.attachToWindow();
    }
}
```

### 方法说明

| 方法             | description  |
|----------------|:------------:|
| attachToWindow | 加入到页面Window层 |




# 最后推荐我写的另一个库，轻松实现在应用内点击小图查看大图的动画放大效果

- [OpenImage](https://github.com/FlyJingFish/OpenImage) 

- [主页查看更多开源库](https://github.com/FlyJingFish)




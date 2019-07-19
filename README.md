## **StatusLayout 管理各种状态布局**
> ### 用于日常activity/fragment/任意view中管理不同状态如 `加载中` `空页面` `网络错误`等等，可以根据业务需求添加不同状态    


## 优点：
* 自由定制需要的状态以及对应布局，只需要一行代码
* 可以定制动画效果
* 可以用在旧项目上，不需要修改原有xml文件
* 可设置全局属性避免重复劳动

## 使用

```
    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
 ```
    
```
	dependencies {
	        implementation 'com.github.Colaman0:StatusLayout:1.0.6'
	}
```


## 效果
<img src="https://raw.githubusercontent.com/Colaman0/StatusLayout/master/screenshot/screenshot.gif" width = "30%" />

### 上面的GIF图展示了切换布局的效果，在错误页面点击retry按钮切换回原本的内容

## 具体使用
### 1.    把`StatusLayout`作为根布局在activity/fragment/view 中使用 

* 在xml内直接把`StatusLayout`作为根布局，注意`StatusLayout`内部子view数量不能超过1个，
所以如果UI上需求需要排列多个View的时候，需要多套一层布局，比如：  

```
    <StatusLayout>
        <LinearLayout>    
            <View/>        
            <View/>
            <View/>
        </LinearLayout>
    </StatusLayout>
```

* 通过 `StatusLayout.init()`方法传入Context以及你要显示到的layout资源文件，比如:   

```
// 在activity/fragment中使用  
StatusLayout.init(this, R.layout.activity_main);
```

这个方法会返回一个StatusLayout对象，所以大家可以在封装BaseActivity的时候这样写:  

```
// 后续可以通过mStatusLayout添加不同状态对应的UI  
StatusLayout mStatusLayout = StatusLayout.init(this, R.layout.activity_main);   
setContentView(mStatusLayout);
```     
            
### 2. 添加不同状态对应的UI以及响应点击事件

通过add方法来添加一种状态布局，传入`StatusConfig`参数类
>这里需要注意的一点是`StatusConfig`的参数`clickRes`，这个参数可传可不传，考虑到有些类似错误重试/空页面，可能需要一个按钮来点击重试而不是整个页面响应点击，当你传了一个按钮的idRes进去之后，就只响应idRes对应的view的点击事件，比较复杂的操作类型，可以传一个view，具体逻辑在外部自己操作view来管理逻辑


```
    add(statusconfig:StatusConfig)
```


### 3. 切换布局
通过`switchLayout()`/`showDefaultContent()`两种方法来切换布局  

* `switchLayout()`方法是用于切换你add进去的布局，只要传入你前面add布局的时候传入的status就可以了
* `showDefaultContent()`用于切换回你默认的UI，比如在切到error状态的UI时，你点击了重试按钮请求成功之后，通过`showDefaultContent()`方法切换正常的布局，具体可以参考下面的代码    

### 4. 不同布局点击的回调

通过`setLayoutClickListener()`方法来设置监听   

```

setLayoutClickListener(new StatusLayout.OnLayoutClickListener() {
    @Override
    public void OnLayoutClick(View view, String status) {
         // View: 对应status的rootView  
         // status:当前status,可以判断当前页面处于哪个status
        switch (status) {
            case LOADING:
                Toast.makeText(MainActivity.this, LOADING, Toast.LENGTH_SHORT).show();
                break;
            case EMPTY:
                Toast.makeText(MainActivity.this, EMPTY, Toast.LENGTH_SHORT).show();
                break;
            case ERROR:  
                 // 这里通过showDefaultContent()方法展示默认的布局
                mStatusLayout.showDefaultContent();
                break;
        }
    }
});
```

### 5. 设置显示/隐藏的动画
通过`setInAnimation()`/`setOutAnimation()` 来设置页面显示/隐藏的的动画, 也可以通过`setDefaultAnimation()`来使用默认的淡入淡出的效果



### 6.设置全局属性
通过`StatusLayout.setGlobalData()`方法来设置全局的属性，很多时候`错误页面/loading`页面整个APP都是一样的，这个时候可以设置全局属性来避免重复代码，后续可以通过`add（）`方法来覆盖全局属性, `StatusConfig`包含的参数和通过`add()`方法传入的参数值是一样的

```
 StatusLayout.setGlobalData( Arrays.asList(
                new StatusConfig(StatusLayout.EMPTY, R.layout.include_empty,0),
                new StatusConfig(StatusLayout.ERROR, R.layout.include_error,R.id.btn_retry)));
```

## 具体使用细节可以参考Demo！！！相信注释可以很好帮助你使用这个库，有什么问题欢迎提issue

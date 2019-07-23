# StatusLayout  : 一个超高自定义度又简单的页面状态管理库
#### 业务场景需求：
##### 在日常开发App的过程中，我们少不了对`Activity`/`Fragment` 等做一些不同状态不同UI的状态管理逻辑，比如`空页面` `错误重试页面` 等等，网上也有很多作者写了开源库来处理这些问题
----
#### 但是我看了一下这些库，个人认为有以下几个小问题
* ##### 大部分都是只定义了`错误` `空数据` `loading`等3-5个左右的状态，并且切换的时候也是已经规定好调用哪些方法，这样就会大大限制了拓展性以及对一些复杂的业务场景的适配
* ##### 对于一些点击事件或者view的处理也不是很到位，比如错误重试等大多数都是传入一个id
* ##### 多个地方需要同样的设置的时候，需要不断copy代码过去
#### 以上几个小问题相信有些开发者也有发现，在用的时候也会觉得还有改进的空间
#### 我在公司的项目中也发现了这些问题，所以在空闲时间写了一个管理库用来管理页面，接下来就给大家介绍一下，相信能给大家在日常开发中带来更多便利，更少的代码，更多的可操作性
------
#### `StatusLayout`有以下几个优点

* ##### 自由定制需要的状态以及对应布局，只需要一行代码
* ##### 可以定制动画效果
* ##### 可以用在旧项目上，不需要修改原有xml文件
* #####  可设置全局属性避免重复劳动

#### 效果图：
> 效果图来看比起普通的库多了一个淡入淡出的动画效果，这部分可以自定义，这里只给大家展现一个最基本的效果


<img src="https://raw.githubusercontent.com/Colaman0/StatusLayout/master/screenshot/screenshot.gif" width = "30%" />

#### 依赖 ：
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
	        implementation 'com.github.Colaman0:StatusLayout:1.0.7'
	}
```

#### 具体使用步骤如下：

### 1.第一步先把`StatusLayout`作为根布局，这里有以下两种写法
* ##### 把`StatusLayout`作为根布局在activity/fragment/view 中使用 
  > 在xml内直接把`StatusLayout`作为根布局，注意`StatusLayout`内部子view数量不能超过1个，
                所以如果UI上需求需要排列多个View的时候，需要多套一层布局，比如：  

    ```
    <StatusLayout>
        <LinearLayout>    
            <View/>        
            <View/>
            <View/>
        </LinearLayout>
    </StatusLayout>
    
    ````     

*  #####  通过 `StatusLayout.init()`方法传入Context以及你要显示到的layout资源文件，这个方法会返回一个StatusLayout对象，所以大家可以在封装BaseActivity的时候这样写:  

    ```
    // 后续可以通过mStatusLayout添加不同状态对应的UI  
    StatusLayout mStatusLayout = StatusLayout.init(this, R.layout.activity_main);   
    setContentView(mStatusLayout);
    ```     
    
    
    
### 2. 添加不同状态对应的UI以及响应点击事件

##### 通过`add(statusconfig : StatusConfig)`方法来添加一种状态布局，传入`StatusConfig`参数类，给大家讲一下每个参数的作用
* ##### `status` ： 作为一个状态布局的status标记，比如`空页面` `错误页面` 等等你想要添加进去的页面，设置一下自己想要添加的`status`
* ##### `layoutRes` : 对应上面的`status`， 一个`status`对应一个view，一个布局，比如上面`status`传入了一个empty，那我们这里对应可以添加一个空页面的layout资源文件id
* ##### `view` ： 跟`layoutRes`相似，考虑到有时候业务需求，某个状态下的页面可能按钮或者一些需要写的逻辑比较多比较复杂， 这个时候可以让开发者自己写一个view传进来，对应的一些逻辑判断则让`view`内部去处理 ,`StatusLayout`只负责切换
* ##### `clickRes` ：每一个布局，可以传递一个id进来，比如`错误重试页面` 可以传一个button的id进来，这样在button被点击的时候，可以通过回调来接收到点击事件 

  ```
    data class StatusConfig(        
            var status: String?,        
            @field:LayoutRes        
            var layoutRes: Int = -1,        
            var view: View? = null,       
            @field:IdRes       
            var clickRes: Int = -1)


  ```
    
### 3. 切换布局
####  通过`switchLayout()`/`showDefaultContent()`两种方法来切换布局  

* ##### `switchLayout(status : String)`方法是用于切换你add进去的布局，只要传入你前面add布局的时候传入的status就可以了
* ##### `showDefaultContent()`用于切换回你默认的UI，比如在切到error状态的UI时，你点击了重试按钮请求成功之后，通过`showDefaultContent()`方法切换正常的布局，可能是你在xml里默认的一个布局，也可以是通过`add`方法添加进去的布局，但是总体来说切换回正常状态布局调用这个方法就可以了，具体可以参考下面关于回调的代码 


### 4.  不同布局点击的回调
##### 上面在`add`方法中讲到了`StatusConfig`中一个`clickRes`变量，相当于告诉`StatusLayout`我要监听这个id的view的点击事件，当它被点击的时候告诉我，可以通过`setLayoutClickListener()`方法来设置监听   
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

##### 讲解一下上面的代码，设置一个layout点击的回调监听，当`layout/clickRes` 对应的view被点击的时候，会回调当前是哪一个status的页面，以及对应的布局view，当我们的`clickRes`不传的时候，默认是整个页面响应点击事件，所以在add的时候比较灵活的处理了关于点击事件的处理，比较复杂的页面建议就在`add`的时候传入一个`view`然后在内部做处理比较合适了，避免拓展出一大堆方法。

### 5. 设置显示/隐藏的动画
##### 通过`setAnimation()` 来设置页面显示/隐藏的的动画, 也可以通过`setGlobalAnim()`来设置一个全局的动画效果，`setAnimation()`的优先级比`setGlobalAnim()`更高


### 6.设置全局属性
##### 考虑到APP里常见的`空页面` `loading` 之类的页面都是比较统一的，这个时候可以通过`StatusLayout.setGlobalData()`方法来设置全局的属性，这个时候可以设置全局属性来避免重复添加的代码，后续可以通过`add（）`方法来覆盖全局属性。
##### `setGlobalData`方法传入的参数和通过`add()`方法传入的参数值是一样的，可以参考一下代码，并且这里考虑到有些地方没有机会用到这些布局或者说不需要这些布局，所以`StatusLayout`只有在切换布局的时候才会去加载这些全局属性布局。
```
StatusLayout.setGlobalData(        
    StatusConfig(status = StatusLayout.STATUS_EMPTY, layoutRes = R.layout.include_empty),        
    StatusConfig(status = StatusLayout.STATUS_ERROR, layoutRes = R.layout.include_error, clickRes = R.id.btn_retry))
```

## 总结
### 以上六点就讲解完了`StatusLayout`的一个使用，在我一开始写的时候，是想着能尽量适应多种场景以及尽可能少的代码逻辑
* #### 比如电商APP中一个订单支付成功失败loading等场景，可以通过`StatusLayout`去对应添加布局，并且对于一些通用的状态，可以设置全局属性避免不同地方出现一样的代码，并且以后需要更换布局的时候只要在设置全局属性的地方修改一下layout就可以了。
* #### 点击回调通过`setLayoutClickListener` 去处理不同状态下的点击事件回调，不需要写不同的回调事件，也可以更好的对于多种多样的布局来做一个适应
#### 整个库的核心想法就是通过`status`来管理页面，`StatusLayout`只负责管理你添加进来的布局，以及对应切换某个`status`的布局。并不会限制得很死要调用某个方法，所以你可以尽情得自定义你的页面，添加各种各样的布局进去，然后通过`switchLayout（）`来切换布局就可以了。
    


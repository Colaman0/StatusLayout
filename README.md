## StatusLayout
#####  用于同一个页面内需要切换多种不同类型布局
##### eg:比如在一个activity里需要有空布局/错误提示等布局

* 使用
```
	allprojects {
		repositories {
			...
			maven { url 'https://www.jitpack.io' }
		}
	}
```
```
dependencies {
	        implementation 'com.github.Colaman0:StatusLayout:1.0.0'
	}
```

* 示例

##### 在布局中直接使用就OK了
```
 <com.colaman.statuslayout.StatusLayout
        android:id="@+id/status_layout"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
```
##### 
 ```
 //defaultInit()方法里传入默认显示的布局，也就是你的内容部分
mStatusLayout.defaultInit(this, R.layout.include_normal)
                //add()方法第一个参数是布局对应的标记，第二个参数是布局资源，第三个参数是表示需不需要延迟加载，true:会用viewstub包装，false:默认的方法
                .add(LOADING, R.layout.include_loading, true)
                .add(EMPTY, R.layout.include_empty, false)
                .add(ERROR, R.layout.include_error, false)
                .needAnimation(true);
```
##### 当你需要切换布局的时候:
```
// 这里的tag是你在add的时候传的标记，如果要切换原本默认显示的内容(也就是defaultInit里的布局)则传入StatusLayout.STATUS_NORMAL
 mStatusLayout.switchLayout(tag);
```


* 具体的实现可以查看代码，实现比较简单，有什么bug或者问题欢迎提issue，暂时没有一些自定义属性，只能通过代码方式去添加，因为是自己项目统一封装了一下不想每次都写一样的代码。

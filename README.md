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
	implementation 'com.github.Colaman0:StatusLayout:1.0.2'
}
```

* 示例

##### 在布局中直接使用就OK了
```
 <com.colaman.statuslayout.StatusLayout
        android:id="@+id/status_layout"
        android:layout_width="match_parent"
        android:layout_height="match_parent" >
       //这里可以实现你想要实现的页面
 </com.colaman.statuslayout.StatusLayout>
```
##### 
 ```
// LOADING的add方法里多加了一个id，这样点击事件的触发就是该id的view而不是默认的整个布局
mStatusLayout.add(LOADING, R.layout.include_loading, R.id.tv_loading)
            .add(EMPTY, R.layout.include_empty)
            .add(ERROR, R.layout.include_error)
            // 这里可以设置显示的动画以及隐藏的动画
            .setInAnimation(R.anim.anim_in)
            .setOutAnimation(R.anim.anim_out)
            // 也可以直接用默认的透明度变化的动画
            .setDefaultAnimation()
            .setLayoutClickListener(new StatusLayout.OnLayoutClickListener() {
                @Override
                public void OnLayoutClick(View view, String status) {
                    // 这里的view就是add的时候传入的 控件id/整个layout,status就是你上面add的时候传入的status
                    switch (status) {
                        case LOADING:
                            ((TextView) view.findViewById(R.id.tv_loading)).setText("load complete");
                            break;
                        case EMPTY:
                            Toast.makeText(MainActivity.this, EMPTY, Toast.LENGTH_SHORT).show();
                            break;
                        case ERROR:
                            Toast.makeText(MainActivity.this, ERROR, Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });
```
##### 当你需要切换布局的时候:
```
// 这里的tag是你在add的时候传的标记，如果要切换原本的布局则调用showDefaultContent()方法
 mStatusLayout.switchLayout(tag); / mStatusLayout.showDefaultContent();
```


* 具体的实现可以查看代码，实现比较简单，有什么bug或者问题欢迎提issue，暂时没有一些自定义属性，只能通过代码方式去添加，因为是自己项目统一封装了一下不想每次都写一样的代码。

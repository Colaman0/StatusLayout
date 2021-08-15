> 基于StatusLayout1.0的功能进行了一次改进和调整，更好去适应业务场景减少工作，高自由定制不局限于Loading Error等布局，尽可能减少不必要的重复代码设置以及引入成本，更方便简单地在已有的页面中引入StatusLayout

### `StatusLayout`有以下几个优点

* ##### 自由定制需要的状态以及对应布局，只需要一行代码
* ##### 可以定制动画效果
* ##### 可以用在旧项目上，不需要修改原有xml文件
* #####  可设置全局属性避免重复劳动

---

### 引入依赖
Github地址: https://github.com/Colaman0/StatusLayout
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
	implementation 'com.github.Colaman0:StatusLayout:2.0.1'
}
```


### 效果图

<img src="https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/a3f8c681047a410f866cb7f5793e4244~tplv-k3u1fbpfcp-zoom-1.image" width = "30%" />


---
### 使用方式

 1. #### 一行代码就可以实现原有View，方便在旧项目中引用
    只需要调用`wrapView` 方法传入view，即可返回一个StatusLayout，并且把View的位置替换成StatusLayout，把View作为`Status.Normal` 状态的布局，也就是默认布局。
    方便在旧项目中去引用，只需要传入某个view就可以了，不需要在xml里手动替换，减少接入成本

2. #### 支持自定义多种页面切换，自由灵活定制
	1. 首先通过`addStatus`方法传入`Status`和`StatusConfig` ，完成添加状态页面布局
		* `Status`表明当前页面的状态类型
		* `StatusConfig`  包含页面的相关内容比如View和点击事件的View id
	2.  在切换布局的时候调用`switchLayout`切换不同`Status`的页面

3. #### 支持不同页面的点击事件回传，支持单个页面多个view的点击回调
     通过`setLayoutActionListener`传入监听器，在`onLayoutAction`方法里获取到点击事件触发时当前页面的`Status`以及对应的View

4. #### 支持设置布局切换的动画
    通过`setLayoutAnimation`方法传入页面进入以及退出的动画效果，默认是渐隐式动画

5. #### 支持设置全局配置，相同状态页面可以不用多次重复配置
    * `setGlobalData` 传入全局数据集合，数据内容和`addStatus` 一致
    * `setGlobalAnim` 设置全局的页面切换效果，`setLayoutAnimation`是针对单个做定制
    
---

#### 整个库的核心想法就是通过`Status`来管理页面，`StatusLayout`只负责管理你添加进来的布局，以及对应切换某个`Status`的布局。并不会限制得很死要调用某个方法，所以你可以尽情得自定义你的页面，添加各种各样的布局进去，然后通过`switchLayout（）`来切换布局就可以了。


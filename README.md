  MeterailDesigner设计风格的demo，其中包括样式的使用实例，属性动画的使用，自定义VIew的使用，沉浸式状态栏，侧滑菜单，录像功能，design包中CoordinateLayout，RecycleView，Behavior等的使用例子 原本是想通过这个demo尽可能把所有Android技术点以代码和注释的方式包括进来，以备之后开发中到处去查资料的窘境，不过写着写着发现写不下去了，因为这是挑战一个大而全的技术知识点大全，不可能把所有细节淋漓尽致的体现出来，总是有顾此失彼的地方，后面想到用注释的方式把一些实现方式先注释起来，不过后面发现注释的代码很难阅读，并且显得整个代码很乱，所以后面打算通过每个小demo就只练习和挖掘一个或几个相似的技术的方法来继续后面的工作。不过之前所谓的大而全的代码还是舍不得删掉，虽然有点乱，不过毕竟是自己一个个代码堆积起来的，所以先上传上来，需要的时候再来查阅。下面我还是简单罗列一下这些技术点的使用位置吧

MainActivity 
// SnackBar
// FloatingActionButton 
// CoordinatorLayout 
// TextInputLayout 
// TableLayout
// AppBarLayout
// CollapsingToolbarLayout 
// DrawerLayout


CtlActivity 
{@link CollapsingToolbarLayout }
item布局中使用CardView；
Palette 获取图片颜色值；
ToolBar ，沉浸式状态栏，（状态栏透明+fitSystemWindow）。


CustomViewActivity
自定义View（自定义View的实现方式大概可以分为三种，自绘控件、组合控件、以及继承控件）；
BottomSheetBehavior（design:23.2.0库里面的）；
android-support-percent库（百分比布局库）；
沉浸式模式；


TestConstraintLayoutActivity 
 ConstraintLayout约束布局的使用


Fragment1 
{@link TextInputLayout 控件} 
{@link SharedPreferences 使用 }


Fragment2 
选择照片，处理图片


Fragment3 
{@link NestedScrollView } 
{@link RecyclerView}


Fragment4 
android-support-percent库（百分比布局库）
自定义View（自定义View的实现方式大概可以分为三种，自绘控件、组合控件、以及继承控件）


Fragment5
Android样式的使用


Fragment6
补间动画/属性动画


Fragment7
 录制视频并播放

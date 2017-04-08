　　MeterailDesigner设计风格的demo，其中包括样式的使用实例，属性动画的使用，自定义VIew的使用，沉浸式状态栏，侧滑菜单，录像功能，design包中CoordinateLayout，RecycleView，Behavior等的使用例子 原本是想通过这个demo尽可能把所有Android技术点以代码和注释的方式包括进来，以备之后开发中到处去查资料的窘境，不过写着写着发现写不下去了，因为这是挑战一个大而全的技术知识点大全，不可能把所有细节淋漓尽致的体现出来，总是有顾此失彼的地方，后面想到用注释的方式把一些实现方式先注释起来，不过后面发现注释的代码很难阅读，并且显得整个代码很乱，所以后面打算通过每个小demo就只练习和挖掘一个或几个相似的技术的方法来继续后面的工作。不过之前所谓的大而全的代码还是舍不得删掉，虽然有点乱，不过毕竟是自己一个个代码堆积起来的，所以先上传上来，需要的时候再来查阅。下面我还是简单罗列一下这些技术点的使用位置吧

####  MainActivity
- SnackBar
- FloatingActionButton
- CoordinatorLayout
- TextInputLayout
- TabLayout
- AppBarLayout
- CollapsingToolbarLayout
- DrawerLayout

####  CtlActivity
- `CollapsingToolbarLayout`的使用：

        CollapsingToolbarLayout ctlRoot = (CollapsingToolbarLayout) findViewById(R.id.ctl_root);
            //在滑动的过程中，会自动变化到该颜色。
            ctlRoot.setContentScrimColor(usePaletteGetRgb());
            //状态栏最终自动变化到该颜色
            ctlRoot.setStatusBarScrimColor(usePaletteGetRgb());
- item布局中使用`CardView`
*配合recyclerView使用*
- `Palette` 获取图片颜色值；
```
private int usePaletteGetRgb() {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.imv_scenery);
        Palette.from(bmp).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch swatch = palette.getVibrantSwatch();
                if (swatch != null) {
                    color = swatch.getRgb();
                }
            }
        });
        return color;
    }
```
- `ToolBar` 实现沉浸式状态栏。

> 设置状态栏透明
        ```
        <!-- 设置状态栏为透明-->
        <item name="android:windowTranslucentStatus">true</item>
        <!--Android 5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色-->
        <item name="android:statusBarColor">@android:color/transparent</item>
        ```
> 布局中从toolbar开始到它的父布局全部设置设置fitSystemWindow 属性为true


####  CustomViewActivity
- `自定义View`

> 自定义View的实现方式大概可以分为三种，自绘控件、组合控件、以及继承控件
　　自绘控件（onMeasure-->onLayout-->onDraw）

- `BottomSheetBehavior`（design包里面）

        //返回这个View引用的BottomSheetBehavior,作为CoordinatorLayout子View(recyclerView)的LayoutParams
        //所以这里需要在recyclerView中设置  app:layout_behavior="@string/bottom_sheet_behavior" 属性
          final BottomSheetBehavior behavior = BottomSheetBehavior.from(recyclerView);
          findViewById(R.id.btn_scroll).setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                      behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                  } else if (behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                      behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                  }
              }
          });

           /* bottomSheet 的状态监听与回调 */
                  behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                      @Override
                      public void onStateChanged(@NonNull View bottomSheet, int newState) {
                          Log.e("onStateChanged", newState + "");
                      }

                      @Override
                      public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                          Log.e("onSlide", slideOffset + "");
                      }
                  });
- `百分比布局`  PercentRelativeLayout，PercentFrameLayoutLayout

>  可以在布局中设置百分比来控制控件在布局中的位置，类似于LinearLayout中的权重widget
app:layout_heightPercent="20%"
app:layout_widthPercent="30%"

- `真正的沉浸式模式`(一般只在游戏和视频类app里面用得到)

        /**
         * 在所有View绘制完毕的时候才会回调的
         *
         * @param hasFocus 是否获得焦点
         */
        @Override
        public void onWindowFocusChanged(boolean hasFocus) {
            super.onWindowFocusChanged(hasFocus);
            if (hasFocus && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                View decorView = getWindow().getDecorView();  //获取当前界面的DecorView
                decorView.setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE  //必须和下面UI FLag的配合同时使用
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION  //让应用的主体内容占用系统导航栏的空间
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN  //会让应用的主体内容占用系统状态栏的空间
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION    //隐藏导航栏
                                | View.SYSTEM_UI_FLAG_FULLSCREEN    //全屏
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        }

####  TestConstraintLayoutActivity
 - `ConstraintLayout`约束布局的使用，主要是为了减少布局嵌套，直接拖拉控件实现


####  Fragment1
- `TextInputLayout`

        <android.support.design.widget.TextInputLayout
            android:id="@+id/til_user"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:hint="请输入你的邮箱"
            tools:ignore="HardcodedText">

            <android.support.v7.widget.AppCompatAutoCompleteTextView
                android:id="@+id/et_user"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:completionHint="请选择用户名。。。"
                android:completionThreshold="1"
                android:inputType="textEmailAddress" />
        </android.support.design.widget.TextInputLayout>

        //setError()方法如果传入非空参数,则setErrorEnabled(true)将自动被调用。
        tilUser.setError("the input user style error,please check email..");

- `SharedPreferences`

```
这里是用于保存所有登录成功的用户户名
private void saveHistory(String userName) {
        SharedPreferences sp = getActivity().getSharedPreferences("history", Context.MODE_PRIVATE);
        //取出之前保存的数据
        Set<String> longHistory = sp.getStringSet("historyUser", new HashSet<String>());
        if (!longHistory.contains(userName)) {
            SharedPreferences.Editor editor = sp.edit();
            users.add(userName);
            users.addAll(longHistory);
            editor.putStringSet("historyUser", users);
            editor.apply();
        }
    }
```


####  Fragment2
- 调用系统相机进行拍照
```
intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
File folder = new File(SD_PATH);
if (!folder.exists()) {//判断是否已存在该文件夹
   folder.mkdir();
}
File file = new File(SD_PATH + System.currentTimeMillis() + ".jpg");
//如果指定了目标uri，data就没有数据，如果没有指定uri，则data就返回有数据,照相机有自己默认的存储路径，
// 拍摄的照片将返回一个缩略图。如果想访问原始图片，可以通过dat extra能够得到原始图片位置.
picUri = Uri.fromFile(file);
intent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
startActivityForResult(intent, REQUEST_CODE_CAMERA);
```
- 调用系统相册
```
选择到的图片保存在onActivityResult()回调方法中的Intent data参数中，可以通过Uri picUri = data.getData();拿到结果   //picUri格式：content://media/external/images/media/7058
intent = new Intent(Intent.ACTION_GET_CONTENT);
intent.setType("image/*"); //指定类型可以是image的任意类型，包括jpg/bmp/gif等等
startActivityForResult(intent, REQUEST_CODE_SELECT_PIC);
```
- 处理图片

> 最后就演变为对图片的uri进行处理，包括存储，剪裁，压缩，上传等处理方式，按照需求进行处理即可.

1. 现将图片uri转换为bitmap，注意内存溢出

        //方式一，可能会导致内存溢出
        Bitmap srcBitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), picUri);

        //方式二，使用输入流输出流处理，先压缩在读取成bitmap
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//只读边,不读内容
        BitmapFactory.decodeStream(in, null, options); //返回值为空

2. 压缩图片，分为按比例压缩（减少内存使用）和按质量压缩（减少图片大小）

        //方式一，比例压缩
        Bitmap bitmap = ThumbnailUtils.extractThumbnail(srcBitmap, imvPic.getWidth(), imvPic.getHeight(), ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        //方式二，质量压缩
            /**
             * 图片压缩（质量压缩方法——适用于压缩之后上传到服务器）
             * <p/>
             * 它是在保持像素的前提下改变图片的位深及透明度等，来达到压缩图片的目的。
             * 进过它压缩的图片文件大小会有改变，但是导入成bitmap后占得内存是不变的。
             * 因为要保持像素不变，所以它就无法无限压缩，到达一个值之后就不会继续变小了。
             */
            public static Bitmap compressBitmap(Bitmap bitmap) throws IOException {
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os); //质量压缩方法,这里100表示不压缩,把压缩后的数据存放到os中
                int options = 100;
                while (os.toByteArray().length / 1024 > 100) { //循环判断如果压缩后图片是否大于100kb,大于继续压缩
                    os.reset(); //重置os,即清空os
                    //第一个参数:图片格式,第二个参数:图片质量,100为最高，0为最差,第三个参数:保存压缩后的数据的流
                    bitmap.compress(Bitmap.CompressFormat.JPEG, options, os);   //这里压缩(100-options)%，把压缩后的数据存放到os中
                    if (options > 10) {
                        options -= 10;
                    } else {
                        options = 80;
                    }
                }
                byte[] bytes = os.toByteArray();
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                return bitmap;
            }

####  Fragment3
- `NestedScrollView` 支持嵌套滚动的ScrollView
- `RecyclerView`
```
final Context mContext = getContext();
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(8, LinearLayoutManager.HORIZONTAL);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);//解决嵌套滚动的问题
        recyclerView.setItemAnimator(new DefaultItemAnimator());//设置增加或删除条目的动画
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.HORIZONTAL, 6, R.color.c_emphasize_blue));
//        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL, R.drawable.recycler_divider_style));
        final RecyclerAdapter adapter = new RecyclerAdapter(mContext, data);
        //如果item为固定高度时，使用这句可以提高效率
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                data.remove(position);
                adapter.notifyItemRemoved(position);
                //必须以snackBar弹出的父CoordinatorLayout作为相对的view
                CoordinatorLayout parentCLRoot = (CoordinatorLayout) getActivity().findViewById(R.id.cl_root);
                Snackbar.make(parentCLRoot, position + " 被删除了", Snackbar.LENGTH_LONG).show();
            }
        });
```


####  Fragment4
- `view滑动`的几种方式
  方式一:调用View的layout方法来重新放置它的位置
  ```
  //获取View自身左边到其父布局左边的距离 --> v.getLeft()
  v.layout(v.getLeft() + offSetX, v.getTop() + offSetY, v.getRight() + offSetX, v.getBottom() + offSetY);
  ```
  方式二:调用View的offset方法来重新放置它的位置,其实和方式一是一样的原理
  ```
   v.offsetLeftAndRight(offSetX);
   v.offsetTopAndBottom(offSetY);
  ```
  方式三:改变view所在的父布局的布局参数
  ```
  RelativeLayout.LayoutParams layoutParams = ((RelativeLayout.LayoutParams) v.getLayoutParams());
  layoutParams.leftMargin = v.getLeft() + offSetX;
  layoutParams.topMargin = v.getTop() + offSetY;
  v.setLayoutParams(layoutParams);
  ```
  方式四:移动view所在的父布局中所有的子view
  ```
   //将偏移量设置为负值
   ((View) v.getParent()).scrollBy(-offSetX, -offSetY);
  ```

####  Fragment5
- `Android样式`的使用
  * *selector*
```
//虚线描边镂空圆角
<!--状态改变时，新状态展示时的淡入和淡出时间，以毫秒为单位-->
<selector xmlns:android="http://schemas.android.com/apk/res/android"
    android:enterFadeDuration="5000"
    android:exitFadeDuration="3000">
    <!-- item是从上往下匹配的，如果匹配到一个item那它就将采用这个item -->
    <item android:drawable="@drawable/bg_rectangle_with_stroke_dash" android:state_pressed="true" />
</selector>
```
   * *shape*
   ```
   <shape xmlns:android="http://schemas.android.com/apk/res/android"
       android:shape="oval">

       <!-- size设置形状的大小 -->
       <size
           android:width="@dimen/rectangle_corners"
           android:height="@dimen/rectangle_corners" />

       <!-- gradient设置渐变 -->
       <!--使用radial渐变时，必须指定渐变的半径-->
       <gradient
           android:centerColor="@color/c_white"
           android:endColor="@color/colorAccent"
           android:gradientRadius="@dimen/rectangle_corners"
           android:startColor="@color/c_emphasize_blue"
           android:type="radial" />

   </shape>
   ```
   * *rotate*
   ```
   <!--android:toDegrees 结束的角度度数，正数表示顺时针，负数表示逆时针-->
   <rotate xmlns:android="http://schemas.android.com/apk/res/android"
       android:fromDegrees="0"
       android:toDegrees="180"
       android:pivotX="50%"
       android:pivotY="50%"
       android:visible="true">

       <!--android:useLevel 一般为false，否则可能环形无法显示，
           只有作为LevelListDrawable使用时才设为true-->
       <shape
           android:dither="true"
           android:innerRadius="50dp"
           android:innerRadiusRatio="3"
           android:shape="ring"
           android:thickness="10dp"
           android:thicknessRatio="9"
           android:useLevel="false">

           <stroke
               android:width="@dimen/stroke_width"
               android:color="@android:color/holo_blue_bright" />

           <!--sweep 扫描性渐变-->
           <gradient
               android:endColor="@android:color/holo_orange_dark"
               android:startColor="@android:color/holo_orange_light"
               android:type="sweep" />

       </shape>
   </rotate>
   ```
   * *layer-list*
   ```
   <layer-list xmlns:android="http://schemas.android.com/apk/res/android">
       <item>
           <bitmap
               android:mipMap="true"
               android:gravity="center"
               android:tileMode="clamp"
               android:src="@mipmap/imv_ctl"/>
       </item>

       <item
           android:left="10dp"
           android:top="10dp">
           <bitmap
               android:mipMap="true"
               android:gravity="center"
               android:src="@mipmap/imv_scenery" />
       </item>

       <item
           android:left="40dp"
           android:top="40dp">
           <bitmap
               android:mipMap="true"
               android:gravity="center"
               android:src="@mipmap/imv_scenery"/>
       </item>
   </layer-list>
   ```

####  Fragment6
- 补间动画
```
首先在布局文件中定义动画
<!--shareInterpolator取值true或false，取true时，指在AnimationSet中定义一个插值器，它下面的所有动画共同。如果设为false，则表示它下面的动画自己定义各自的插值器。-->
<set xmlns:android="http://schemas.android.com/apk/res/android"
    android:duration="6000"
    android:shareInterpolator="true"
    android:interpolator="@android:anim/anticipate_overshoot_interpolator">

    <alpha
        android:fromAlpha="0.0"
        android:toAlpha="1.0" />

    <scale
        android:repeatCount="infinite"
        android:repeatMode="reverse"

        android:fromXScale="0.0"
        android:fromYScale="0.0"
        android:pivotX="50%"
        android:pivotY="50%"
        android:toXScale="1.0"
        android:toYScale="1.0" />

    <rotate
        android:repeatCount="infinite"
        android:repeatMode="reverse"

        android:fromDegrees="0"
        android:pivotX="50%"
        android:pivotY="50%"
        android:toDegrees="360" />

    <translate
        android:fromXDelta="-100"
        android:fromYDelta="-100"
        android:toXDelta="100"
        android:toYDelta="100" />

</set>

然后在代码中进行设置
Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.set_anim_test);
btnAnimObject.startAnimation(animation);
```
- 属性动画
```
布局文件中定义动画样式
<objectAnimator xmlns:android="http://schemas.android.com/apk/res/android"
    android:duration="3000"
    android:interpolator="@android:anim/decelerate_interpolator"
    android:propertyName="rotation"
    android:valueFrom="-90"
    android:valueTo="180"
    android:valueType="floatType" />

代码中进行调用
ObjectAnimator objectAnimator = (ObjectAnimator) AnimatorInflater.loadAnimator(getContext(), R.animator.object_animator_test);
objectAnimator.setTarget(btnAnimObject);
objectAnimator.start();
```


####  Fragment7
 - 录制视频
 初始化surfaceView和Camera
 - 播放视频


## 简单的Markdown语法规则
### 1. 标题
### 2. 列表
- 有序列表
  1. 1
    1. 1.1
    2. 1.2
  2. 2
  3. 3

+ 无序列表
 - 1
 + 2
 * 3
 * 4
 - 5

### 3. 引用
> 这里是引用的\*文字*
>>1+1&gt;2

### 4. 图片与链接
* 链接 [github](https://github.com/glmsb)
       <http://www.baidu.com>
* 图片 [头像](https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1491546898&di=bdd4ac362725ec1d50e7b25ecd1247d7&src=http://img.tupianzj.com/uploads/allimg/160309/9-16030Z92137.jpg)
* 图片 ![图标](https://github.com/glmsb/MaterialDesigner/blob/master/app/src/main/res/mipmap-xxhdpi/ic_material.png)

### 5. 斜体与粗体
*斜体*  **粗体文字**   ***加粗的斜体***
### 6. 表格
| 标记符 | 含义  |
|:-----:| -----:|
| #     | 标题  |
| * , - , + | 无序列表 |
| 1.    | 有序列表 |
| [] () | 链接    |
| ![] ()| 图片    |
| * *   | 斜体    |
| ** ** | 粗体    |
| ~~ ~~ | 删除线    |
|***,---| 分割线 |
| :     | 表格中的对齐方式 |
| \` `,tab  | 代码框 |
| \&gt; | 大于符号 |
| \     | 插入标记符 |
### 7. 代码框
`
public static void main(){
    System.out.println("Hello Markdown!");
}`

    public static void main(){
            System.out.println("Hello Markdown!");
        }
### 7. 分割线
***
---
### 8.删除线
 ~~要删除的内容~~


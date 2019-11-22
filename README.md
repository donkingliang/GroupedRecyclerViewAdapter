# GroupedRecyclerViewAdapter

GroupedRecyclerViewAdapter可以很方便的实现RecyclerView的分组显示，并且每个组都可以包含组头、组尾和子项；可以方便实现多种Type类型的列表，可以实现如QQ联系人的列表一样的列表展开收起功能，还可以实现头部悬浮吸顶功能等。下面先让我们看一下它所能够实现的一些效果：

![分组的列表](https://github.com/donkingliang/GroupedRecyclerViewAdapter/blob/master/GroupedRecyclerViewAdapter%E6%BC%94%E7%A4%BA%E5%9B%BE/%E5%88%86%E7%BB%84%E7%9A%84%E5%88%97%E8%A1%A8.jpg) ![不带组尾的列表](https://github.com/donkingliang/GroupedRecyclerViewAdapter/blob/master/GroupedRecyclerViewAdapter%E6%BC%94%E7%A4%BA%E5%9B%BE/%E4%B8%8D%E5%B8%A6%E7%BB%84%E5%B0%BE%E7%9A%84%E5%88%97%E8%A1%A8.jpg) ![不带组头的列表](https://github.com/donkingliang/GroupedRecyclerViewAdapter/blob/master/GroupedRecyclerViewAdapter%E6%BC%94%E7%A4%BA%E5%9B%BE/%E4%B8%8D%E5%B8%A6%E7%BB%84%E5%A4%B4%E7%9A%84%E5%88%97%E8%A1%A8.jpg) ![子项为Grid的列表](https://github.com/donkingliang/GroupedRecyclerViewAdapter/blob/master/GroupedRecyclerViewAdapter%E6%BC%94%E7%A4%BA%E5%9B%BE/%E5%AD%90%E9%A1%B9%E4%B8%BAGrid%E7%9A%84%E5%88%97%E8%A1%A8.jpg) ![子项为Grid的列表(各组子项的Span不同)](https://github.com/donkingliang/GroupedRecyclerViewAdapter/blob/master/GroupedRecyclerViewAdapter%E6%BC%94%E7%A4%BA%E5%9B%BE/%E5%AD%90%E9%A1%B9%E4%B8%BAGrid%E7%9A%84%E5%88%97%E8%A1%A8(%E5%90%84%E7%BB%84%E5%AD%90%E9%A1%B9%E7%9A%84Span%E4%B8%8D%E5%90%8C).jpg) ![头、尾和子项都支持多种类型的列表](https://github.com/donkingliang/GroupedRecyclerViewAdapter/blob/master/GroupedRecyclerViewAdapter%E6%BC%94%E7%A4%BA%E5%9B%BE/%E5%A4%B4%E3%80%81%E5%B0%BE%E5%92%8C%E5%AD%90%E9%A1%B9%E9%83%BD%E6%94%AF%E6%8C%81%E5%A4%9A%E7%A7%8D%E7%B1%BB%E5%9E%8B%E7%9A%84%E5%88%97%E8%A1%A8.jpg)  

![多种子项类型的列表](https://github.com/donkingliang/GroupedRecyclerViewAdapter/blob/master/GroupedRecyclerViewAdapter%E6%BC%94%E7%A4%BA%E5%9B%BE/%E5%A4%9A%E7%A7%8D%E5%AD%90%E9%A1%B9%E7%B1%BB%E5%9E%8B%E7%9A%84%E5%88%97%E8%A1%A8.jpg)

还可以很容易的实时列表的展开收起效果：

![可展开收起的列表](https://github.com/donkingliang/GroupedRecyclerViewAdapter/blob/master/GroupedRecyclerViewAdapter%E6%BC%94%E7%A4%BA%E5%9B%BE/%E5%8F%AF%E5%B1%95%E5%BC%80%E6%94%B6%E8%B5%B7%E7%9A%84%E5%88%97%E8%A1%A8.gif)

还可以轻松实现头部悬浮吸顶的效果：

![头部吸顶的列表](https://github.com/donkingliang/GroupedRecyclerViewAdapter/blob/master/GroupedRecyclerViewAdapter%E6%BC%94%E7%A4%BA%E5%9B%BE/%E5%A4%B4%E9%83%A8%E5%90%B8%E9%A1%B6%E7%9A%84%E5%88%97%E8%A1%A8.gif)

以上展示的只是GroupedRecyclerViewAdapter能实现的一些常用效果，其实使用GroupedRecyclerViewAdapter还可以很容易的实现一些更加复杂的列表效果。在我的GroupedRecyclerViewAdapter项目的Demo中给出了上面几种效果的实现例子，并且有详细的注释说明，有兴趣的同学可以到我的GitHub下载源码。下面直接讲解GroupedRecyclerViewAdapter的使用。

**1、引入依赖** 

在Project的build.gradle在添加以下代码

```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
在Module的build.gradle在添加以下代码

```
	implementation 'com.github.donkingliang:GroupedRecyclerViewAdapter:2.1.0'
```
**注意：** 从2.0.0版本开始，GroupedRecyclerViewAdapter的依赖迁移至Androidx。如果你的项目还没有迁移或使用Androidx，可以使用1.3.6版本。

**2、继承GroupedRecyclerViewAdapter**

```java
public class GroupedListAdapter extends GroupedRecyclerViewAdapter {
}
```

**3、实现GroupedRecyclerViewAdapter里的方法**

GroupedRecyclerViewAdapter是一个抽象类，它提供了一系列需要子类去实现的方法。

```java
	//返回组的数量
	public abstract int getGroupCount();

	//返回当前组的子项数量
    public abstract int getChildrenCount(int groupPosition);

	//当前组是否有头部
    public abstract boolean hasHeader(int groupPosition);

	//当前组是否有尾部
    public abstract boolean hasFooter(int groupPosition);

	//返回头部的布局id。(如果hasHeader返回false，这个方法不会执行)
    public abstract int getHeaderLayout(int viewType);

	//返回尾部的布局id。(如果hasFooter返回false，这个方法不会执行)
    public abstract int getFooterLayout(int viewType);

	//返回子项的布局id。
    public abstract int getChildLayout(int viewType);

	//绑定头部布局数据。(如果hasHeader返回false，这个方法不会执行)
    public abstract void onBindHeaderViewHolder(BaseViewHolder holder, int groupPosition);

	//绑定尾部布局数据。(如果hasFooter返回false，这个方法不会执行)
    public abstract void onBindFooterViewHolder(BaseViewHolder holder, int groupPosition);

	//绑定子项布局数据。
    public abstract void onBindChildViewHolder(BaseViewHolder holder,
                                               int groupPosition, int childPosition);
```
还可是重写GroupedRecyclerViewAdapter方法实现头、尾和子项的多种类型item。效果就像上面的第6张图一样。

```java
	//返回头部的viewType。
	public int getHeaderViewType(int groupPosition);

	//返回尾部的viewType。
    public int getFooterViewType(int groupPosition) ;

	//返回子项的viewType。
    public int getChildViewType(int groupPosition, int childPosition) ;
```
**4、设置点击事件的监听**

GroupedRecyclerViewAdapter提供了对列表的点击事件的监听方法。

```java
	//设置组头点击事件
    public void setOnHeaderClickListener(OnHeaderClickListener listener) {
        mOnHeaderClickListener = listener;
    }

    //设置组尾点击事件
    public void setOnFooterClickListener(OnFooterClickListener listener) {
        mOnFooterClickListener = listener;
    }

    // 设置子项点击事件
    public void setOnChildClickListener(OnChildClickListener listener) {
        mOnChildClickListener = listener;
    }
```

####  **注意事项：**

**1、对方法重写的注意。**

如果我们直接继承RecyclerView.Adapter去实现自己的Adapter时，一般会重写Adapter中的以下几个方法：

```java
public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

public void onBindViewHolder(RecyclerView.ViewHolder holder, int position);

public int getItemCount();

public int getItemViewType(int position);
```
但如果是使用GroupedRecyclerViewAdapter，就一定不能去重写这几个方法，因为在GroupedRecyclerViewAdapter中已经对这几个方法做了实现，而且是对实现列表分组至关重要的，如果子类重写了这几个方法，可能会破坏GroupedRecyclerViewAdapter的功能。
从前面给出的GroupedRecyclerViewAdapter的方法我们可以看到，这些方法其实就是对应RecyclerView.Adapter的这4个方法的，所以我们直接使用GroupedRecyclerViewAdapter提供的方法即可。
RecyclerView.Adapter中的
```java
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

```
对应GroupedRecyclerViewAdapter中的
```java
	//返回头部的布局id。(如果hasHeader返回false，这个方法不会执行)
    public abstract int getHeaderLayout(int viewType);

	//返回尾部的布局id。(如果hasFooter返回false，这个方法不会执行)
    public abstract int getFooterLayout(int viewType);

	//返回子项的布局id。
    public abstract int getChildLayout(int viewType);
```
这里之所以返回的是布局id而不是ViewHolder ，是因为在GroupedRecyclerViewAdapter项目中已经提供了一个通用的ViewHolder：BaseViewHolder。所以使用者只需要提供布局的id即可，不需要自己去实现ViewHolder。

```java
	@Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(getLayoutId(mTempPosition, viewType), parent, false);
        return new BaseViewHolder(view);
    }

    private int getLayoutId(int position, int viewType) {
        int type = judgeType(position);
        if (type == TYPE_HEADER) {
            return getHeaderLayout(viewType);
        } else if (type == TYPE_FOOTER) {
            return getFooterLayout(viewType);
        } else if (type == TYPE_CHILD) {
            return getChildLayout(viewType);
        }
        return 0;
    }
```
RecyclerView.Adapter中的
```java
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position);
```
对应GroupedRecyclerViewAdapter中的

```java
	//绑定头部布局数据。(如果hasHeader返回false，这个方法不会执行)
    public abstract void onBindHeaderViewHolder(BaseViewHolder holder, int groupPosition);

	//绑定尾部布局数据。(如果hasFooter返回false，这个方法不会执行)
    public abstract void onBindFooterViewHolder(BaseViewHolder holder, int groupPosition);

	//绑定子项布局数据。
    public abstract void onBindChildViewHolder(BaseViewHolder holder,
                                               int groupPosition, int childPosition);
```
RecyclerView.Adapter中的
```java
	public int getItemCount();
```
对应GroupedRecyclerViewAdapter中的

```java
	//返回组的数量
	public abstract int getGroupCount();

	//返回当前组的子项数量
    public abstract int getChildrenCount(int groupPosition);

```
RecyclerView.Adapter中的
```java
	public int getItemViewType(int position);
```
对应GroupedRecyclerViewAdapter中的

```java
	//返回头部的viewType。
	public int getHeaderViewType(int groupPosition);

	//返回尾部的viewType。
    public int getFooterViewType(int groupPosition) ;

	//返回子项的viewType。
    public int getChildViewType(int groupPosition, int childPosition) ;
```

**2、对列表操作的注意**

RecyclerView.Adapter提供了一系列对列表进行操作的方法。如：

```java
//更新操作
public final void notifyDataSetChanged();
public final void notifyItemChanged(int position);
public final void notifyItemChanged(int position, Object payload);
public final void notifyItemRangeChanged(int positionStart, int itemCount);
public final void notifyItemRangeChanged(int positionStart, int itemCount, Object payload);

//插入操作
public final void notifyItemInserted(int position);
public final void notifyItemRangeInserted(int positionStart, int itemCount);

//删除操作
public final void notifyItemRemoved(int position)
public final void notifyItemRangeRemoved(int positionStart, int itemCount);
```
在GroupedRecyclerViewAdapter不建议使用RecyclerView.Adapter的任何对列表的操作方法，因为这些方法都是基于列表的操作，它的position是相对于整个列表而言的，而GroupedRecyclerViewAdapter是分组的列表，它对列表的操作应该是基于组的。同时GroupedRecyclerViewAdapter使用了组结构来维护整个列表的结构，使我们可以对列表进行组的操作，在列表发生变化时GroupedRecyclerViewAdapter需要及时对组结构进行调整，如果使用了RecyclerView.Adapter中的方法对列表进行更新，GroupedRecyclerViewAdapter可能因为无法及时调整组结构而发生异常。所以在使用中应该避免使用这些方法。GroupedRecyclerViewAdapter同样提供了一系列对列表进行操作的方法，我们应该使用GroupedRecyclerViewAdapter所提供的方法。

```java
	 //****** 刷新操作 *****//

    //通知数据列表刷新。对应 notifyDataSetChanged();
    public void notifyDataChanged();

    //通知一组数据刷新，包括组头,组尾和子项
    public void notifyGroupChanged(int groupPosition);

    //通知多组数据刷新，包括组头,组尾和子项
    public void notifyGroupRangeChanged(int groupPosition, int count);

    // 通知组头刷新
    public void notifyHeaderChanged(int groupPosition);

    // 通知组尾刷新
    public void notifyFooterChanged(int groupPosition);

    // 通知一组里的某个子项刷新
    public void notifyChildChanged(int groupPosition, int childPosition);

    // 通知一组里的多个子项刷新
    public void notifyChildRangeChanged(int groupPosition, int childPosition, int count);

    // 通知一组里的所有子项刷新
    public void notifyChildrenChanged(int groupPosition);

    //****** 删除操作 *****//
    // 通知所有数据删除
    public void notifyDataRemoved();

    // 通知一组数据删除，包括组头,组尾和子项
    public void notifyGroupRemoved(int groupPosition);

    // 通知多组数据删除，包括组头,组尾和子项
    public void notifyGroupRangeRemoved(int groupPosition, int count);

    // 通知组头删除
    public void notifyHeaderRemoved(int groupPosition);

    // 通知组尾删除
    public void notifyFooterRemoved(int groupPosition);

    // 通知一组里的某个子项删除
    public void notifyChildRemoved(int groupPosition, int childPosition);

    // 通知一组里的多个子项删除
    public void notifyChildRangeRemoved(int groupPosition, int childPosition, int count);

    // 通知一组里的所有子项删除
    public void notifyChildrenRemoved(int groupPosition);
    
    //****** 插入操作 *****//
    // 通知一组数据插入
    public void notifyGroupInserted(int groupPosition);

    // 通知多组数据插入
    public void notifyGroupRangeInserted(int groupPosition, int count);

    // 通知组头插入
    public void notifyHeaderInserted(int groupPosition);
    
    // 通知组尾插入
    public void notifyFooterInserted(int groupPosition);

    // 通知一个子项到组里插入
    public void notifyChildInserted(int groupPosition, int childPosition);

    // 通知一组里的多个子项插入
    public void notifyChildRangeInserted(int groupPosition, int childPosition, int count);

    // 通知一组里的所有子项插入
    public void notifyChildrenInserted(int groupPosition);
```
***注意：*** GroupedRecyclerViewAdapter不管理列表数据源，在调用notifyxxxRemoved或者notifyxxxInserted等方法刷新列表前，请相应的刷新数据源。也就是说，对数据的操作应该写在对列表的刷新前。

**3、使用GridLayoutManager的注意**

如果要使用GridLayoutManager，一定要使用项目中所提供的GroupedGridLayoutManager。因为分组列表如果要使用GridLayoutManager实现网格布局，就要保证组的头部和尾部是要单独占用一行的。否则组的头、尾可能会跟子项混着一起，造成布局混乱。同时GroupedGridLayoutManager提供了对子项的SpanSize的修改方法，使用GroupedGridLayoutManager可以实现更多的复杂列表布局。

```java
	//直接使用GroupedGridLayoutManager实现子项的Grid效果
    GroupedGridLayoutManager gridLayoutManager = new GroupedGridLayoutManager(this, 2, adapter);
   rvList.setLayoutManager(gridLayoutManager);
   

   GroupedGridLayoutManager gridLayoutManager = new GroupedGridLayoutManager(this, 4, adapter){
       //重写这个方法 改变子项的SpanSize。
       //这个跟重写SpanSizeLookup的getSpanSize方法的使用是一样的。
       @Override
       public int getChildSpanSize(int groupPosition, int childPosition) {
            if(groupPosition % 2 == 1){
                 return 2;
            }
            return super.getChildSpanSize(groupPosition, childPosition);
       }
   };
   rvList.setLayoutManager(gridLayoutManager);
```

**4、BaseViewHolder的使用**

项目中提供了一个通用的ViewHolder：BaseViewHolder。提供了根据viewId获取View的方法和对View、TextView、ImageView的常用设置方法。

```java
//根据id获取View
TextView  textView = holder.get(R.id.tv_header);

//View、TextView、ImageView的常用设置方法。并且支持方法连缀调用
holder.setText(R.id.tv_header, "内容")
                .setImageResource(R.id.iv_image, 资源id)
                .setBackgroundRes(R.id.view,资源id);
```
BaseViewHolder是可以通用的，在普通的Adapter中也可以使用，可以省去每次都要创建ViewHolder的麻烦。

**5、头部悬浮吸顶功能**

应一些朋友的反馈，我在1.2.0版本中新加了列表的头部悬浮吸顶功能。使用起来非常的简单，只需要用框架里提供的StickyHeaderLayout包裹一下你的RecyclerView就可以了。当然，你需要使用GroupedRecyclerViewAdapter才能看到效果。

```xml
    <!-- 用StickyHeaderLayout包裹RecyclerView -->
    <com.donkingliang.groupedadapter.widget.StickyHeaderLayout
        android:id="@+id/sticky_layout"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <androidx.recyclerview.widget.RecyclerView
            android:id="@+id/rv_list"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />
    </com.donkingliang.groupedadapter.widget.StickyHeaderLayout>
```
StickyHeaderLayout提供了一个设置是否显示悬浮吸顶的方法。
```java
	//是否吸顶，默认为true。
	stickyLayout.setSticky(true);
```

**6、使用DataBinding**

GroupedRecyclerViewAdapter在1.3.0版本加入了对DataBinding的支持。要想在Adapter中使用DataBinding，只需要在GroupedRecyclerViewAdapter的构造函数的useBinding参数传true即可。
```java
public class BindingAdapter extends GroupedRecyclerViewAdapter {

    public BindingAdapter(Context context) {
    	//只要在这里传true，Adapter就会用DataBinding的方式加载列表的item布局。默认为false。
        super(context, true);
    }
}
```
然后同过BaseViewHolder的getBinding()就可以获取到item对应的ViewDataBinding对象。
```java
    @Override
    public void onBindHeaderViewHolder(BaseViewHolder holder, int groupPosition) {
    	//获取ViewDataBinding对象。
        AdapterBindingHeaderBinding binding = holder.getBinding();
    }

    @Override
    public void onBindFooterViewHolder(BaseViewHolder holder, int groupPosition) {
    	//获取ViewDataBinding对象。
        AdapterBindingFooterBinding binding = holder.getBinding();
    }

    @Override
    public void onBindChildViewHolder(BaseViewHolder holder, int groupPosition, int childPosition) {
    	//获取ViewDataBinding对象。
        AdapterBindingChildBinding binding = holder.getBinding();
    }
```




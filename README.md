# GroupedRecyclerViewAdapter

GroupedRecyclerViewAdapter可以很方便的实现RecyclerView的分组显示，并且每个组都可以包含组头、组尾和子项；可以方便实现多种Type类型的列表，可以实现如QQ联系人的列表一样的列表展开收起功能等。下面先让我们看一下它所能够实现的一些效果：

![分组的列表](http://img.blog.csdn.net/20170323195156026?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMDE3NzAyMg==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)   ![不带组尾的列表](http://img.blog.csdn.net/20170323195228745?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMDE3NzAyMg==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)  ![不带组头的列表](http://img.blog.csdn.net/20170323195318281?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMDE3NzAyMg==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast) 

 ![子项为Grid的列表](http://img.blog.csdn.net/20170323195352204?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMDE3NzAyMg==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)   ![子项为Grid的列表(各组子项的Span不同)](http://img.blog.csdn.net/20170323195424205?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMDE3NzAyMg==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)  ![头、尾和子项都支持多种类型的列表](http://img.blog.csdn.net/20170323195508909?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMDE3NzAyMg==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)  

![多种子项类型的列表](http://img.blog.csdn.net/20170323195549660?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMDE3NzAyMg==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

还可以很容易的实时列表的展开收起效果：

![可展开收起的列表](https://github.com/donkingliang/GroupedRecyclerViewAdapter/blob/master/GroupedRecyclerViewAdapter%E6%BC%94%E7%A4%BA%E5%9B%BE/%E5%8F%AF%E5%B1%95%E5%BC%80%E6%94%B6%E8%B5%B7%E7%9A%84%E5%88%97%E8%A1%A8.gif)

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
	compile 'com.github.donkingliang:GroupedRecyclerViewAdapter:1.1.0'
```

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

	//刷新数据列表。对应 notifyDataSetChanged();
    public void changeDataSet();

    //刷新一组数据，包括组头,组尾和子项
    public void changeGroup(int groupPosition);

    //刷新多组数据，包括组头,组尾和子项
    public void changeRangeGroup(int groupPosition, int count);

    // 刷新组头
    public void changeHeader(int groupPosition);

    //刷新组尾
    public void changeFooter(int groupPosition);

    // 刷新一组里的某个子项
    public void changeChild(int groupPosition, int childPosition);

    //刷新一组里的多个子项
    public void changeRangeChild(int groupPosition, int childPosition, int count);

    // 刷新一组里的所有子项
    public void changeChildren(int groupPosition);

    //****** 删除操作 *****//
    // 删除所有数据
    public void removeAll();

    //删除一组数据，包括组头,组尾和子项
    public void removeGroup(int groupPosition);

    // 删除多组数据，包括组头,组尾和子项
    public void removeRangeGroup(int groupPosition, int count);

    // 删除组头
    public void removeHeader(int groupPosition);

    // 删除组尾
    public void removeFooter(int groupPosition);

    //删除一组里的某个子项
    public void removeChild(int groupPosition, int childPosition);

    // 删除一组里的多个子项
    public void removeRangeChild(int groupPosition, int childPosition, int count);

    //删除一组里的所有子项
    public void removeChildren(int groupPosition);
    
    //****** 插入操作 *****//
    // 插入一组数据
    public void insertGroup(int groupPosition);

    //插入一组数据
    public void insertRangeGroup(int groupPosition, int count);

    //插入组头
    public void insertHeader(int groupPosition);
    
    // 插入组尾
    public void insertFooter(int groupPosition);

    //插入一个子项到组里
    public void insertChild(int groupPosition, int childPosition);

    // 插入一组里的多个子项
    public void insertRangeChild(int groupPosition, int childPosition, int count);

    //插入一组里的所有子项
    public void insertChildren(int groupPosition);
```

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

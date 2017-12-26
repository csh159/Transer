package com.scott.example.adapter;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017/7/7.</p>
 * <p>Describe:
 *  参照BaseRecyclerViewAdapterHelper 实现的ListView adapter
 * </p>
 */

public abstract class BaseQuckListAdapter<T,VH extends BaseListHolder> extends BaseAdapter {

    private List<T> mDatas;
    protected SparseArray<Integer> layouts;
    protected LayoutInflater mInflater;

    public BaseQuckListAdapter(List<T> datas) {
        mDatas = datas;
    }


    public void addItemView(int type,int layout) {
        if(layouts == null) {
            layouts = new SparseArray<>();
        }
        layouts.put(type,layout);
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Object getItem(int i) {
        return mDatas.get(i);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        T data = mDatas.get(i);
        return createConvertView(data,i,view,viewGroup);
    }

    private View createConvertView(T t, int position,View convertView, ViewGroup parent) {
        VH holder = null;
        if(convertView != null) {
            holder = (VH) convertView.getTag();
            if(t instanceof IListData &&
                    holder.getItemViewType() != ((IListData)t).getItemType()) {
                holder = null;
            }
        }

        if(holder == null) {
            int layoutId = layouts.get(t instanceof IListData ?
                    ((IListData)t).getItemType() : 0);
            if (mInflater == null) {
                mInflater = LayoutInflater.from(parent.getContext());
            }
            convertView = mInflater.inflate(layoutId,parent,false);
            holder = createBaseViewHolder(convertView);
            convertView.setTag(holder);
        }
        holder.setItemType(t instanceof IListData ? ((IListData)t).getItemType() : 0);
        convert(holder,position,t);
        return convertView;
    }

    public abstract void convert(final VH holder,int position,final T item);

    /**
     * if you want to use subclass of BaseViewHolder in the adapter,
     * you must override the method to create new ViewHolder.
     *
     * @param view view
     * @return new ViewHolder
     */
    protected VH createBaseViewHolder(View view) {
        Class temp = getClass();
        Class z = null;
        while (z == null && null != temp) {
            z = getInstancedGenericKClass(temp);
            temp = temp.getSuperclass();
        }
        VH k = createGenericKInstance(z, view);
        return null != k ? k : (VH) new BaseListHolder(view);
    }

    /**
     * try to create Generic K instance
     *
     * @param z
     * @param view
     * @return
     */
    private VH createGenericKInstance(Class z, View view) {
        try {
            Constructor constructor;
            String buffer = Modifier.toString(z.getModifiers());
            String className = z.getName();
            // inner and unstatic class
            if (className.contains("$") && !buffer.contains("static")) {
                constructor = z.getDeclaredConstructor(getClass(), View.class);
                return (VH) constructor.newInstance(this, view);
            } else {
                constructor = z.getDeclaredConstructor(View.class);
                return (VH) constructor.newInstance(view);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * get generic parameter K
     *
     * @param z
     * @return
     */
    private Class getInstancedGenericKClass(Class z) {
        Type type = z.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
            for (Type temp : types) {
                if (temp instanceof Class) {
                    Class tempClass = (Class) temp;
                    if (BaseListHolder.class.isAssignableFrom(tempClass)) {
                        return tempClass;
                    }
                }
            }
        }
        return null;
    }

}

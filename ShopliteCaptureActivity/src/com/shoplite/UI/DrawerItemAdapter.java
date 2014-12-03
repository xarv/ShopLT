/**
 * 
 */
package com.shoplite.UI;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;

import com.google.zxing.client.android.CaptureActivity;
import com.shoplite.UI.BaseItemCard.OnClickActionButtonListener;
import com.shoplite.models.Product;

import eu.livotov.zxscan.R;

/**
 * @author I300291
 *
 */
public class DrawerItemAdapter extends ArrayAdapter<Product>{

	/**
	 * @param context
	 * @param resource
	 * @param objects
	 */
	 protected Context mContext;
	 
	 protected int mRowLayoutId = R.layout.drawer_item_container;
	 protected ArrayList<Product> drawerItemList;
	 protected int innerviewTypeCount=1;
	
	private AlertDialog AddDialog;

	public DrawerItemAdapter(Context context,ArrayList<Product> itemSimmilarFamily) {
			super(context,R.layout.drawer_item_container , itemSimmilarFamily);
			mContext = context;
			this.drawerItemList = itemSimmilarFamily;
		
	}

	 @Override
     public View getView(int position, View convertView, ViewGroup parent)
     {
		 if (convertView == null) {
		        // inflate if not recycled
		        convertView = LayoutInflater.from(mContext).inflate(R.layout.drawer_item_container, parent, false);
		        DrawerItemCard drawerItem = new DrawerItemCard(mContext,drawerItemList.get(position));
			 	drawerItem.setParentView(mContext, (ViewGroup) convertView);
			 	drawerItem.setActionButtonOnClick(new OnClickActionButtonListener() {
					
					@Override
					public void onClick(Product item, View view) {
						CaptureActivity.addToItem = new AddItemCard(mContext,item);
						BaseCardView itemContainer =(BaseCardView)AddDialog.findViewById(R.id.itemView);
						CaptureActivity.addToItem.setParentView(mContext, itemContainer);
						final DrawerLayout itemDrawerLayout = (DrawerLayout)AddDialog.findViewById(R.id.drawer_add_item);
				    	final FrameLayout itemDrawer = (FrameLayout)AddDialog.findViewById(R.id.simmilar_item_container);
				    	itemDrawerLayout.closeDrawer(itemDrawer);
				    	CaptureActivity.addToItem.setActionButtonOnClick(new OnClickActionButtonListener() {
							
							@Override
							public void onClick(Product itemCategory, View view) {
								itemDrawerLayout.openDrawer(itemDrawer);
							}
						});
					}
				});
		 }
		 	
		 	
		    // populate the views
		   
		    return convertView;
         
     }

     @Override
     public int getCount()
     {
         return this.drawerItemList != null ? this.drawerItemList.size() : 0;
     }

	/**
	 * @param itemContainer
	 */
	public void setAddCardView(AlertDialog addDialog) {
		
		this.AddDialog = addDialog;
	}

}

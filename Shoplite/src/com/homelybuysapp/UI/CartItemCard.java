package com.homelybuysapp.UI;


import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;

import com.homelybuys.homelybuysApp.R;
import com.homelybuysapp.Utils.Globals;
import com.homelybuysapp.activities.HomeActivity;
import com.homelybuysapp.fragments.CartFragment;
import com.homelybuysapp.interfaces.PackListInterface;
import com.homelybuysapp.models.OrderItemDetail;
import com.homelybuysapp.models.PackList;
import com.homelybuysapp.models.Product;


public class CartItemCard extends BasicCartItemCard implements PackListInterface{

	
	protected CustomNumberPicker measurePicker;
	protected CustomNumberPicker qtyPicker;
	protected ImageButton delete_button;
	
	
	//Animation Control Members
	protected float previouspoint = 0 ;
	protected float startPoint = 0;
	protected Resources r = mContext.getResources();
	protected float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, r.getDisplayMetrics());
	protected int SWIPE_MIN_DISTANCE = 50;
	protected boolean animationMode ;
	
	public CartItemCard(Context context,Product addedItem){
		super(context,addedItem);
	}
	public void setParentView(Context context,ViewGroup container, ArrayList<Product> cartItemListRecieved,
			 ItemListAdapter cartItemAdapter)
	{
		this.cartItemList = cartItemListRecieved;
		this.itemListAdapter = cartItemAdapter;
		setUpView(context, container);
	}
	
	
	public boolean isAnimationMode() {
		return animationMode;
	}
	public void setAnimationMode(boolean animationMode) {
		this.animationMode = animationMode;
	}
		
	
	public Button getItemButton() {
		return itemButton;
	}
	public void setItemButton(Button itemButton) {
		this.itemButton = itemButton;
	}
		
	public NumberPicker getMeasurePicker() {
		return measurePicker;
	}

	public void setMeasurePicker(CustomNumberPicker measurePicker) {
		this.measurePicker = measurePicker;
	}

	public NumberPicker getQtyPicker() {
		return qtyPicker;
	}

	public void setQtyPicker(CustomNumberPicker qtyPicker) {
		this.qtyPicker = qtyPicker;
	}

	
	
	
	
	@Override
	public void setUpView(Context context , ViewGroup container) {
		super.setUpView(context, container);
		
		setMeasurePicker((CustomNumberPicker)containerView.findViewById(R.id.item_measure_picker));
		setQtyPicker((CustomNumberPicker)containerView.findViewById(R.id.item_quantity_picker));
		
		itemEditView.setVisibility(View.GONE);
		itemCheckBox.setVisibility(View.GONE);
		itemButton.setVisibility(View.VISIBLE);
		
		
		delete_button = (ImageButton)containerView.findViewById(R.id.delete_cart_item);
      	delete_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(ViewCompat.hasTransientState(innerView)){
					
					final Animation animation = AnimationUtils.loadAnimation(mContext,R.anim.slideout);
				    animation.setAnimationListener(new AnimationListener() {
				        @Override
				        public void onAnimationStart(Animation animation) {
				        }

				        @Override
				        public void onAnimationRepeat(Animation animation) {
				        }

				        @Override
				        public void onAnimationEnd(Animation animation) {
				        	delete_from_list();
				        }
				    });
				    containerView.startAnimation(animation);
				    
					
					
				}
				
				
			}
		});
	
		
		itemButton.setOnClickListener(new OnClickListener() {
			Double itemCurrentPrize = null;
			@Override
			public void onClick(View v) {
				Animation animFadeOut = AnimationUtils.loadAnimation(mContext, android.R.anim.fade_out);
		    	 Animation animFadeIn = AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in);
		    	 
		    	 //initiate edit
					if(getItemEditView().getVisibility() == View.GONE){
						 ViewCompat.setHasTransientState(innerView,true);
						getItemEditView().setAnimation(animFadeIn);
					    getItemEditView().setVisibility(View.VISIBLE);
						setActionButtonText("Done");
						itemCurrentPrize = item.getTotalPrice();
					
					}
				//finalize edit
					
					
					else{
						if (itemCurrentPrize != null){
							Globals.cartTotalPrice -= itemCurrentPrize;
							Globals.cartTotalPrice += item.getTotalPrice();
						}
						HomeActivity.actionBar.setTitle(r.getText(R.string.shopping_cart)+
        				"    " + Double.toString(Math.round(Globals.cartTotalPrice*100.0/100.0)) +" "+ r.getText(R.string.currency));
						getItemEditView().setAnimation(animFadeOut);
				    	getItemEditView().setVisibility(View.GONE);
						setActionButtonText("Edit");
						 ViewCompat.setHasTransientState(innerView,false);
						
					}
			}
		});
		setActionButtonText(context.getResources().getString(R.string.edit));
		containerView.setOnTouchListener(new OnTouchListener() {
			
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch(event.getAction())
                {
	                case MotionEvent.ACTION_DOWN:
	                {          
	                    startPoint=event.getX();
	                                           
	                }
	                break;
	                case MotionEvent.ACTION_MOVE:
	                {       
	                	
	                	previouspoint=event.getX();
		                 
	                	 if(previouspoint - startPoint > SWIPE_MIN_DISTANCE){
	                		 //Right side swap
	         				

	                		 Log.e("current animationmode",String.valueOf(ViewCompat.hasTransientState(innerView)));
	                		 if(ViewCompat.hasTransientState(innerView)){
	                			 ViewCompat.setHasTransientState(innerView,false);
	                			TranslateAnimation Anim = new TranslateAnimation(-px, 0, 0, 0);
		           			 	Anim.setInterpolator(new BounceInterpolator());
		           			 	Anim.setDuration(300);
		           			 	Anim.setFillAfter(true);
		           			 	innerView.startAnimation(Anim);
		           			   
		           			 	animationMode = false;
		           			 	getItemButton().setEnabled(true);
		                    }
	
	                    }else if(startPoint - previouspoint > SWIPE_MIN_DISTANCE){
	                    	// Left side swap

	                    	 Log.e("current animationmode",String.valueOf(animationMode));
	                    	if(!innerView.hasTransientState()){
	                    		 ViewCompat.setHasTransientState(innerView,true);
	                    		TranslateAnimation Anim = new TranslateAnimation(0, -px, 0, 0);
	                    		Anim.setInterpolator(new BounceInterpolator());
		           			 	Anim.setDuration(300);
		           			 	Anim.setFillAfter(true);
		           			 	innerView.startAnimation(Anim);
		           			 	getItemButton().setEnabled(false);
		           			 	animationMode = true;
		           			   
	                    	}
	                    }
	                }break;
	                case MotionEvent.ACTION_CANCEL:
	                {       
                	
	
	                }break;
                }
				
				return true;
			}
		});
		
		initMeasurePicker();
		initQtyPicker();
		updateView();
		
		String url = "http://s3-ap-southeast-1.amazonaws.com/static.shoplite/product_image/"+getItem().getId()+".jpg";
		fetchItemImage(url);
				
		
		
		
		
		
	}

	public void initMeasurePicker(){
		String[]  measureStringArray = new String[item.getItemList().size()] ;
		for(int i = 0 ; i < item.getItemList().size();i++ ){
			measureStringArray[i] = item.getItemList().get(i).getName();
		}
		try{
			measurePicker.setMinValue(0);
			measurePicker.setMaxValue(0);
			measurePicker.setDisplayedValues(measureStringArray);
			measurePicker.setMaxValue(item.getItemList().size()-1);
			measurePicker.setOnValueChangedListener(new OnValueChangeListener() {
				
				@Override
				public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
					item.setCurrentItemId(item.getItemList().get(newVal).getId());
					item.setCurrentMeasure(item.getItemList().get(newVal).getName());
					item.setCurrentMsrPrice( item.getItemList().get(newVal).getPrice());
					item.setTotalPrice( (double) Math.round((item.getCurrentQty()* item.getCurrentMsrPrice() * 100.0)/100.0));
					
					updateView();
				}
			});
			measurePicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public void initQtyPicker(){
		try{
			String[]  qtyStringArray = {"1 Qty", "2 Qty", "3 Qty", "4 Qty","5 Qty"};
			
				
			qtyPicker.setMaxValue(0);
			qtyPicker.setMinValue(0);
			qtyPicker.setDisplayedValues(qtyStringArray);
			qtyPicker.setMaxValue(4);

			qtyPicker.setOnValueChangedListener(new OnValueChangeListener() {
				
				@Override
				public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
					
					item.setCurrentQty( newVal + 1);
					item.setTotalPrice( (double) Math.round((item.getCurrentQty()*item.getCurrentMsrPrice() * 100.0)/100.0));
					updateView();
				}
			});
			qtyPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	

	@Override
	public void updateView(){
		super.updateView();
		
	}
			
	@Override
	public OnClickActionButtonListener getActionButtonOnClick() {
		return super.getOnClickActionButtonListener();
		 
	}
	
	@Override
	public void setActionButtonText(String text) {
		super.setActionButtonText(text);
	}

	@Override
	public void setActionButtonOnClick(	OnClickActionButtonListener actionButtonListener) {
		super.setOnClickActionButtonListener(actionButtonListener);
	}
	
	@Override
	public void fetchItemImage(String url) {
		super.fetchItemImage(url);
	}

	
	
	//Packlist Interface Methods
	
	protected void delete_from_list() {
		
	 	int index = Globals.item_added_list.indexOf(getItem().getCurrentItemId());
	 	if(index >=0){
			TranslateAnimation Anim = new TranslateAnimation(-px, 0, 0, 0);
		 	Anim.setDuration(10);
		 	Anim.setFillAfter(true);
		 	innerView.startAnimation(Anim);
		 	
		 	animationMode = false;
		 	getItemButton().setEnabled(true);
		 	
		 	cartItemList.remove(getItem());
		 	
		 	Globals.item_added_list.remove(index);
		 	Globals.cartTotalPrice -= item.getTotalPrice();
		 	
		 	HomeActivity.actionBar.setTitle(r.getText(R.string.shopping_cart)+
					 "    "+  Double.toString(Math.round(Globals.cartTotalPrice*100.0/100.0))+" " + r.getText(R.string.currency));
		 	HomeActivity.productsNumberView.setText(String.valueOf(cartItemList.size()));
		 	
	
			if(cartItemList.isEmpty()){
				CartFragment.emptyCartState();
			}
			itemListAdapter.updateCart(cartItemList);
	 	}
	 	innerView.setHasTransientState(false);
	}
	
	@Override
	public void sendPackList() {
		
	}
	
	@Override
	public void PackListSuccess(PackList obj) {
//		if(obj.pckProd.getState()==DBState.DELETE){
//			for(int i = 0 ;i < obj.pckProd.getProducts().size() ; i++){
//				if(CartGlobals.cartList.contains(obj.pckProd.getProducts().get(i)))
//					CartGlobals.cartList.remove(obj.pckProd.getProducts().get(i));
//				if(CartGlobals.recentDeletedItems.contains(obj.pckProd.getProducts().get(i)))
//					CartGlobals.recentDeletedItems.remove(obj.pckProd.getProducts().get(i));
//			}
//			
//		}
//		else if (obj.pckProd.getState() == DBState.INSERT){
//			for(int i = 0 ;i < obj.pckProd.getProducts().size() ; i++){
//				CartGlobals.cartList.add(obj.pckProd.getProducts().get(i));
//			}
//		}
//		else{
//			
//		}
	}
	
	@Override
	public void editPackList() {
//		if(item.isSent()){
//			OrderItemDetail itemToDelete = new OrderItemDetail(item.getCurrentItemId(), item.getCurrentQty());
//			PackList pl = new PackList();
//		}
	}
	
	@Override
	public void deletePackList(OrderItemDetail itemToDelete) {
//		ArrayList<OrderItemDetail> deleteList = new ArrayList<OrderItemDetail>();
//		deleteList.add(itemToDelete);
//		PackList pl = new PackList();
//		pl.pckProd = new PackProducts( DBState.DELETE, Product.getToSendList(Globals.item_order_list));
//		
//	
//		//CartGlobals.CartServerRequestQueue.add(pl);
//		pl.sendPackList(this);
	}
	@Override
	public void packListFailure() {
		// TODO Auto-generated method stub
		
	}
	

	
}

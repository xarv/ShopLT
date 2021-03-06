package com.homelybuysapp.fragments;


import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.homelybuys.homelybuysApp.R;
import com.homelybuysapp.UI.Controls;
import com.homelybuysapp.UI.ItemListAdapter;
import com.homelybuysapp.Utils.Globals;
import com.homelybuysapp.activities.CheckoutActivity;
import com.homelybuysapp.activities.HomeActivity;
import com.homelybuysapp.interfaces.ControlsInterface;
import com.homelybuysapp.models.SaveList;




public class CartFragment extends Fragment implements ControlsInterface {

	private MenuItem importToCart;
	protected ListView cartItemsListView;
	protected static ItemListAdapter cartAdapter;
	protected static LinearLayout emptyCartView;
	public static AlertDialog alertDialog;
	private static Button saveListButton;
	private static Button checkoutButton;
	protected static DialogFragment saveListDialog;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        
		View root = inflater.inflate(R.layout.activity_cart, container, false);
		root.setOnTouchListener(new OnTouchListener()
	    {
			@Override
			public boolean onTouch(View v, MotionEvent arg1) {
				return true;
			}
	    });
		saveListButton = (Button) root.findViewById(R.id.save_cart_list);
		checkoutButton = (Button) root.findViewById(R.id.checkout_button);
		cartItemsListView = (ListView)root.findViewById(R.id.cart_item_list_view);
		emptyCartView = (LinearLayout)root.findViewById(R.id.empty_cart_view);
		 setHasOptionsMenu(true);
        return root;
    }
		
	
	@Override
	public void onResume()
	{
		super.onResume();	
		cartAdapter = new ItemListAdapter(getActivity(), Globals.item_order_list,"cartItem");
		HomeActivity.actionBar.setTitle(getResources().getText(R.string.shopping_cart)+
				"    " + Double.toString(Math.round(Globals.cartTotalPrice*100.0/100.0)) + " " +getResources().getText(R.string.currency));
		HomeActivity.productsNumberView.setText(String.valueOf(Globals.item_order_list.size()));
		if(Globals.item_order_list.size() == 0){
			emptyCartState();
			cartAdapter.updateCart(Globals.item_order_list);
		}
		else{
			filledCartState();
			saveListButton.setOnClickListener(new OnClickListener() {
		          public void onClick(View v) {
		        	  if(Globals.item_order_list.size() >0){
		        		  show_save_list_dialog();
		        	  }
		          }
		       });
			checkoutButton.setOnClickListener(new OnClickListener() {
		          public void onClick(View v) {
		        	  	checkout();
		          }			
		       });
			
			
			cartItemsListView.setAdapter(cartAdapter);
			
		}
		
		
		
	}
	
	public static void emptyCartState()
	{
		emptyCartView.setVisibility(View.VISIBLE);
		saveListButton.setVisibility(View.GONE);
		checkoutButton.setVisibility(View.GONE);
	}
	public static void filledCartState()
	{
		saveListButton.setVisibility(View.VISIBLE);
		checkoutButton.setVisibility(View.VISIBLE);
		emptyCartView.setVisibility(View.GONE);
	}
	public static void updateCart()
	{
		if(cartAdapter != null){
			cartAdapter.updateCart(Globals.item_order_list);
		}
	}
		
	@Override
	public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
//		// Inflate the menu; this adds products to the action bar if it is present.
//		super.onCreateOptionsMenu(menu, getActivity().getMenuInflater());
//		return false;
		inflater.inflate(R.menu.import_to_cart,menu);
		HomeActivity.productsNumberView.setText(String.valueOf(Globals.item_order_list.size()));
	   importToCart = menu.findItem(R.id.import_to_cart);
//       importToCart.setVisible(true);
       importToCart.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				//if(!cartFrag.isDetached()){
					ArrayList<SaveList> savedLists = Globals.dbhelper.getAllSavedShopList();
					if(savedLists.size()>0){
						saveListDialog = SavedListsFragment.newInstance();
						saveListDialog.show(getChildFragmentManager(), "SaveListDialog"); 
						
					}
					else{
						Toast.makeText(getActivity(), "You Don't have any Saved List, Create One to Use Import",
								Toast.LENGTH_LONG).show();
					}
				//}
				return true;
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		
		return true;
		//return super.onOptionsItemSelected(item);
	}
	
	protected void show_save_list_dialog()
	{
		Controls.show_alert_dialog( this, getActivity(), R.layout.save_list_layout,200);
	}
	private void checkout() {
		
		Intent i = new Intent(getActivity(),CheckoutActivity.class);
		getActivity().startActivity(i);
		
	}
	
	
	/* (non-Javadoc)
	 * @see com.homelybuysapp.interfaces.ControlsInterface#positive_button_alert_method()
	 */
	@Override
	public void positive_button_alert_method() {
		EditText listNameInput = (EditText) CartFragment.alertDialog.findViewById(R.id.list_save_name_input);
		listNameInput.setError(null);
		if(listNameInput.getText().toString().length() <= 0){
			listNameInput.setError(getString(R.string.error_field_required));
		}
		else{
			try{
				
				boolean result = Globals.dbhelper.storeShoppingList(listNameInput.getText().toString(),Globals.item_order_list);
				if(result){
					alertDialog.dismiss();
					Toast.makeText(getActivity(), "Shopping List saved successfully as "+listNameInput.getText().toString(), Toast.LENGTH_LONG).show();
					SaveList recentlySavedList = Globals.dbhelper.getSavedShopList(listNameInput.getText().toString());
					if(recentlySavedList != null){
						SavedListsFragment.getSavedLists().add(recentlySavedList);
						SavedListsFragment.getSaveListAdapter().updateSavedLists(SavedListsFragment.getSavedLists());
					}
					
				}
				else{
					listNameInput.setError("Shopping List already exist with the name "+listNameInput.getText().toString());
					
				}
					
			}
			catch(Exception e){
				e.printStackTrace();
				
			}

		}
	}
	
	@Override
	public void negative_button_alert_method() {
		CartFragment.alertDialog.dismiss();
			
	}
	
	@Override
	public void save_alert_dialog(AlertDialog alertDialog) {
		CartFragment.alertDialog = alertDialog;
	}
	
	@Override
	public void neutral_button_alert_method() {
		
	}
	
	

}

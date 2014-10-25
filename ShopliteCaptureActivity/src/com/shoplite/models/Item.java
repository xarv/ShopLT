package com.shoplite.models;


import java.util.ArrayList;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.util.Log;
import com.shoplite.Utils.Globals;
import com.shoplite.connection.ConnectionInterface;
import com.shoplite.connection.ServerConnectionMaker;
import com.shoplite.connection.ServiceProvider;
import com.shoplite.interfaces.ItemInterface;

public class Item implements ConnectionInterface {

	private static ItemInterface calling_class_object;
	private int id;
	private String name;
	private int itemCategory;
	private double price;
	private int quantity;
	private boolean get_item_bool;
	private boolean get_items_from_brand_bool;
	private Input ItemInput;
	private Input brandInput;
	
	public int getId() {
		return this.id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getItemCategory() {
		return this.itemCategory;
	}
	public void setItemCategory(int itemCategory) {
		this.itemCategory = itemCategory;
	}
	public double getPrice() {
		return this.price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getQuantity() {
		return this.quantity;
	}
	public void setQuantity(int barcode) {
		this.quantity = barcode;
	}
	
	public Item(int id, String name, double price, int quantity) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.quantity = quantity;
	}
	
	public  void getItem( ItemInterface calling_class_object) {
		
		Item.calling_class_object = calling_class_object;
		this.get_item_bool = true;
		ItemInput = new Input(this.getId(),"ItemCategoryId");
		
		String shopURL = null;
		shopURL  = "https://" + Globals.connected_shop_url;
		ServerConnectionMaker.sendRequest(this, shopURL);
		
	}
	
	public  void getItems( ItemInterface calling_class_object,int brandId) {
		
		Item.calling_class_object = calling_class_object;
		this.get_items_from_brand_bool = true;
		brandInput = new Input(brandId,"brand");
		
		String shopURL = null;
		shopURL  = "https://" + Globals.connected_shop_url;
		ServerConnectionMaker.sendRequest(this, shopURL);
		
	}
	
	@Override
	public void sendRequest(ServiceProvider serviceProvider) {
		if(this.get_items_from_brand_bool == true ){
			serviceProvider.getItems( brandInput,new Callback<ArrayList<ItemCategory>>(){

				@Override
				public void failure(RetrofitError response) {
					if (response.isNetworkError()) {
						Log.e("Service Unavailable", "503"); // Use another code if you'd prefer
				    }
					else{
						Log.e("Get Items Failure",response.getMessage());
					}
					
					ServerConnectionMaker.recieveResponse(null);
					
				}

				@Override
				public void success(ArrayList<ItemCategory> itemFamily, Response response) {
					
					ServerConnectionMaker.recieveResponse(response);
					Globals.simmilar_item_list = itemFamily;
					Item.calling_class_object.ItemListGetSuccess(itemFamily);
				}
				
			});
		}
		else if(this.get_item_bool == true){
			serviceProvider.getItem( ItemInput,new Callback<ItemCategory>(){

				@Override
				public void failure(RetrofitError response) {
					if (response.isNetworkError()) {
						Log.e("Service Unavailable", "503"); 	
				    }
					else{
						Log.e("Get Item Failure",response.getMessage());
					}
					
					ServerConnectionMaker.recieveResponse(null);
					
				}

				@Override
				public void success(ItemCategory item, Response response) {
					
					ServerConnectionMaker.recieveResponse(response);
					Globals.fetched_item_category = item;
								//Currently calling all the products at the same time of the item fetch, have to move it to demand based fetching
					Item.calling_class_object.ItemGetSuccess(item);
					
				}
				
			});
		}
	
	}

}


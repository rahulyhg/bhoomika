package khaanavali.customer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import khaanavali.customer.adapter.PlusMinusButtonListener;
import khaanavali.customer.adapter.ProductAdapter;
import khaanavali.customer.model.HotelDetail;
import khaanavali.customer.model.MenuAdapter;
import khaanavali.customer.model.Order;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity implements PlusMinusButtonListener {


    Order order;
    HotelDetail hotelDetail;
    ArrayList<MenuAdapter> mMenulist;
    ProductAdapter dataAdapter;
    TextView orderTotalCharge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Intent intent = getIntent();
        Gson gson = new Gson();
        order = gson.fromJson(intent.getStringExtra("order"), Order.class);
        hotelDetail = gson.fromJson(intent.getStringExtra("HotelDetail"), HotelDetail.class);

        int totalCost = 0;
        mMenulist = new ArrayList<MenuAdapter>();
        for(int i = 0; i< order.getMenuItems().size();i++)
        {
            MenuAdapter menuAdapter = new MenuAdapter( order.getMenuItems().get(i));
            mMenulist.add(menuAdapter);
            totalCost +=  menuAdapter.getNo_of_order() * menuAdapter.getPrice();
        }
        order.setBill_value(totalCost,hotelDetail.getDeliverCharge());


        dataAdapter = new ProductAdapter(CartActivity.this,
                R.layout.product_detail_list_layout,mMenulist);
        dataAdapter.setListener(this);
        ListView listView = (ListView) findViewById(R.id.listView_cart);

        TextView deliveryCharge = (TextView) findViewById(R.id.orderDetailDeliveryRupees);
        orderTotalCharge = (TextView) findViewById(R.id.order_total_charge);

        listView.setAdapter(dataAdapter);
        Button btn= (Button) findViewById(R.id.orderDetailButton_next);

        //TextView vendor_name = (TextView) findViewById(R.id.vendor_add_cart_name);
        //vendor_name.setText(order.getHotel().getName());

        deliveryCharge.setText(String.valueOf(hotelDetail.getDeliverCharge()));
        orderTotalCharge.setText(String.valueOf(order.getTotalCost()));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ArrayList<MenuAdapter> menulist = dataAdapter.getmMenulist();
//                int total =0;
//                for(int i = 0; i< menulist.size();i++)
//                {
//                    if(menulist.get(i).getNo_of_order() > 1) {
//                        total += menulist.get(i).getNo_of_order() * menulist.get(i).getPrice();
//                    }
//                }
//                order.setBill_value(total);
                if (order.getTotalCost() <= 0) {
                    Toast.makeText(getApplicationContext(), "Cart Empty -please select Some Items", Toast.LENGTH_LONG).show();
                } else {
                    Intent i = new Intent(CartActivity.this, CutomerEnterDetailsActivity.class);
                    Gson gson = new Gson();

                    String strOrder = gson.toJson(order);
                    i.putExtra("order", strOrder);
                    String strHotelDetail = gson.toJson(hotelDetail);
                    i.putExtra("HotelDetail",strHotelDetail);
                    startActivity(i);
                }
            }
        });
        setToolBar(order.getHotel().getName());
    }
    private void setToolBar(String title) {
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(tb);

        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_action_back);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void buttonClicked(int position, int value) {
        ArrayList<MenuAdapter> menulist = dataAdapter.getmMenulist();
        int total =0;
        for(int i = 0; i< menulist.size();i++)
        {
            if(menulist.get(i).getNo_of_order() > 0) {
                total += menulist.get(i).getNo_of_order() * menulist.get(i).getPrice();
            }
        }
        order.setBill_value(total,hotelDetail.getDeliverCharge());
        orderTotalCharge.setText(String.valueOf(order.getBill_value()));
    }
}

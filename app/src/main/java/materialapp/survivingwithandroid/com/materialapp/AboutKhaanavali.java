package materialapp.survivingwithandroid.com.materialapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;


/**
 * Created by dganeshappa on 6/4/2016.
 */

/**
 * Created by gagan on 11/6/2015.
 */
public class AboutKhaanavali extends Fragment {

    View rootview;
    // Session Manager Class
    // Button Logout
    Button btnLogout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview=inflater.inflate(R.layout.about_khaanavali,container,false);
        // Session class instance


        TextView lblEmail = (TextView) rootview.findViewById(R.id.lblknvlEmail);
        TextView lblphone = (TextView) rootview.findViewById(R.id.lblknvlPhone);
        TextView lblwebsite = (TextView) rootview.findViewById(R.id.lblknvlwebsite);
        ((MainActivity) getActivity())
                .setActionBarTitle("About Khaanavali");

        // get user data from session// displaying user data

        lblEmail.setText(Html.fromHtml("Email: <b>" + "dayasudhankg@gmail.com" + "</b>"));
        lblphone.setText(Html.fromHtml("Phone: <b>" + "9566229075" + "</b>"));
        lblwebsite.setText(Html.fromHtml("Website: <b>" + "http://khaanavali.com" + "</b>"));


        return rootview;
    }



}

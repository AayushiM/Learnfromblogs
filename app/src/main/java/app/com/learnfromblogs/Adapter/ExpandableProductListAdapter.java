package app.com.learnfromblogs.Adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import app.com.learnfromblogs.API.API;
import app.com.learnfromblogs.Model.AllFeed.Datum;
import app.com.learnfromblogs.Model.ModelChild;
import app.com.learnfromblogs.Model.repoer.ReportModel;
import app.com.learnfromblogs.R;
import app.com.learnfromblogs.Utils.APIClient;
import app.com.learnfromblogs.Utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Champ on 12-03-2018.
 */

public class ExpandableProductListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader;
   // header titles
    // child data in format of header title, child title
    private HashMap<String, List<ModelChild>> _listDataChild;
    String  strTokenId,strUid;

    FragmentManager fragmentManager;
    Fragment fragment;
    Bundle bundle;
    FragmentTransaction fragmentTransaction;

    public ExpandableProductListAdapter(Context context,  List<String> listDataHeader,
                                        HashMap<String, List<ModelChild>> listChildData,String  strTokenId,String strUid) {
        this._context = context;

        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.strUid = strUid;
        this.strTokenId = strTokenId;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {



        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_submenu, null);
        }

        TextView txtName = (TextView) convertView.findViewById(R.id.submenu_txt_name);
        TextView txtDescription = (TextView) convertView.findViewById(R.id.submenu_txt_description);
        TextView txtTime = (TextView) convertView.findViewById(R.id.submenu_txt_date);
        ImageView submenu_img_close =  convertView.findViewById(R.id.submenu_img_close);
        final int childText1 = (int) getChildId(groupPosition, childPosition);
        submenu_img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteNotification(_listDataChild.get(_listDataHeader.get(groupPosition)).get(childText1).getId());
                _listDataChild.remove(_listDataHeader.get(groupPosition));
                notifyDataSetChanged();
            }
        });

//

        txtName.setText(_listDataChild.get(this._listDataHeader.get(groupPosition)).get(childText1).getStrName());
        txtDescription.setText(_listDataChild.get(this._listDataHeader.get(groupPosition)).get(childText1).getStrMessage());
        txtTime.setText(_listDataChild.get(this._listDataHeader.get(groupPosition)).get(childText1).getStrTime());





        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
//        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.listheader, null);
        }


        TextView lblListHeader = (TextView) convertView.findViewById(R.id.submenu);
        String date = Constants.DateFormate(_listDataHeader.get(groupPosition));
        Log.e("date",date);
        lblListHeader.setText(date);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private void deleteNotification(String id) {
        final ProgressDialog loading = new ProgressDialog(_context);
                loading.setMessage("Please Wait..");
                loading.setCancelable(false);
                loading.setCanceledOnTouchOutside(false);
                loading.show();
                API apiService = APIClient.getClient().create(API.class);
                Call<ReportModel> call1 = apiService.api_delete_notification(strTokenId,strUid,id);
                call1.enqueue(new Callback<ReportModel>() {
                    @Override
                    public void onResponse(Call<ReportModel> call, retrofit2.Response<ReportModel> response) {
                        loading.dismiss();
                        try {
                            if (response.body().getSuccess()){
                                Toast.makeText(_context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ReportModel> call, Throwable t) {
                        loading.dismiss();
                        Log.e("loginData", t.getMessage() + "");
                    }
                });
            }


}

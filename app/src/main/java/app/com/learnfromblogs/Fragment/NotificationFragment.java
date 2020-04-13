package app.com.learnfromblogs.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import app.com.learnfromblogs.API.API;
import app.com.learnfromblogs.Adapter.ExpandableProductListAdapter;
import app.com.learnfromblogs.Model.ModelChild;
import app.com.learnfromblogs.R;
import app.com.learnfromblogs.Utils.APIClient;
import app.com.learnfromblogs.Utils.Constants;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class NotificationFragment extends Fragment {

    RecyclerView rcNotification;
    public String strUid,strToken;
    HashMap<String, List<ModelChild>> listDataChild;
    ExpandableListView expandableListView;
    List <String> uniqueList;
    ExpandableProductListAdapter adapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_notification_fragment, container, false);
        Constants.hideKeyboard(getActivity());
        strToken = Constants.loginSharedPreferences.getString(Constants.logintoken,"");
        strUid = Constants.loginSharedPreferences.getString(Constants.uid,"");

        findId(root);
        apiNotification();

        return root;
    }

    private void apiNotification() {

        final ProgressDialog loading = new ProgressDialog(getActivity());
        loading.setMessage("Please Wait..");
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);
        loading.show();


        API apiService = APIClient.getClient().create(API.class);
        Call<ResponseBody> call1 = apiService.api_notification(strToken,strUid,""+1);
        call1.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                loading.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONObject jsondata1 = jsonObject.getJSONObject("data");
                    JSONObject jsondata2 = jsondata1.getJSONObject("data");
                    Log.e("success",""+jsondata2);

                    uniqueList = new ArrayList<>();
                    listDataChild = new HashMap<>();
                    Iterator<?> keys = jsondata2.keys();
                    while( keys.hasNext() ) {
                        String key = (String)keys.next();
                        uniqueList.add(key);
//                        Log.e("keyyyyyyyy","1   "+key);

                        JSONArray arr = jsondata2.getJSONArray(key);
                        JSONObject element;
                        List<ModelChild> subctname = new ArrayList<>();
                        for(int i = 0; i < arr.length(); i++){
                            element = arr.getJSONObject(i);
                            ModelChild modelChild = new ModelChild();
                            String msg = element.getString("message");
                            String name = element.getString("username");
                            String date = element.getString("created_at");
                            String id = element.getString("id");
                            modelChild.setStrMessage(msg);
                            modelChild.setStrName(name);
                            modelChild.setStrTime(date);
                            modelChild.setId(id);
                            subctname.add(modelChild);
//                            Log.e("msg",msg);
                        }
                        listDataChild.put(key, subctname);
//                        if ( jsondata2.get(key) instanceof JSONObject ) {
////                            System.out.println(key); // do whatever you want with it
//                            Log.e("key","1   "+key);
//                        }
                    }

                    Log.e("uniqueList", String.valueOf(uniqueList.size()));
                    adapter = new ExpandableProductListAdapter(getActivity(),uniqueList, listDataChild,strToken,strUid);
                    expandableListView.setAdapter(adapter);

                    expandableListView.setDividerHeight(0);
                    for(int i=0; i < adapter.getGroupCount(); i++){
                        expandableListView.expandGroup(i);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loading.dismiss();

                Log.e("loginData", t.getMessage() + "");
            }
        });

    }

    private void findId(View root) {
//        rcNotification = root.findViewById(R.id.notification_recyclerview);
        expandableListView = root.findViewById(R.id.notification_expandable_list);
    }
}

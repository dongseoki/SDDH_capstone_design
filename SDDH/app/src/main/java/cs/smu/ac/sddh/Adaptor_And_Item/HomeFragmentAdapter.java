package cs.smu.ac.sddh.Adaptor_And_Item;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import cs.smu.ac.sddh.fragments.HomeItemView;

public class HomeFragmentAdapter extends BaseAdapter {
    ArrayList<HomeInfo> infos=new ArrayList<>();
    @Override
    public int getCount() {
        return infos.size();
    }

    @Override
    public Object getItem(int position) {
        return infos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public void addItem(HomeInfo homeInfo){
        infos.add(homeInfo);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HomeItemView view=new HomeItemView(parent.getContext());
        HomeInfo homeInfo=infos.get(position);
        view.setUnivImage(homeInfo.getUnivImage());
        view.setUnivName(homeInfo.getUnivName());
        return view;
    }
}

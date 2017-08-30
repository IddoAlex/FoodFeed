package colman.iddo.foodfeed.fragments;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.LinkedList;
import java.util.List;

import colman.iddo.foodfeed.R;
import colman.iddo.foodfeed.model.FoodItemModel;
import colman.iddo.foodfeed.model.FoodItem;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FoodItemsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class FoodItemsFragment extends Fragment {

    static final int REQUEST_WRITE_STORAGE = 2;

    private OnFragmentInteractionListener mListener;

    private List<FoodItem> data = new LinkedList<>();

    public FoodItemsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_food_items, container, false);

        ListView list = contentView.findViewById(R.id.fliList);

        final FoodItemsAdapter adapter = new FoodItemsAdapter(getActivity());

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*
                // TODO: change accordingly
                StudentDetailsFragment studentDetailsFragment = new StudentDetailsFragment();
                Bundle myBundle = new Bundle();
                myBundle.putString("STID", data.get(position).id);
                studentDetailsFragment.setArguments(myBundle);
                getFragmentManager().beginTransaction()
                        .replace(((ViewGroup) getView().getParent()).getId(), studentDetailsFragment).addToBackStack(null).commit();
                */
            }
        });

        FoodItemModel.instance.getAllFoodItemsAndObserve(new FoodItemModel.GetAllStudentsAndObserveCallback() {
            @Override
            public void onComplete(List<FoodItem> list) {
                data = list;
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancel() {
                // Nothing to do
            }
        });


        // App permissions
        boolean hasPermission = (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(getActivity(),new String[]{
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
        }

        return contentView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    class FoodItemsAdapter extends BaseAdapter {

        private Context context;

        public FoodItemsAdapter(Context context){
            this.context = context;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            return null;
        }
    }
}

package colman.iddo.foodfeed.activities;

import android.content.Context;
import android.os.Bundle;
import colman.iddo.foodfeed.R;

public class MainActivity extends BaseActivity {

    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();

        setContentView(R.layout.activity_main);
    }

    public static Context getMyContext() {
        return context;
    }
}

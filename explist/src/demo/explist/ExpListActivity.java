package demo.explist;

import java.util.ArrayList;
import java.util.List;

import demo.explist.classes.DomParser;
import demo.explist.classes.MainGroup;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

public class ExpListActivity extends Activity implements Runnable {	
	private List<MainGroup> lsGroup = new ArrayList<MainGroup>();    
	private ExpListAdapter adapter;
    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Retrive the ExpandableListView from the layout
        final ExpandableListView listView = (ExpandableListView) findViewById(R.id.explist);
        listView.setDividerHeight(0);

        // Collapse all groups that are not clicked        
        listView.setOnGroupExpandListener(new OnGroupExpandListener() {
        	public void onGroupExpand(int groupPosition) {
        		int len = adapter.getGroupCount();
        	    for (int i = 0; i < len; i++) {
        	    	if (i != groupPosition || i == 2 || i == 6) {
        	    		listView.collapseGroup(i);
        	        }
        	    }
        	    
        	}
        });

        listView.setOnChildClickListener(new OnChildClickListener()
        {
            
            @Override
            public boolean onChildClick(ExpandableListView arg0, View arg1, int arg2, int arg3, long arg4)
            {
 //               Toast.makeText(getBaseContext(), "Child clicked", Toast.LENGTH_LONG).show();
                return false;
            }
        });
        
        listView.setOnGroupClickListener(new OnGroupClickListener()
        {
            
            @Override
            public boolean onGroupClick(ExpandableListView arg0, View arg1, int arg2, long arg3)
            {
                
                return false;
            }
        });


        // Initialize the adapter with blank groups and children 
        // We will be adding children on a thread, and then update the ListView
        

        // Set this blank adapter to the list view
        
        DomParser dp = new DomParser(getApplicationContext());
    	lsGroup = dp.parse();
        adapter = new ExpListAdapter(this, lsGroup);
        listView.setAdapter(adapter);
    }
    
    @Override
    public void run()
    {
       
    	DomParser dp = new DomParser(getApplicationContext());
    	lsGroup = dp.parse();
            // Notify the adapter
            handler.sendEmptyMessage(1);
        
    }

    private Handler handler = new Handler()
    {

        @Override
        public void handleMessage(Message msg)
        {
            adapter.notifyDataSetChanged();
            super.handleMessage(msg);
        }

    };
}
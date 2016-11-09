package nev.com.concentrate;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;



public class ViewAppList extends AppCompatActivity{
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewapp_layout);
        final ListView userInstalledApps = (ListView)findViewById(R.id.installed_app_list);
        final List<AppList> installedApps = getInstalledApps();
        final List<String> selpack = new ArrayList<>();
        final ListAdapter installedAppAdapter = new ListAdapter(ViewAppList.this, installedApps);
        userInstalledApps.setAdapter(installedAppAdapter);
        userInstalledApps.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        userInstalledApps.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            String[] s ;
            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {
                count = count+1;
                actionMode.setTitle(count+ " items selected");
                s= new String[selpack.size()];

                selpack.add(userInstalledApps.getChildAt(i).toString());


            }

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                MenuInflater inflater = actionMode.getMenuInflater();
                inflater.inflate(R.menu.context_menu,menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                System.out.println(s);
                Bundle b = new Bundle();
                b.putStringArray("selitem",s);
                i.putExtras(b);
                startActivity(i);

                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {

            }
        });
    }
    private List<AppList> getInstalledApps() {
        List<AppList> res = new ArrayList<AppList>();
        List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            if ((isSystemPackage(p) == false)) {
                String appName = p.applicationInfo.loadLabel(getPackageManager()).toString();
                Drawable icon = p.applicationInfo.loadIcon(getPackageManager());
                res.add(new AppList(appName, icon));
            }
        }
        return res;
    }


    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true : false;
    }
}




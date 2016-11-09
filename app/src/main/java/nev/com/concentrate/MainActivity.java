package nev.com.concentrate;

import android.app.KeyguardManager;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {
    private static final String TAG ="1" ;
    Button whitelist,setwallpaper;
    private static final int REQUEST_CODE = 1;
    RelativeLayout wallpaper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle b = getIntent().getExtras();
        String[] result = b.getStringArray("selitem");
        System.out.println(result);
        ListView lv = (ListView) findViewById(R.id.selectedlist);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,result);
        lv.setAdapter(adapter);


        whitelist = (Button) findViewById(R.id.button);
        whitelist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent i = new Intent(MainActivity.this,ViewAppList.class);
                startActivity(i);

        }});
        setwallpaper = (Button) findViewById(R.id.setwallpaper);
        setwallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);

                Intent customChooserIntent = Intent.createChooser(i, "Pick an image");
                startActivityForResult(customChooserIntent, 10);



            }
        });

}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);


                }


    boolean toggle=false;

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        KeyguardManager.KeyguardLock key;
        KeyguardManager km = (KeyguardManager)getSystemService(KEYGUARD_SERVICE);

        //This is deprecated, but it is a simple way to disable the lockscreen in code
        key = km.newKeyguardLock("IN");

        switch(item.getItemId()){
            case R.id.menu_toogle:
                if(toggle){
                    item.setIcon(R.drawable.ic_clear_black_18dp);
                    stopService(new Intent(this,LockScreenService.class));
                    key.disableKeyguard();
                    toggle=false;

                }else{
                    item.setIcon(R.drawable.ic_done_black_18dp);
                    startService(new Intent(this,LockScreenService.class));
                    key.reenableKeyguard();
                    //Intent i = new Intent(MainActivity.this,LockScreenActivity.class);
                    //startActivity(i);
                    toggle=true;
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == 10){
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                Drawable drawable = new BitmapDrawable(getResources(), BitmapFactory.decodeFile(picturePath));
                RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl);
                rl.setBackground(drawable);


}}}}

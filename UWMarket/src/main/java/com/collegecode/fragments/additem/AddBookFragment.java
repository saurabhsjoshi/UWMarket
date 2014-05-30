package com.collegecode.fragments.additem;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.collegecode.api.AppNotifier;
import com.collegecode.api.OnParseComplete;
import com.collegecode.api.ParseAPI;
import com.collegecode.barcodescanner.IntentIntegrator;
import com.collegecode.barcodescanner.ZXingLibConfig;
import com.collegecode.fragments.Market;
import com.collegecode.objects.Book;
import com.collegecode.uwmarket.Home;
import com.collegecode.uwmarket.R;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by saurabh on 5/28/14.
 * Fragment to publish book on market
 */
public class AddBookFragment extends Fragment {

    ArrayAdapter<CharSequence> adapter_condition;
    ArrayAdapter<CharSequence> adapter_category;

    public View view;

    public ImageView img;

    public TextView txt_isbn;

    public Button btn_img;

    Spinner spn_category;
    Spinner spn_condition;

    private ZXingLibConfig zxingLibConfig;

    @Override
    public void onCreate(Bundle savedInstanceState ){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_book,container, false);
        getActivity().setTitle("Enter Details");
        zxingLibConfig = new ZXingLibConfig();
        zxingLibConfig.useFrontLight = true;

        btn_img = (Button) view.findViewById(R.id.btn_img);

        (btn_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageIntent();
            }
        });

        (view.findViewById(R.id.btn_scan)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator.initiateScan(getActivity(), zxingLibConfig);
            }
        });

        img = (ImageView) view.findViewById(R.id.img_book);
        txt_isbn = (TextView) view.findViewById(R.id.txt_isbn);

        spn_category = (Spinner) view.findViewById(R.id.spn_category);
        adapter_category = ArrayAdapter.createFromResource(getActivity(),R.array.array_book_category, android.R.layout.simple_spinner_item);
        adapter_category.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_category.setAdapter(adapter_category);

        spn_condition = (Spinner) view.findViewById(R.id.spn_condition);
        adapter_condition = ArrayAdapter.createFromResource(getActivity(),R.array.array_condition, android.R.layout.simple_spinner_item);
        adapter_condition.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_condition.setAdapter(adapter_condition);

        return view;
    }

    public Uri outputFileUri = null;
    public Uri final_path = null;

    private void openImageIntent() {
        // Determine Uri of camera image to save.
        final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "UWMarket" + File.separator);
        boolean t = root.mkdirs();
        if(t)
            Log.i("UWMLOG", "Dir Created");
        final String fname = "img.jpg";
        final File sdImageMainDirectory = new File(root, fname);
        outputFileUri = Uri.fromFile(sdImageMainDirectory);
        // Camera.
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getActivity().getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for(ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            cameraIntents.add(intent);
        }

        // Filesystem.
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        // Chooser of filesystem options.
        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Source");

        // Add the camera options.
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[]{}));
        getActivity().startActivityForResult(chooserIntent, 214);
    }

    public int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    public Bitmap decodeSampledBitmapFromResource(Uri selectedImageUri ,int reqWidth, int reqHeight) {
        InputStream is;
        try {
            is = getActivity().getContentResolver().openInputStream(selectedImageUri);

            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(is, null, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            is.close();
            is = getActivity().getContentResolver().openInputStream(selectedImageUri);
            return BitmapFactory.decodeStream(is, null, options);
        } catch (Exception e) {

            e.printStackTrace();
        }
        return null;

    }


    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.fragment_upload, menu);
    }

    public Book getBook(){
        //Create book from info
        Book b = new Book();
        b.title = getTextfromId(R.id.txt_title);
        b.author = getTextfromId(R.id.txt_author);
        b.price = getTextfromId(R.id.txt_price);
        b.isbn = getTextfromId(R.id.txt_isbn);
        b.description = getTextfromId(R.id.txt_description);
        b.category = adapter_category.getItem(spn_category.getSelectedItemPosition()).toString();
        b.condition = adapter_condition.getItem(spn_condition.getSelectedItemPosition()).toString();
        if(final_path != null)
            b.img_url = final_path.toString();
        else
            b.img_url = null;

        b.fbId = "temp";
        return b;
    }

    public String getTextfromId(int id){
        try {
            return ((TextView) view.findViewById(id)).getText().toString();
        }catch (Exception ignore){return "";}
    }

    public void publishBook(){
        final AppNotifier an = new AppNotifier(getActivity());
        an.showNotification("UWMarket", "Publishing book to market..", R.drawable.ic_action_upload);
        ParseAPI api = new ParseAPI(getActivity());

        api.uploadBook(getBook(), new OnParseComplete() {
            @Override
            public void OnTaskComplete(Exception e, Object Result) {
                if(e == null)
                    an.removeNotification("Book has been published to market!");
                else
                    an.removeNotification("Error publishing book! Draft saved.");
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_upload:
                publishBook();
                return true;
            case R.id.action_cancel:
                getActivity().setTitle("Market");
                ((Home) getActivity()).changeFragmentwithAnim(new Market(), R.anim.slide_in,R.anim.slide_out);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}



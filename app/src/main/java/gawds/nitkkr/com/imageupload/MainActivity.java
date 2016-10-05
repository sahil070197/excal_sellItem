package gawds.nitkkr.com.imageupload;

import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageBase64;
import com.kosalgeek.android.photoutil.ImageLoader;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.EachExceptionsHandler;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

public class MainActivity extends AppCompatActivity {

    private ImageView imgView;
    private Button upload,cancel;
    CameraPhoto cameraPhoto;
    GalleryPhoto galleryPhoto;
    final  int CAMERA_REQUEST=1;
    final int GALLERY_REQUEST=2;
    String selectedphoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgView = (ImageView) findViewById(R.id.ImageView);
        upload = (Button) findViewById(R.id.imguploadbtn);
        cancel = (Button) findViewById(R.id.imgcancelbtn);
        cameraPhoto=new CameraPhoto(getApplicationContext());
        galleryPhoto=new GalleryPhoto(getApplicationContext());
        upload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(selectedphoto=="" || selectedphoto==null)
                {
                    Toast.makeText(MainActivity.this,"Invalid Image",Toast.LENGTH_SHORT).show();
                }
                try {

                    Bitmap bitmap=ImageLoader.init().from(selectedphoto).requestSize(1024,1024).getBitmap();
                    String encodedImage= ImageBase64.encode(bitmap);
                    HashMap<String,String> map=new HashMap<String, String>();
                    map.put("image",encodedImage);
                    PostResponseAsyncTask task= new PostResponseAsyncTask(MainActivity.this, map, new AsyncResponse() {
                        @Override
                        public void processFinish(String s) {
                            if(true)
                            {
                                //success message
                            }
                            else
                            {
//                                faliiure
                            }
                        }
                    });
                    task.execute("http://www.almerston.com/excalibur/upload_image.php");
                    task.setEachExceptionsHandler(new EachExceptionsHandler() {
                        @Override
                        public void handleIOException(IOException e) {
                            Toast.makeText(getApplicationContext(),"Server Connection Faliure",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void handleMalformedURLException(MalformedURLException e) {


                        }
                        @Override
                        public void handleProtocolException(ProtocolException e) {

                        }

                        @Override
                        public void handleUnsupportedEncodingException(UnsupportedEncodingException e) {

                        }
                    });
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        cancel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                MainActivity.this.finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_image_gallery, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.camera:
                try {
                    startActivityForResult(cameraPhoto.takePhotoIntent(),CAMERA_REQUEST);
                    cameraPhoto.addToGallery();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(),"error in camera",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                return true;

            case R.id.gallery:
                startActivityForResult(galleryPhoto.openGalleryIntent(),GALLERY_REQUEST);
                return true;
        }
        return false;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri selectedImageUri ;
        Bitmap bitmap;
        switch (requestCode) {
            case GALLERY_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    selectedImageUri = data.getData();
                    galleryPhoto.setPhotoUri(selectedImageUri);
                    try {
                        selectedphoto=galleryPhoto.getPath();
                        bitmap= ImageLoader.init().from(galleryPhoto.getPath()).requestSize(512,512).getBitmap();
                        imgView.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CAMERA_REQUEST:
                if (resultCode == RESULT_OK) {

                    try {
                        selectedphoto=cameraPhoto.getPhotoPath();
                        bitmap= ImageLoader.init().from(cameraPhoto.getPhotoPath()).requestSize(512,512).getBitmap();
                        imgView.setImageBitmap(rotate(bitmap,90));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
                else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    private Bitmap rotate(Bitmap source, float angle)
    {
        Matrix matrix=new Matrix();
        matrix.postRotate(angle);
        Bitmap bitmap=Bitmap.createBitmap(source,0,0,source.getWidth(),source.getHeight(),matrix,true);
        return bitmap;
    }
//
//    class ImageGalleryTask extends AsyncTask<Void, Void, String> {
//        @SuppressWarnings("unused")
//        @Override
//        protected String doInBackground(Void... unsued) {
//            InputStream is;
//            BitmapFactory.Options bfo;
//            Bitmap bitmapOrg;
//            ByteArrayOutputStream bao ;
//
//            bfo = new BitmapFactory.Options();
//            bfo.inSampleSize = 2;
//            //bitmapOrg = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/" + customImage, bfo);
//
//            bao = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);
//            byte [] ba = bao.toByteArray();
//            String ba1 = Base64.encodeToString(ba,Base64.DEFAULT);
//            HashMap<String,String> nameValuePairs = new HashMap<>();
//            nameValuePairs.put("image",ba1);
//            nameValuePairs.put("cmd","image_android.jpg");
//            Log.d("image struct" ,ba1);
//            Log.v("log_tag", System.currentTimeMillis()+".jpg");
//            try{
//                httpRequest request=new httpRequest();
//                String response=request.sendPostRequest("http://www.almerston.com/excalibur/images/upload_image.php",nameValuePairs);
//                Log.v("log_tag", "response" +response );
//            }catch(Exception e){
//                Log.v("log_tag", "Error in http connection "+e.toString());
//            }
//            return "Success";
//            // (null);
//        }
//
//        @Override
//        protected void onProgressUpdate(Void... unsued) {
//
//        }
//
//        @Override
//        protected void onPostExecute(String sResponse) {
//            try {
//                if (dialog.isShowing())
//                    dialog.dismiss();
//            } catch (Exception e) {
//                Toast.makeText(getApplicationContext(),
//                        e.getMessage(),
//                        Toast.LENGTH_LONG).show();
//                Log.e("Error", e.getMessage(), e);
//            }
//        }
//
//    }
//
//    public String getPath(Uri uri) {
//        String[] projection = { MediaStore.Images.Media.DATA };
//        Cursor cursor = managedQuery(uri, projection, null, null, null);
//        if (cursor != null) {
//            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
//            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
//            int column_index = cursor
//                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            cursor.moveToFirst();
//            return cursor.getString(column_index);
//        } else
//            return null;
//    }
//
//    public void decodeFile(String filePath) {
//        // Decode image size
//        BitmapFactory.Options o = new BitmapFactory.Options();
//        o.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(filePath, o);
//
//        // The new size we want to scale to
//        final int REQUIRED_SIZE = 1024;
//
//        // Find the correct scale value. It should be the power of 2.
//        int width_tmp = o.outWidth, height_tmp = o.outHeight;
//        int scale = 1;
//        while (true) {
//            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
//                break;
//            width_tmp /= 2;
//            height_tmp /= 2;
//            scale *= 2;
//        }
//
//        // Decode with inSampleSize
//        BitmapFactory.Options o2 = new BitmapFactory.Options();
//        o2.inSampleSize = scale;
//        bitmap = BitmapFactory.decodeFile(filePath, o2);
//
//        imgView.setImageBitmap(bitmap);
//
//    }



}


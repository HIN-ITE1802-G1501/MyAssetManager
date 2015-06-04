package no.hin.student.myassetmanager.Fragments;


import android.app.Fragment;
 import android.content.Context;
 import android.content.Intent;
 import android.graphics.Bitmap;
 import android.graphics.BitmapFactory;
 import android.media.Image;
 import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
 import android.os.Environment;
 import android.provider.MediaStore;
 import android.util.Log;
 import android.view.LayoutInflater;
 import android.view.View;
 import android.view.ViewGroup;
 import android.widget.AdapterView;
 import android.widget.Button;
 import android.widget.ImageButton;
 import android.widget.ImageView;
 import android.widget.ListView;
 import android.widget.TextView;
 import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
 import java.io.IOException;
import java.text.SimpleDateFormat;
 import java.util.Date;

 import no.hin.student.myassetmanager.Activities.ActivityMain;
 import no.hin.student.myassetmanager.Classes.AssetManagerAdapter;
 import no.hin.student.myassetmanager.Classes.Category;
 import no.hin.student.myassetmanager.Classes.Equipment;
 import no.hin.student.myassetmanager.Classes.WebAPI;
 import no.hin.student.myassetmanager.R;


public class FragmentAddEquipment extends Fragment implements View.OnClickListener {
     private static final String TAG = "MyAssetManger-log";
     private Context context;
     private Category selectedCategory = null;
     private Equipment equipment;
     private Boolean addNewEquipment = true;
     static final int REQUEST_TAKE_PHOTO = 1001;
     String mCurrentPhotoPath;

     public FragmentAddEquipment() {

     }

     @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
         // Inflate the layout for this fragment
         return inflater.inflate(R.layout.fragment_add_equipment, container, false);
     }

     @Override
     public void onStart() {
         super.onStart();

         ImageButton btnEquipmentCamera = (ImageButton)getView().findViewById(R.id.btnEquipmentCamera);
         btnEquipmentCamera.setOnClickListener(this);

         ImageButton btnEquipmentSave = (ImageButton)getView().findViewById(R.id.btnEquipmentSave);
         btnEquipmentSave.setOnClickListener(this);
     }

     @Override
      public void onActivityResult(int requestCode, int resultCode, Intent data) {
         ActivityMain activityMain = ((ActivityMain)getActivity());

         if (requestCode ==  REQUEST_TAKE_PHOTO) {
             Log.d(TAG, "Picture taken");
             if (!mCurrentPhotoPath.equals("")) {
                 ScalePictureAsyncTask task = new ScalePictureAsyncTask(mCurrentPhotoPath);
                 task.execute();
             }
             Log.d(TAG, "Image file" + mCurrentPhotoPath);
      } else
         Log.d(TAG,"Picture not taken");
      }

    class ScalePictureAsyncTask extends AsyncTask<Integer, Void, Bitmap> {
                 private Bitmap bitmap = null;
                 private String imageFile;

                  public ScalePictureAsyncTask(String imageFile) {
                      this.imageFile = imageFile;
                  }


                  @Override
                  protected Bitmap doInBackground(Integer... params) {
                      bitmap = BitmapFactory.decodeFile(imageFile);

                      int width = bitmap.getWidth();
                      int height = bitmap.getHeight();
                      if (width > height) {
                          return Bitmap.createScaledBitmap(bitmap, 150, height / (width / 150), false);
                      } else {
                          return Bitmap.createScaledBitmap(bitmap, width / (height / 150), height, false);
                      }
                  }

                  @Override
                  protected void onPostExecute(Bitmap bitmap) {
                      ImageView ivAddEquipmentPicture = ((ImageView) getView().findViewById(R.id.ivAddEquipmentPicture));

                      ByteArrayOutputStream stream = new ByteArrayOutputStream();
                      bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
                      byte[] image = stream.toByteArray();

                      ivAddEquipmentPicture.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));
                      equipment.setImage(image);
                  }
              }

     private File createImageFile() throws IOException {
                     try {
                         String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                         String imageFileName = "JPEG_" + timeStamp + "_";
                         File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                         File image = File.createTempFile(imageFileName, ".jpg", storageDir);

                         mCurrentPhotoPath = image.getAbsolutePath();
                         return image;
                     } catch (Exception e) {
                         Log.d(TAG, e.toString());
                     }
                     return null;
                 }

     public void editEquipment(Equipment equipment) {
                     this.addNewEquipment = false;
                     this.equipment = equipment;

                     ((TextView) getView().findViewById(R.id.etAddEquipmentBrand)).setText(this.equipment.getBrand());
                     ((TextView) getView().findViewById(R.id.etAddEquipmentModel)).setText(this.equipment.getModel());
                     ((TextView) getView().findViewById(R.id.etAddEquipmentItNumber)).setText(this.equipment.getIt_no());
                     ((TextView) getView().findViewById(R.id.etAddEquipmentDescription)).setText(this.equipment.getDescription());
                     ImageView ivAddEquipmentPicture = ((ImageView) getView().findViewById(R.id.ivAddEquipmentPicture));
                     ivAddEquipmentPicture.setImageBitmap(BitmapFactory.decodeByteArray(this.equipment.getImage(), 0, this.equipment.getImage().length));
                     populateListViewWithCategories(getView().getContext());
                 }

     public void newEquipment() {
                     this.addNewEquipment = true;
                     this.equipment = new Equipment();

                     ((TextView) getView().findViewById(R.id.etAddEquipmentBrand)).setText("");
                     ((TextView) getView().findViewById(R.id.etAddEquipmentModel)).setText("");
                     ((TextView) getView().findViewById(R.id.etAddEquipmentItNumber)).setText("");
                     ((TextView) getView().findViewById(R.id.etAddEquipmentDescription)).setText("");
                     populateListViewWithCategories(getView().getContext());
                 }


     public void populateListViewWithCategories(Context context) {
                     this.context = context;

                     final ListView lwAddEquipmentCategory = (ListView)getView().findViewById(R.id.lwAddEquipmentCategory);
                     final AssetManagerAdapter adapter = new AssetManagerAdapter(context, Category.getCategories());
                     lwAddEquipmentCategory.setAdapter(adapter);

                     lwAddEquipmentCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                         public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                             selectedCategory =(Category) (lwAddEquipmentCategory.getItemAtPosition(myItemInt));

                             equipment.setType(selectedCategory.getName());
                         }
                     });
                 }

     @Override
                 public void onClick(View v) {
                     try {
                         ActivityMain activityMain = ((ActivityMain)getActivity());
                         switch(v.getId()) {
                             case R.id.btnEquipmentSave:
                                 try {
                                     String brand = ((TextView) getView().findViewById(R.id.etAddEquipmentBrand)).getText().toString();
                                     String model = ((TextView) getView().findViewById(R.id.etAddEquipmentModel)).getText().toString();
                                     String it_no = ((TextView) getView().findViewById(R.id.etAddEquipmentItNumber)).getText().toString();
                                     String description = ((TextView) getView().findViewById(R.id.etAddEquipmentDescription)).getText().toString();
                                     String date = new SimpleDateFormat("dd.MM.yyyy").format(new java.util.Date());
                                     String category = "";
                                     if (selectedCategory != null)
                                         category = selectedCategory.getName();

                                     if (brand.equals("") || model.equals("") || it_no.equals("") || category.equals("")) {
                                         Toast.makeText(v.getContext(), "Vennligst fyll ut alle felter og velg en kategori.", Toast.LENGTH_LONG).show();
                                     } else {
                                         if (addNewEquipment) {
                                             equipment.setBrand(((TextView) getView().findViewById(R.id.etAddEquipmentBrand)).getText().toString());
                                             equipment.setModel(((TextView) getView().findViewById(R.id.etAddEquipmentModel)).getText().toString());
                                             equipment.setIt_no(((TextView) getView().findViewById(R.id.etAddEquipmentItNumber)).getText().toString());
                                             equipment.setAquired(date);
                                             equipment.setDescription(((TextView) getView().findViewById(R.id.etAddEquipmentDescription)).getText().toString());
                                             WebAPI.doAddEquipment(context, equipment);
                                         } else {
                                             equipment.setBrand(((TextView) getView().findViewById(R.id.etAddEquipmentBrand)).getText().toString());
                                             equipment.setModel(((TextView) getView().findViewById(R.id.etAddEquipmentModel)).getText().toString());
                                             equipment.setIt_no(((TextView) getView().findViewById(R.id.etAddEquipmentItNumber)).getText().toString());
                                             equipment.setDescription(((TextView) getView().findViewById(R.id.etAddEquipmentDescription)).getText().toString());
                                             WebAPI.doUpdateEquipment(v.getContext(), equipment);
                                         }
                                     }
                                 } catch (Exception e) {
                                     Log.e(TAG, e.toString());
                                 }
                                 break;
                             case R.id.btnEquipmentCamera:
                                  Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                  if (takePictureIntent.resolveActivity(activityMain.getPackageManager()) != null) {
                                      File photoFile = null;
                                      try {
                                          photoFile = createImageFile();
                                      } catch (Exception e) {
                                        Log.d(TAG, e.toString());
                                      }
                                      if (photoFile != null) {
                                         try {
                                          takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                                          startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                                         } catch (Exception e) {
                                             Log.d(TAG, e.toString());
                                         }
                                      }
                                  }

                                 break;
                         }
                     } catch (Exception e) {
                         Log.d("-log", e.toString());
                     }
                 }
 }

package com.bashalir.go4lunch.Controllers.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bashalir.go4lunch.Api.UserHelper;
import com.bashalir.go4lunch.MainActivity;
import com.bashalir.go4lunch.R;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RestaurantActivity extends AppCompatActivity {

    private final String mTag = getClass().getSimpleName();
    @BindView(R.id.activity_restaurant_name_tv)
    TextView mName;
    @BindView(R.id.activity_restaurant_address_tv)
    TextView mAddress;
    @BindView(R.id.activity_restaurant_photo_iv)
    ImageView mPhoto;
    @BindView(R.id.activity_restaurant_rating_rb)
    RatingBar mRating;
    @BindView((R.id.fab))
    FloatingActionButton mFab;

    private GeoDataClient mGeoDataClient;
    private CharSequence mPhone;
    private Uri mWebUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        String idPlace = intent.getStringExtra("idPlace");

        Toast.makeText(getApplicationContext(), idPlace, Toast.LENGTH_SHORT).show();

        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(this);

        configureFab(idPlace);


        getPlaceDetails(idPlace);

        // Request photos and metadata for the specified place.

        getPhoto(idPlace);



    }

    private void configureFab(String idPlace) {


        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /*mFab.setColorFilter(getResources().getColor(R.color.background_dark));
               mFab.setImageDrawable(getResources().getDrawable(R.drawable.ic_call_green_24dp));
               mFab.setBackgroundColor(getResources().getColor(R.color.background_dark));*/

               mFab.setBackgroundTintList(getResources().getColorStateList(R.color.fab));
                mFab.setRippleColor(getResources().getColorStateList(R.color.fab));
               mName.setText("Coucou2");

                UserHelper.updateRestaurant(idPlace, FirebaseAuth.getInstance().getUid());

            }
        });
    }


    private void getPhoto(String idPlace) {

        final Task<PlacePhotoMetadataResponse> photoMetadataResponse = mGeoDataClient.getPlacePhotos(idPlace);
        photoMetadataResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoMetadataResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlacePhotoMetadataResponse> task) {
                // Get the list of photos.
                PlacePhotoMetadataResponse photos = task.getResult();
                if (photos.getPhotoMetadata().getCount() > 0) {
                    // Get the PlacePhotoMetadataBuffer (metadata for all of the photos).
                    PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();
                    // Get the first photo in the list.
                    PlacePhotoMetadata photoMetadata = photoMetadataBuffer.get(0);
                    // Get the attribution text.
                    CharSequence attribution = photoMetadata.getAttributions();
                    // Get a full-size bitmap for the photo.
                    Task<PlacePhotoResponse> photoResponse = mGeoDataClient.getPhoto(photoMetadata);
                    photoResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoResponse>() {
                        @Override
                        public void onComplete(@NonNull Task<PlacePhotoResponse> task) {
                            PlacePhotoResponse photo = task.getResult();
                            Bitmap bitmap = photo.getBitmap();
                            mPhoto.setImageBitmap(bitmap);
                            mPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        }
                    });
                }
            }
        });


    }

    private void getPlaceDetails(String idPlace) {


        mGeoDataClient.getPlaceById(idPlace).addOnCompleteListener(new OnCompleteListener<PlaceBufferResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlaceBufferResponse> task) {
                if (task.isSuccessful()) {
                    PlaceBufferResponse places = task.getResult();
                    Place myPlace = places.get(0);
                    Log.i(mTag, "Place found: " + myPlace.getName());

                    mName.setText(myPlace.getName());
                    mAddress.setText(myPlace.getAddress());
                    mWebUri = myPlace.getWebsiteUri();
                    mPhone = myPlace.getPhoneNumber();
                    mRating.setRating(myPlace.getRating());

                    places.release();
                } else {
                    Log.e(mTag, "Place not found.");
                }
            }
        });

    }


}

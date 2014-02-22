package com.jrew.geocatch.mobile.fragment;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.dao.DomainDatabaseManager;
import com.jrew.geocatch.mobile.reciever.ServiceResultReceiver;
import com.jrew.geocatch.mobile.service.ImageService;
import com.jrew.geocatch.mobile.service.ReviewService;
import com.jrew.geocatch.mobile.util.*;
import com.jrew.geocatch.web.model.ClientImage;
import com.jrew.geocatch.web.model.ClientImagePreview;
import com.jrew.geocatch.web.model.DomainProperty;
import com.jrew.geocatch.web.model.ImageReview;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 11/11/13
 * Time: 4:16 PM
 */
public class PhotoBrowsingFragment extends SherlockFragment {

    /**
     *
     */
    public enum BrowsingMode {

       /** **/
       FOREIGN_PHOTO_BROWSING,

       /** **/
       OWN_PHOTO_BROWSING
    }

    /** **/
    private ServiceResultReceiver imageResultReceiver;

    /** **/
   // private ProgressDialog progressDialog;

    /** **/
    private ClientImage clientImage;

    /** **/
    private BrowsingMode browsingMode = BrowsingMode.FOREIGN_PHOTO_BROWSING;

    /** **/
    private boolean isLikeSelected, isDislikeSelected, isReportSelected;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        // Action bar subtitle
        ActionBarUtil.initActionBar(ActionBar.NAVIGATION_MODE_STANDARD, getActivity());
        ActionBarUtil.setActionBarSubtitle(R.string.photoBrowsingFragmentLabel, getActivity());

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

      //  progressDialog = DialogUtil.createProgressDialog(getActivity());

        View photoBrowsingLayout = inflater.inflate(R.layout.photo_browsing_fragment, container, false);

        final ImageView imageView = (ImageView) photoBrowsingLayout.findViewById(R.id.imageView);

        // Uploading photo date
        final LinearLayout uploadingDateLayout = (LinearLayout) photoBrowsingLayout.findViewById(R.id.uploadingDateLayout);
        final TextView date = (TextView) photoBrowsingLayout.findViewById(R.id.uploadingDateTextView);
        final DateFormat browsingDateFormat = new SimpleDateFormat(getResources().getString(R.config.mobileDateFormat));

        // Photo description
        final LinearLayout descriptionLayout = (LinearLayout) photoBrowsingLayout.findViewById(R.id.descriptionLayout);
        final TextView descriptionView = (TextView) photoBrowsingLayout.findViewById(R.id.photoDescription);

        final LinearLayout fishDomainPropertyTag = (LinearLayout) photoBrowsingLayout.findViewById(R.id.fishDomainPropertyTag);
        final TextView fishTextView = (TextView) photoBrowsingLayout.findViewById(R.id.fishTextView);

        final LinearLayout toolDomainPropertyTag = (LinearLayout) photoBrowsingLayout.findViewById(R.id.toolDomainPropertyTag);
        final TextView toolTextView = (TextView) photoBrowsingLayout.findViewById(R.id.toolTextView);

        final LinearLayout baitDomainPropertyTag = (LinearLayout) photoBrowsingLayout.findViewById(R.id.baitDomainPropertiesView);
        final TextView baitTextView = (TextView) photoBrowsingLayout.findViewById(R.id.baitTextView);

        final RelativeLayout reviewLayout = (RelativeLayout) photoBrowsingLayout.findViewById(R.id.reviewLayout);
        final ImageView likesImageView = (ImageView) reviewLayout.findViewById(R.id.likesImageView);
        final TextView likesCount = (TextView) reviewLayout.findViewById(R.id.likesCount);
        final ImageView dislikesImageView = (ImageView) reviewLayout.findViewById(R.id.dislikesImageView);
        final TextView dislikesCount = (TextView) reviewLayout.findViewById(R.id.dislikesCount);
        final ImageView reportsImageView = (ImageView) reviewLayout.findViewById(R.id.reportsImageView);
        final TextView reportsCount = (TextView) reviewLayout.findViewById(R.id.reportsCount);

        Handler fragmentHandler = new Handler();
        imageResultReceiver = new ServiceResultReceiver(fragmentHandler);
        imageResultReceiver.setReceiver(new ServiceResultReceiver.Receiver() {

            @Override
            public void onReceiveResult(int resultCode, Bundle resultData) {

                switch (resultCode) {

                    case ImageService.ResultStatus.LOAD_IMAGE_DATA_FINISHED:

                        clientImage = (ClientImage) resultData.getSerializable(ImageService.RESULT_KEY);

                        ImageLoader imageLoader = ImageLoader.getInstance();
                        imageLoader.loadImage(clientImage.getPath(), new SimpleImageLoadingListener() {
                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                                // domain properties
                                List<DomainProperty> domainProperties = clientImage.getDomainProperties();
                                String locale = Locale.getDefault().getLanguage();
                                for (DomainProperty domainProperty : domainProperties) {

                                    if (!locale.equalsIgnoreCase(domainProperty.getLocale())) {
                                        domainProperty =
                                                DomainDatabaseManager.loadLocalizedDomainProperty(domainProperty, getActivity());
                                    }

                                    long domainPropertyType = domainProperty.getType();
                                    if (domainPropertyType == 1) {
                                        fishDomainPropertyTag.setVisibility(View.VISIBLE);
                                        fishTextView.setText(domainProperty.getValue());
                                    } else if (domainPropertyType == 2) {
                                        toolDomainPropertyTag.setVisibility(View.VISIBLE);
                                        toolTextView.setText(domainProperty.getValue());
                                    } else if (domainPropertyType == 3) {
                                        baitDomainPropertyTag.setVisibility(View.VISIBLE);
                                        baitTextView.setText(domainProperty.getValue());
                                    }
                                }

                                // Photo
                                imageView.setImageBitmap(loadedImage);

                                Display display = getActivity().getWindowManager().getDefaultDisplay();
                                double scaleFactor = LayoutUtil.getViewWidthScaleFactor(display.getWidth(), loadedImage.getWidth(), 0);
                                imageView.setLayoutParams(new LinearLayout.LayoutParams((int)
                                        (loadedImage.getWidth() * scaleFactor), (int) (loadedImage.getHeight() * scaleFactor)));

                                // reviews
                                reviewLayout.setVisibility(View.VISIBLE);
                                likesCount.setText(Integer.toString(clientImage.getLikesCount()));
                                isLikeSelected = clientImage.isLikeSelected();
                                if (isLikeSelected) {
                                    likesImageView.setImageResource(R.drawable.like_selected);
                                } else {
                                    likesImageView.setImageResource(R.drawable.like_unselected);
                                }

                                likesImageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (!isDislikeSelected) {

                                            ImageReview imageReview =  createImageReview(clientImage);
                                            imageReview.setReviewType(ImageReview.ReviewType.LIKE);
                                            if (isLikeSelected) {
                                                // select like
                                                imageReview.setSelected(false);
                                            } else {
                                                // unselect like
                                                imageReview.setSelected(true);
                                            }

                                            ServiceUtil.callUploadReviewService(imageReview, imageResultReceiver, getActivity());
                                        }
                                    }
                                });


                                dislikesCount.setText(Integer.toString(clientImage.getDislikesCount()));
                                isDislikeSelected = clientImage.isDislikeSelected();
                                if (isDislikeSelected) {
                                    dislikesImageView.setImageResource(R.drawable.dislike_selected);
                                } else {
                                    dislikesImageView.setImageResource(R.drawable.dislike_unselected);
                                }

                                dislikesImageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (!isLikeSelected) {

                                            ImageReview imageReview =  createImageReview(clientImage);
                                            imageReview.setReviewType(ImageReview.ReviewType.DISLIKE);
                                            if (isDislikeSelected) {
                                                // select like
                                                imageReview.setSelected(false);
                                            } else {
                                                // unselect like
                                                imageReview.setSelected(true);
                                            }

                                            ServiceUtil.callUploadReviewService(imageReview, imageResultReceiver, getActivity());
                                        }
                                    }
                                });

                                reportsCount.setText(Integer.toString(clientImage.getReportsCount()));
                                isReportSelected = clientImage.isReportSelected();
                                if (isReportSelected) {
                                    reportsImageView.setImageResource(R.drawable.report_selected);
                                } else {
                                    reportsImageView.setImageResource(R.drawable.report_unselected);
                                }

                                reportsImageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        ImageReview imageReview =  createImageReview(clientImage);
                                        imageReview.setReviewType(ImageReview.ReviewType.REPORT);
                                        if (isReportSelected) {
                                            imageReview.setSelected(false);
                                        } else {
                                            imageReview.setSelected(true);
                                        }

                                        ServiceUtil.callUploadReviewService(imageReview, imageResultReceiver, getActivity());
                                    }
                                });

                                // date
                                date.setText(browsingDateFormat.format(clientImage.getDate()));
                                uploadingDateLayout.setVisibility(View.VISIBLE);

                                // description
                                String description = clientImage.getDescription();
                                if (description != null && description.length() > 0) {
                                    descriptionView.setText(description);
                                    descriptionLayout.setVisibility(View.VISIBLE);
                                }
                            }
                        });

                        break;

                    case ImageService.ResultStatus.DELETE_IMAGE_FINISHED:
                        //progressDialog.hide();
                        FragmentSwitcherHolder.getFragmentSwitcher().showUploadedPhotosFragment();
                        break;

                    case ImageService.ResultStatus.ERROR:
                        showCommunicationError();
                        break;

                    case ReviewService.ResultStatus.UPLOAD_REVIEW_FINISHED:

                        if (resultData.getBoolean(ReviewService.RESULT_KEY)) {

                            ImageReview imageReview = (ImageReview) resultData.getSerializable(ReviewService.REVIEW_KEY);
                            ImageReview.ReviewType reviewType = imageReview.getReviewType();

                            if (ImageReview.ReviewType.LIKE == reviewType) {

                                String likesCountText = likesCount.getText().toString();
                                int likes = Integer.parseInt(likesCountText);

                                isLikeSelected = imageReview.isSelected();
                                if (isLikeSelected) {
                                    likesImageView.setImageResource(R.drawable.like_selected);
                                    likes++;
                                } else {
                                    likesImageView.setImageResource(R.drawable.like_unselected);
                                    likes--;
                                }

                                likesCount.setText(Integer.toString(likes));

                            } else if (ImageReview.ReviewType.DISLIKE == reviewType) {

                                String dislikesCountText = dislikesCount.getText().toString();
                                int dislikes = Integer.parseInt(dislikesCountText);

                                isDislikeSelected = imageReview.isSelected();
                                if (isDislikeSelected) {
                                    dislikesImageView.setImageResource(R.drawable.dislike_selected);
                                    dislikes++;
                                } else {
                                    dislikesImageView.setImageResource(R.drawable.dislike_unselected);
                                    dislikes--;
                                }
                                dislikesCount.setText(Integer.toString(dislikes));

                            } else if (ImageReview.ReviewType.REPORT == reviewType) {

                                String reportsCountText = reportsCount.getText().toString();
                                int reports = Integer.parseInt(reportsCountText);

                                isReportSelected = imageReview.isSelected();
                                if (isReportSelected) {
                                    reportsImageView.setImageResource(R.drawable.report_selected);
                                    reports++;
                                } else {
                                    reportsImageView.setImageResource(R.drawable.report_unselected);
                                    reports--;
                                }

                                reportsCount.setText(Integer.toString(reports));
                            }
                        } else {
                            showCommunicationError();
                        }
                        break;

                    case ReviewService.ResultStatus.ERROR:
                        showCommunicationError();
                        break;
                }
            }

        });

//        reviewResultReceiver = new ServiceResultReceiver(fragmentHandler);
//        reviewResultReceiver.setReceiver(new ServiceResultReceiver.Receiver() {
//
//            @Override
//            public void onReceiveResult(int resultCode, Bundle resultData) {
//
//                switch (resultCode) {
//
//
//                }
//            }
//        });

        Bundle fragmentData = getArguments();
        if (fragmentData != null && !fragmentData.isEmpty()) {

            if (fragmentData.containsKey(BrowsingMode.OWN_PHOTO_BROWSING.toString())) {
                browsingMode = BrowsingMode.OWN_PHOTO_BROWSING;
            }

            ClientImagePreview image = (ClientImagePreview) fragmentData.getSerializable(ImageService.IMAGE_KEY);
            ServiceUtil.callLoadImageDataService(image, imageResultReceiver, getActivity());
        }

        return photoBrowsingLayout;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, com.actionbarsherlock.view.MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_photo_browsing, menu);

        switch (browsingMode) {
            case FOREIGN_PHOTO_BROWSING:
                // Remove delete photo menu option
                menu.removeItem(R.id.deleteImageMenuOption);
                break;

            case  OWN_PHOTO_BROWSING:
                // Remove report photo menu option
                break;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int pressedMenuItemId = item.getItemId();
        switch (pressedMenuItemId) {
            case R.id.backMenuOption:
                FragmentSwitcherHolder.getFragmentSwitcher().popBackStack();
                break;

            case R.id.deleteImageMenuOption:
                if (clientImage != null) {
                   // progressDialog.show();

                    Bundle requestBundle = new Bundle();
                    requestBundle.putLong(ImageService.IMAGE_ID_KEY, clientImage.getId());
                    requestBundle.putString(ImageService.DEVICE_ID_KEY, CommonUtil.getDeviceId(getActivity()));
                    ServiceUtil.callDeleteImageService(requestBundle, imageResultReceiver, getActivity());
                }
                break;

        }

        return true;
    }

    /**
     *
     * @param clientImage
     * @return
     */
    private ImageReview createImageReview(ClientImage clientImage) {

        ImageReview imageReview = new ImageReview();
        imageReview.setImageId(clientImage.getId());
        imageReview.setDeviceId(CommonUtil.getDeviceId(getActivity()));

        return imageReview;
    }

    /**
     *
     */
    private void showCommunicationError() {
        Toast.makeText(getActivity(), getResources().getString(R.string.commonServerCommunicationError),
                Toast.LENGTH_LONG).show();
    }
}

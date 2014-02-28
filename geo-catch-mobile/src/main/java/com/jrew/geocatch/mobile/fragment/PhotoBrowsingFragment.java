package com.jrew.geocatch.mobile.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
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
import com.jrew.geocatch.mobile.service.cache.ImageCache;
import com.jrew.geocatch.mobile.util.*;
import com.jrew.geocatch.web.model.ClientImage;
import com.jrew.geocatch.web.model.ClientImagePreview;
import com.jrew.geocatch.web.model.DomainProperty;
import com.jrew.geocatch.web.model.ImageReview;
import com.squareup.picasso.Callback;

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

    /** **/
    private ImageView photoImageView;

    /** **/
    private LinearLayout fishDomainPropertyTag, toolDomainPropertyTag, baitDomainPropertyTag,
            uploadingDateLayout, descriptionLayout;

    /** **/
    private TextView fishTextView, toolTextView, baitTextView, date, descriptionView;

    /** **/
    private RelativeLayout reviewLayout;

    /** **/
    private ImageView likesImageView, dislikesImageView, reportsImageView;

    /** **/
    private TextView likesCount, dislikesCount, reportsCount;

    /** **/
    private DateFormat browsingDateFormat;

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
    private ProgressDialog loadingDialog, deletingDialog;

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

        LayoutUtil.showFragmentContainer(getActivity());

        View photoBrowsingLayout = inflater.inflate(R.layout.photo_browsing_fragment, container, false);

        photoImageView = (ImageView) photoBrowsingLayout.findViewById(R.id.imageView);

        DialogInterface.OnCancelListener listener = new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                PicassoHolder.getPicasso().cancelRequest(photoImageView);
                ServiceUtil.abortImageService(getActivity());
                LayoutUtil.showRefreshLayout(getActivity(), R.string.photoLoadingCancelled);
            }
        };

        loadingDialog = DialogUtil.createProgressDialog(getActivity(), R.string.photoLoadingMessage, listener);

        // Uploading photo date
        uploadingDateLayout = (LinearLayout) photoBrowsingLayout.findViewById(R.id.uploadingDateLayout);
        date = (TextView) photoBrowsingLayout.findViewById(R.id.uploadingDateTextView);
        browsingDateFormat = new SimpleDateFormat(getResources().getString(R.config.mobileDateFormat));

        // Photo description
        descriptionLayout = (LinearLayout) photoBrowsingLayout.findViewById(R.id.descriptionLayout);
        descriptionView = (TextView) photoBrowsingLayout.findViewById(R.id.photoDescription);

        fishDomainPropertyTag = (LinearLayout) photoBrowsingLayout.findViewById(R.id.fishDomainPropertyTag);
        fishTextView = (TextView) photoBrowsingLayout.findViewById(R.id.fishTextView);

        toolDomainPropertyTag = (LinearLayout) photoBrowsingLayout.findViewById(R.id.toolDomainPropertyTag);
        toolTextView = (TextView) photoBrowsingLayout.findViewById(R.id.toolTextView);

        baitDomainPropertyTag = (LinearLayout) photoBrowsingLayout.findViewById(R.id.baitDomainPropertiesView);
        baitTextView = (TextView) photoBrowsingLayout.findViewById(R.id.baitTextView);

        reviewLayout = (RelativeLayout) photoBrowsingLayout.findViewById(R.id.reviewLayout);
        likesImageView = (ImageView) reviewLayout.findViewById(R.id.likesImageView);
        likesCount = (TextView) reviewLayout.findViewById(R.id.likesCount);
        dislikesImageView = (ImageView) reviewLayout.findViewById(R.id.dislikesImageView);
        dislikesCount = (TextView) reviewLayout.findViewById(R.id.dislikesCount);
        reportsImageView = (ImageView) reviewLayout.findViewById(R.id.reportsImageView);
        reportsCount = (TextView) reviewLayout.findViewById(R.id.reportsCount);

        Handler fragmentHandler = new Handler();
        imageResultReceiver = new ServiceResultReceiver(fragmentHandler);
        imageResultReceiver.setReceiver(new ServiceResultReceiver.Receiver() {

            @Override
            public void onReceiveResult(int resultCode, Bundle resultData) {

                switch (resultCode) {

                    case ImageService.ResultStatus.LOAD_IMAGE_DATA_FINISHED:

                        clientImage = (ClientImage) resultData.getSerializable(ImageService.RESULT_KEY);
                        ImageCache.getInstance().add(clientImage);
                        processClientImageLoading(clientImage);
                        break;

                    case ImageService.ResultStatus.DELETE_IMAGE_FINISHED:
                        if (deletingDialog.isShowing()) {
                            deletingDialog.dismiss();
                        }

                        if (resultData.getBoolean(ImageService.RESULT_KEY)) {
                            // 1) Remove image from cache
                            long imageId = resultData.getLong(ImageService.IMAGE_ID_KEY, 0);
                            ImageCache.getInstance().removeImage(imageId);
                            // 2) Show uploaded images fragment
                            FragmentSwitcherHolder.getFragmentSwitcher().showUploadedPhotosFragment();
                        }
                        break;

                    case ImageService.ResultStatus.ERROR:

                        if (loadingDialog != null && !loadingDialog.isShowing()) {
                            loadingDialog.dismiss();
                        }
                        if (deletingDialog != null && !deletingDialog.isShowing()) {
                            deletingDialog.dismiss();
                        }

                        LayoutUtil.showRefreshLayout(getActivity(), R.string.photoLoadingError);
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
                                clientImage.setLikesCount(likes);
                                clientImage.setLikeSelected(isLikeSelected);

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
                                clientImage.setDislikesCount(dislikes);
                                clientImage.setDislikeSelected(isDislikeSelected);

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
                                clientImage.setReportsCount(reports);
                                clientImage.setReportSelected(isReportSelected);
                            }

                            ImageCache.getInstance().replace(clientImage);

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

        Bundle fragmentData = getArguments();
        if (fragmentData != null && !fragmentData.isEmpty()) {

            if (fragmentData.containsKey(BrowsingMode.OWN_PHOTO_BROWSING.toString())) {
                browsingMode = BrowsingMode.OWN_PHOTO_BROWSING;
            }

            ClientImagePreview image = (ClientImagePreview) fragmentData.getSerializable(ImageService.IMAGE_KEY);

            clientImage = ImageCache.getInstance().getClientImage(image.getId());
            if (clientImage != null) {
                processClientImageLoading(clientImage);
            } else {
                ServiceUtil.callLoadImageDataService(image, imageResultReceiver, getActivity());
            }

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
                showDeleteAlertDialog();
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
        if (loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }

        Toast.makeText(getActivity(), getResources().getString(R.string.commonServerCommunicationError),
                Toast.LENGTH_LONG).show();
    }



    /**
     *
     * @param clientImage
     */
    private void processClientImageLoading(final ClientImage clientImage) {

        ImageCache.getInstance().add(clientImage);

        Callback callback = new Callback() {
            @Override
            public void onSuccess() {
                if (getActivity() != null) {

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

//                    // Photo
//                    photoImageView.setImageBitmap(loadedImage);
//
                    Display display = getActivity().getWindowManager().getDefaultDisplay();
                    photoImageView.setLayoutParams(new LinearLayout.LayoutParams((int)
                            (display.getWidth()), (int) (display.getWidth())));

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

                    if (loadingDialog.isShowing()) {
                        loadingDialog.dismiss();
                    }

                }
            }

            @Override
            public void onError() {
                if (loadingDialog != null && loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
                LayoutUtil.showRefreshLayout(getActivity(), R.string.photoLoadingError);
            }
        };

        PicassoHolder.getPicasso().load(clientImage.getPath()).into(photoImageView, callback);
    }

    /**
     *
     * @return
     */
    private void showDeleteAlertDialog() {

        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setIcon(R.drawable.emo_im_sad)
                .setTitle(R.string.photoDeleteAlertTitle);

        // Add the buttons
        builder.setNegativeButton(R.string.photoDeleteAlertCancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton(R.string.photoDeleteAlertYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (clientImage != null) {
                    deletingDialog = DialogUtil.createProgressDialog(getActivity(), R.string.photoDeletingMessage);
                    Bundle requestBundle = new Bundle();
                    requestBundle.putLong(ImageService.IMAGE_ID_KEY, clientImage.getId());
                    requestBundle.putString(ImageService.DEVICE_ID_KEY, CommonUtil.getDeviceId(getActivity()));
                    ServiceUtil.callDeleteImageService(requestBundle, imageResultReceiver, getActivity());
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}

package com.jrew.geocatch.mobile.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.adapter.UploadedPhotosAdapter;
import com.jrew.geocatch.mobile.reciever.ServiceResultReceiver;
import com.jrew.geocatch.mobile.service.ImageService;
import com.jrew.geocatch.mobile.service.cache.ImageCache;
import com.jrew.geocatch.mobile.util.*;
import com.jrew.geocatch.web.model.ClientImagePreview;
import com.jrew.geocatch.web.model.criteria.SearchCriteria;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 06.02.14
 * Time: 16:15
 * To change this template use File | Settings | File Templates.
 */
public class UploadedPhotosFragment extends SherlockFragment {

    /** **/
    private ProgressDialog loadingDialog;

    /** **/
    private UploadedPhotosAdapter uploadedPhotosAdapter;

    /** **/
    private ServiceResultReceiver resultReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Action bar subtitle
        ActionBarUtil.setActionBarSubtitle(R.string.uploadedPhotosFragmentLabel, getActivity());

        final View layout = inflater.inflate(R.layout.uploaded_photos_fragment, container, false);;
        if (WebUtil.isNetworkAvailable(getActivity())) {

            LayoutUtil.showFragmentContainer(getActivity());

            final GridView photosGridView = (GridView) layout.findViewById(R.id.photosGridView);

            // This fragment available only in portrait mode.
            // So, the bigger side os display height
            int displayHeight = CommonUtil.getDisplayLargerSideSize(getActivity());
            double thumbnailScaleFactor = Double.parseDouble(
                   getResources().getString(R.config.gridPhotosThumbnailSizeScaleFactor));
            int cellSize = (int) (displayHeight * thumbnailScaleFactor);
            int displayWidth = CommonUtil.getDisplaySmallerSideSize(getActivity());

            // Get num of columns of grid view columns
            int columnsNumber = displayWidth / cellSize;
            photosGridView.setNumColumns(columnsNumber);

            int widthRest = displayWidth % cellSize;

            double gridPhotosSpacingFactor = Double.parseDouble(
                    getResources().getString(R.config.gridPhotosSpacingFactor));
            int cellSpacing = (int) (cellSize * gridPhotosSpacingFactor);
            photosGridView.setHorizontalSpacing(cellSpacing);
            photosGridView.setVerticalSpacing(cellSpacing);

            int gridViewMargin = (widthRest - ((columnsNumber - 1) * cellSpacing)) / 2;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(gridViewMargin, 0, gridViewMargin, 0);
            photosGridView.setLayoutParams(layoutParams);


            DialogInterface.OnCancelListener listener = new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    ServiceUtil.abortImageService(getActivity());
                    if (uploadedPhotosAdapter.getImagesCount() == 0) {
                        LayoutUtil.showRefreshLayout(getActivity(), R.string.uploadedPhotosLoadingCancelled);
                    }
                }
            };

            if (uploadedPhotosAdapter == null) {
                uploadedPhotosAdapter = new UploadedPhotosAdapter(getActivity(), cellSize);
            }

            if (uploadedPhotosAdapter.getImagesCount() == 0) {
                loadingDialog = DialogUtil.createProgressDialog(getActivity(), R.string.uploadedPhotosLoadingMessage, listener);
            } else {
                uploadedPhotosAdapter.setImages(ImageCache.getInstance().getOwnClientImagePreview());
                uploadedPhotosAdapter.notifyDataSetChanged();
            }

            photosGridView.setAdapter(uploadedPhotosAdapter);

            resultReceiver = new ServiceResultReceiver(new Handler());
            resultReceiver.setReceiver(new ServiceResultReceiver.Receiver() {

                @Override
                public void onReceiveResult(int resultCode, Bundle resultData) {

                    switch (resultCode) {
                        case ImageService.ResultStatus.LOAD_IMAGES_FINISHED:
                            if (resultData.containsKey(ImageService.RESULT_KEY)) {

                                List<ClientImagePreview> loadedImages = (List<ClientImagePreview>) resultData.getSerializable(ImageService.RESULT_KEY);
                                if (loadedImages != null && !loadedImages.isEmpty()) {
                                    ImageCache.getInstance().addClientImagesPreview(loadedImages);

                                    if (loadedImages.size() > uploadedPhotosAdapter.getImagesCount()) {
                                        uploadedPhotosAdapter.setImages(loadedImages);
                                        uploadedPhotosAdapter.notifyDataSetChanged();
                                    }
                                } else {
                                    TextView textView = (TextView) layout.findViewById(R.id.noPhotosYet);
                                    textView.setText(getResources().getString(R.string.uploadedPhotosNoPhotosYet));
                                    textView.setVisibility(View.VISIBLE);
                                }

                                if (loadingDialog!= null && loadingDialog.isShowing()) {
                                    loadingDialog.dismiss();
                                }

                            } else {
                                if (loadingDialog.isShowing()) {
                                    loadingDialog.dismiss();
                                }
                                LayoutUtil.showRefreshLayout(getActivity(), R.string.uploadedPhotosLoadingError);
                            }
                            break;

                        case ImageService.ResultStatus.ERROR:
                            if (loadingDialog.isShowing()) {
                                loadingDialog.dismiss();
                            }

                            if (uploadedPhotosAdapter.getImagesCount() == 0) {
                                LayoutUtil.showRefreshLayout(getActivity(), R.string.uploadedPhotosLoadingError);
                            } else {
                                Toast.makeText(getActivity(), getResources().getString(R.string.uploadedPhotosLoadingError),
                                        Toast.LENGTH_SHORT).show();
                            }
                            break;

                        case ImageService.ResultStatus.ABORTED:
                            break;
                    }
                }
            });

            loadImagesServiceCall();

        } else {
            LayoutUtil.showRefreshLayout(getActivity(), R.string.noNetworkConnectionError);
        }

        return layout;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, com.actionbarsherlock.view.MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_user_photos, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int pressedMenuItemId = item.getItemId();

        FragmentSwitcher fragmentSwitcher = FragmentSwitcherHolder.getFragmentSwitcher();
        switch (pressedMenuItemId) {
            case R.id.proceedToMapMenuOption:
                fragmentSwitcher.showMapFragment();
                break;

            case R.id.takeImageMenuOption:
                fragmentSwitcher.showGetPhotoFragment();
                break;
        }

        return true;
    }

    /**
     *
     */
    public void loadImagesServiceCall() {

        SearchCriteria searchCriteria = new SearchCriteria();

        // Device Id
        searchCriteria.setDeviceId(CommonUtil.getDeviceId(getActivity()));
        searchCriteria.setLoadOwnImages(true);

        ServiceUtil.callLoadImagesService(searchCriteria, resultReceiver, getActivity());
    }
}

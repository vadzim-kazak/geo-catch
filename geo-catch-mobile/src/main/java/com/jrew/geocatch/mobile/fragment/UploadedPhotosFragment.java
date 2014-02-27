package com.jrew.geocatch.mobile.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
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

        // Action bar subtitle
        ActionBarUtil.setActionBarSubtitle(R.string.uploadedPhotosFragmentLabel, getActivity());

        View layout = null;
        if (WebUtil.isNetworkAvailable(getActivity())) {

            LayoutUtil.showFragmentContainer(getActivity());

            layout = inflater.inflate(R.layout.uploaded_photos_fragment, container, false);
            final GridView photosGridView = (GridView) layout.findViewById(R.id.photosGridView);

            DialogInterface.OnCancelListener listener = new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {

                    ServiceUtil.abortImageService(getActivity());
                }
            };

            if (uploadedPhotosAdapter == null) {
                uploadedPhotosAdapter = new UploadedPhotosAdapter(getActivity());
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
                                ImageCache.getInstance().addClientImagesPreview(loadedImages);

                                if (loadedImages != null && loadedImages.size() > uploadedPhotosAdapter.getImagesCount()) {
                                    uploadedPhotosAdapter.setImages(loadedImages);
                                    uploadedPhotosAdapter.notifyDataSetChanged();
                                }

                                if (loadingDialog!= null && loadingDialog.isShowing()) {
                                    loadingDialog.dismiss();
                                }

                            } else {
                                LayoutUtil.showRefreshLayout(getActivity(), R.string.uploadedPhotosLoadingError);
                            }
                            break;

                        case ImageService.ResultStatus.ERROR:
                            if (loadingDialog.isShowing()) {
                                loadingDialog.dismiss();
                                LayoutUtil.showRefreshLayout(getActivity(), R.string.uploadedPhotosLoadingError);
                            }
                            break;

                        case ImageService.ResultStatus.ABORTED:
                            LayoutUtil.showRefreshLayout(getActivity(), R.string.uploadedPhotosLoadingCancelled);
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

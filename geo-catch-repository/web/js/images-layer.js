var ImageLayer = function(map, imageId) {

    /** Image provider url prefix **/
    var imageSearchProviderUrl = "http://${repository.domain.name}/${repository.context.path}/${repository.search.path}";

    /**  **/
    var imageProviderUrl = "http://${repository.domain.name}/${repository.context.path}/${repository.provider.path}";

    /** InfoWindow template id for JsRender **/
    var infoWindowTemplateId = 'infoWindowTmpl';

    /**  **/
    var dateFormatToDisplay = 'YYYY MMM DD, HH:mm:ss';

    /**  **/
    var imageDateTimeFormat = 'YYDDDHHmmssSSS';

    /**  **/
    var imagePreviewZoomLevel = 16;

    /**  **/
    var zoomThreshold = 10;

    /** **/
    var iconSize = {
        small: 28,
        big: 34
    }

    var showInfoWindowDelay = 500;

    /** Array of loaded images **/
    var images = [];

    this.showImageOnLoad = function(imageId) {
        // make ajax call to marker provide service
        $.ajax({
            dataType: "json",
            url: imageProviderUrl + "/" + imageId,
            type: "GET",
            contentType: "application/json; charset=utf-8",
            success: function (fullImage) {

                map.setCenter(new google.maps.LatLng(fullImage.latitude, fullImage.longitude));
                map.setZoom(imagePreviewZoomLevel);

                var showImageOnLoadCallback = function() {
                    for (var i = 0; i < images.length; i++) {
                        var image = images[i];
                        if (image.id == imageId) {

                            image.fullImage = fullImage;
                            image.infoWindow = createInfoWindow(image.fullImage);

                            setTimeout(function(){
                                google.maps.event.trigger(image.marker, "click");
                            }, showInfoWindowDelay);

                            break;
                        }
                    }
                }

                loadImages(showImageOnLoadCallback);
            }
        });
    }

    /**
     *
     */
    this.handleChangeViewBoundsEvent = function() {
        loadImages();
        removeInvisibleImages();
    }

    /**
     *
     */
    loadImages = function(loadImagesCallback) {

        var requestData = {
            viewBounds : getViewBounds(),
            deviceId: 0,
            owner : 'any',
            domainProperties: []
        }

        var fish = $("#fish").val();
        if (fish && fish.length > 0 ) {
            requestData.domainProperties.push({
                type: 1,
                locale : $('#language').val(),
                item : fish
            });
        }

        var tool = $("#fishingTool").val();
        if (tool && tool.length > 0) {
            requestData.domainProperties.push({
                type: 2,
                locale : $('#language').val(),
                item : tool
            });
        }

        var bait = $("#fishingBait").val();
        if (bait && bait.length > 0) {
            requestData.domainProperties.push({
                type: 3,
                locale : $('#language').val(),
                item : bait
            });
        }

        // make ajax call to image provision service
        $.ajax({
            dataType: "json",
            url: generateImageProviderUrl(),
            type: "POST",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(requestData),
            success: function(response) {

                for (var i = 0; i < response.length; i++) {
                    // Check if current image is currently shown on map
                    var image = response[i];
                    if(!isImageLoaded(image)) {
                        showImage(image);
                        images.push(image);
                    }
                }

                if (loadImagesCallback !== undefined) {
                    loadImagesCallback();
                }
            }
        });
    }

    /**
     * @returns {string}
     */
    var generateImageProviderUrl = function() {
        return imageSearchProviderUrl;
    }

    /**
     * @returns {string}
     */
    var getViewBounds = function() {
        // Get current view bounds
        var viewBounds = map.getBounds();

        var northEast = viewBounds.getNorthEast();
        var southWest = viewBounds.getSouthWest();

        return { northEastLat : northEast.lat(),
                 northEastLng : northEast.lng(),
                 southWestLat : southWest.lat(),
                 southWestLng : southWest.lng() }
    }

    /**
     * @param image
     */
    var showImage = function(image) {

        // Update image with position property
        image.position = new google.maps.LatLng(image.latitude, image.longitude);

        // Create & set to image new rich marker entity
        image.marker = new RichMarker({
            position: image.position,
            map: map,
            anchor: RichMarkerPosition.TOP,
            content: createIcon(image)
        });

        image.marker.size = getRequiredIconSize();

        // Create & set to image new info window entity
        //image.infoWindow = loadFullImage(image);
        google.maps.event.addListener(image.marker, 'click', function() {
            closeOpenedInfoWindows();
            if (image.fullImage) {
                if (image.infoWindow == undefined) {
                    image.infoWindow = createInfoWindow(image.fullImage);
                } else {
                    image.infoWindow.setContent(createInfoWindowData(image.fullImage));
                }
                image.infoWindow.open(map, image.marker);
            } else {
                loadFullImage(image);
            }
        });
    }

    /**
     * @param image
     */
    var createIcon = function(image) {

        var size = getRequiredIconSize();

        return '<img src="' + image.thumbnailPath + '"' +
            ' width="' + size + '"' +
            ' height="' + size + '"' +
            ' style="border:1px solid white;-moz-box-shadow:0px 0px 10px #000;-webkit-box-shadow:0px 0px 10px #000;box-shadow:0px 0px 10px #000;"/>';
    }


    var loadFullImage = function(image) {
        // make ajax call to marker provide service
        $.ajax({
            dataType: "json",
            url: imageProviderUrl + "/" + image.id,
            type: "GET",
            contentType: "application/json; charset=utf-8",
            success: function (fullImage) {
                image.infoWindow = createInfoWindow(fullImage);
                image.fullImage = fullImage;
                image.infoWindow.open(map, image.marker);
            }
        });

    }

    /**
     *
     * @param image
     * @returns {google.maps.InfoWindow}
     */
    var createInfoWindow = function(image) {

        return new google.maps.InfoWindow({
            content: createInfoWindowData(image)
        });
    }

    var createInfoWindowData = function(image) {
        var domainProperties = image.domainProperties;
        for (var i = 0; i < domainProperties.length; i++) {
            domainProperties[i].value = getLocalizedValue($('#language').val(), domainProperties[i]);
        }

        if (image.parsedDate === undefined) {
            // Convert date to string
            // It is assumed that string comes in
            image.parsedDate = moment(image.date, imageDateTimeFormat).format(dateFormatToDisplay);
        }

        return $("#" + infoWindowTemplateId).render(image);
    }

    /**
     * @param image
     * @returns {boolean}
     */
    var isImageLoaded = function(image) {
        for (var i = 0; i < images.length; i++){
            if (images[i].id == image.id) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     */
    var removeInvisibleImages = function() {
        var visibleImages = [];
        for (var i = 0; i < images.length; i++){
            var image = images[i];
            if (isImageVisible(image)) {
                visibleImages.push(image);
            } else {
                // Remove marker from map
                image.marker.setMap(null);
            }
        }
        images = visibleImages;
    }

    /**
     * @param image
     * @returns {boolean}
     */
    var isImageVisible = function(image) {
       return map.getBounds().contains(image.position);
    }

    /**
     *
     */
    this.handleChangeZoomEvent = function() {

        loadImages();

        removeInvisibleImages();

        var imagesToUpdate = filterIconsBySize();
        if (imagesToUpdate.length > 0) {
            updateIconsSize(imagesToUpdate);
        }
    }

    /**
     * @returns {boolean}
     */
    var filterIconsBySize = function() {
        var requiredIconSize = getRequiredIconSize();
        var imagesToUpdate = [];
        for (var i = 0; i < images.length; i++){
            var iconSize = images[i].marker.size;
            if (iconSize != requiredIconSize) {
                imagesToUpdate.push(images[i]);
            }
        }

        return imagesToUpdate;
    }

    /**
     * @returns {number}
     */
    var getRequiredIconSize = function() {
        var zoom = map.getZoom();
        if (zoom > zoomThreshold) {
            return iconSize.big;
        }
        return iconSize.small;
    }

    /**
     *
     */
    var updateIconsSize = function(imagesToUpdate) {
        for (var i = 0; i < imagesToUpdate.length; i++){
            var image = imagesToUpdate[i];
            var newIcon = createIcon(image);
            image.marker.setContent(newIcon);
            image.marker.size = getRequiredIconSize();
        }
    }

    /**
     *
     */
    this.refresh = function() {
        for (var i = 0; i < images.length; i++){
            var image = images[i];
            // Remove marker from map
            image.marker.setMap(null);
        }

        images = [];

        loadImages();
    }

    /**
     *
     */
    var closeOpenedInfoWindows = function() {
        for (var i = 0; i < images.length; i++){
            var image = images[i];
            if (image.infoWindow != undefined) {
                var map = image.infoWindow.getMap();
                if (map !== null && typeof map !== "undefined") {
                    image.infoWindow.close();
                }
            }
        }
    }
}
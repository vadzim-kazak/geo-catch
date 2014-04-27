var ImageLayer = function(map, imageId) {

    /** Image provider url prefix **/
    var imageSearchProviderUrl = "http://${repository.domain.name}/${repository.context.path}/${repository.search.path}";

    var imageProviderUrl = "http://${repository.domain.name}/${repository.context.path}/${repository.provider.path}";

    /** InfoWindow template id for JsRender **/
    var infoWindowTemplateId = 'infoWindowTmpl';

    /**  **/
    var zoomThreshold = 12;

    var dateFormat = 'YYYY MMM DD, HH:mm:ss';

    /** **/
    var iconSize = {
        small: 24,
        big: 32
    }

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
                map.setZoom(16);

                var showImageOnLoadCallback = function() {
                    for (var i = 0; i < images.length; i++) {
                        var image = images[i];
                        if (image.id == imageId) {

                            image.fullImage = fullImage;
                            image.infoWindow = createInfoWindow(image.fullImage);

                            setTimeout(function(){
                                google.maps.event.trigger(image.marker, "click");
                                console.log("clicked");
                            }, 500);

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
        // Create & set to image new marker entity
        image.marker = new google.maps.Marker({
            position: image.position,
            map: map,
            icon: createIcon(image)
        });

        // Create & set to image new info window entity
        //image.infoWindow = loadFullImage(image);
        google.maps.event.addListener(image.marker, 'click', function() {
            if (image.fullImage) {
                image.infoWindow = createInfoWindow(image.fullImage);
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
        var iconSize = getRequiredIconSize();
        return {
            url : image.thumbnailPath,
            size : new google.maps.Size(iconSize, iconSize), // desired size
            scaledSize: new google.maps.Size(iconSize, iconSize),
            strokeColor: 'white',
            strokeWeight: 3
        };
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

        var domainProperties = image.domainProperties;
        for (var i = 0; i < domainProperties.length; i++) {
            domainProperties[i].value = getLocalizedValue($('#language').val(), domainProperties[i]);
        }

        if (image.parsedDate === undefined) {
            // Convert date to string
            // It is assumed that string comes in
            image.parsedDate = moment(image.date, "YYDDDHHmmssSSS").format(dateFormat);
        }

        var infoWindow = $("#" + infoWindowTemplateId).render(image);
        return new google.maps.InfoWindow({
            content: infoWindow
        });
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
        if (isNeedToUpdateIconSize()) {
            updateIconsSize();
        }
    }

    /**
     * @returns {boolean}
     */
    var isNeedToUpdateIconSize = function() {
        var requiredIconSize = getRequiredIconSize();
        for (var i = 0; i < images.length; i++){
            var iconSize = images[i].marker.icon.size.width;
            if (iconSize != requiredIconSize) {
                return true;
            }
        }
        return false;
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
    var updateIconsSize = function() {
        for (var i = 0; i < images.length; i++){
            var image = images[i];
            var newIcon = createIcon(image);
            image.marker.setIcon(newIcon);
        }
    }

    this.refresh = function() {
        for (var i = 0; i < images.length; i++){
            var image = images[i];
            // Remove marker from map
            image.marker.setMap(null);
        }

        images = [];

        loadImages();
    }

}
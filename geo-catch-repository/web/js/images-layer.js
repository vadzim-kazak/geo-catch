var ImageLayer = function(map) {

    /** Image provider url prefix **/
    var imageProviderUrl = "http://${repository.domain.name}/${repository.context.path}/${repository.search.path}";

    /** InfoWindow template id for JsRender **/
    var infoWindowTemplateId = 'infoWindowTmpl';

    /**  **/
    var zoomThreshold = 8;

    /** **/
    var iconSize = {
        small: 16,
        big: 32
    }

    /** Array of loaded images **/
    var images = [];

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
    loadImages = function() {

        var currentLocale="ru";

        requestData = {
            viewBounds : getViewBounds(),
            deviceId: $('#deviceId').val(),
            owner : $('#owner').val(),
            domainProperties: []
        }

        var fish = $("#fish").val();
        if (fish && fish.length > 0 ) {
            requestData.domainProperties.push({
                type: 1,
                locale : currentLocale,
                item : fish
            });
        }

        var tool = $("#fishingTool").val();
        if (tool && tool.length > 0) {
            requestData.domainProperties.push({
                type: 2,
                locale : currentLocale,
                item : tool
            });
        }

        var bite = $("#fishingBite").val();
        if (bite && bite.length > 0) {
            requestData.domainProperties.push({
                type: 3,
                locale : currentLocale,
                item : bite
            });
        }

        if($("#hours").is(':checked')){
            var dayPeriod = {
                fromHour:  $("#fromHour").val(),
                toHour: $("#toHour").val()
            }

            requestData.dayPeriod = dayPeriod;
        }

        if($("#months").is(':checked')){
            var monthPeriod = {
                fromMonth:  $("#fromMonth").val(),
                toMonth: $("#toMonth").val()
            }

            requestData.monthPeriod = monthPeriod;
        }

        // make ajax call to marker provide service
        $.ajax({
            dataType: "json",
            url: generateImageProviderUrl(),
            type: "POST",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(requestData),
            success: imageProviderResponseHandler
        });

    }

    /**
     * @returns {string}
     */
    var generateImageProviderUrl = function() {
        return imageProviderUrl;
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
     *
     */
    var imageProviderResponseHandler = function(response) {

        for (var i = 0; i < response.length; i++) {
            // Check if current image is currently shown on map
            var image = response[i];
            if(!isImageLoaded(image)) {
                showImage(image);
                images.push(image);
            }
        }
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
            scaledSize: new google.maps.Size(iconSize, iconSize)
        };
    }


    var loadFullImage = function(image) {
        // make ajax call to marker provide service
        $.ajax({
            dataType: "json",
            url: generateImageProviderUrl() + "/" + image.id,
            type: "GET",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(requestData),
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
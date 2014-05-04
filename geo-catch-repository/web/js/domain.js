// Somain data container
var domainData = {};

/**
 *
 * @param locale
 * @param containerData
 */
function loadDomainProperties(locale, containerData) {

    if (domainData[locale] === undefined) {
        populateDomainProperties(locale, containerData);
    }
}

/**
 *
 * @param locale
 * @param containerData
 */
function populateDomainProperties(locale, containerData) {

    if (domainData[locale] === undefined) {

        $.ajax({
            dataType: "json",
            url: '/${repository.context.path}/${repository.domain.path}/' + locale,
            type: "GET",
            contentType: "application/json; charset=utf-8",
            data: null,
            success: function(response) {
                domainData[locale] = response;
                handleDomainData(response);
            }
        });

    } else {
        handleDomainData(domainData[locale]);
    }

    /**
     *
     */
    function handleDomainData(response) {

        var arrayLength = containerData.length;
        for (var i = 0; i < arrayLength; i++) {
            var container = containerData[i];
            var domainProperties = filterDomainProperties(response, container.type);
            populateSelectList(domainProperties, container.id);
        }
    }

    /**
     *
     * @param domainProperties
     * @param type
     * @returns {Array}
     */
    function filterDomainProperties(domainProperties, type) {

        var result = [];
        var counter = 0;
        var currentItem = 0;

        for (var i = 0; i < domainProperties.length; i++) {
            // Check if current image is currently shown on map
            var domainProperty = domainProperties[i];
            if (domainProperty.type == type && domainProperty.item != currentItem) {
                result[counter] = domainProperty;
                currentItem = domainProperty.item;
                counter++;
            }
        }

        return result;
    }

    /**
     *
     * @param domainProperties
     * @param containerId
     */
    function populateSelectList(domainProperties, containerId) {

        $('#' + containerId)
            .find('option')
            .remove()
            .end();

        var option= '<option value="" selected></option>';
        $('#' + containerId).append(option);

        for (var i = 0; i < domainProperties.length; i++) {
            // Check if current image is currently shown on map
            var domainProperty = domainProperties[i];
            var option= '<option value="' + domainProperty.item + '">' + domainProperty.value + '</option>';
            $('#' + containerId).append(option);
        }
    }
}

function getLocalizedValue(locale, domainProperty) {
    if (domainData[locale] !== undefined) {
        var localizedDomainProperties = domainData[locale];
        for (var i = 0; i < localizedDomainProperties.length; i++) {
            if (domainProperty.item == localizedDomainProperties[i].item) {
                return localizedDomainProperties[i].value;
            }
        }
    }

    return domainProperty.value;
}

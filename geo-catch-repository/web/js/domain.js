/**
 *
 * @param type
 * @param locale
 * @param containerId
 */
function populateDomainProperty(type, locale, containerId, isItem, isFirstEmpty) {

    $.ajax({
        dataType: "json",
        url: '/fishing/repo/domain/' + type + '/' + locale,
        type: "GET",
        contentType: "application/json; charset=utf-8",
        data: null,
        success: function(response) {
            populateSelect(response);
        }
    });

    /**
     *
     */
    var populateSelect = function(response) {

        if (isFirstEmpty) {
            var option= '<option value="" selected></option>';
            $('#' + containerId).append(option);
        }
        for (var i = 0; i < response.length; i++) {
            // Check if current image is currently shown on map
            var domainProperty = response[i];
            if (isItem) {
                var option= '<option value="' + domainProperty.item + '">' + domainProperty.value + '</option>';
            } else {
                var option= '<option value="' + domainProperty.id + '">' + domainProperty.value + '</option>';
            }
            $('#' + containerId).append(option);
        }
    }
}

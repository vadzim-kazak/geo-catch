/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 5/9/14
 * Time: 4:47 AM
 * To change this template use File | Settings | File Templates.
 */
function sendEmail(modalId, emailId, messageId, subject, nameId) {

    /**  **/
    var emailSenderUrl = 'http://${repository.domain.name}/${repository.context.path}/${repository.util.path}/email';

    var name = "";
    if (nameId != undefined) {
        name = $('#' + nameId).val();
    }

    var emailData = {
        from: $('#' + emailId).val(),
        message: $('#' + messageId).val(),
        subject: subject,
        name: name
    }

    showLoading();

    $.ajax({
        dataType: "text",
        url: emailSenderUrl,
        type: "POST",
        cache: false,
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(emailData),
        success: function(response) {
            hideLoading();

            $('#' + modalId).find('.modal-body > div').hide();
            $('#' + modalId).find('.sendConfirmation').show();

            $('#' + modalId).find('.modal-footer > button').hide();
            $('#' + modalId).find('.confirmationClose').show();
        },
        error: function(response) {
            hideLoading();
            $('#' + modalId).find('.error-block').show();
        }
    });

    function showLoading() {
        $('#' + modalId).find('.modal-content').block({ message: null,
                                                        overlayCSS: {backgroundColor: '#bbb' }
                                                      });
        $('#' + modalId).find('.loading-indicator').show();
    }

    function hideLoading() {
        $('#' + modalId).find('.modal-content').unblock();
        $('#' + modalId).find('.loading-indicator').hide();
    }
}

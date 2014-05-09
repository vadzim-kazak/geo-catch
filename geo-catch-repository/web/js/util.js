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

    $('#' + modalId).find('.loading-indicator').show();


    $.ajax({
        dataType: "text",
        url: emailSenderUrl,
        type: "POST",
        cache: false,
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(emailData),
        success: function(response) {
            $('#' + modalId).find('.loading-indicator').hide();
            $('#' + modalId).modal('hide');
        },
        error: function(response) {
            $('#' + modalId).find('.loading-indicator').hide();
            $('#' + modalId).find('.error-block').show();
        }
    });
}

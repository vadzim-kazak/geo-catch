<html>
<head>
    <script src="js/jquery-1.10.2.js"></script>
    <script src="js/jquery.upload-1.0.2.min.js"></script>
    <script src="js/domain.js"></script>

    <script type="text/javascript">

        var defaultLocale = 'ru';

        function setImageData() {
            var image = {
                deviceId : $('#deviceId').val(),
                description : $('#description').val(),
                latitude : $('#latitude').val(),
                longitude : $('#longitude').val(),
                date : $('#date').val(),
                privacyLevel : $('#privacyLevel').val(),
                rating : $('#rating').val(),
                domainProperties : [
                    {
                        id : $('#fishId').val()
                    }
                ]
            }

            if ($("#fishingTool").val() != "") {
                image.domainProperties.push({
                    id : $('#fishingTool').val()
                });
            }

            if ($("#fishingByte").val() != "") {
                image.domainProperties.push({
                    id : $('#fishingByte').val()
                });
            }

            $('#image').val(JSON.stringify(image));
        }

        $( document ).ready(function() {
            populateDomainProperty('1', 'ru', 'fish');
            populateDomainProperty('2', 'ru', 'fishingTool', false, true);
            populateDomainProperty('3', 'ru', 'fishingBite', false, true);
        });

    </script>

</head>
<body>
<form action="${pageContext.request.contextPath}/repo/images" method="post" enctype="multipart/form-data" >
    <input type="text" id="deviceId" value="1"/> Device ID
    <br/>

    <select id="privacyLevel">
        <option value="PUBLIC" selected>Public</option>
        <option value="PRIVATE">Private</option>
    </select> Privacy Level<br/>

    <input type="text" name="description" id="description" value="bla"/> Description
    <br/>
    <input type="text" name="latitude" id="latitude" value="12"/> latitude
    <br/>
    <input type="text" name="longitude" id="longitude" value="12"/> longitude
    <br/>
    <input type="text" name="date" id="date" value="13023121212128"/> date
    <br/>
    <input type="text" name="rating" id="rating" value="1"/> rating
    <br/>

    Fish: <select id="fish">
          </select><br/>

    Fishing tool: <select id="fishingTool">
                  </select><br/>

    Fishing bite: <select id="fishingBite">
                  </select></br>

    <!--
    New domain properties: <br/>
    <input type="text" id="fishId" value="1"/> fish id
    <br/>
    <input type="text" id="fishItem" value="1"/> fish item
    <br/>
    <input type="text" name="fishValue" id="fishValue" value="pike"/> fish value
    <br/>
    <input type="text" name="fishLocale" id="fishLocale" value="en"/> fish locale
    <br/>
    <input type="text" id="fishingToolId" value="5000"/> fishing tool id
    <br/>
    <input type="text" id="fishingToolItem" value="5000"/> fishing tool item
    <br/>
    <input type="text" name="fishingToolValue" id="fishingToolValue" value="spinning"/> fishing tool value
    <br/>
    <input type="text" name="fishingToolLocale" id="fishingToolLocale" value="en"/> fishing tool locale
    <hr/>
    <br/> -->

    Load file: <input type="file" name="file" id="file" size="40" onclick="setImageData();"/>
    <br/>
    <input type="hidden" id="image" name="image">
    <input type="submit">
</form >

</body>
</html>
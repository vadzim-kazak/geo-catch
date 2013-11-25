<html>
<head>
    <script src="js/jquery-1.10.2.js"></script>
    <script src="js/jquery.upload-1.0.2.min.js"></script>

    <script type="text/javascript">

        function setImageData() {
            var image = {
                userId : $('#userId').val(),
                description : $('#description').val(),
                latitude : $('#latitude').val(),
                longitude : $('#longitude').val(),
                date : $('#date').val(),
                rating : $('#rating').val(),
                domainProperties : [
                    {
                        id : $('#fishId').val(),
                        type : 1,
                        item :   $('#fishItem').val(),
                        locale :  $('#fishLocale').val(),
                        value:  $('#fishValue').val()
                    },
                    {
                        id :   $('#fishingToolId').val(),
                        type : 2,
                        item :   $('#fishingToolItem').val(),
                        locale :  $('#fishingToolLocale').val(),
                        value:  $('#fishingToolValue').val()
                    }
                ]
            }

            $('#image').val(JSON.stringify(image));
        }

    </script>

</head>
<body>
<form action="${pageContext.request.contextPath}/spring/images/upload" method="post" enctype="multipart/form-data" >
    <input type="text" name="userId" id="userId" value="1"/> User ID
    <br/>
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
    <input type="text" id="fishId" value="1"/> fish id
    <br/>
    <input type="text" id="fishItem" value="1"/> fish item
    <br/>
    <input type="text" name="fishValue" id="fishValue" value="pike"/> fish value
    <br/>
    <input type="text" name="fishLocale" id="fishLocale" value="en"/> fish locale
    <br/>
    <input type="text" id="fishingToolId" value="5"/> fishing tool id
    <br/>
    <input type="text" id="fishingToolItem" value="10"/> fishing tool item
    <br/>
    <input type="text" name="fishingToolValue" id="fishingToolValue" value="rod"/> fishing tool value
    <br/>
    <input type="text" name="fishingToolLocale" id="fishingToolLocale" value="en"/> fishing tool locale
    <br/>
    Load file: <input type="file" name="file" id="file" size="40" onclick="setImageData();"/>
    <br/>
    <input type="hidden" id="image" name="image">
    <input type="submit">
</form >

</body>
</html>
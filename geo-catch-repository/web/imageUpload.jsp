<html>
    <head>
    </head>
    <body>
        <form action="${pageContext.request.contextPath}/spring/images/upload" method="post" enctype="multipart/form-data">
            Load file: <input type="file" name="file" size="40" />
            <br/>
            <input type="text" name="userId"/> User ID
            <br/>
            <input type="text" name="description"/> Description
            <br/>
            <input type="text" name="latitude"/> latitude
            <br/>
            <input type="text" name="longitude"/> longitude
            <br/>
            <input type="text" name="date"/> date
            <br/>
            <input type="text" name="rating"/> rating
            <br/>
            <input type="submit" />
        </form>
    </body>
</html>
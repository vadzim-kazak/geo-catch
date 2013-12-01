<%--
  Created by IntelliJ IDEA.
  User: Vadim
  Date: 7/9/13
  Time: 9:31 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title></title>
      <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />

      <style type="text/css">
          html { height: 100% }
          body { height: 100%; margin: 0; padding: 0 }
          #map-canvas { height: 100% }
      </style>

      <script src="js/jquery-1.10.2.js"></script>
      <script src="js/jsrender.js"></script>
      <script src="js/images-layer.js"></script>
      <script src="js/domain.js"></script>
      <script type="text/javascript"
              src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBI_eGAZN3QUbNyJIxV73LWlRf2iCUa5ew&sensor=false">
      </script>

      <script id="infoWindowTmpl" type="text/x-jquery-tmpl">
          <div>
            {{:description}}
            <img src="{{:path}}" />
          </div>
      </script>

      <script type="text/javascript">

          // Enable the visual refresh
          google.maps.visualRefresh = true;

          /**
          * Initializes google map on page loading
          */
          function initialize() {

              var mapOptions = {
                  center: new google.maps.LatLng(0, 0),
                  zoom: 4,
                  mapTypeId: google.maps.MapTypeId.TERRAIN
              };

              var map = new google.maps.Map(document.getElementById("map-canvas"), mapOptions);
              var imageLayer = new ImageLayer(map);

             //Add map events listeners
             google.maps.event.addListener(map, 'dragend', imageLayer.handleChangeViewBoundsEvent);
             google.maps.event.addListener(map, 'zoom_changed', imageLayer.handleChangeViewBoundsEvent);
             google.maps.event.addListener(map, 'zoom_changed', imageLayer.handleChangeZoomEvent);

             $( document ).ready(function() {
                $("#refresh").click(imageLayer.refresh);

                 populateDomainProperty('1', 'ru', 'fish', true, true);
                 populateDomainProperty('2', 'ru', 'fishingTool', true, true);
                 populateDomainProperty('3', 'ru', 'fishingBite', true, true);
             });
          }

          google.maps.event.addDomListener(window, 'load', initialize);
      </script>
  </head>
  <body>
  <div>
     Device id: <input type="text" id="deviceId" value="0"/>
     Owner: <select id="owner">
                <option value="any" selected>From anyone</option>
                <option value="self">Only my</option>
             </select>

     Fish: <select id="fish">
           </select>

     Fishing tool: <select id="fishingTool">
                   </select>

      Fishing bite: <select id="fishingBite">
      </select>

     <input type="button" id="refresh" value="Refresh" >
  </div>
  <div id="map-canvas"></div>
  </body>
</html>
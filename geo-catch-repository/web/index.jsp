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
          {{for domainProperties}}
              {{if #index < #parent.data.length - 1}}
                {{:value}} <b>|</b>
              {{else}}
                {{:value}}
              {{/if}}
          {{/for}}
          <div>
            <img src="{{:path}}" width="500" height="500"/>
          </div>
          <div>{{:description}}</div>
      </script>

      <script type="text/javascript">

          var domainContainers = [
              {
                  id : "fish",
                  type: 1
              },
              {
                  id : "fishingTool",
                  type: 2
              },{
                  id : "fishingBait",
                  type: 3
              }
          ]

          locale = 'ru';

          // Enable the visual refresh
          google.maps.visualRefresh = true;

          /**
          * Initializes google map on page loading
          */
          function initialize() {

              var mapOptions = {
                  center: new google.maps.LatLng(0, 0),
                  zoom: 4,
                  mapTypeId: google.maps.MapTypeId.ROADMAP
              };

              var map = new google.maps.Map(document.getElementById("map-canvas"), mapOptions);
              imageLayer = new ImageLayer(map);

             //Add map events listeners
             google.maps.event.addListener(map, 'dragend', imageLayer.handleChangeViewBoundsEvent);
             google.maps.event.addListener(map, 'zoom_changed', imageLayer.handleChangeViewBoundsEvent);
             google.maps.event.addListener(map, 'zoom_changed', imageLayer.handleChangeZoomEvent);

             $( document ).ready(function() {
                 populateDomainProperties(locale, domainContainers);
             });
          }

          google.maps.event.addDomListener(window, 'load', initialize);

          function handleLocaleSelection() {

              var selectedLocale = $("#language").val();
              if (selectedLocale != locale) {
                locale = selectedLocale;
                populateDomainProperties(locale, domainContainers);
              }
          }

          function refresh() {
              imageLayer.refresh();
          }

      </script>
  </head>
  <body>
  <div style="overflow:hidden">


      <div style="float:left; position:relative">

         <img src="icons/icon.png" width="20" height="20" />

         Fish: <select id="fish" onchange="refresh()"></select>

         Tool: <select id="fishingTool" onchange="refresh()"></select>

         Bait: <select id="fishingBait" onchange="refresh()"></select>

     </div>

     <div style="float:right">
         Language: <select id="language" onchange="handleLocaleSelection()">
                    <option value="en">English</option>
                    <option value="ru" selected>Russian</option>
                   </select>
     </div>
  </div>
  <div id="map-canvas"></div>
  </body>
</html>
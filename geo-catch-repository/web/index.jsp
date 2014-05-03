<%--
  Created by IntelliJ IDEA.
  User: Vadim
  Date: 7/9/13
  Time: 9:31 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
  <head>

      <link rel="icon" type="image/png" href="icons/favicon.png" />

      <title>Geo-Catch | Fishing</title>

      <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />

      <style type="text/css">
          html { height: 100% }
          body { height: 100%; margin: 0; padding: 0 }
          #map-canvas { height: 100% }
      </style>

      <script type="text/javascript"
              src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBI_eGAZN3QUbNyJIxV73LWlRf2iCUa5ew&sensor=false">
      </script>
      <script src="js/jquery-1.10.2.js"></script>
      <script src="js/jsrender.js"></script>
      <script src="js/images-layer.js"></script>
      <script src="js/domain.js"></script>
      <script src="js/moment.min.js"></script>
      <script src="js/richmarker.js"></script>

      <!-- Google Analytics -->
      <script>
          (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
              (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
                  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
          })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

          ga('create', 'UA-50623419-1', 'geo-catch.com');
          ga('send', 'pageview');

      </script>

      <script id="infoWindowTmpl" type="text/x-jquery-tmpl">
          <div style="min-height: 590px;max-height:750px;">
              {{for domainProperties}}

                  {{if type == 1}}
                    <img src="icons/fish.png" width="20" height="20" style="vertical-align:middle"/>
                  {{/if}}
                  {{if type == 2}}
                    <img src="icons/rod.png" width="20" height="20" style="vertical-align:middle"/>
                  {{/if}}
                  {{if type == 3}}
                    <img src="icons/hook.png" width="20" height="20" style="vertical-align:middle"/>
                  {{/if}}

                  {{:value}}

              {{/for}}
              <input type="button" value="Share" onclick="copyToClipboard('http://${repository.domain.name}/${repository.context.path}?image={{:id}}')" style="float: right;">
              <div>
                <img src="{{:path}}" width="500" height="500"/>
              </div>
              <div><img src="icons/like_unselected.png" width="18" height="18" style="vertical-align:middle"/> {{:likesCount}} <img src="icons/dislike_unselected.png" width="18" height="18" style="vertical-align:middle"/> {{:dislikesCount}} <img src="icons/report_unselected.png" width="18" height="18" style="vertical-align:middle"/> {{:reportsCount}}</div>
              <div><img src="icons/clock.png" width="18" height="18" style="vertical-align:middle"/> {{:parsedDate}}</div>
              {{if description}}
                <div><img src="icons/chat.png" width="18" height="18" style="vertical-align:middle"/> {{:description}}</div>
              {{/if}}
          </div>
      </script>

      <script type="text/javascript">

          var imageId = 0;
          <c:if test="${not empty param.image}">
            imageId = <c:out value="${param.image}"/>;
          </c:if>

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
                  // temporary center of map Minsk, Belarus
                  center: new google.maps.LatLng(53.9475743, 27.5376985),
                  zoom: 6,
                  mapTypeId: google.maps.MapTypeId.ROADMAP
              };

              var map = new google.maps.Map(document.getElementById("map-canvas"), mapOptions);
              imageLayer = new ImageLayer(map);

             //Add map events listeners
             google.maps.event.addListener(map, 'dragend', imageLayer.handleChangeViewBoundsEvent);
             google.maps.event.addListener(map, 'zoom_changed', imageLayer.handleChangeZoomEvent);

             google.maps.event.addListenerOnce(map, 'idle', function(){
                if (imageId > 0) {
                    imageLayer.showImageOnLoad(imageId);
                } else {
                    imageLayer.handleChangeViewBoundsEvent();
                }
             });

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

          function copyToClipboard(link) {
              window.prompt("Copy to clipboard: Ctrl+C, Enter", link);
          }

      </script>
  </head>
  <body>
  <div style="overflow:hidden">

      <div style="float:left; position:relative">

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
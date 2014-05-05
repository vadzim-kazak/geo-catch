<%--
  Created by IntelliJ IDEA.
  User: Vadim
  Date: 7/9/13
  Time: 9:31 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
  <head>

      <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/icons/favicon.png" />

      <title><spring:message code="app.title" /></title>

      <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />

      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootswatch.min.css">
      <style type="text/css">
          #map-canvas { width: 100%; height: 93%; margin-top: 50px}
      </style>

      <script type="text/javascript"
              src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBI_eGAZN3QUbNyJIxV73LWlRf2iCUa5ew&sensor=false">
      </script>
      <script src="${pageContext.request.contextPath}/js/jquery-1.10.2.js"></script>
      <script src="${pageContext.request.contextPath}/js/jsrender.js"></script>
      <script src="${pageContext.request.contextPath}/js/images-layer.js"></script>
      <script src="${pageContext.request.contextPath}/js/domain.js"></script>
      <script src="${pageContext.request.contextPath}/js/moment.min.js"></script>
      <script src="${pageContext.request.contextPath}/js/richmarker.js"></script>

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
          <div class="roundInfoWindow" style="min-height: 580px;max-height:750px;">
              <div>
                  <ul class="nav nav-pills" style="height: 30px">
                  {{for domainProperties}}

                      <li>
                      {{if type == 1}}
                        <img src="${pageContext.request.contextPath}/icons/fish.png" width="20" height="20" style="vertical-align:middle"/>
                      {{/if}}
                      {{if type == 2}}
                        <img src="${pageContext.request.contextPath}/icons/rod.png" width="20" height="20" style="vertical-align:middle"/>
                      {{/if}}
                      {{if type == 3}}
                        <img src="${pageContext.request.contextPath}/icons/hook.png" width="20" height="20" style="vertical-align:middle"/>
                      {{/if}}

                      <span class="text-center">{{:value}}</span>
                      </li>

                  {{/for}}

                      <li class="pull-right">
                        <input type="button" class="btn btn-primary btn-xs" value="<spring:message code="share.label" />" onclick="copyToClipboard('http://${repository.domain.name}/${repository.context.path}?image={{:id}}&locale=${pageContext.response.locale}')" style="float:right;">
                      </li>
                  </ul>
              </div>
              <div>
                <img src="{{:path}}" width="500" height="500"/>
              </div>
              <div>
                <ul class="nav nav-pills">
                    <li>
                        <img src="${pageContext.request.contextPath}/icons/clock.png" width="18" height="18" style="vertical-align:middle"/> <span class="text-center">{{:parsedDate}}</span>
                    </li>
                    <li class="pull-right">
                        <img src="${pageContext.request.contextPath}/icons/report_unselected.png" width="18" height="18" style="vertical-align:middle"/> <span class="text-center">{{:reportsCount}}</span>
                    </li>
                    <li class="pull-right">
                        <img src="${pageContext.request.contextPath}/icons/dislike_unselected.png" width="18" height="18" style="vertical-align:middle"/> <span class="text-center">{{:dislikesCount}}</span>
                    </li>
                    <li class="pull-right">
                        <img src="${pageContext.request.contextPath}/icons/like_unselected.png" width="18" height="18" style="vertical-align:middle"/> <span class="text-center">{{:likesCount}}</span>
                    </li>
                </ul>
              </div>
              {{if description}}
                <div><img src="${pageContext.request.contextPath}/icons/chat.png" width="18" height="18" style="vertical-align:middle"/> <span class="text-center">{{:description}}</span></div>
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
                  type: 1,
                  label: '<spring:message code="dropdown.fish.label" />'
              },
              {
                  id : "fishingTool",
                  type: 2,
                  label: '<spring:message code="dropdown.tool.label" />'
              },{
                  id : "fishingBait",
                  type: 3,
                  label: '<spring:message code="dropdown.bait.label" />'
              }
          ]

          domainContainers.clearLabel = '<spring:message code="dropdown.label.clear" />';

          locale = '${pageContext.response.locale}';

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
                  mapTypeId: google.maps.MapTypeId.ROADMAP,
                  streetViewControl: false,
                  mapTypeControl: false
              };

              var map = new google.maps.Map(document.getElementById("map-canvas"), mapOptions);
              imageLayer = new ImageLayer(map);

             //Add map events listeners
             google.maps.event.addListener(map, 'dragend', imageLayer.handleChangeViewBoundsEvent);
             google.maps.event.addListener(map, 'zoom_changed', imageLayer.handleChangeZoomEvent);
             google.maps.event.addListener(map, 'dragend', function() {loadDomainProperties(locale, domainContainers);});
             google.maps.event.addListener(map, 'zoom_changed', function() {loadDomainProperties(locale, domainContainers);});

             google.maps.event.addListenerOnce(map, 'idle', function(){
                if (imageId > 0) {
                    imageLayer.showImageOnLoad(imageId);
                } else {
                    imageLayer.handleChangeViewBoundsEvent();
                }

                 loadDomainProperties(locale, domainContainers);
             });
          }

          google.maps.event.addDomListener(window, 'load', initialize);

          function handleLocaleSelection() {

              var selectedLocale = $("#language").val();
              if (selectedLocale != locale) {
                  window.location.href = '${pageContext.request.contextPath}?locale=' + selectedLocale;
              }
          }

          function refresh() {
              imageLayer.refresh();
          }

          function copyToClipboard(link) {
              window.prompt("<spring:message code="share.message" />", link);
          }

      </script>
  </head>
  <body>
  <div class="navbar navbar-default navbar-fixed-top">

      <span class="navbar-brand"><spring:message code="app.title" /></span>

      <div class="navbar-collapse collapse navbar-responsive-collapse">

          <ul class="nav navbar-nav">
              <li>
                  <div class="input-group navbar-form" style="width: 250px">
                      <span class="input-group-addon"><img src="${pageContext.request.contextPath}/icons/fish.png" width="20" height="20" /></span>
                      <select id="fish" class="form-control" style="width: 200px" onchange="refresh()"></select>
                  </div>
              </li>
              <li>
                  <div class="input-group navbar-form" style="width: 250px">
                      <span class="input-group-addon"><img src="${pageContext.request.contextPath}/icons/rod.png" width="20" height="20" /></span>
                      <select id="fishingTool" class="form-control" style="width: 200px" onchange="refresh()"></select>
                  </div>
              </li>
              <li>
                  <div class="input-group navbar-form" style="width: 250px">
                      <span class="input-group-addon"><img src="${pageContext.request.contextPath}/icons/hook.png" width="20" height="20" /></span>
                      <select id="fishingBait" class="form-control" style="width: 200px" onchange="refresh()"></select>
                  </div>
              </li>
          </ul>

          <c:set var="pageLocale" value="${pageContext.response.locale}" />
          <c:if test="${not fn:startsWith(pageContext.response.locale, 'en') and
                   not fn:startsWith(pageContext.response.locale, 'ru')}">
              <%-- Current locale isn't supported. Set default locale here --%>
              <c:set var="pageLocale" value="en" />
          </c:if>

          <div class="nav navbar-nav navbar-right">
              <div class="input-group navbar-form" style="width: 250px;">
                  <span class="input-group-addon"> <spring:message code="language" /></span>
                  <select id="language" class="form-control" onchange="handleLocaleSelection()">
                      <option value="en" <c:if test="${fn:startsWith(pageLocale, 'en')}">selected</c:if>><spring:message code="language.en" /></option>
                      <option value="ru" <c:if test="${fn:startsWith(pageLocale, 'ru')}">selected</c:if>><spring:message code="language.ru" /></option>
                  </select>
              </div>
          </div>

      </div>
  </div>

  <div id="map-canvas"></div>

  <%--
  <div style="margin-left: 2%;margin-right: 2%">

      <div class="bs-docs-section" style="margin-top: 1%">
          <div class="col-lg-10">
              <h2 id="nav-tabs">Info</h2>
              <div class="bs-component">
                  <ul class="nav nav-tabs">
                      <li class="active"><a href="#home" data-toggle="tab">About</a></li>
                      <li class=""><a href="#profile" data-toggle="tab">FAQ</a></li>
                      <li class=""><a href="#profile" data-toggle="tab">Get mobile app</a></li>
                      <li class=""><a href="#profile" data-toggle="tab">Feedback</a></li>
                  </ul>
                  <div id="myTabContent" class="tab-content">
                      <div class="tab-pane fade active in" id="home">
                          <p>Raw denim you probably haven't heard of them jean shorts Austin. Nesciunt tofu stumptown aliqua, retro synth master cleanse. Mustache cliche tempor, williamsburg carles vegan helvetica. Reprehenderit butcher retro keffiyeh dreamcatcher synth. Cosby sweater eu banh mi, qui irure terry richardson ex squid. Aliquip placeat salvia cillum iphone. Seitan aliquip quis cardigan american apparel, butcher voluptate nisi qui.</p>
                      </div>
                      <div class="tab-pane fade" id="profile">
                          <p>Food truck fixie locavore, accusamus mcsweeney's marfa nulla single-origin coffee squid. Exercitation +1 labore velit, blog sartorial PBR leggings next level wes anderson artisan four loko farm-to-table craft beer twee. Qui photo booth letterpress, commodo enim craft beer mlkshk aliquip jean shorts ullamco ad vinyl cillum PBR. Homo nostrud organic, assumenda labore aesthetic magna delectus mollit.</p>
                      </div>
                      <div class="tab-pane fade" id="dropdown1">
                          <p>Etsy mixtape wayfarers, ethical wes anderson tofu before they sold out mcsweeney's organic lomo retro fanny pack lo-fi farm-to-table readymade. Messenger bag gentrify pitchfork tattooed craft beer, iphone skateboard locavore carles etsy salvia banksy hoodie helvetica. DIY synth PBR banksy irony. Leggings gentrify squid 8-bit cred pitchfork.</p>
                      </div>
                      <div class="tab-pane fade" id="dropdown2">
                          <p>Trust fund seitan letterpress, keytar raw denim keffiyeh etsy art party before they sold out master cleanse gluten-free squid scenester freegan cosby sweater. Fanny pack portland seitan DIY, art party locavore wolf cliche high life echo park Austin. Cred vinyl keffiyeh DIY salvia PBR, banh mi before they sold out farm-to-table VHS viral locavore cosby sweater.</p>
                      </div>
                  </div>
                  <div id="source-button" class="btn btn-primary btn-xs" style="display: none;">&lt; &gt;</div></div>
          </div>
      </div>

      <div class="bs-docs-section">
          <footer>
              <div class="bs-component">
                  <div class="col-lg-12">
                      <h5 align="center">2014 (c) Jrew</h5>
                  </div>
              </div>
          </footer>
      </div>

  </div>
  --%>
  </body>
</html>
<!DOCTYPE HTML>
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
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta charset="utf-8" />
<meta name="description" content="Geotagged fishing photos displayed on map." >
<meta name="keywords" content="fishing, fish, geo, geotag, geotagging, gps, map" >

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootswatch.customized.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/chosen.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/chosen.bootstrap.css">
<style type="text/css">

    html { height: 100% }
    body { height: 100%; margin: 0; padding: 0; padding-top: 50px; }
    #map-canvas { width: 100%; height: 100%;}

    @font-face {
        font-family: 'Glyphicons Halflings';
        src: url("${pageContext.request.contextPath}/fonts/glyphicons-halflings-regular.eot");
        src: url('${pageContext.request.contextPath}/fonts/glyphicons-halflings-regular.eot?#iefix') format('embedded-opentype'), url('${pageContext.request.contextPath}/fonts/glyphicons-halflings-regular.woff') format('woff'), url('${pageContext.request.contextPath}/fonts/glyphicons-halflings-regular.ttf') format('truetype'), url('${pageContext.request.contextPath}/fonts/glyphicons-halflings-regular.svg#glyphicons_halflingsregular') format('svg');
    }

    select {
        /*Remove down arrow under firefox*/
        -moz-appearance: none;
        -o-appearance:none;
        text-indent: 0.01px;
        text-overflow: '';
        -webkit-box-sizing : border-box !important;‌​
    -moz-box-sizing : border-box !important;
        box-sizing : border-box !important;
    }

        /*Remove down arrow under IE */
    select::-ms-expand {
        display: none;
    }

    .trace {
        border: 2px;
        border-color: red;
        border-style: solid;
    }

    @media (max-width: 1000px) {
        .navbar-header {
            float: none;
        }
        .navbar-toggle {
            display: block;
        }
        .navbar-collapse {
            border-top: 1px solid transparent;
            box-shadow: inset 0 1px 0 rgba(255,255,255,0.1);
        }
        .navbar-collapse.collapse {
            display: none!important;
        }
        .navbar-nav {
            float: none!important;
            margin: 7.5px -15px;
        }
        .navbar-nav>li {
            float: none;
        }
        .navbar-nav>li>a {
            padding-top: 10px;
            padding-bottom: 10px;
        }
    }

    .loading-indicator {
        position: absolute;
        left: 50%;
        top: 50%;
        margin-left: -16px; /* -1 * image width / 2 */
        margin-top: -16px;  /* -1 * image height / 2 */
        z-index: 5;
    }

    .error-block {
        display: none;
    }

    textarea.form-control {
        line-height: 20px !important;
    }

    .no-indent {
        padding: 0px !important;
        margin: 0px !important;
    }

    .top-indent {
        margin-top: 2px !important;
    }

    .no-indent-right {
        padding-right: 0px !important;
        margin-right: 0px !important;
    }

    .no-indent-left {
        padding-left: 0px !important;
        margin-left: 0px !important;
    }

    div.full-height {
        height: 100% !important;
    }

    .divider {
        border-bottom: 1px solid #ddd;
        margin-top: 2px;
        margin-bottom: 2px;
    }

    div.faq {
        margin-left: 6px !important;
        margin-right: 6px !important;
        padding: 5px !important;
    }

    /* Overriding bootstrap classes */
    .form-control {
        line-height:38px !important;
    }

    .navbar-form .form-control {
        width:100% !important;
    }

    .dropdown-menu{
        margin:0 0 0 !important;
    }

</style>

<script src="${pageContext.request.contextPath}/js/html5shiv.min.js"></script>
<script src="${pageContext.request.contextPath}/js/respond.min.js"></script>
<script type="text/javascript"
        src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBI_eGAZN3QUbNyJIxV73LWlRf2iCUa5ew&sensor=false">
</script>
<script src="${pageContext.request.contextPath}/js/json2.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery-1.10.2.js"></script>
<script src="${pageContext.request.contextPath}/js/jsrender.js"></script>
<script src="${pageContext.request.contextPath}/js/images-layer.js"></script>
<script src="${pageContext.request.contextPath}/js/domain.js"></script>
<script src="${pageContext.request.contextPath}/js/moment.min.js"></script>
<script src="${pageContext.request.contextPath}/js/richmarker.js"></script>
<script src="${pageContext.request.contextPath}/js/chosen.jquery.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery.blockUI.js"></script>
<script src="${pageContext.request.contextPath}/js/util.js"></script>


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

    // to be placed anywhere before the jquery.chosen initialization
    var getHiddenOffsetWidth = function (el) {
        // save a reference to a cloned element that can be measured
        var $hiddenElement = $(el).clone().appendTo('body');
        // calculate the width of the clone
        var width = $hiddenElement.outerWidth();
        // remove the clone from the DOM
        $hiddenElement.remove();
        return width * 0.7;
    };

    $(document).ready(function () {
        var config = {
            '.disable-search'          : {disable_search: true},
            '.chosen-select'           : {},
            '.chosen-select-deselect'  : {allow_single_deselect:true, no_results_text:'<spring:message code="dropdown.no.results" />'},
            '.chosen-select-no-single' : {disable_search_threshold:5},
            '.chosen-select-width'     : {width:"95%"}
        }

        for (var selector in config) {
            $(selector).chosen(config[selector]);
        }

        // Backups for all modal windows.
        var modalBackups = $('.modal').clone();
        $('body').on('hidden.bs.modal', '.modal', function () {
            $(this).remove();
            var modalClone = modalBackups.closest('#' + $(this).attr('id')).clone();
            $('body').append(modalClone);
        });
    });

    // Triggered during modal close event
    $(function(){
        $("[data-hide]").on("click", function(){
            $(this).hide();
        });
    });

</script>
</head>
<body>
<div class="navbar navbar-default navbar-fixed-top">

    <div class="container-fluid">

        <div class="row">

            <div class="col-md-5 no-indent-right">
                <div class="navbar-header">
                    <div class="nav dropdown">
                        <a class="navbar-brand dropdown-toggle" data-toggle="dropdown">
                            <spring:message code="app.title" />
                            <b class="caret"></b>
                        </a>
                        <ul class="dropdown-menu">
                            <li><a href="#aboutModal" data-toggle="modal"><h5><spring:message code="app.about" /></h5></a></li>
                            <li><a href="#faqModal" data-toggle="modal"><h5><spring:message code="app.faq" /></h5></a></li>
                            <li><a href="#mobileModal" data-toggle="modal"><h5><spring:message code="app.mobile" /></h5></a></li>
                            <li><a href="#feedbackModal" data-toggle="modal"><h5><spring:message code="app.feedback" /></h5></a></li>
                        </ul>
                        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navbar-collapse">
                            <span class="sr-only">Toggle navigation</span>
                            <span class="icon-bar"></span>
                            <span class="icon-bar"></span>
                            <span class="icon-bar"></span>
                        </button>
                    </div>
                </div>
            </div>

            <div class="col-md-19 no-indent-left">

                <div class="navbar-collapse collapse navbar-responsive-collapse" id="navbar-collapse">

                    <div class="row top-indent">
                        <div class="col-md-5 no-indent">
                            <div class="input-group navbar-form">
                                <span class="input-group-addon"><img src="${pageContext.request.contextPath}/icons/fish.png" width="15" height="15" /></span>
                                <select id="fish" data-placeholder="<spring:message code="dropdown.fish.label" />" class="form-control chosen-select-deselect" onchange="refresh()">
                                    <option value=""></option>
                                </select>
                            </div>
                        </div>

                        <div class="col-md-5 no-indent full-height">
                            <div class="input-group navbar-form">
                                <span class="input-group-addon"><img src="${pageContext.request.contextPath}/icons/rod.png" width="15" height="15" /></span>
                                <select id="fishingTool" data-placeholder="<spring:message code="dropdown.tool.label" />" class="form-control chosen-select-deselect" onchange="refresh()">
                                    <option value=""></option>
                                </select>
                            </div>
                        </div>

                        <div class="col-md-5 no-indent full-height">
                            <div class="input-group navbar-form">
                                <span class="input-group-addon"><img src="${pageContext.request.contextPath}/icons/hook.png" width="15" height="15" /></span>
                                <select id="fishingBait" data-placeholder="<spring:message code="dropdown.bait.label" />" class="form-control chosen-select-deselect" onchange="refresh()">
                                    <option value=""></option>
                                </select>
                            </div>
                        </div>

                        <c:set var="pageLocale" value="${pageContext.response.locale}" />
                        <c:if test="${not fn:startsWith(pageContext.response.locale, 'en') and
                                        not fn:startsWith(pageContext.response.locale, 'ru')}">
                            <%-- Current locale isn't supported. Set default locale here --%>
                            <c:set var="pageLocale" value="en" />
                        </c:if>

                        <div class="col-md-6 col-md-offset-3">
                                <div class="input-group navbar-form">
                                    <span class="input-group-addon"> <spring:message code="language" /></span>
                                    <select id="language" class="form-control chosen-select disable-search" onchange="handleLocaleSelection()">
                                        <option value="en" <c:if test="${fn:startsWith(pageLocale, 'en')}">selected</c:if>><spring:message code="language.en" /></option>
                                        <option value="ru" <c:if test="${fn:startsWith(pageLocale, 'ru')}">selected</c:if>><spring:message code="language.ru" /></option>
                                    </select>
                                </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<div id="map-canvas"></div>

<!--About Modal -->
<div class="modal fade" id="aboutModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><spring:message code="app.about" /></h4>
            </div>
            <div class="modal-body">
                <spring:message code="app.about.modal.body" />
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>

<!--FAQ Modal -->
<div class="modal fade" id="faqModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><spring:message code="app.faq" /></h4>
            </div>
            <div class="modal-body">
                <div class="container-fluid">

                    <div class="row alert alert-info faq">
                        <div class="col-md-24">
                            <span class="text-info"><strong><spring:message code="app.faq.question.sign" /></strong> - <spring:message code="app.faq.question.sign.description" />,
                                                    <strong><spring:message code="app.faq.answer.sign" /></strong> - <spring:message code="app.faq.answer.description" />.</span>
                        </div>
                    </div>

                    <div class="row well faq">
                        <div class="col-md-24">
                            <span class="text-info"><spring:message code="app.faq.question.sign" /></span> <spring:message code="app.faq.question1" />
                        </div>
                        <div class="col-md-24">
                            <div class="divider"></div>
                        </div>
                        <div class="col-md-24">
                            <span class="text-info"><spring:message code="app.faq.answer.sign" /></span> <spring:message code="app.faq.answer1" />
                        </div>
                    </div>

                    <div class="row well faq">
                        <div class="col-md-24">
                            <span class="text-info"><spring:message code="app.faq.question.sign" /></span> <spring:message code="app.faq.question2" />
                        </div>
                        <div class="col-md-24">
                            <div class="divider"></div>
                        </div>
                        <div class="col-md-24">
                            <span class="text-info"><spring:message code="app.faq.answer.sign" /></span> <spring:message code="app.faq.answer2" />
                        </div>
                    </div>

                    <div class="row well faq">
                        <div class="col-md-24">
                            <span class="text-info"><spring:message code="app.faq.question.sign" /></span> <spring:message code="app.faq.question3" />
                        </div>
                        <div class="col-md-24">
                            <div class="divider"></div>
                        </div>
                        <div class="col-md-24">
                            <span class="text-info"><spring:message code="app.faq.answer.sign" /></span> <spring:message code="app.faq.answer3" />
                        </div>
                    </div>

                    <div class="row well faq">
                        <div class="col-md-24">
                            <span class="text-info"><spring:message code="app.faq.question.sign" /></span> <spring:message code="app.faq.question4" />
                        </div>
                        <div class="col-md-24">
                            <div class="divider"></div>
                        </div>
                        <div class="col-md-24">
                            <span class="text-info"><spring:message code="app.faq.answer.sign" /></span> <spring:message code="app.faq.answer4" />
                        </div>
                    </div>

                    <div class="row well faq">
                        <div class="col-md-24">
                            <span class="text-info"><spring:message code="app.faq.question.sign" /></span> <spring:message code="app.faq.question5" />
                        </div>
                        <div class="col-md-24">
                            <div class="divider"></div>
                        </div>
                        <div class="col-md-24">
                            <span class="text-info"><spring:message code="app.faq.answer.sign" /></span> <spring:message code="app.faq.answer5" />
                        </div>
                    </div>

                    <div class="row well faq">
                        <div class="col-md-24">
                            <span class="text-info"><spring:message code="app.faq.question.sign" /></span> <spring:message code="app.faq.question6" />
                        </div>
                        <div class="col-md-24">
                            <div class="divider"></div>
                        </div>
                        <div class="col-md-24">
                            <span class="text-info"><spring:message code="app.faq.answer.sign" /></span> <spring:message code="app.faq.answer6" />
                        </div>
                    </div>

                    <div class="row well faq">
                        <div class="col-md-24">
                            <span class="text-info"><spring:message code="app.faq.question.sign" /></span> <spring:message code="app.faq.question7" />
                        </div>
                        <div class="col-md-24">
                            <div class="divider"></div>
                        </div>
                        <div class="col-md-24">
                            <span class="text-info"><spring:message code="app.faq.answer.sign" /></span> <spring:message code="app.faq.answer7" />
                        </div>
                    </div>

                    <div class="row well faq">
                        <div class="col-md-24">
                            <span class="text-info"><spring:message code="app.faq.question.sign" /></span> <spring:message code="app.faq.question8" />
                        </div>
                        <div class="col-md-24">
                            <div class="divider"></div>
                        </div>
                        <div class="col-md-24">
                            <span class="text-info"><spring:message code="app.faq.answer.sign" /></span> <spring:message code="app.faq.answer8" />
                        </div>
                    </div>

                    <div class="row well faq">
                        <div class="col-md-24">
                            <span class="text-info"><spring:message code="app.faq.question.sign" /></span> <spring:message code="app.faq.question9" />
                        </div>
                        <div class="col-md-24">
                            <div class="divider"></div>
                        </div>
                        <div class="col-md-24">
                            <span class="text-info"><spring:message code="app.faq.answer.sign" /></span> <spring:message code="app.faq.answer9" />
                        </div>
                    </div>

                    <div class="row well faq">
                        <div class="col-md-24">
                            <span class="text-info"><spring:message code="app.faq.question.sign" /></span> <spring:message code="app.faq.question10" />
                        </div>
                        <div class="col-md-24">
                            <div class="divider"></div>
                        </div>
                        <div class="col-md-24">
                            <span class="text-info"><spring:message code="app.faq.answer.sign" /></span> <spring:message code="app.faq.answer10" />
                        </div>
                    </div>

                    <div class="row well faq">
                        <div class="col-md-24">
                            <span class="text-info"><spring:message code="app.faq.question.sign" /></span> <spring:message code="app.faq.question11" />
                        </div>
                        <div class="col-md-24">
                            <div class="divider"></div>
                        </div>
                        <div class="col-md-24">
                            <span class="text-info"><spring:message code="app.faq.answer.sign" /></span> <spring:message code="app.faq.answer11" />
                        </div>
                    </div>

                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>

<!--Get mobile app modal -->
<div class="modal fade" id="mobileModal" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <img src="${pageContext.request.contextPath}/icons/ajax-loader.gif" class="loading-indicator" style="display:none" />
            <div class="modal-header">
                <button type="button" class="close close-modal" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><spring:message code="app.mobile" /></h4>
            </div>
            <div class="modal-body">
                <div class="bs-component error-block" data-hide="alert">
                    <div class="alert alert-dismissable alert-danger">
                        <button type="button" class="close">×</button>
                        <spring:message code="app.modal.ajax.error" />
                    </div>
                </div>
                <div class="container-fluid">

                    <div class="row">
                        <div class="col-lg-24">
                            <p><spring:message code="app.mobile.body" /></p>
                        </div>
                    </div>

                    <form class="form-horizontal">
                        <fieldset>
                            <legend></legend>
                            <div class="form-group">
                                <label for="userName" class="col-lg-4 control-label"><spring:message code="app.feedback.name.label" /></label>
                                <div class="col-lg-20">
                                    <input type="text" class="form-control" id="userName" placeholder="<spring:message code="app.feedback.name.placeholder" />">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="userEmail" class="col-lg-4 control-label"><spring:message code="app.feedback.email.label" /></label>
                                <div class="col-lg-20">
                                    <input type="text" class="form-control" id="userEmail" placeholder="<spring:message code="app.feedback.email.placeholder" />">
                                </div>
                            </div>
                        </fieldset>
                    </form>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary close-modal" onclick="sendEmail('mobileModal', 'userEmail', '', 'Get App Request', 'userName')"><span class="glyphicon glyphicon-send"></span>&nbsp;&nbsp;<spring:message code="app.modal.send.label" /></button>
            </div>
        </div>
    </div>
</div>

<!--Feedback modal -->
<div class="modal fade" id="feedbackModal" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <img src="${pageContext.request.contextPath}/icons/ajax-loader.gif" class="loading-indicator" style="display:none" />
            <div class="modal-header">
                <button type="button" class="close close-modal" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><spring:message code="app.feedback" /></h4>
            </div>
            <div class="modal-body">
                <div class="bs-component error-block" data-hide="alert">
                    <div class="alert alert-dismissable alert-danger">
                        <button type="button" class="close">×</button>
                        <spring:message code="app.modal.ajax.error" />
                    </div>
                </div>
                <div class="container-fluid">

                        <div class="row">
                            <div class="col-lg-24">
                                <p><spring:message code="app.feedback.description" /></p>
                            </div>
                        </div>

                        <form class="form-horizontal">
                            <fieldset>
                                <legend></legend>
                                <div class="form-group">
                                    <label for="feedbackName" class="col-lg-4 control-label"><spring:message code="app.feedback.name.label" /></label>
                                    <div class="col-lg-20">
                                        <input type="text" class="form-control" id="feedbackName" placeholder="<spring:message code="app.feedback.name.placeholder" />">
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for="feedbackEmail" class="col-lg-4 control-label"><spring:message code="app.feedback.email.label" /></label>
                                    <div class="col-lg-20">
                                        <input type="text" class="form-control" id="feedbackEmail" placeholder="<spring:message code="app.feedback.email.placeholder" />">
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for="feedbackMessage" class="col-lg-4 control-label"><spring:message code="app.feedback.message.label" /></label>
                                    <div class="col-lg-20">
                                        <textarea class="form-control" rows="10" id="feedbackMessage" placeholder="<spring:message code="app.feedback.message.placeholder" />"></textarea>
                                    </div>
                                </div>
                            </fieldset>
                        </form>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary close-modal" onclick="sendEmail('feedbackModal', 'feedbackEmail', 'feedbackMessage', 'Feedback', 'feedbackName')"><span class="glyphicon glyphicon-send"></span>&nbsp;&nbsp;<spring:message code="app.modal.send.label" /></button>
            </div>
        </div>
    </div>
</div>

</body>
</html>
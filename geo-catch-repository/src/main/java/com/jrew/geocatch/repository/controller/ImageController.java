package com.jrew.geocatch.repository.controller;

import com.jrew.geocatch.repository.model.Image;
import com.jrew.geocatch.repository.model.ViewBounds;
import com.jrew.geocatch.repository.service.ImageProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.validation.Valid;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 9/3/13
 * Time: 7:52 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/images")
public class ImageController {

    String dateFormatPattern;

    @Autowired
    ServletContext servletContext;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String uploadImage(@RequestBody @Valid Image image) throws IOException {

        WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(servletContext);

        ImageProvider imageProvider = (ImageProvider) context.getBean("imageProvider");
        imageProvider.uploadImage(image);

        return "imageUpload";
    }

    @RequestMapping(value = "/load", method = RequestMethod.POST)
    public  @ResponseBody List<Image> loadImage(@ModelAttribute("ViewBounds") ViewBounds viewBounds) throws IOException {

        WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(servletContext);

        ImageProvider imageProvider = (ImageProvider) context.getBean("imageProvider");
        List<Image> images = imageProvider.getImages(viewBounds);

        return images;
    }

    @RequestMapping(value = "/load/{northEastLat}/{northEastLng}/{southWestLat}/{southWestLng}",
                    method = RequestMethod.GET, produces = "application/json")
    public  @ResponseBody List<Image> ajaxLoadImage(@Valid ViewBounds viewBounds) throws IOException {

        WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        ImageProvider imageProvider = (ImageProvider) context.getBean("imageProvider");
        List<Image> images = imageProvider.getImages(viewBounds);

        return images;
    }



    @Value("${global.dateFormatPattern}")
    public void setDateFormatPattern(String dateFormatPattern) {
        this.dateFormatPattern = dateFormatPattern;
    }

    /**
     *
     */
    @InitBinder("Image")
    public void setDisallowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id", "thumbnailPath", "path", "isDeleted");
    }

    /**
     *
     * @param dataBinder
     */
    @InitBinder("Image")
    public void registerCustomDateEditor(WebDataBinder dataBinder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatPattern);
        dateFormat.setLenient(false);
        CustomDateEditor customDateEditor = new CustomDateEditor(dateFormat, true);
        dataBinder.registerCustomEditor(Date.class, customDateEditor);
    }
}

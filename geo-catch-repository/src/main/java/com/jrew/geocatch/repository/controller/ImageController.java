package com.jrew.geocatch.repository.controller;

import com.jrew.geocatch.repository.model.Image;
import com.jrew.geocatch.repository.model.ViewBounds;
import com.jrew.geocatch.repository.service.DomainPropertyService;
import com.jrew.geocatch.repository.service.ImageService;
import com.jrew.geocatch.repository.util.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.validation.Valid;
import javax.validation.Validator;
import java.io.IOException;
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

    @Autowired
    ServletContext servletContext;

    @Resource
    private Validator validator;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String uploadImage(@RequestParam("image") Image image,
                              @RequestPart("file") MultipartFile file) throws IOException {

        image.setFile(file);
        ValidationUtils.validate(image, validator);

        WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(servletContext);

        DomainPropertyService domainPropertyService = (DomainPropertyService) context.getBean("domainPropertyService");
        domainPropertyService.processDomainProperties(image.getDomainProperties());

        ImageService imageService = (ImageService) context.getBean("imageService");
        imageService.uploadImage(image);

        return "imageUpload";
    }

    @RequestMapping(value = "/load", method = RequestMethod.POST)
    public  @ResponseBody List<Image> loadImage(@ModelAttribute("ViewBounds") ViewBounds viewBounds) throws IOException {

        WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(servletContext);

        ImageService imageService = (ImageService) context.getBean("imageService");
        List<Image> images = imageService.getImages(viewBounds);

        return images;
    }

    @RequestMapping(value = "/load/{northEastLat}/{northEastLng}/{southWestLat}/{southWestLng}",
                    method = RequestMethod.GET, produces = "application/json")
    public  @ResponseBody List<Image> ajaxLoadImage(@Valid ViewBounds viewBounds) throws IOException {

        WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        ImageService imageService = (ImageService) context.getBean("imageProvider");
        List<Image> images = imageService.getImages(viewBounds);

        return images;
    }

}

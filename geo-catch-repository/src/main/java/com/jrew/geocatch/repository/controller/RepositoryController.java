package com.jrew.geocatch.repository.controller;

import com.jrew.geocatch.repository.model.DomainProperty;
import com.jrew.geocatch.repository.model.Image;
import com.jrew.geocatch.repository.model.criteria.SearchCriteria;
import com.jrew.geocatch.repository.service.DomainPropertyService;
import com.jrew.geocatch.repository.service.ImageService;
import com.jrew.geocatch.repository.util.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
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
@RequestMapping("/")
public class RepositoryController {

    @Autowired
    ImageService imageService;

    @Autowired
    DomainPropertyService domainPropertyService;

    @Resource
    private Validator validator;

    @RequestMapping(value = "/images", method = RequestMethod.POST)
    public String uploadImage(@RequestParam("image") Image image,
                              @RequestPart("file") MultipartFile file) throws IOException {

        image.setFile(file);
        ValidationUtils.validate(image, validator);

        domainPropertyService.processDomainProperties(image.getDomainProperties());
        imageService.uploadImage(image);

        return "imageUpload";
    }

//    @RequestMapping(value = "/load/{northEastLat}/{northEastLng}/{southWestLat}/{southWestLng}",
//                    method = RequestMethod.GET, produces = "application/json")
           @RequestMapping(value = "images", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody List<Image> loadImage(@RequestBody SearchCriteria searchCriteria) {

        return imageService.getImages(searchCriteria);
    }

    @RequestMapping(value = "domain/{type}/{locale}",
                    method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<DomainProperty> loadDomainInfo(@PathVariable("type") Long type,
                                               @PathVariable("locale") String locale) {

        return domainPropertyService.loadDomainProperties(type, locale);
    }

}

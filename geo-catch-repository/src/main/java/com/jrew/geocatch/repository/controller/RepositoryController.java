package com.jrew.geocatch.repository.controller;

import com.jrew.geocatch.repository.model.ClientImage;
import com.jrew.geocatch.repository.model.ClientImagePreview;
import com.jrew.geocatch.repository.model.DomainProperty;
import com.jrew.geocatch.repository.model.Image;
import com.jrew.geocatch.repository.service.DomainPropertyService;
import com.jrew.geocatch.repository.service.ImageService;
import com.jrew.geocatch.web.model.criteria.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @RequestMapping(value = "images", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void uploadImage(@RequestBody @Valid Image image) throws IOException {

        domainPropertyService.processDomainProperties(image.getDomainProperties());
        imageService.uploadImage(image);
    }

    @RequestMapping(value = "images/{imageId}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void updateImage(@RequestBody Image image) {
        imageService.updateImage(image);
    }

    @RequestMapping(value = "images/{imageId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteImage(@RequestParam("deviceId") String deviceId, @PathVariable("imageId") long imageId) {
       imageService.deleteImage(imageId, deviceId);
    }

    @RequestMapping(value = "images/{imageId}", method = RequestMethod.GET)
    public @ResponseBody ClientImage getImage(@PathVariable("imageId") long imageId) {
        return imageService.getImage(imageId);
    }

    @RequestMapping(value = "search", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody List<ClientImagePreview> loadImage(@RequestBody SearchCriteria searchCriteria) {

        return imageService.getImages(searchCriteria);
    }

    @RequestMapping(value = "domain/{locale}",
                    method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<DomainProperty> loadDomainsInfo(@PathVariable("locale") String locale) {
        return domainPropertyService.loadDomainProperties(locale);
    }
}

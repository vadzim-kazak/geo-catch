package com.jrew.geocatch.repository.controller;

import com.jrew.geocatch.repository.model.DomainProperty;
import com.jrew.geocatch.repository.model.Image;
import com.jrew.geocatch.repository.service.DomainPropertyService;
import com.jrew.geocatch.repository.service.ImageReviewService;
import com.jrew.geocatch.repository.service.ImageService;
import com.jrew.geocatch.web.model.ClientImage;
import com.jrew.geocatch.web.model.ClientImagePreview;
import com.jrew.geocatch.web.model.ImageReview;
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

    /** **/
    @Autowired
    private ImageService imageService;

    /** **/
    @Autowired
    private DomainPropertyService domainPropertyService;

    /** **/
    @Autowired
    private ImageReviewService imageReviewService;

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

    @RequestMapping(value = "images/{imageId}/{deviceId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteImage(@PathVariable("imageId") long imageId, @PathVariable("deviceId") String deviceId) {
       imageService.deleteImage(imageId, deviceId);
    }

    @RequestMapping(value = "images/{imageId}", method = RequestMethod.GET)
    public @ResponseBody ClientImage loadImage(@PathVariable("imageId") long imageId) {
        return imageService.getImage(imageId);
    }

    @RequestMapping(value = "search", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody List<ClientImagePreview> loadImages(@RequestBody SearchCriteria searchCriteria) {

        return imageService.getImages(searchCriteria);
    }

    @RequestMapping(value = "domain/{locale}",
                    method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<DomainProperty> loadDomainsInfo(@PathVariable("locale") String locale) {
        return domainPropertyService.loadDomainProperties(locale);
    }

    @RequestMapping(value = "reviews", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void handleReview(@RequestBody ImageReview imageReview) {
        imageReviewService.handleReview(imageReview);
    }
}

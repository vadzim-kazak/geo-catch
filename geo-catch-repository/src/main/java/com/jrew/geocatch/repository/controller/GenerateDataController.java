package com.jrew.geocatch.repository.controller;

import com.jrew.geocatch.repository.model.DomainProperty;
import com.jrew.geocatch.repository.model.Image;
import org.apache.http.StatusLine;
import org.apache.http.protocol.BasicHttpContext;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 12/12/13
 * Time: 2:30 PM
 */
@Controller
@RequestMapping("/generate")
public class GenerateDataController {

    @Value("#{configProperties['global.dateFormatPattern']}")
    private String dateFormat;

    @RequestMapping(value = "/start/{imagesNumber}", method = RequestMethod.GET)
    public void generateImages(@PathVariable("imagesNumber") int imagesNumber) throws Exception {
        for (int i = 0; i < imagesNumber; i++) {
            uploadImage();
        }
    }

    private void uploadImage() throws Exception {

        File file = null;

        Random random = new Random();

        Image image = new Image();
        // Image id
        int deviceId = random.nextInt(10000);
        image.setDeviceId(Integer.toString(deviceId));
        image.setDescription("This image is uploaded from device " + deviceId);
        image.setLatitude(-90 + random.nextDouble() * 180);
        image.setLongitude(-180 + random.nextDouble() * 360);

        long currentTime =  System.currentTimeMillis();
        Date date = new Date((long)((0.9d + 0.1d * random.nextDouble()) * currentTime));
        image.setDate(date);

        // 80% of images will have PUBLIC privacy level
        double choice = random.nextDouble();
        if (choice > 0.2d) {
            image.setPrivacyLevel(Image.PrivacyLevel.PUBLIC);
        } else {
            image.setPrivacyLevel(Image.PrivacyLevel.PRIVATE);
        }

        image.setRating(random.nextInt(100));

        int imageChoice = random.nextInt(4);
        List<DomainProperty> domainProperties = new ArrayList<DomainProperty>();
        switch(imageChoice) {
            case 0:
                file = new File("d:/temp/fish/Щука.jpg");
                DomainProperty fish = new DomainProperty();
                fish.setId(1);
                domainProperties.add(fish);

                DomainProperty tool = new DomainProperty();
                tool.setId(5000);
                domainProperties.add(tool);

                DomainProperty bite = new DomainProperty();
                bite.setId(8000);
                domainProperties.add(bite);


                break;

            case 1:
                file = new File("d:/temp/fish/окунь.jpg");
                fish = new DomainProperty();
                fish.setId(3);
                domainProperties.add(fish);

                tool = new DomainProperty();
                tool.setId(5000);
                domainProperties.add(tool);

                bite = new DomainProperty();
                bite.setId(8000);
                domainProperties.add(bite);
                break;

            case 2:
                file = new File("d:/temp/fish/Лещ.jpg");
                fish = new DomainProperty();
                fish.setId(5);
                domainProperties.add(fish);

                tool = new DomainProperty();
                tool.setId(5002);
                domainProperties.add(tool);

                bite = new DomainProperty();
                bite.setId(8003);
                domainProperties.add(bite);
                break;

            case 3:
                file = new File("d:/temp/fish/линь.jpg");
                fish = new DomainProperty();
                fish.setId(7);
                domainProperties.add(fish);

                tool = new DomainProperty();
                tool.setId(5002);
                domainProperties.add(tool);

                bite = new DomainProperty();
                bite.setId(8003);
                domainProperties.add(bite);
                break;
        }

        image.setDomainProperties(domainProperties);

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDateFormat(new SimpleDateFormat(dateFormat));
        String parameter = ow.writeValueAsString(image);

        String url = "http://localhost:8080/fishing/repo/images";

        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();

        MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

        entity.addPart("image", new StringBody(parameter));
        entity.addPart("file", new FileBody(file));

        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(entity);
        HttpResponse response = httpClient.execute(httpPost, localContext);
        StatusLine status = response.getStatusLine();
    }

}




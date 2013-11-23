package com.jrew.geocatch.repository.converter;

import com.jrew.geocatch.repository.model.Image;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 11/14/13
 * Time: 2:37 PM
 *
 * HttpRequest to Image converter
 */
public class ImageConverter implements Converter<String, Image> {

    @Value("${global.dateFormatPattern}")
    private String dateFormatPattern;

    @Override
    public Image convert(String request) {

        Image image = null;

        try {
            ObjectMapper mapper = new ObjectMapper();

            /** Set date formatter to object mapper **/
            final DateFormat dateFormat = new SimpleDateFormat(dateFormatPattern);
            mapper.setDateFormat(dateFormat);

            image = mapper.readValue(request, Image.class);

        } catch (Exception exc) {
            exc.printStackTrace();

        }

        //validator.validate(image);
        return image;
    }


//    public void setDateFormatPattern(String dateFormatPattern) {
//        this.dateFormatPattern = dateFormatPattern;
//    }
}

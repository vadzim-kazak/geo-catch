package com.jrew.geocatch.repository.converter;

import com.jrew.geocatch.repository.model.Image;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.core.convert.converter.Converter;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 11/14/13
 * Time: 2:37 PM
 *
 * HttpRequest to Image converter
 */
public class ImageConverter implements Converter<JsonNode, Image> {

    @Override
    public Image convert(JsonNode jsonNode) {

        ObjectMapper objectMapper = new ObjectMapper();
        Image image = objectMapper.convertValue(jsonNode, Image.class);

        return image;
    }

}

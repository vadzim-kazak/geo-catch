import org.apache.sanselan.Sanselan;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.common.RationalNumber;
import org.apache.sanselan.formats.jpeg.JpegImageMetadata;
import org.apache.sanselan.formats.tiff.TiffField;
import org.apache.sanselan.formats.tiff.TiffImageMetadata;
import org.apache.sanselan.formats.tiff.constants.TiffConstants;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 4/8/14
 * Time: 3:18 PM
 */
public class Grabber {

    private static final String APP_KEY = "AIzaSyDfONNlJ4dXbQ5aOYVbzvcVSdBmmi17YCU";
    private static final String CUSTOM_SEARCH_ENGINE_ID = "015818362943938412551:ry9yvwdtvia";
    private static final int SEARCH_RESULTS_COUNTER = 10;
    private static final int PAGE_COUNTER = 10;
    private static final int MIN_IMAGE_SIDE_SIZE = 700;
    private static int GPS_COUNTER = 0;

    public static void main(String[] args) {

        GPS_COUNTER = 0;

        String query = "pike geotag";

        for (int i = 0; i < PAGE_COUNTER; i++) {

            RestTemplate restTemplate = new RestTemplate();

            StringBuilder uri = new StringBuilder();
            uri.append("https://www.googleapis.com/customsearch/v1?key=")
                    .append(APP_KEY)
                    .append("&cx=")
                    .append(CUSTOM_SEARCH_ENGINE_ID)
                    .append("&q=")
                    .append(query)
                    .append("&searchType=image&start=")
                    .append(i * SEARCH_RESULTS_COUNTER + 1)
                    .append("&fileType=jpg&imgSize=xxlarge");

           try {
               ResponseEntity<JsonNode> responseEntity = restTemplate.getForEntity(uri.toString(), JsonNode.class);
               if (responseEntity.getStatusCode() == HttpStatus.OK) {
                   JsonNode response = responseEntity.getBody();
                   ArrayNode items = (ArrayNode) response.get("items");
                   Iterator<JsonNode> iterator = items.getElements();
                   while (iterator.hasNext()) {

                       JsonNode item = iterator.next();
                       JsonNode image = item.get("image");

                       JsonNode heightNode = image.get("height");
                       JsonNode widthNode = image.get("width");
                       if (MIN_IMAGE_SIDE_SIZE <= heightNode.getIntValue() &&
                               MIN_IMAGE_SIDE_SIZE <= widthNode.getIntValue()) {

                           System.out.println("Processing link: " + item.get("link").asText());
                           String url = item.get("link").asText();
                           processImage(url);
                       }
                   }
               }
           } catch (Exception exception) {
            exception.printStackTrace();
           }
        }

        System.out.println("__________________________________");
        System.out.println("GPS Counter: " + GPS_COUNTER);
        System.out.println("__________________________________");

    }

    /**
     *
     * @param url
     */
    private static void processImage(String url) {

        try {

           InputStream input = new URL(url).openStream();
           IImageMetadata imageMetadata = Sanselan.getMetadata(input, "bla");

           if (imageMetadata instanceof JpegImageMetadata){
               JpegImageMetadata jpegMetadata = (JpegImageMetadata) imageMetadata;

//               TiffField field = jpegMetadata.findEXIFValue(TiffConstants.GPS_TAG_GPS_LATITUDE);
//               if (field != null) {
//                   System.out.println("**********************");
//                   System.out.println(field.getValueDescription());
//                   System.out.println("**********************");
//                   GPS_COUNTER++;
//                   return;
//               }
//
//               field = jpegMetadata.findEXIFValue(TiffConstants.GPS_TAG_GPS_LATITUDE_REF);
//               if (field != null) {
//                   System.out.println("**********************");
//                   System.out.println(field.getValueDescription());
//                   System.out.println("**********************");
//                   GPS_COUNTER++;
//                   return;
//               }
//
//               field = jpegMetadata.findEXIFValue(TiffConstants.GPS_TAG_GPS_LONGITUDE_REF);
//               if (field != null) {
//                   System.out.println("**********************");
//                   System.out.println(field.getValueDescription());
//                   System.out.println("**********************");
//                   GPS_COUNTER++;
//                   return;
//               }
//
//               field = jpegMetadata.findEXIFValue(TiffConstants.GPS_TAG_GPS_LONGITUDE);
//               if (field != null) {
//                   System.out.println("**********************");
//                   System.out.println(field.getValueDescription());
//                   System.out.println("**********************");
//                   GPS_COUNTER++;
//                   return;
//               }

               TiffImageMetadata exifMetadata = jpegMetadata.getExif();
               if (exifMetadata != null) {
                   TiffImageMetadata.GPSInfo gpsInfo = exifMetadata.getGPS();
                   if (gpsInfo != null) {

                       GPS_COUNTER++;

                       double longitude = gpsInfo.getLongitudeAsDegreesEast();
                       double latitude = gpsInfo.getLatitudeAsDegreesNorth();

                       System.out.println("	" + "GPS Description: " + gpsInfo);
                       System.out.println("	" + "GPS Longitude (Degrees East): " + longitude);
                       System.out.println("	" + "GPS Latitude (Degrees North): " + latitude);
                       return;
                   }
               }


               // more specific example of how to manually access GPS values
               TiffField gpsLatitudeRefField = jpegMetadata
                       .findEXIFValue(TiffConstants.GPS_TAG_GPS_LATITUDE_REF);
               TiffField gpsLatitudeField = jpegMetadata
                       .findEXIFValue(TiffConstants.GPS_TAG_GPS_LATITUDE);
               TiffField gpsLongitudeRefField = jpegMetadata
                       .findEXIFValue(TiffConstants.GPS_TAG_GPS_LONGITUDE_REF);
               TiffField gpsLongitudeField = jpegMetadata
                       .findEXIFValue(TiffConstants.GPS_TAG_GPS_LONGITUDE);
               if (gpsLatitudeRefField != null && gpsLatitudeField != null
                       && gpsLongitudeRefField != null
                       && gpsLongitudeField != null)
               {
                   // all of these values are strings.
                   String gpsLatitudeRef = (String) gpsLatitudeRefField.getValue();
                   RationalNumber gpsLatitude[] = (RationalNumber[]) (gpsLatitudeField
                           .getValue());
                   String gpsLongitudeRef = (String) gpsLongitudeRefField
                           .getValue();
                   RationalNumber gpsLongitude[] = (RationalNumber[]) gpsLongitudeField
                           .getValue();

                   RationalNumber gpsLatitudeDegrees = gpsLatitude[0];
                   RationalNumber gpsLatitudeMinutes = gpsLatitude[1];
                   RationalNumber gpsLatitudeSeconds = gpsLatitude[2];

                   RationalNumber gpsLongitudeDegrees = gpsLongitude[0];
                   RationalNumber gpsLongitudeMinutes = gpsLongitude[1];
                   RationalNumber gpsLongitudeSeconds = gpsLongitude[2];

                   // This will format the gps info like so:
                   //
                   // gpsLatitude: 8 degrees, 40 minutes, 42.2 seconds S
                   // gpsLongitude: 115 degrees, 26 minutes, 21.8 seconds E

                   System.out.println("	" + "GPS Latitude: "
                           + gpsLatitudeDegrees.toDisplayString() + " degrees, "
                           + gpsLatitudeMinutes.toDisplayString() + " minutes, "
                           + gpsLatitudeSeconds.toDisplayString() + " seconds "
                           + gpsLatitudeRef);
                   System.out.println("	" + "GPS Longitude: "
                           + gpsLongitudeDegrees.toDisplayString() + " degrees, "
                           + gpsLongitudeMinutes.toDisplayString() + " minutes, "
                           + gpsLongitudeSeconds.toDisplayString() + " seconds "
                           + gpsLongitudeRef);

                   GPS_COUNTER++;

                   return;
               }
           }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}

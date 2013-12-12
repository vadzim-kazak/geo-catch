package com.jrew.geocatch.repository.service.generator;

import com.jrew.geocatch.repository.model.Image;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 9/11/13
 * Time: 1:15 PM
 *
 * Generates file name depending on geo coordinates and creation time accroding to following template:
 * <latitude>separator<longitude>separator<timestamp>
 */
public class GeoTimeFileNameGeneratorImpl implements FileNameGenerator {

    /** Used for date postfix generation **/
    @Value("#{configProperties['fileNameGenerator.dateFormatPattern']}")
    private String dateFormatPattern = null;

    /** Separator of values in file name **/
    @Value("#{configProperties['fileNameGenerator.fileNameSeparator']}")
    private String fileNameSeparator = null;

    /** Specifies how many digits after dot will be taken **/
    @Value("#{configProperties['fileNameGenerator.degreeFraction']}")
    private int degreeFraction = 0;

    /** **/
    private DecimalFormat decimalFormat;

    /** **/
    private SimpleDateFormat simpleDateFormat;

    /**
     * Constructor
     */
    public GeoTimeFileNameGeneratorImpl() {}

    @Override
    public String generate(Image image, MultipartFile file) throws IllegalArgumentException {

        if (decimalFormat == null) {
            decimalFormat = new DecimalFormat();
            decimalFormat.setMaximumFractionDigits(degreeFraction);
        }

        if (simpleDateFormat == null) {
            simpleDateFormat = new SimpleDateFormat(dateFormatPattern);
        }


        if (file == null) {
            throw new IllegalArgumentException("Provided image file is empty.");
        }

        String extension = FilenameUtils.getExtension(file.getOriginalFilename());

        return decimalFormat.format(image.getLatitude()) + fileNameSeparator +
               decimalFormat.format(image.getLongitude()) + fileNameSeparator +
               simpleDateFormat.format(new Date()) + "." + extension;
    }
}

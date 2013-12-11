package com.jrew.geocatch.repository.service.generator;

import com.jrew.geocatch.repository.model.Image;
import org.apache.commons.io.FilenameUtils;
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
    private final String dateFormatPattern;

    /** Separator of values in file name **/
    private final String fileNameSeparator;

    /** Specifies how many digits after dot will be taken **/
    private final int degreeFraction;

    /** **/
    private final  DecimalFormat decimalFormat;

    /** **/
    private final SimpleDateFormat simpleDateFormat;

    /**
     * Constructor
     *
     * @param dateFormatPattern
     * @param fileNameSeparator
     * @param degreeFraction
     */
    public GeoTimeFileNameGeneratorImpl(String dateFormatPattern, String fileNameSeparator, int degreeFraction) {
        this.dateFormatPattern = dateFormatPattern;
        this.fileNameSeparator = fileNameSeparator;
        this.degreeFraction = degreeFraction;

        decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(degreeFraction);

        simpleDateFormat = new SimpleDateFormat(dateFormatPattern);
    }

    @Override
    public String generate(Image image, MultipartFile file) throws IllegalArgumentException {

        if (file == null) {
            throw new IllegalArgumentException("Provided image file is empty.");
        }

        String extension = FilenameUtils.getExtension(file.getOriginalFilename());

        return decimalFormat.format(image.getLatitude()) + fileNameSeparator +
               decimalFormat.format(image.getLongitude()) + fileNameSeparator +
               simpleDateFormat.format(new Date()) + "." + extension;
    }
}

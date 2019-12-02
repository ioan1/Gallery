package fr.redby.gallery.service.albums.bean;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

/**
 * Class wrapping an album.
 * @author Ioan Bernevig
 * @version $Revision$
 */
@SuppressWarnings("squid:S1068")
public class Album implements Comparable{

    private static final Logger LOGGER = LoggerFactory.getLogger( Album.class );

    @JsonFormat(pattern = "dd/MM/yyyy") private Date date;
    private String category;
    private String name;
    @Id
    private String id;
    private String path;
    @Transient
    private List<File> files;
    private int pictures;
    private int videos;
    private int others;

    /**
     * Default constructor.
     */
    public Album() {
        super();
    }

    /**
     * Copy constructor considering a category and a folder.
     * @param category the year
     * @param directory the folder of the current album
     */
    public Album(final String category, final File directory) {
        this.category = category;
        this.name = directory.getName();
        this.id = directory.getName();
        this.path = directory.getAbsolutePath();

        // Extract date and name when available
        Pattern p = Pattern.compile("([\\d]{8})[ -]*(.*)");
        Matcher matcher = p.matcher(name);
        if (matcher.matches()) {
            try {
                this.date = new SimpleDateFormat("yyyyMMdd").parse(matcher.group(1));
            } catch (ParseException e) {
                this.date = null;
            }
            this.name = matcher.group(2);
        }

        // Parse the files and get the number of pictures, videos and other files.
        this.files = parseDirectory(directory);
        this.pictures = (int) this.files.stream().filter(f -> TypeOfFile.defineFor(f).equals(TypeOfFile.PICTURE)).count();
        this.videos = (int) this.files.stream().filter(f -> TypeOfFile.defineFor(f).equals(TypeOfFile.VIDEO)).count();
        this.others = (int) this.files.stream().filter(f -> TypeOfFile.defineFor(f).equals(TypeOfFile.OTHER)).count();
    }

    /**
     * Returns ALL files within the album.
     *
     * @param directory the directory to parse
     * @return a list of directories
     */
    private List<File> parseDirectory(File directory) {
        List<File> items = new ArrayList<>();
        LOGGER.info("parseDirectory in {}, exists={}.", directory.getPath(), directory.exists());
        for (File file : directory.listFiles()) {
            items.add(file);
            if (file.isDirectory()) {
                items.addAll(parseDirectory(file));
            }
        }
        return items;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public int compareTo(Object o) {
        Album other = (Album) o;
        if (this.category.equals(other.category)) {
            return this.category.compareTo(other.category);
        } else {
            if (this.date != null && other.date != null) {
                return this.date.compareTo(other.date);
            } else {
                return this.name.compareTo(other.name);
            }
        }
    }
}

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
 * @author Ioan Bernevig
 * @version $Revision$
 */
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

    public Album() {
        super();
    }

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
     * @param directory
     * @return
     */
    private List<File> parseDirectory(File directory) {
        List<File> files = new ArrayList<>();
        LOGGER.info("parseDirectory in {}, exists={}.", directory.getPath(), directory.exists());
        for (File file : directory.listFiles()) {
            files.add(file);
            if (file.isDirectory()) {
                files.addAll(parseDirectory(file));
            }
        }
        return files;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public Date getDate() {
        return date;
    }

    public int getPictures() {
        return pictures;
    }

    public int getVideos() {
        return videos;
    }

    public int getOthers() {
        return others;
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

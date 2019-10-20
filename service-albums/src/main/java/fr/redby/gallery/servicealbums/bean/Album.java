
package fr.redby.gallery.servicealbums.bean;

import java.beans.Transient;
import java.io.File;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author Ioan Bernevig
 * @version $Revision$
 */
public class Album {

    @JsonFormat(pattern="dd/MM/yyyy")
    private Date date;
    private String category;
    private String name;
    private String path;
    private List<File> files;
    private int pictures;
    private int videos;
    private int others;

    public Album(final String category, final File directory) {
        this.category = category;
        this.name = directory.getName();
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
    }

    /**
     * Returns ALL files within the album.
     * @param directory
     * @return
     */
    private List<File> parseDirectory(File directory) {
        System.out.println("parseDirectory in " + directory.getPath() + ", exists=" + directory.exists());
        List<File> files = Arrays.asList(directory.listFiles());
        System.out.println("Found the following number of files: " + files);
        for(File file : files) {
            if (file.isDirectory()) {
                files.addAll(parseDirectory(file));
            }
        }
        return files;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getPictures() {
        return (int) this.files.stream().filter(f->TypeOfFile.defineFor(f).equals(TypeOfFile.PICTURE)).count();
    }

    public int getVideos() {
        return (int) this.files.stream().filter(f->TypeOfFile.defineFor(f).equals(TypeOfFile.VIDEO)).count();
    }

    public int getOthers() {
        return (int) this.files.stream().filter(f->TypeOfFile.defineFor(f).equals(TypeOfFile.OTHER)).count();
    }

}

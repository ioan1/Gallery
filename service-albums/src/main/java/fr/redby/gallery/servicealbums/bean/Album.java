
package fr.redby.gallery.servicealbums.bean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

    public Album(final String category, final String name, String absolutePath) {
        this.category = category;
        this.name = name;
        this.path = absolutePath;

        // Extract date when available
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
}

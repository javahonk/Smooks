package util;


import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

/**
 * @author Javahonk 1/23/2021 8:42 PM
 */
public class ClassPathUtil {

    public static String getResourceContent(String path) throws IOException{
        InputStream inputStream = ClassPathUtil.class.getClassLoader().getResourceAsStream(path);
        if (inputStream == null) throw new IOException("Classpath resource not found: " +path);

        try{
            return IOUtils.toString(inputStream, "UTF-8");
        } finally {
            if(inputStream != null){
                inputStream.close();
            }
        }

    }

    public static InputStream getResourceStream(String path) throws IOException{
        return ClassPathUtil.class.getClassLoader().getResourceAsStream(path);
    }

    public static List<String> getResourceLines(String path) throws IOException{
        InputStream inputStream =  ClassPathUtil.class.getClassLoader().getResourceAsStream(path);
        try{
            List<String> lines = IOUtils.readLines(inputStream, "UTF-8");
            return  lines;
        } finally {
            if(inputStream != null){
                inputStream.close();
            }
        }
    }


}
